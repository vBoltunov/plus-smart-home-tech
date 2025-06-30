package ru.yandex.practicum.commerce.interaction_api.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.interaction_api.dto.DeliveryDto;
import ru.yandex.practicum.commerce.interaction_api.dto.OrderDto;

import java.util.UUID;

@FeignClient(name = "delivery")
public interface DeliveryClient {
    String DELIVERY_ENDPOINT = "/api/v1/delivery";

    @PutMapping(DELIVERY_ENDPOINT)
    DeliveryDto planDelivery(@Valid @RequestBody DeliveryDto delivery);

    @PostMapping(DELIVERY_ENDPOINT + "/successful")
    void deliverySuccessful(@RequestBody UUID orderId);

    @PostMapping(DELIVERY_ENDPOINT + "/picked")
    void deliveryPicked(@RequestBody UUID orderId);

    @PostMapping(DELIVERY_ENDPOINT + "/failed")
    void deliveryFailed(@RequestBody UUID orderId);

    @PostMapping(DELIVERY_ENDPOINT + "/cost")
    Double deliveryCost(@Valid @RequestBody OrderDto order);
}