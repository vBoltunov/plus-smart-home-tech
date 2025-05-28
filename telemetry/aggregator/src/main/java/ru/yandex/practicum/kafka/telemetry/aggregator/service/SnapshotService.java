package ru.yandex.practicum.kafka.telemetry.aggregator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnapshotService {
    private final Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        String hubId = event.getHubId();
        String sensorId = event.getId();
        long eventTimestampMillis = event.getTimestamp();
        Instant eventTimestamp = Instant.ofEpochMilli(eventTimestampMillis);

        // Получаем или создаём снапшот для хаба
        SensorsSnapshotAvro snapshot = snapshots.computeIfAbsent(hubId, k -> {
            SensorsSnapshotAvro newSnapshot = new SensorsSnapshotAvro();
            newSnapshot.setHubId(hubId);
            newSnapshot.setTimestamp(eventTimestamp);
            newSnapshot.setSensorsState(new HashMap<>());
            return newSnapshot;
        });

        // Проверяем текущее состояние датчика
        SensorStateAvro oldState = snapshot.getSensorsState().get(sensorId);
        if (oldState != null && (oldState.getTimestamp().toEpochMilli() >= eventTimestampMillis ||
                oldState.getData().equals(event.getPayload()))) {
            log.debug("No update needed for sensor {} in hub {}", sensorId, hubId);
            return Optional.empty();
        }

        // Создаём новое состояние датчика
        SensorStateAvro newState = new SensorStateAvro();
        newState.setTimestamp(eventTimestamp);
        newState.setData(event.getPayload());

        // Обновляем снапшот
        snapshot.getSensorsState().put(sensorId, newState);
        snapshot.setTimestamp(eventTimestamp);
        snapshots.put(hubId, snapshot);

        log.info("Updated snapshot for hub {} with sensor {}", hubId, sensorId);
        return Optional.of(snapshot);
    }
}