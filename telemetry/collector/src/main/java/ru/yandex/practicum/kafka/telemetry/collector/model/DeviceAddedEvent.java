package ru.yandex.practicum.kafka.telemetry.collector.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.collector.model.enums.DeviceType;
import ru.yandex.practicum.kafka.telemetry.collector.model.enums.HubEventType;

/**
 * Represents an event triggered when a new device is added to the system.
 *
 * Fields:
 * - `id` - Unique identifier of the device.
 * - `deviceType` - Type of the device being added.
 */
@Getter
@Setter
@ToString
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class DeviceAddedEvent extends HubEvent {
    @NotBlank
    String id;
    @NotNull
    DeviceType deviceType;

    /**
     * Returns the type of this event.
     *
     * @return {@code DEVICE_ADDED}
     */
    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}