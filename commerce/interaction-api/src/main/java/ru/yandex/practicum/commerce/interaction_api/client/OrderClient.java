package ru.yandex.practicum.commerce.interaction_api.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.commerce.interaction_api.dto.OrderDto;
import ru.yandex.practicum.commerce.interaction_api.enums.OrderState;
import ru.yandex.practicum.commerce.interaction_api.requests.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interaction_api.requests.ProductReturnRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "order")
public interface OrderClient {
    String ORDER_ENDPOINT = "/api/v1/order";

    @GetMapping(ORDER_ENDPOINT)
    List<OrderDto> getClientOrders(@RequestParam("username") String username);

    @PutMapping(ORDER_ENDPOINT)
    OrderDto createNewOrder(@Valid @RequestBody CreateNewOrderRequest request);

    @PostMapping(ORDER_ENDPOINT + "/return")
    OrderDto productReturn(@Valid @RequestBody ProductReturnRequest productReturnRequest);

    @PostMapping(ORDER_ENDPOINT + "/payment")
    OrderDto payment(@RequestBody Map<String, UUID> request);

    @PostMapping(ORDER_ENDPOINT + "/payment/failed")
    OrderDto paymentFailed(@RequestBody UUID orderId);

    @PostMapping(ORDER_ENDPOINT + "/delivery")
    OrderDto delivery(@RequestBody Map<String, UUID> request);

    @PostMapping(ORDER_ENDPOINT + "/delivery/failed")
    OrderDto deliveryFailed(@RequestBody UUID orderId);

    @PostMapping(ORDER_ENDPOINT + "/completed")
    OrderDto complete(@RequestBody UUID orderId);

    @PostMapping(ORDER_ENDPOINT + "/calculate/total")
    OrderDto calculateTotalCost(@RequestBody UUID orderId);

    @PostMapping(ORDER_ENDPOINT + "/calculate/delivery")
    OrderDto calculateDeliveryCost(@RequestBody UUID orderId);

    @PostMapping(ORDER_ENDPOINT + "/assembly")
    OrderDto assembly(@RequestBody Map<String, UUID> request);

    @PostMapping(ORDER_ENDPOINT + "/assembly/failed")
    OrderDto assemblyFailed(@RequestBody UUID orderId);

    @PostMapping(ORDER_ENDPOINT + "/updatePaymentStatus")
    OrderDto updatePaymentStatus(@RequestBody UUID orderId, @RequestParam("status") OrderState status);
}