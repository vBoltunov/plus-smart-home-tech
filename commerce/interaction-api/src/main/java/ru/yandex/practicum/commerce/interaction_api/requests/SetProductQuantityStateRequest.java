package ru.yandex.practicum.commerce.interaction_api.requests;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.interaction_api.enums.Availability;

import java.util.UUID;

/**
 * Request to change the availability level of a product in the shopping store.
 *
 * Fields:
 * - `productId` – Identifier of the product whose availability is being updated.
 * - `quantityState` – New availability value to be set (e.g., FEW, MANY).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SetProductQuantityStateRequest {
    UUID productId;
    Availability quantityState;
}