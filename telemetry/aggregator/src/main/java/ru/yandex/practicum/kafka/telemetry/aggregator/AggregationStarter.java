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
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.aggregator.service.SnapshotService;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {
    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final KafkaProducer<String, SensorsSnapshotAvro> producer;
    private final SnapshotService snapshotService;

    @Value("${kafka.sensor-events-topic}")
    private String sensorEventsTopic;

    @Value("${kafka.snapshots-topic}")
    private String snapshotsTopic;

    public void start() {
        try {
            consumer.subscribe(Collections.singletonList(sensorEventsTopic));
            log.info("Subscribed to topic: {}", sensorEventsTopic);

            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    SensorEventAvro event = record.value();
                    log.debug("Processing event: {}", event);

                    snapshotService.updateState(event).ifPresent(snapshot -> {
                        ProducerRecord<String, SensorsSnapshotAvro> producerRecord =
                                new ProducerRecord<>(snapshotsTopic, snapshot.getHubId(), snapshot);
                        producer.send(producerRecord, (metadata, exception) -> {
                            if (exception != null) {
                                log.error("Error sending snapshot to topic {}: {}", snapshotsTopic, exception.getMessage());
                            } else {
                                log.debug("Sent snapshot to topic {}: partition {}, offset {}",
                                        snapshotsTopic, metadata.partition(), metadata.offset());
                            }
                        });
                    });
                }
                consumer.commitSync();
            }
        } catch (WakeupException ignored) {
            log.info("Received shutdown signal");
        } catch (Exception e) {
            log.error("Error during event processing", e);
        } finally {
            try {
                producer.flush();
                consumer.commitSync();
            } finally {
                log.info("Closing consumer");
                consumer.close();
                log.info("Closing producer");
                producer.close();
            }
        }
    }
}