package ru.yandex.practicum.telemetry.analyzer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;

/**
 * Represents a condition in a smart home scenario.
 *
 * This class encapsulates condition-related data, including type, comparison operation,
 * value, associated scenario, and linked sensor.
 *
 * Fields:
 * - `id` - Unique identifier of the condition.
 * - `type` - Defines the type of condition.
 * - `operation` - Specifies the comparison operation.
 * - `value` - The threshold value for the condition.
 * - `scenario` - The scenario in which the condition is applied.
 * - `sensor` - The sensor associated with the condition.
 */
@Entity
@Table(name = "conditions")
@SecondaryTable(name = "scenario_conditions", pkJoinColumns = @PrimaryKeyJoinColumn(name = "condition_id"))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Condition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    ConditionTypeAvro type;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation")
    ConditionOperationAvro operation;

    @Column(name = "value")
    Integer value;

    @ManyToOne
    @JoinColumn(name = "scenario_id", table = "scenario_conditions")
    Scenario scenario;

    @ManyToOne
    @JoinColumn(name = "sensor_id", table = "scenario_conditions")
    Sensor sensor;
}