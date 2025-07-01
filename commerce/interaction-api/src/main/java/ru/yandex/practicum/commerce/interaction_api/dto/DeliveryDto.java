package ru.yandex.practicum.commerce.interaction_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.interaction_api.enums.DeliveryState;

import java.util.UUID;

/**
 * Represents a delivery in the system.
 *
 * Fields:
 * - `deliveryId` – Unique identifier of the delivery.
 * - `fromAddress` – Source address of the delivery.
 * - `toAddress` – Destination address of the delivery.
 * - `orderId` – Identifier of the associated order.
 * - `deliveryState` – Current state of the delivery.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryDto {
    @NotNull
    UUID deliveryId;
    @NotNull
    AddressDto fromAddress;
    @NotNull
    AddressDto toAddress;
    @NotNull
    UUID orderId;
    @NotNull
    DeliveryState deliveryState;
}