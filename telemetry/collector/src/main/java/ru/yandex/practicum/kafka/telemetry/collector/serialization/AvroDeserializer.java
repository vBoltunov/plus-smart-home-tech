package ru.yandex.practicum.kafka.telemetry.collector.serialization;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

@Slf4j
public class AvroDeserializer<T extends SpecificRecordBase> implements Deserializer<T> {

    private final Class<T> type;

    public AvroDeserializer(Class<T> type) {
        this.type = type;
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(data, null);
            SpecificDatumReader<T> reader = new SpecificDatumReader<>(type);
            T result = reader.read(null, decoder);
            log.info("Deserialized Avro data: type={}, topic={}, payload={}",
                    type.getSimpleName(), topic,
                    result instanceof ru.yandex.practicum.kafka.telemetry.event.HubEventAvro ?
                            ((ru.yandex.practicum.kafka.telemetry.event.HubEventAvro) result).getPayload().getClass().getSimpleName() :
                            result instanceof ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro ?
                                    ((ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro) result).getPayload().getClass().getSimpleName() : "unknown");
            return result;
        } catch (IOException e) {
            log.error("Failed to deserialize Avro data: type={}, topic={}", type.getSimpleName(), topic, e);
            throw new RuntimeException("Failed to deserialize Avro data", e);
        }
    }
}