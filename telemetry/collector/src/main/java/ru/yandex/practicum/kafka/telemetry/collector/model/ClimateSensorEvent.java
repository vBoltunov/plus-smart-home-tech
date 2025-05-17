package ru.yandex.practicum.kafka.telemetry.collector.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.kafka.telemetry.collector.model.enums.SensorEventType;

/**
 * Represents a climate sensor event in the telemetry system.
 *
 * This class encapsulates climate-related sensor data, including temperature,
 * humidity, and CO2 levels. It extends {@link SensorEvent} to inherit
 * common event properties such as ID, hub ID, and timestamp.
 *
 * Annotations:
 * - `@Getter` and `@Setter` generate getter and setter methods.
 * - `@ToString(callSuper = true)` provides a string representation, including inherited fields.
 * - `@NotNull` ensures that all climate-related fields are mandatory.
 *
 * Fields:
 * - `temperatureC` - Represents the temperature in Celsius.
 * - `humidity` - Represents the relative humidity percentage.
 * - `co2Level` - Represents the CO2 concentration in parts per million (ppm).
 *
 * Overrides:
 * - `getType()` returns the specific sensor event type as {@link SensorEventType#CLIMATE_SENSOR_EVENT}.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ClimateSensorEvent extends SensorEvent {
    @NotNull
    private Integer temperatureC;
    @NotNull
    private Integer humidity;
    @NotNull
    private Integer co2Level;

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}