package ru.yandex.practicum.kafka.telemetry.collector.serialization;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Serializer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class AvroSerializer<T extends SpecificRecordBase> implements Serializer<T> {

    @Override
    public byte[] serialize(String topic, T data) {
        if (data == null) {
            return null;
        }
        log.info("Serializing Avro data: type={}, topic={}, payload={}",
                data.getClass().getSimpleName(), topic,
                data instanceof HubEventAvro ? ((HubEventAvro) data).getPayload() :
                        data instanceof SensorEventAvro ? ((SensorEventAvro) data).getPayload() : "unknown");
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
            SpecificDatumWriter<T> writer = new SpecificDatumWriter<>(data.getSchema());
            writer.write(data, encoder);
            encoder.flush();
            log.info("Serialized Avro data: type={}, topic={}, size={} bytes",
                    data.getClass().getSimpleName(), topic, out.size());
            return out.toByteArray();
        } catch (IOException e) {
            log.error("Failed to serialize Avro data: type={}, topic={}",
                    data.getClass().getSimpleName(), topic, e);
            throw new RuntimeException("Failed to serialize Avro data", e);
        }
    }
}