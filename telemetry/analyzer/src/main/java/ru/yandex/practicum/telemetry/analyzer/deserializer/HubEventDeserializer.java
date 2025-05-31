package ru.yandex.practicum.telemetry.analyzer.deserializer;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.common.serialization.Deserializer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.util.Map;

public class HubEventDeserializer implements Deserializer<HubEventAvro> {
    private final KafkaAvroDeserializer avroDeserializer;

    public HubEventDeserializer() {
        this.avroDeserializer = new KafkaAvroDeserializer();
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        avroDeserializer.configure(configs, isKey);
    }

    @Override
    public HubEventAvro deserialize(String topic, byte[] data) {
        return (HubEventAvro) avroDeserializer.deserialize(topic, data);
    }

    @Override
    public void close() {
        avroDeserializer.close();
    }
}