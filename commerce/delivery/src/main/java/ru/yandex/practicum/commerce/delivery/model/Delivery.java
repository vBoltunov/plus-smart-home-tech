package ru.yandex.practicum.commerce.delivery.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.yandex.practicum.commerce.interaction_api.dto.AddressDto;
import ru.yandex.practicum.commerce.interaction_api.enums.DeliveryState;

import java.util.UUID;

/**
 * Entity representing a delivery in the system.
 *
 * Fields:
 * - `id` – Unique identifier of the delivery.
 * - `orderId` – Identifier of the associated order.
 * - `fromAddress` – Source address of the delivery (serialized as JSON).
 * - `toAddress` – Destination address of the delivery (serialized as JSON).
 * - `deliveryVolume` – Total volume of the delivery.
 * - `deliveryWeight` – Total weight of the delivery.
 * - `fragile` – Indicates if the delivery contains fragile items.
 * - `state` – Current state of the delivery.
 */
@Entity
@Table(name = "deliveries", schema = "delivery")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Delivery {
    @Id
    UUID id;

    @Column(name = "order_id", nullable = false)
    UUID orderId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "from_address", columnDefinition = "jsonb")
    AddressDto fromAddress;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "to_address", columnDefinition = "jsonb")
    AddressDto toAddress;

    @Column(name = "delivery_volume")
    Double deliveryVolume;

    @Column(name = "delivery_weight")
    Double deliveryWeight;

    @Column
    Boolean fragile;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    DeliveryState state;
}