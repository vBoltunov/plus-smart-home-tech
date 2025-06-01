package ru.yandex.practicum.telemetry.analyzer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Represents a sensor in a smart home system.
 *
 * This class encapsulates sensor-related data, including unique identifier
 * and associated hub ID.
 *
 * Fields:
 * - `id` - Unique identifier of the sensor.
 * - `hubId` - Identifier of the hub associated with the sensor.
 */
@Entity
@Table(name = "sensors")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Sensor {
    @Id
    @Column(name = "id")
    String id;

    @Column(name = "hub_id", nullable = false)
    String hubId;
}