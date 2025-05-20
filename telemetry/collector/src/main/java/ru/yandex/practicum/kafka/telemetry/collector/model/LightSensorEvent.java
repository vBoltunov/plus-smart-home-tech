package ru.yandex.practicum.kafka.telemetry.collector.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.collector.model.enums.SensorEventType;

/**
 * Represents a light sensor event in the system.
 *
 * Includes measurements for link quality and ambient light levels.
 *
 * Fields:
 * - `linkQuality` - Signal strength metric.
 * - `luminosity` - Light intensity measured by the sensor.
 */
@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class LightSensorEvent extends SensorEvent {
    Integer linkQuality;
    Integer luminosity;

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}