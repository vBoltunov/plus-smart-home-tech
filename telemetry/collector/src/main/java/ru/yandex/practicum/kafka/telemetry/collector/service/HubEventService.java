package ru.yandex.practicum.kafka.telemetry.collector.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.collector.config.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.collector.mapper.HubEventMapper;
import ru.yandex.practicum.kafka.telemetry.collector.model.HubEvent;

@Service("hubEventService")
@Slf4j
public class HubEventService implements EventService {
    private final KafkaConfig kafkaConfig;

    public HubEventService(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

    @Override
    public void processEvent(Object event) {
        if (!(event instanceof HubEvent hubEvent)) {
            throw new IllegalArgumentException("Invalid event type: expected HubEvent");
        }
        var avroEvent = HubEventMapper.toAvro(hubEvent);

        kafkaConfig.getProducer().send(new ProducerRecord<>(kafkaConfig.getHubEventsTopic(), hubEvent.getHubId(), avroEvent));
        log.info("Sent hub event to {}: {}", kafkaConfig.getHubEventsTopic(), hubEvent);
    }
}