package ru.yandex.practicum.kafka.telemetry.collector.serialization;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class AvroSerializer<T extends SpecificRecordBase> implements Serializer<T> {

    @Override
    public byte[] serialize(String topic, T data) {
        if (data == null) {
            return null;
        }
        String payloadType = "unknown";
        if (data instanceof ru.yandex.practicum.kafka.telemetry.event.HubEventAvro) {
            payloadType = ((ru.yandex.practicum.kafka.telemetry.event.HubEventAvro) data).getPayload().getClass().getSimpleName();
        } else if (data instanceof ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro) {
            payloadType = ((ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro) data).getPayload().getClass().getSimpleName();
        }
        log.info("Serializing Avro data: type={}, topic={}, payloadType={}",
                data.getClass().getSimpleName(), topic, payloadType);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
            SpecificDatumWriter<T> writer = new SpecificDatumWriter<>(data.getSchema());
            writer.write(data, encoder);
            encoder.flush();
            log.info("Serialized Avro data: type={}, topic={}, payloadType={}, size={} bytes",
                    data.getClass().getSimpleName(), topic, payloadType, out.size());
            return out.toByteArray();
        } catch (IOException e) {
            log.error("Failed to serialize Avro data: type={}, topic={}, payloadType={}",
                    data.getClass().getSimpleName(), topic, payloadType, e);
            throw new RuntimeException("Failed to serialize Avro data", e);
        }
    }
}