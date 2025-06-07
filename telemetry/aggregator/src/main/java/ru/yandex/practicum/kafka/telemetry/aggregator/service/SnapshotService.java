package ru.yandex.practicum.kafka.telemetry.aggregator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

        if (snapshot.getSensorsState() == null) {
            log.warn("sensorsState для хаба {} был null, инициализируем", hubId);
            snapshot.setSensorsState(new HashMap<>());
        }

        Map<String, SensorStateAvro> sensorsState = snapshot.getSensorsState();
        SensorStateAvro oldState = sensorsState.get(sensorId);

        if (oldState != null) {
            long oldTs = oldState.getTimestamp().toEpochMilli();
            // Расслабляем проверку: пропускаем только явно устаревшие события (разница > 1ms)
            if (oldTs > eventTimestampMillis + 1) {
                log.debug("Пропущено устаревшее событие: старый ts={}, новый ts={}", oldTs, eventTimestampMillis);
                return Optional.empty();
            }

            Object newPayload = event.getPayload();
            Object oldPayload = oldState.getData();

            if (!oldPayload.getClass().equals(newPayload.getClass())) {
                log.warn("Типы payload не совпадают: old={}, new={}, обновляем снапшот",
                        oldPayload.getClass().getSimpleName(), newPayload.getClass().getSimpleName());
            } else if (newPayload instanceof ClimateSensorAvro newClimate &&
                    oldPayload instanceof ClimateSensorAvro oldClimate) {
                if (Objects.equals(oldClimate.getTemperatureC(), newClimate.getTemperatureC()) &&
                        Objects.equals(oldClimate.getHumidity(), newClimate.getHumidity()) &&
                        Objects.equals(oldClimate.getCo2Level(), newClimate.getCo2Level())) {
                    log.debug("ClimateSensorAvro: данные не изменились, пропускаем, tempC={}, humidity={}, co2={}",
                            newClimate.getTemperatureC(), newClimate.getHumidity(), newClimate.getCo2Level());
                    return Optional.empty();
                }
                log.debug("ClimateSensorAvro: данные изменились, oldTempC={}, newTempC={}, oldHum={}, newHum={}",
                        oldClimate.getTemperatureC(), newClimate.getTemperatureC(),
                        oldClimate.getHumidity(), newClimate.getHumidity());
            } else if (newPayload instanceof LightSensorAvro newLight &&
                    oldPayload instanceof LightSensorAvro oldLight) {
                if (Objects.equals(oldLight.getLinkQuality(), newLight.getLinkQuality()) &&
                        Objects.equals(oldLight.getLuminosity(), newLight.getLuminosity())) {
                    log.debug("LightSensorAvro: данные не изменились, пропускаем, luminosity={}, linkQuality={}",
                            newLight.getLuminosity(), newLight.getLinkQuality());
                    return Optional.empty();
                }
                log.debug("LightSensorAvro: данные изменились, oldLum={}, newLum={}",
                        oldLight.getLuminosity(), newLight.getLuminosity());
            } else if (newPayload instanceof MotionSensorAvro newMotion &&
                    oldPayload instanceof MotionSensorAvro oldMotion) {
                if (Objects.equals(oldMotion.getLinkQuality(), newMotion.getLinkQuality()) &&
                        Objects.equals(oldMotion.getMotion(), newMotion.getMotion()) &&
                        Objects.equals(oldMotion.getVoltage(), newMotion.getVoltage())) {
                    log.debug("MotionSensorAvro: данные не изменились, пропускаем, motion={}, voltage={}",
                            newMotion.getMotion(), newMotion.getVoltage());
                    return Optional.empty();
                }
                log.debug("MotionSensorAvro: данные изменились, oldMotion={}, newMotion={}",
                        oldMotion.getMotion(), newMotion.getMotion());
            } else if (newPayload instanceof SwitchSensorAvro newSwitch &&
                    oldPayload instanceof SwitchSensorAvro oldSwitch) {
                if (Objects.equals(oldSwitch.getState(), newSwitch.getState())) {
                    log.debug("SwitchSensorAvro: данные не изменились, пропускаем, state={}", newSwitch.getState());
                    return Optional.empty();
                }
                log.debug("SwitchSensorAvro: данные изменились, oldState={}, newState={}",
                        oldSwitch.getState(), newSwitch.getState());
            } else if (newPayload instanceof TemperatureSensorAvro newTemp &&
                    oldPayload instanceof TemperatureSensorAvro oldTemp) {
                if (Objects.equals(oldTemp.getTemperatureC(), newTemp.getTemperatureC()) &&
                        Objects.equals(oldTemp.getTemperatureF(), newTemp.getTemperatureF())) {
                    log.debug("TemperatureSensorAvro: данные не изменились, пропускаем, tempC={}", newTemp.getTemperatureC());
                    return Optional.empty();
                }
                log.debug("TemperatureSensorAvro: данные изменились, oldTempC={}, newTempC={}",
                        oldTemp.getTemperatureC(), newTemp.getTemperatureC());
            } else {
                log.warn("Неизвестный тип сенсора: {}, обновляем снапшот, payload={}",
                        newPayload.getClass().getSimpleName(), newPayload);
            }
            log.debug("Обновление состояния датчика {}: старое ts={}, новое ts={}", sensorId, oldTs, eventTimestampMillis);
        } else {
            log.debug("Добавление нового датчика в снапшот: {}, payload={}", sensorId, event.getPayload());
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