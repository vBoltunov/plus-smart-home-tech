package ru.yandex.practicum.telemetry.analyzer.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.service.SnapshotHandler;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotProcessor {
    private final KafkaConsumer<String, SensorsSnapshotAvro> snapshotConsumer;
    private final SnapshotHandler snapshotHandler;

    @Value("${kafka.topics.snapshots}")
    private String snapshotsTopic;

    public void start() {
        try {
            snapshotConsumer.subscribe(List.of(snapshotsTopic));
            log.info("Subscribed to snapshots topic: {}", snapshotsTopic);
            Runtime.getRuntime().addShutdownHook(new Thread(snapshotConsumer::wakeup));

            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = snapshotConsumer.poll(Duration.ofMillis(1000));
                for (var record : records) {
                    SensorsSnapshotAvro snapshot = record.value();
                    log.info("Processing snapshot: hubId={}", snapshot.getHubId());
                    snapshotHandler.handle(snapshot);
                }
                snapshotConsumer.commitSync();
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Error processing snapshots", e);
        } finally {
            try {
                snapshotConsumer.commitSync();
            } finally {
                snapshotConsumer.close();
                log.info("Snapshot consumer closed");
            }
        }
    }
}