package ru.yandex.practicum.kafka.telemetry.collector.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.collector.mapper.HubEventMapper;
import ru.yandex.practicum.kafka.telemetry.collector.mapper.SensorEventMapper;
import ru.yandex.practicum.kafka.telemetry.collector.model.HubEvent;
import ru.yandex.practicum.kafka.telemetry.collector.model.SensorEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class CollectorService {
    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;
    private static final String SENSOR_TOPIC = "telemetry.sensors.v1";
    private static final String HUB_TOPIC = "telemetry.hubs.v1";

    public void processSensorEvent(SensorEvent event) {
        try {
            var avroEvent = SensorEventMapper.toAvro(event);
            kafkaTemplate.send(SENSOR_TOPIC, event.getId(), avroEvent)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to send sensor event to Kafka: {}", ex.getMessage());
                        } else {
                            log.info("Sent sensor event to {}: {}", SENSOR_TOPIC, event);
                        }
                    });
        } catch (Exception e) {
            log.error("Error processing sensor event: {}", e.getMessage());
            throw new RuntimeException("Failed to process sensor event", e);
        }
    }

    public void processHubEvent(HubEvent event) {
        try {
            var avroEvent = HubEventMapper.toAvro(event);
            kafkaTemplate.send(HUB_TOPIC, event.getHubId(), avroEvent)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to send hub event to Kafka: {}", ex.getMessage());
                        } else {
                            log.info("Sent hub event to {}: {}", HUB_TOPIC, event);
                        }
                    });
        } catch (Exception e) {
            log.error("Error processing hub event: {}", e.getMessage());
            throw new RuntimeException("Failed to process hub event", e);
        }
    }
}