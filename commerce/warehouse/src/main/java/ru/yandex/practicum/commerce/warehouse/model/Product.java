package ru.yandex.practicum.commerce.warehouse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Represents detailed warehouse-level data about a product.
 *
 * Unlike the storefront model, this entity stores physical measurements
 * and handling specifications, such as size, fragility, and inventory.
 *
 * Fields:
 * - `productId` – External identifier matching the product across services.
 * - `width` – Product width in centimeters or meters.
 * - `height` – Product height in centimeters or meters.
 * - `depth` – Product depth in centimeters or meters.
 * - `weight` – Product weight in kilograms.
 * - `fragile` – Indicates whether the product requires special handling.
 * - `quantity` – Total number of units of this product in stock.
 */
@Data
@Entity
@Table(name = "products", schema = "warehouse")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Product {
    @Id
    @Column(name = "product_id")
    UUID productId;

    @Column(nullable = false)
    Double width;

    @Column(nullable = false)
    Double height;

    @Column(nullable = false)
    Double depth;

    @Column(nullable = false)
    Double weight;

    @Column(nullable = false)
    Boolean fragile;

    @Column(nullable = false)
    Long quantity;
}
