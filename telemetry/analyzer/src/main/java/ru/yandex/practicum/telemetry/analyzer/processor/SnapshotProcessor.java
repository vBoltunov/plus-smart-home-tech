package ru.yandex.practicum.telemetry.analyzer.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.service.SnapshotHandler;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnapshotProcessor {
    private final KafkaConsumer<String, SensorsSnapshotAvro> snapshotConsumer;
    private final SnapshotHandler snapshotHandler;

    @Value("${kafka.topics.snapshots}")
    private String snapshotsTopic;

    @Scheduled(fixedDelay = 1000, initialDelay = 1000)
    public void start() {
        if (snapshotConsumer.subscription().isEmpty()) {
            snapshotConsumer.subscribe(List.of(snapshotsTopic));
            log.info("Subscribed to snapshots topic: {}", snapshotsTopic);
        }
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