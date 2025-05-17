package ru.yandex.practicum.kafka.telemetry.collector.mapper;

import ru.yandex.practicum.kafka.telemetry.collector.model.ClimateSensorEvent;
import ru.yandex.practicum.kafka.telemetry.collector.model.LightSensorEvent;
import ru.yandex.practicum.kafka.telemetry.collector.model.MotionSensorEvent;
import ru.yandex.practicum.kafka.telemetry.collector.model.SensorEvent;
import ru.yandex.practicum.kafka.telemetry.collector.model.SwitchSensorEvent;
import ru.yandex.practicum.kafka.telemetry.collector.model.TemperatureSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.*;

public class SensorEventMapper {

    public static SensorEventAvro toAvro(SensorEvent event) {
        SensorEventAvro.Builder builder = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp().toEpochMilli());

        switch (event) {
            case ClimateSensorEvent climate ->
                    builder.setPayload(ClimateSensorAvro.newBuilder()
                            .setTemperatureC(climate.getTemperatureC())
                            .setHumidity(climate.getHumidity())
                            .setCo2Level(climate.getCo2Level())
                            .build());

            case LightSensorEvent light ->
                    builder.setPayload(LightSensorAvro.newBuilder()
                            .setLinkQuality(light.getLinkQuality())
                            .setLuminosity(light.getLuminosity())
                            .build());

            case MotionSensorEvent motion ->
                    builder.setPayload(MotionSensorAvro.newBuilder()
                            .setLinkQuality(motion.getLinkQuality())
                            .setMotion(motion.getMotion())
                            .setVoltage(motion.getVoltage())
                            .build());

            case SwitchSensorEvent sw ->
                    builder.setPayload(SwitchSensorAvro.newBuilder()
                            .setState(sw.getState())
                            .build());

            case TemperatureSensorEvent temp ->
                    builder.setPayload(TemperatureSensorAvro.newBuilder()
                            .setTemperatureC(temp.getTemperatureC())
                            .setTemperatureF(temp.getTemperatureF())
                            .build());

            default ->
                    throw new IllegalArgumentException("Unknown sensor event type: " + event.getClass().getSimpleName());
        }

        return builder.build();
    }
}