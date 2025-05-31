package ru.yandex.practicum.telemetry.analyzer.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.deserializer.SensorsSnapshotDeserializer;
import ru.yandex.practicum.telemetry.analyzer.service.SnapshotHandler;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Slf4j
@Service
public class SnapshotProcessor {
    private final KafkaConsumer<String, SensorsSnapshotAvro> snapshotConsumer;
    private final SnapshotHandler snapshotHandler;

    public SnapshotProcessor(@Value("${spring.kafka.consumer.group-id}") String groupId,
                             @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers,
                             SnapshotHandler snapshotHandler) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SensorsSnapshotDeserializer.class);
        props.put("schema.registry.url", "http://localhost:8081");
        props.put("specific.avro.reader", "true");
        this.snapshotConsumer = new KafkaConsumer<>(props);
        this.snapshotHandler = snapshotHandler;
        snapshotConsumer.subscribe(List.of("telemetry.snapshots.v1"));
    }

    @Scheduled(fixedDelay = 1000)
    public void start() {
        ConsumerRecords<String, SensorsSnapshotAvro> records = snapshotConsumer.poll(Duration.ofMillis(100));
        for (var record : records) {
            try {
                SensorsSnapshotAvro snapshot = record.value();
                log.info("Processing snapshot: hubId={}", snapshot.getHubId());
                snapshotHandler.handle(snapshot);
            } catch (Exception e) {
                log.error("Failed to deserialize snapshot at offset {}", record.offset(), e);
                snapshotConsumer.seek(new TopicPartition(record.topic(), record.partition()), record.offset() + 1);
            }
        }
        snapshotConsumer.commitSync();
    }
}