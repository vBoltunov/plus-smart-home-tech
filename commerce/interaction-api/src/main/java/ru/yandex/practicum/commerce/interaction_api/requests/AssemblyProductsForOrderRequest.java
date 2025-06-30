package ru.yandex.practicum.commerce.interaction_api.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.UUID;

/**
 * Request assembly product(s) according to the order.
 *
 * Fields:
 * - `orderId` – Identifier of the order.
 * - `products` – Product(s) to be assembled.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssemblyProductsForOrderRequest {
    @NotNull
    @JsonProperty("orderId")
    UUID orderId;
    @NotNull
    @JsonProperty("products")
    Map<UUID, Long> products;
}
