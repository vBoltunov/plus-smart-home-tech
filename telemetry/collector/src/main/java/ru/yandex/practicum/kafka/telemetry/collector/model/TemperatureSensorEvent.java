package ru.yandex.practicum.kafka.telemetry.collector.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.collector.model.enums.SensorEventType;

/**
 * Represents a temperature sensor event.
 *
 * Captures both Celsius and Fahrenheit temperature readings.
 *
 * Fields:
 * - `temperatureC` - Temperature in Celsius.
 * - `temperatureF` - Temperature in Fahrenheit.
 */
@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TemperatureSensorEvent extends SensorEvent {
    Integer temperatureC;
    Integer temperatureF;

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}