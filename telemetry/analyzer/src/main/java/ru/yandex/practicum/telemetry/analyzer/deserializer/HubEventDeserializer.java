package ru.yandex.practicum.telemetry.analyzer.deserializer;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.util.Map;

@Slf4j
public class HubEventDeserializer implements Deserializer<HubEventAvro> {
    private final KafkaAvroDeserializer avroDeserializer;

    public HubEventDeserializer() {
        this.avroDeserializer = new KafkaAvroDeserializer();
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        log.info("Configuring HubEventDeserializer with configs: {}", configs);
        avroDeserializer.configure(configs, isKey);
    }

    @Override
    public HubEventAvro deserialize(String topic, byte[] data) {
        if (data == null || data.length == 0) {
            log.warn("Received null or empty data for topic: {}", topic);
            return null;
        }
        try {
            Object result = avroDeserializer.deserialize(topic, data);
            if (result instanceof HubEventAvro) {
                log.debug("Successfully deserialized HubEventAvro from topic: {}", topic);
                return (HubEventAvro) result;
            } else {
                log.error("Deserialized object is not HubEventAvro: {}", result);
                return null;
            }
        } catch (Exception e) {
            log.error("Error deserializing data from topic {}: {}", topic, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void close() {
        log.info("Closing HubEventDeserializer");
        avroDeserializer.close();
    }
}