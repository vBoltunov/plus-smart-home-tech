package ru.yandex.practicum.kafka.telemetry.collector.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.collector.model.enums.HubEventType;

import java.util.List;

/**
 * Represents an event triggered when a new scenario is added to the hub.
 *
 * Fields:
 * - `name` - Unique name of the scenario.
 * - `conditions` - List of conditions that must be met for the scenario to activate.
 * - `actions` - List of device actions executed when the scenario triggers.
 */
@Getter
@Setter
@ToString
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ScenarioAddedEvent extends HubEvent {
    @NotBlank
    String name;
    @NotEmpty
    List<ScenarioCondition> conditions;
    @NotEmpty
    List<DeviceAction> actions;

    /**
     * Returns the type of this event.
     *
     * @return {@code SCENARIO_ADDED}
     */
    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}