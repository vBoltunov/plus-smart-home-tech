package ru.yandex.practicum.kafka.telemetry.aggregator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.*;

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

        log.debug("Обновление снапшота: hubId={}, sensorId={}, timestamp={}", hubId, sensorId, eventTimestampMillis);

        SensorsSnapshotAvro snapshot = snapshots.computeIfAbsent(hubId, id -> {
            log.info("Создан новый снапшот для хаба: {}", hubId);
            SensorsSnapshotAvro snap = new SensorsSnapshotAvro();
            snap.setHubId(hubId);
            snap.setTimestamp(Instant.ofEpochMilli(eventTimestampMillis));
            snap.setSensorsState(new HashMap<>());
            return snap;
        });

        Map<String, SensorStateAvro> sensorsState = snapshot.getSensorsState();
        SensorStateAvro oldState = sensorsState.get(sensorId);

        if (oldState != null) {
            long oldTs = oldState.getTimestamp().toEpochMilli();
            if (oldTs >= eventTimestampMillis) {
                log.debug("Пропущено устаревшее событие: старый ts={}, новый ts={}", oldTs, eventTimestampMillis);
                return Optional.empty();
            }

            Object newPayload = event.getPayload();
            Object oldPayload = oldState.getData();

            if (!oldPayload.getClass().equals(newPayload.getClass())) {
                log.warn("Типы payload не совпадают: old={}, new={}", oldPayload.getClass(), newPayload.getClass());
            } else if (newPayload instanceof ClimateSensorAvro newClimate &&
                    oldPayload instanceof ClimateSensorAvro oldClimate) {
                if (oldClimate.getTemperatureC() == newClimate.getTemperatureC() &&
                        oldClimate.getHumidity() == newClimate.getHumidity() &&
                        oldClimate.getCo2Level() == newClimate.getCo2Level()) {
                    log.debug("ClimateSensorAvro: данные не изменились");
                    return Optional.empty();
                }
            } else if (newPayload instanceof LightSensorAvro newLight &&
                    oldPayload instanceof LightSensorAvro oldLight) {
                if (oldLight.getLinkQuality() == newLight.getLinkQuality() &&
                        oldLight.getLuminosity() == newLight.getLuminosity()) {
                    log.debug("LightSensorAvro: данные не изменились");
                    return Optional.empty();
                }
            } else if (newPayload instanceof MotionSensorAvro newMotion &&
                    oldPayload instanceof MotionSensorAvro oldMotion) {
                if (oldMotion.getLinkQuality() == newMotion.getLinkQuality() &&
                        oldMotion.getMotion() == newMotion.getMotion() &&
                        oldMotion.getVoltage() == newMotion.getVoltage()) {
                    log.debug("MotionSensorAvro: данные не изменились");
                    return Optional.empty();
                }
            } else if (newPayload instanceof SwitchSensorAvro newSwitch &&
                    oldPayload instanceof SwitchSensorAvro oldSwitch) {
                if (oldSwitch.getState() == newSwitch.getState()) {
                    log.debug("SwitchSensorAvro: данные не изменились");
                    return Optional.empty();
                }
            } else if (newPayload instanceof TemperatureSensorAvro newTemp &&
                    oldPayload instanceof TemperatureSensorAvro oldTemp) {
                if (oldTemp.getTemperatureC() == newTemp.getTemperatureC() &&
                        oldTemp.getTemperatureF() == newTemp.getTemperatureF()) {
                    log.debug("TemperatureSensorAvro: данные не изменились");
                    return Optional.empty();
                }
            } else {
                log.warn("Необрабатываемый тип сенсора: {}", newPayload.getClass().getSimpleName());
            }
            log.debug("Обновление состояния датчика {}: старое ts={}, новое ts={}", sensorId, oldTs, eventTimestampMillis);
        } else {
            log.debug("Добавление нового датчика в снапшот: {}", sensorId);
        }

        SensorStateAvro newState = new SensorStateAvro();
        newState.setTimestamp(Instant.ofEpochMilli(eventTimestampMillis));
        newState.setData(event.getPayload());
        sensorsState.put(sensorId, newState);
        snapshot.setTimestamp(Instant.ofEpochMilli(eventTimestampMillis));
        log.info("Снапшот обновлён для хаба {}: sensorId={}, timestamp={}", hubId, sensorId, eventTimestampMillis);
        return Optional.of(snapshot);
    }
}