package ru.yandex.practicum.commerce.interaction_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Represents a request to increase the stock of a specific product in the warehouse.
 *
 * Fields:
 * - `productId` – Unique identifier of the product to be added.
 * - `quantity` – Number of items to add; must be at least 1.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AddProductToWarehouseRequest {
    @NotNull
    UUID productId;
    @NotNull
    @Min(1)
    Long quantity;
}
