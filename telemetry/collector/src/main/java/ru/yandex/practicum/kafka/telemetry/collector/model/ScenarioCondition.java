package ru.yandex.practicum.kafka.telemetry.collector.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.kafka.telemetry.collector.model.enums.ConditionOperation;
import ru.yandex.practicum.kafka.telemetry.collector.model.enums.ConditionType;

/**
 * Represents a condition within a scenario.
 *
 * Defines logical conditions that trigger scenario actions.
 *
 * Fields:
 * - `sensorId` - Target sensor ID.
 * - `type` - Condition type.
 * - `operation` - Logical operation.
 * - `value` - Optional threshold value.
 */
@Getter
@Setter
@ToString
public class ScenarioCondition {
    @NotBlank
    private String sensorId;
    @NotNull
    private ConditionType type;
    @NotNull
    private ConditionOperation operation;
    private Object value;
}