package ru.yandex.practicum.commerce.interaction_api.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.interaction_api.enums.OrderState;

import java.util.Map;
import java.util.UUID;

/**
 * Represents an order in the system.
 *
 * Fields:
 * - `orderId` – Unique identifier of the order.
 * - `shoppingCartId` – Identifier of the shopping cart, nullable.
 * - `products` – Map of product identifiers to their quantities.
 * - `paymentId` – Identifier of the payment, nullable.
 * - `deliveryId` – Identifier of the delivery, nullable.
 * - `state` – Status of the order (e.g., NEW, PAID, DELIVERED).
 * - `deliveryWeight` – Total weight of the delivery in kilograms.
 * - `deliveryVolume` – Total volume of the delivery in cubic meters.
 * - `fragile` – Indicates if the order contains fragile items.
 * - `totalPrice` – Total cost of the order.
 * - `deliveryPrice` – Cost of delivery.
 * - `productPrice` – Cost of products in the order.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {
    UUID orderId;
    UUID shoppingCartId;
    Map<String, Long> products;
    UUID paymentId;
    UUID deliveryId;
    OrderState state;
    Double deliveryWeight;
    Double deliveryVolume;
    Boolean fragile;
    Double totalPrice;
    Double deliveryPrice;
    Double productPrice;
}
