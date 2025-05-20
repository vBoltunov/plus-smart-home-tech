package ru.yandex.practicum.kafka.telemetry.collector.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.collector.model.enums.SensorEventType;

/**
 * Represents a motion sensor event.
 *
 * This event captures motion detection along with link quality and voltage data.
 *
 * Fields:
 * - `linkQuality` - Signal strength.
 * - `motion` - Boolean flag indicating motion detection.
 * - `voltage` - Sensor operating voltage.
 */
@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class MotionSensorEvent extends SensorEvent {
    Integer linkQuality;
    Boolean motion;
    Integer voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}