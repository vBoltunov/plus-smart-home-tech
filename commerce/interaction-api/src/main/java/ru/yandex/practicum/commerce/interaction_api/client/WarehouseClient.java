package ru.yandex.practicum.commerce.interaction_api.client;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.interaction_api.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.interaction_api.dto.AddressDto;
import ru.yandex.practicum.commerce.interaction_api.dto.BookedProductsDto;
import ru.yandex.practicum.commerce.interaction_api.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.interaction_api.dto.ShoppingCartDto;

@FeignClient(name = "warehouse")
public interface WarehouseClient {
    String WAREHOUSE_ENDPOINT = "/api/v1/warehouse";

    @PutMapping(WAREHOUSE_ENDPOINT)
    void newProductInWarehouse(@Valid @RequestBody NewProductInWarehouseRequest request);

    @PostMapping(WAREHOUSE_ENDPOINT + "/add")
    void addProductToWarehouse(@Valid @RequestBody AddProductToWarehouseRequest request);

    @PostMapping(WAREHOUSE_ENDPOINT + "/check")
    BookedProductsDto checkProductQuantityEnoughForShoppingCart(@Valid @RequestBody ShoppingCartDto cart);

    @GetMapping(WAREHOUSE_ENDPOINT + "/address")
    AddressDto getWarehouseAddress();
}