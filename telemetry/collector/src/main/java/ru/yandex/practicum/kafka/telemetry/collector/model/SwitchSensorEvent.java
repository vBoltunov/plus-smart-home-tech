package ru.yandex.practicum.kafka.telemetry.collector.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.collector.model.enums.SensorEventType;

/**
 * Represents a switch sensor event, capturing state changes.
 *
 * Fields:
 * - `state` - Boolean value indicating whether the switch is on or off.
 */
@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SwitchSensorEvent extends SensorEvent {
    @NotNull
    Boolean state;

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}