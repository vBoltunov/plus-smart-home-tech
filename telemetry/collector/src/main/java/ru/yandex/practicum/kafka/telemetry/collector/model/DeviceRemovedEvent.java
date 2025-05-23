package ru.yandex.practicum.kafka.telemetry.collector.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.collector.model.enums.HubEventType;

/**
 * Represents an event triggered when a device is removed from the system.
 *
 * Fields:
 * - `id` - Unique identifier of the removed device.
 */
@Getter
@Setter
@ToString
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class DeviceRemovedEvent extends HubEvent {
    @NotBlank
    String id;

    /**
     * Returns the type of this event.
     *
     * @return {@code DEVICE_REMOVED}
     */
    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}