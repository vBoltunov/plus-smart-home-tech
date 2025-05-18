package ru.yandex.practicum.kafka.telemetry.collector.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.collector.config.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.collector.mapper.SensorEventMapper;
import ru.yandex.practicum.kafka.telemetry.collector.model.SensorEvent;

@Service("sensorEventService")
@Slf4j
public class SensorEventService implements EventService {
    private final KafkaConfig kafkaConfig;

    public SensorEventService(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

    @Override
    public void processEvent(Object event) {
        if (!(event instanceof SensorEvent sensorEvent)) {
            throw new IllegalArgumentException("Invalid event type: expected SensorEvent");
        }
        var avroEvent = SensorEventMapper.toAvro(sensorEvent);
        kafkaConfig.getProducer().send(new ProducerRecord<>(kafkaConfig.getSensorEventsTopic(), sensorEvent.getId(), avroEvent));
        log.info("Sent sensor event to {}: {}", kafkaConfig.getSensorEventsTopic(), sensorEvent);
    }
}