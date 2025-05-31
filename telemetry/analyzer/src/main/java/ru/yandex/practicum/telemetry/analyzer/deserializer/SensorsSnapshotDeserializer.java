package ru.yandex.practicum.telemetry.analyzer.deserializer;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.common.serialization.Deserializer;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Map;

public class SensorsSnapshotDeserializer implements Deserializer<SensorsSnapshotAvro> {
    private final KafkaAvroDeserializer avroDeserializer = new KafkaAvroDeserializer();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        avroDeserializer.configure(configs, isKey);
    }

    @Override
    public SensorsSnapshotAvro deserialize(String topic, byte[] data) {
        return (SensorsSnapshotAvro) avroDeserializer.deserialize(topic, data);
    }

    @Override
    public void close() {
        avroDeserializer.close();
    }
}