package ru.yandex.practicum.commerce.interaction_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Represents a physical address used for delivery or billing.
 *
 * Fields:
 * - `country` – Name of the country.
 * - `city` – Name of the city.
 * - `street` – Street name.
 * - `house` – House number or designation.
 * - `flat` – Apartment, suite or room number.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AddressDto {
    String country;
    String city;
    String street;
    String house;
    String flat;
}
