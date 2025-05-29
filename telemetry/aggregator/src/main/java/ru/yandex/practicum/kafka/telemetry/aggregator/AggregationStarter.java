package ru.yandex.practicum.kafka.telemetry.aggregator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.aggregator.service.SnapshotService;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter implements ApplicationRunner, Closeable {
    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final KafkaProducer<String, SensorsSnapshotAvro> producer;
    private final SnapshotService snapshotService;
    private volatile boolean running = true;

    @Value("${kafka.sensor-events-topic}")
    private String sensorEventsTopic;

    @Value("${kafka.snapshots-topic}")
    private String snapshotsTopic;

    @Override
    public void run(ApplicationArguments args) {
        start();
    }

    public void start() {
        try {
            log.info("Подписка на топик: {}", sensorEventsTopic);
            consumer.subscribe(Collections.singletonList(sensorEventsTopic));
            while (running) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(Duration.ofMillis(100));
                if (records.isEmpty()) {
                    log.debug("Нет записей из топика {}", sensorEventsTopic);
                    continue;
                }
                log.debug("Получено {} записей из топика {}", records.count(), sensorEventsTopic);
                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    SensorEventAvro event = record.value();
                    log.info("Обработка события: hubId={}, sensorId={}, timestamp={}",
                            event.getHubId(), event.getId(), event.getTimestamp());
                    try {
                        snapshotService.updateState(event).ifPresent(snapshot -> {
                            ProducerRecord<String, SensorsSnapshotAvro> producerRecord =
                                    new ProducerRecord<>(snapshotsTopic, snapshot.getHubId(), snapshot);
                            log.info("Отправка снапшота для хаба {} в топик {}", snapshot.getHubId(), snapshotsTopic);
                            producer.send(producerRecord, (metadata, exception) -> {
                                if (exception != null) {
                                    log.error("Ошибка отправки снапшота в топик {}: {}", snapshotsTopic, exception.getMessage());
                                } else {
                                    log.debug("Снапшот отправлен: topic={}, partition={}, offset={}",
                                            snapshotsTopic, metadata.partition(), metadata.offset());
                                }
                            });
                        });
                    } catch (Exception e) {
                        log.error("Ошибка обработки события: hubId={}, sensorId={}", event.getHubId(), event.getId(), e);
                    }
                }
                try {
                    consumer.commitSync();
                    log.debug("Коммит смещений выполнен");
                } catch (Exception e) {
                    log.error("Ошибка коммита смещений", e);
                }
            }
        } catch (WakeupException ignored) {
            log.info("Получен сигнал завершения");
        } catch (Exception e) {
            log.error("Критическая ошибка обработки событий", e);
        } finally {
            close();
        }
    }

    @Override
    public void close() {
        running = false;
        log.info("Закрытие AggregationStarter");
        try {
            producer.flush();
            log.info("Продюсер сбросил данные");
            consumer.commitSync();
            log.info("Финальный коммит смещений");
        } catch (Exception e) {
            log.error("Ошибка при закрытии", e);
        } finally {
            consumer.close();
            log.info("Консьюмер закрыт");
            producer.close();
            log.info("Продюсер закрыт");
        }
    }
}