package ru.yandex.practicum.commerce.shopping_cart.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Represents a single product entry within a shopping cart.
 *
 * This entity links a product ID to its quantity and associates it
 * with a specific shopping cart instance.
 *
 * Fields:
 * - `id` – Unique identifier of the cart item.
 * - `cart` – The parent shopping cart to which this item belongs.
 * - `productId` – Unique ID of the product added to the cart.
 * - `quantity` – Number of units of this product in the cart.
 */
@Entity
@Table(name = "cart_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    ShoppingCart cart;

    @Column(name = "product_id", nullable = false)
    UUID productId;

    @Column(nullable = false)
    Long quantity;
}