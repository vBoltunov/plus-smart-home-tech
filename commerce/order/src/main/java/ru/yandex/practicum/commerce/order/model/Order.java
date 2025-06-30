package ru.yandex.practicum.commerce.order.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.yandex.practicum.commerce.interaction_api.dto.AddressDto;
import ru.yandex.practicum.commerce.interaction_api.enums.OrderState;

import java.util.Map;
import java.util.UUID;

/**
 * Entity representing an order in the system.
 *
 * Fields:
 * - `id` – Unique identifier of the order.
 * - `username` – The user who placed the order.
 * - `shoppingCartId` – Identifier of the associated shopping cart.
 * - `products` – Map of product IDs to their quantities.
 * - `paymentId` – Identifier of the payment, nullable.
 * - `deliveryId` – Identifier of the delivery, nullable.
 * - `state` – Status of the order (e.g., NEW, PAID).
 * - `deliveryWeight` – Total weight of the delivery in kilograms.
 * - `deliveryVolume` – Total volume of the delivery in cubic meters.
 * - `fragile` – Indicates if the order contains fragile items.
 * - `totalPrice` – Total cost of the order.
 * - `deliveryPrice` – Cost of delivery.
 * - `productPrice` – Cost of products in the order.
 * - `deliveryAddress` – The delivery address (stored as JSONB).
 */
@Entity
@Table(name = "orders", schema = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    String username;

    @Column(name = "shopping_cart_id")
    UUID shoppingCartId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "products", nullable = false)
    Map<UUID, Long> products;

    @Column(name = "payment_id")
    UUID paymentId;

    @Column(name = "delivery_id")
    UUID deliveryId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    OrderState state;

    @Column(name = "delivery_weight")
    Double deliveryWeight;

    @Column(name = "delivery_volume")
    Double deliveryVolume;

    @Column
    Boolean fragile;

    @Column(name = "total_price")
    Double totalPrice;

    @Column(name = "delivery_price")
    Double deliveryPrice;

    @Column(name = "product_price")
    Double productPrice;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "delivery_address", columnDefinition = "jsonb")
    AddressDto deliveryAddress;
}