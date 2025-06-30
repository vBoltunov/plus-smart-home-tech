package ru.yandex.practicum.commerce.interaction_api.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

/**
 * Request to return products from an order.
 *
 * Fields:
 * - `orderId` – Identifier of the order to return.
 * - `products` – Map of product identifiers to their quantities to return.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReturnRequest {
    @NotNull
    UUID orderId;
    @NotNull
    Map<String, Long> products;
}
