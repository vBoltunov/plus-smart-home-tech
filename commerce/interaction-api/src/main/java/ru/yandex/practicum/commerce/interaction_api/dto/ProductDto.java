package ru.yandex.practicum.commerce.interaction_api.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.interaction_api.enums.Availability;
import ru.yandex.practicum.commerce.interaction_api.enums.Category;
import ru.yandex.practicum.commerce.interaction_api.enums.Status;

import java.util.UUID;

/**
 * Represents a product exposed to clients and consumers via API.
 *
 * Fields:
 * - `productId` – Unique identifier of the product.
 * - `productName` – Human-readable name of the product.
 * - `description` – Optional description or details about the product.
 * - `imageSrc` – URL to a product image or illustration.
 * - `productCategory` – Product category enum indicating the product's type.
 * - `quantityState` – Availability status (e.g., MANY, FEW, ENDED).
 * - `productState` – State of the product lifecycle (e.g., ACTIVE or DEACTIVATE).
 * - `price` – Cost of the product in monetary units.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDto {
    UUID productId;
    String productName;
    String description;
    String imageSrc;
    Category productCategory;
    Availability quantityState;
    Status productState;
    Double price;
}