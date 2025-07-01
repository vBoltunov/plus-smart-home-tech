package ru.yandex.practicum.commerce.interaction_api.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.interaction_api.dto.OrderDto;
import ru.yandex.practicum.commerce.interaction_api.dto.PaymentDto;

import java.util.UUID;

@FeignClient(name = "payment")
public interface PaymentClient {
    String PAYMENT_ENDPOINT = "/api/v1/payment";

    @PostMapping(PAYMENT_ENDPOINT)
    PaymentDto payment(@Valid @RequestBody OrderDto order);

    @PostMapping(PAYMENT_ENDPOINT + "/totalCost")
    Double getTotalCost(@Valid @RequestBody OrderDto order);

    @PostMapping(PAYMENT_ENDPOINT + "/refund")
    void paymentSuccess(@RequestBody UUID paymentId);

    @PostMapping(PAYMENT_ENDPOINT + "/productCost")
    Double productCost(@Valid @RequestBody OrderDto order);

    @PostMapping(PAYMENT_ENDPOINT + "/failed")
    void paymentFailed(@RequestBody UUID paymentId);
}