package ru.yandex.practicum.commerce.interaction_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Map;

/**
 * Represents a shopping cart that holds selected products and their quantities.
 *
 * Fields:
 * - `shoppingCartId` – Unique identifier of the shopping cart.
 * - `products` – Map of product identifiers to the quantities added to the cart.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ShoppingCartDto {
    @NotNull
    String shoppingCartId;
    @NotNull
    Map<String, Long> products;
}