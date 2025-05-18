package ru.yandex.practicum.kafka.telemetry.collector.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.kafka.telemetry.collector.model.*;
import ru.yandex.practicum.kafka.telemetry.collector.model.enums.SensorEventType;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Slf4j
public class SensorEventMapper {

    public static SensorEventAvro toAvro(SensorEvent event) {
        log.info("Маппинг события датчика: {}, тип: {}, класс: {}", event, event.getType(), event.getClass().getSimpleName());

        SensorEventAvro.Builder builder = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp().toEpochMilli());

        // Используем SensorEventType для определения типа события
        SensorEventType eventType = event.getType();
        log.debug("Тип события из getType(): {}", eventType);

        switch (eventType) {
            case CLIMATE_SENSOR_EVENT:
                if (!(event instanceof ClimateSensorEvent climate)) {
                    log.error("Несоответствие типа: ожидалось ClimateSensorEvent, получено {}", event.getClass().getSimpleName());
                    throw new IllegalArgumentException("Ожидалось событие ClimateSensorEvent, но получено: " + event.getClass().getSimpleName());
                }
                builder.setPayload(ClimateSensorAvro.newBuilder()
                        .setTemperatureC(climate.getTemperatureC())
                        .setHumidity(climate.getHumidity())
                        .setCo2Level(climate.getCo2Level())
                        .build());
                break;

            case LIGHT_SENSOR_EVENT:
                if (!(event instanceof LightSensorEvent light)) {
                    log.error("Несоответствие типа: ожидалось LightSensorEvent, получено {}", event.getClass().getSimpleName());
                    throw new IllegalArgumentException("Ожидалось событие LightSensorEvent, но получено: " + event.getClass().getSimpleName());
                }
                builder.setPayload(LightSensorAvro.newBuilder()
                        .setLinkQuality(light.getLinkQuality())
                        .setLuminosity(light.getLuminosity())
                        .build());
                break;

            case MOTION_SENSOR_EVENT:
                if (!(event instanceof MotionSensorEvent motion)) {
                    log.error("Несоответствие типа: ожидалось MotionSensorEvent, получено {}", event.getClass().getSimpleName());
                    throw new IllegalArgumentException("Ожидалось событие MotionSensorEvent, но получено: " + event.getClass().getSimpleName());
                }
                builder.setPayload(MotionSensorAvro.newBuilder()
                        .setLinkQuality(motion.getLinkQuality())
                        .setMotion(motion.getMotion())
                        .setVoltage(motion.getVoltage())
                        .build());
                break;

            case SWITCH_SENSOR_EVENT:
                if (!(event instanceof SwitchSensorEvent sw)) {
                    log.error("Несоответствие типа: ожидалось SwitchSensorEvent, получено {}", event.getClass().getSimpleName());
                    throw new IllegalArgumentException("Ожидалось событие SwitchSensorEvent, но получено: " + event.getClass().getSimpleName());
                }
                builder.setPayload(SwitchSensorAvro.newBuilder()
                        .setState(sw.getState())
                        .build());
                break;

            case TEMPERATURE_SENSOR_EVENT:
                if (!(event instanceof TemperatureSensorEvent temp)) {
                    log.error("Несоответствие типа: ожидалось TemperatureSensorEvent, получено {}", event.getClass().getSimpleName());
                    throw new IllegalArgumentException("Ожидалось событие TemperatureSensorEvent, но получено: " + event.getClass().getSimpleName());
                }
                builder.setPayload(TemperatureSensorAvro.newBuilder()
                        .setTemperatureC(temp.getTemperatureC())
                        .setTemperatureF(temp.getTemperatureF())
                        .build());
                break;

            default:
                log.error("Неизвестный тип события датчика: {}", eventType);
                throw new IllegalArgumentException("Неизвестный тип события датчика: " + eventType);
        }

        return builder.build();
    }
}