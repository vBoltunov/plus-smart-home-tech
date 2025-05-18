package ru.yandex.practicum.kafka.telemetry.collector.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.collector.config.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.collector.mapper.HubEventMapper;
import ru.yandex.practicum.kafka.telemetry.collector.mapper.SensorEventMapper;
import ru.yandex.practicum.kafka.telemetry.collector.model.HubEvent;
import ru.yandex.practicum.kafka.telemetry.collector.model.SensorEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class CollectorService {
    private final KafkaConfig kafkaConfig;

    public void processSensorEvent(SensorEvent event) {
        if (!(event instanceof SensorEvent)) {
            throw new IllegalArgumentException("Invalid event type: expected SensorEvent");
        }
        var avroEvent = SensorEventMapper.toAvro(event);
        kafkaConfig.getProducer().send(new ProducerRecord<>(kafkaConfig.getSensorEventsTopic(), event.getId(), avroEvent));
        log.info("Sent sensor event to {}: {}", kafkaConfig.getSensorEventsTopic(), event);
    }

    public void processHubEvent(HubEvent event) {
        if (!(event instanceof HubEvent)) {
            throw new IllegalArgumentException("Invalid event type: expected HubEvent");
        }
        var avroEvent = HubEventMapper.toAvro(event);
        kafkaConfig.getProducer().send(new ProducerRecord<>(kafkaConfig.getHubEventsTopic(), event.getHubId(), avroEvent));
        log.info("Sent hub event to {}: {}", kafkaConfig.getHubEventsTopic(), event);
    }
}