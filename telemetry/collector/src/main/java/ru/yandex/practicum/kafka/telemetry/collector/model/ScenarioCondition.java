package ru.yandex.practicum.kafka.telemetry.collector.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ScenarioCondition {
    @NotBlank
    String sensorId;
    @NotNull
    ConditionType type;
    @NotNull
    ConditionOperation operation;

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.CLASS,
            include = JsonTypeInfo.As.PROPERTY,
            property = "@class"
    )
    Object value;
}