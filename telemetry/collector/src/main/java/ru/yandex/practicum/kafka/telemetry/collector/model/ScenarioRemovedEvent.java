package ru.yandex.practicum.kafka.telemetry.collector.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.collector.model.enums.HubEventType;

/**
 * Represents an event triggered when a scenario is removed from the hub.
 *
 * Fields:
 * - `name` - Unique name of the scenario being removed.
 */
@Getter
@Setter
@ToString
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ScenarioRemovedEvent extends HubEvent {
    @NotBlank
    String name;

    /**
     * Returns the type of this event.
     *
     * @return {@code SCENARIO_REMOVED}
     */
    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}