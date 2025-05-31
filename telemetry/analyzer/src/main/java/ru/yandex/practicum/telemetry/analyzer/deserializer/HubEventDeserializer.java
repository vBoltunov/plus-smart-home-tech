package ru.yandex.practicum.telemetry.analyzer.deserializer;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.common.serialization.Deserializer;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class HubEventDeserializer implements Deserializer<HubEventAvro> {
    private final DecoderFactory decoderFactory = DecoderFactory.get();
    private final Schema schema = HubEventAvro.getClassSchema();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Конфигурация не требуется
    }

    @Override
    public HubEventAvro deserialize(String topic, byte[] data) {
        if (data == null || data.length == 0) {
            log.warn("Received null or empty data for topic: {}", topic);
            return null;
        }
        try {
            Decoder decoder = decoderFactory.binaryDecoder(data, null);
            DatumReader<HubEventAvro> reader = new SpecificDatumReader<>(schema);
            HubEventAvro result = reader.read(null, decoder);
            log.debug("Successfully deserialized HubEventAvro from topic: {}", topic);
            return result;
        } catch (IOException e) {
            log.error("Error deserializing HubEventAvro from topic {}: {}", topic, e.getMessage(), e);
            throw new RuntimeException("Failed to deserialize HubEventAvro", e);
        }
    }

    @Override
    public void close() {
        // Ресурсы не требуются
    }
}