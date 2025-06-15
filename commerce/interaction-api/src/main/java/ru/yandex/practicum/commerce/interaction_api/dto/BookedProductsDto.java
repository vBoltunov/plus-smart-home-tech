package ru.yandex.practicum.commerce.interaction_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Contains product characteristics relevant to delivery calculations.
 *
 * Fields:
 * - `deliveryWeight` – Total weight of all booked items, in kilograms.
 * - `deliveryVolume` – Combined volume of the items, in cubic meters.
 * - `fragile` – Indicates if the booked products require special handling due to fragility.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class BookedProductsDto {
    @NotNull
    Double deliveryWeight;
    @NotNull
    Double deliveryVolume;
    @NotNull
    Boolean fragile;
}
