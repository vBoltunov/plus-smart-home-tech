package ru.yandex.practicum.kafka.telemetry.aggregator.serialization;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public class SensorsSnapshotSerializer extends BaseAvroSerializer<SensorsSnapshotAvro> {
    public SensorsSnapshotSerializer() {
        super(SensorsSnapshotAvro.getClassSchema());
    }
}