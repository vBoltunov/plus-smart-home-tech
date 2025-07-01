package ru.yandex.practicum.commerce.delivery.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.delivery.model.Delivery;
import ru.yandex.practicum.commerce.interaction_api.dto.DeliveryDto;
import ru.yandex.practicum.commerce.interaction_api.dto.OrderDto;
import ru.yandex.practicum.commerce.interaction_api.enums.DeliveryState;

@Component
public class DeliveryMapper {

    public DeliveryDto toDto(Delivery delivery) {
        return DeliveryDto.builder()
                .deliveryId(delivery.getId())
                .fromAddress(delivery.getFromAddress())
                .toAddress(delivery.getToAddress())
                .orderId(delivery.getOrderId())
                .deliveryState(delivery.getState())
                .build();
    }

    public Delivery toEntity(DeliveryDto dto, OrderDto order) {
        return Delivery.builder()
                .id(dto.getDeliveryId())
                .orderId(dto.getOrderId())
                .fromAddress(dto.getFromAddress())
                .toAddress(dto.getToAddress())
                .deliveryVolume(order != null && order.getDeliveryVolume() != null ? order.getDeliveryVolume() : 0.0)
                .deliveryWeight(order != null && order.getDeliveryWeight() != null ? order.getDeliveryWeight() : 0.0)
                .fragile(order != null && order.getFragile() != null && order.getFragile())
                .state(dto.getDeliveryState() != null ? dto.getDeliveryState() : DeliveryState.CREATED)
                .build();
    }
}