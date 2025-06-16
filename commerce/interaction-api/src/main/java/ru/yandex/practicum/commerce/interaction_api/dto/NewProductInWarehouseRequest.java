package ru.yandex.practicum.commerce.interaction_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Request to register a new product in the warehouse database.
 *
 * Fields:
 * - `productId` – Unique identifier of the new product.
 * - `fragile` – Whether the product needs special handling.
 * - `dimension` – Physical dimensions of the product (width, height, depth).
 * - `weight` – Product weight in kilograms; must be greater than 0.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class NewProductInWarehouseRequest {
    @NotNull
    UUID productId;
    Boolean fragile;
    @NotNull
    DimensionDto dimension;
    @NotNull
    @Min(1)
    Double weight;
}
