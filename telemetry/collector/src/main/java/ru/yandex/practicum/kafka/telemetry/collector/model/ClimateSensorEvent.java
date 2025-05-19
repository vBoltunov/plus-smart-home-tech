package ru.yandex.practicum.kafka.telemetry.collector.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.collector.model.enums.SensorEventType;

/**
 * Represents a climate sensor event in the telemetry system.
 *
 * This class encapsulates climate-related sensor data, including temperature,
 * humidity, and CO2 levels. It extends {@link SensorEvent} to inherit
 * common event properties such as ID, hub ID, and timestamp.
 *
 * Fields:
 * - `temperatureC` - Represents the temperature in Celsius.
 * - `humidity` - Represents the relative humidity percentage.
 * - `co2Level` - Represents the CO2 concentration in parts per million (ppm).
 */
@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ClimateSensorEvent extends SensorEvent {
    @NotNull
    Integer temperatureC;
    @NotNull
    Integer humidity;
    @NotNull
    Integer co2Level;

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}