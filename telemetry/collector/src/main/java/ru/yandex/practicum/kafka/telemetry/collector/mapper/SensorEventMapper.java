package ru.yandex.practicum.kafka.telemetry.collector.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.kafka.telemetry.collector.model.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Slf4j
public class SensorEventMapper {

    public static SensorEventAvro toAvro(SensorEvent event) {
        log.info("Маппинг события датчика: {}, класс: {}", event, event.getClass().getSimpleName());

        SensorEventAvro.Builder builder = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp().toEpochMilli());

        if (event instanceof ClimateSensorEvent climate) {
            builder.setPayload(ClimateSensorAvro.newBuilder()
                    .setTemperatureC(climate.getTemperatureC())
                    .setHumidity(climate.getHumidity())
                    .setCo2Level(climate.getCo2Level())
                    .build());
        } else if (event instanceof LightSensorEvent light) {
            builder.setPayload(LightSensorAvro.newBuilder()
                    .setLinkQuality(light.getLinkQuality())
                    .setLuminosity(light.getLuminosity())
                    .build());
        } else if (event instanceof MotionSensorEvent motion) {
            builder.setPayload(MotionSensorAvro.newBuilder()
                    .setLinkQuality(motion.getLinkQuality())
                    .setMotion(motion.getMotion())
                    .setVoltage(motion.getVoltage())
                    .build());
        } else if (event instanceof SwitchSensorEvent sw) {
            builder.setPayload(SwitchSensorAvro.newBuilder()
                    .setState(sw.getState())
                    .build());
        } else if (event instanceof TemperatureSensorEvent temp) {
            builder.setPayload(TemperatureSensorAvro.newBuilder()
                    .setTemperatureC(temp.getTemperatureC())
                    .setTemperatureF(temp.getTemperatureF())
                    .build());
        } else {
            log.error("Неподдерживаемый тип события датчика: {}", event.getClass().getSimpleName());
            throw new IllegalArgumentException("Неподдерживаемый тип события датчика: " + event.getClass().getSimpleName());
        }

        return builder.build();
    }
}