package ru.yandex.practicum.commerce.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.interaction_api.client.OrderClient;
import ru.yandex.practicum.commerce.interaction_api.dto.OrderDto;
import ru.yandex.practicum.commerce.interaction_api.enums.OrderState;
import ru.yandex.practicum.commerce.interaction_api.requests.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.interaction_api.requests.ProductReturnRequest;
import ru.yandex.practicum.commerce.order.service.OrderService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderClient {
    private final OrderService orderService;

    @Override
    public List<OrderDto> getClientOrders(String username) {
        return orderService.getClientOrders(username);
    }

    @Override
    public OrderDto createNewOrder(@Valid CreateNewOrderRequest request) {
        return orderService.createNewOrder(request);
    }

    @Override
    public OrderDto productReturn(@Valid ProductReturnRequest productReturnRequest) {
        return orderService.productReturn(productReturnRequest);
    }

    @Override
    public OrderDto payment(@RequestBody Map<String, UUID> request) {
        return orderService.payment(request);
    }

    @Override
    public OrderDto paymentFailed(UUID orderId) {
        return orderService.paymentFailed(orderId);
    }

    @Override
    public OrderDto delivery(@RequestBody Map<String, UUID> request) {
        return orderService.delivery(request);
    }

    @Override
    public OrderDto deliveryFailed(UUID orderId) {
        return orderService.deliveryFailed(orderId);
    }

    @Override
    public OrderDto complete(UUID orderId) {
        return orderService.complete(orderId);
    }

    @Override
    public OrderDto calculateTotalCost(UUID orderId) {
        return orderService.calculateTotalCost(orderId);
    }

    @Override
    public OrderDto calculateDeliveryCost(UUID orderId) {
        return orderService.calculateDeliveryCost(orderId);
    }

    @Override
    public OrderDto assembly(@RequestBody Map<String, UUID> request) {
        return orderService.assembly(request);
    }

    @Override
    public OrderDto assemblyFailed(UUID orderId) {
        return orderService.assemblyFailed(orderId);
    }

    @Override
    public OrderDto updatePaymentStatus(UUID orderId, OrderState status) {
        return orderService.updatePaymentStatus(orderId, status);
    }
}