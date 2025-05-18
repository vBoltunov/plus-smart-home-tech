package ru.yandex.practicum.kafka.telemetry.collector.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.kafka.telemetry.collector.model.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Slf4j
public class SensorEventMapper {

    public static SensorEventAvro toAvro(SensorEvent event) {
        log.info("Маппинг события датчика: {}, класс: {}, тип: {}", event, event.getClass().getSimpleName(), event.getType());

        SensorEventAvro.Builder builder = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp().toEpochMilli());

        if (event instanceof ClimateSensorEvent climate) {
            log.info("Маппинг ClimateSensorEvent: temperatureC={}, humidity={}, co2Level={}",
                    climate.getTemperatureC(), climate.getHumidity(), climate.getCo2Level());
            builder.setPayload(ClimateSensorAvro.newBuilder()
                    .setTemperatureC(climate.getTemperatureC() != null ? climate.getTemperatureC() : 0)
                    .setHumidity(climate.getHumidity() != null ? climate.getHumidity() : 0)
                    .setCo2Level(climate.getCo2Level() != null ? climate.getCo2Level() : 0)
                    .build());
        } else if (event instanceof LightSensorEvent light) {
            log.info("Маппинг LightSensorEvent: linkQuality={}, luminosity={}",
                    light.getLinkQuality(), light.getLuminosity());
            builder.setPayload(LightSensorAvro.newBuilder()
                    .setLinkQuality(light.getLinkQuality() != null ? light.getLinkQuality() : 0)
                    .setLuminosity(light.getLuminosity() != null ? light.getLuminosity() : 0)
                    .build());
        } else if (event instanceof MotionSensorEvent motion) {
            log.info("Маппинг MotionSensorEvent: linkQuality={}, motion={}, voltage={}",
                    motion.getLinkQuality(), motion.getMotion(), motion.getVoltage());
            builder.setPayload(MotionSensorAvro.newBuilder()
                    .setLinkQuality(motion.getLinkQuality() != null ? motion.getLinkQuality() : 0)
                    .setMotion(motion.getMotion() != null ? motion.getMotion() : false)
                    .setVoltage(motion.getVoltage() != null ? motion.getVoltage() : 0)
                    .build());
        } else if (event instanceof SwitchSensorEvent sw) {
            log.info("Маппинг SwitchSensorEvent: state={}", sw.getState());
            builder.setPayload(SwitchSensorAvro.newBuilder()
                    .setState(sw.getState() != null ? sw.getState() : false)
                    .build());
        } else if (event instanceof TemperatureSensorEvent temp) {
            log.info("Маппинг TemperatureSensorEvent: temperatureC={}, temperatureF={}",
                    temp.getTemperatureC(), temp.getTemperatureF());
            builder.setPayload(TemperatureSensorAvro.newBuilder()
                    .setTemperatureC(temp.getTemperatureC() != null ? temp.getTemperatureC() : 0)
                    .setTemperatureF(temp.getTemperatureF() != null ? temp.getTemperatureF() : 0)
                    .build());
        } else {
            log.error("Неподдерживаемый тип события датчика: {}", event.getClass().getSimpleName());
            throw new IllegalArgumentException("Неподдерживаемый тип события датчика: " + event.getClass().getSimpleName());
        }

        log.info("Событие {} замаплено в payload типа {}", event.getClass().getSimpleName(), builder.getPayload().getClass().getSimpleName());
        return builder.build();
    }
}