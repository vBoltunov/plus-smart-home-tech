package ru.yandex.practicum.commerce.interaction_api.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

/**
 * Represents a payment in the system.
 *
 * Fields:
 * - `paymentId` – Unique identifier of the payment.
 * - `totalPayment` – Total cost of the payment.
 * - `deliveryTotal` – Cost of delivery.
 * - `feeTotal` – Tax amount (e.g., VAT).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDto {
    UUID paymentId;
    Double totalPayment;
    Double deliveryTotal;
    Double feeTotal;
}