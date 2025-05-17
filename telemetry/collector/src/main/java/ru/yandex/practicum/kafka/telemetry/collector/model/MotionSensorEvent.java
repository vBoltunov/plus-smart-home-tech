package ru.yandex.practicum.kafka.telemetry.collector.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
public class MotionSensorEvent extends SensorEvent {
    @NotNull
    private Integer linkQuality;
    @NotNull
    private Boolean motion;
    @NotNull
    private Integer voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}