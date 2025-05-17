package ru.yandex.practicum.kafka.telemetry.collector.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.kafka.telemetry.collector.model.enums.ActionType;

/**
 * Represents an action performed by a device.
 *
 * This class defines an action triggered by a device, such as turning it on/off or adjusting a setting.
 *
 * Fields:
 * - `sensorId` - Unique identifier of the target sensor.
 * - `type` - The type of action being performed.
 * - `value` - Optional numeric value associated with the action.
 */
@Getter
@Setter
@ToString
public class DeviceAction {
    @NotBlank
    private String sensorId;
    @NotNull
    private ActionType type;
    private Integer value;
}