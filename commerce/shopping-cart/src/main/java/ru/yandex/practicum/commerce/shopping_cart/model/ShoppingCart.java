package ru.yandex.practicum.commerce.shopping_cart.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.interaction_api.enums.Status;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a shopping cart belonging to a user.
 *
 * Encapsulates the user's selected products, the cart's status,
 * and links to the individual cart items.
 *
 * Fields:
 * - `id` – Unique identifier of the shopping cart.
 * - `username` – The owner (user) associated with the cart.
 * - `status` – Current state of the cart (e.g., ACTIVE, COMPLETED).
 * - `items` – Collection of product entries (cart items) in the cart.
 */

@Entity
@Table(name = "shopping_cart")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "cart_status", nullable = false)
    Status status;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    Set<CartItem> items = new HashSet<>();
}