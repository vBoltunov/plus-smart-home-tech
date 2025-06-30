package ru.yandex.practicum.commerce.warehouse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.interaction_api.client.WarehouseClient;
import ru.yandex.practicum.commerce.interaction_api.requests.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.interaction_api.dto.AddressDto;
import ru.yandex.practicum.commerce.interaction_api.dto.BookedProductsDto;
import ru.yandex.practicum.commerce.interaction_api.requests.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.interaction_api.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.warehouse.service.WarehouseService;

@RestController
@RequiredArgsConstructor
public class WarehouseController implements WarehouseClient {
    private final WarehouseService warehouseService;

    @Override
    public void newProductInWarehouse(@Valid NewProductInWarehouseRequest request) {
        warehouseService.newProductInWarehouse(request);
    }

    @Override
    public void addProductToWarehouse(@Valid AddProductToWarehouseRequest request) {
        warehouseService.addProductToWarehouse(request);
    }

    @Override
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(@Valid ShoppingCartDto cart) {
        return warehouseService.checkProductQuantityEnoughForShoppingCart(cart);
    }

    @Override
    public AddressDto getWarehouseAddress() {
        return warehouseService.getWarehouseAddress();
    }
}