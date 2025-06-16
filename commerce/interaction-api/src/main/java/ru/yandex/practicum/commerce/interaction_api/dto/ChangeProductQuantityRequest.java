package ru.yandex.practicum.commerce.interaction_api.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Request to update the quantity of a product in storage or display.
 *
 * Fields:
 * - `productId` – Identifier of the product to be updated.
 * - `newQuantity` – New quantity to be set for the specified product.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeProductQuantityRequest {
    UUID productId;
    Long newQuantity;
}
