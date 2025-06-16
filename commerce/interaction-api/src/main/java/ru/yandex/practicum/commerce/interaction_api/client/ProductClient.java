package ru.yandex.practicum.commerce.interaction_api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.commerce.interaction_api.dto.ProductDto;
import ru.yandex.practicum.commerce.interaction_api.enums.Availability;

import java.util.UUID;

@FeignClient(name = "shopping-store")
public interface ProductClient {
    String PRODUCT_ENDPOINT = "/api/v1/shopping-store";

    @GetMapping(PRODUCT_ENDPOINT)
    Page<ProductDto> getProducts(@RequestParam String category, Pageable pageable);

    @GetMapping(PRODUCT_ENDPOINT + "/{productId}")
    ProductDto getProduct(@PathVariable UUID productId);

    @PutMapping(PRODUCT_ENDPOINT)
    ProductDto createProduct(@RequestBody ProductDto product);

    @PostMapping(PRODUCT_ENDPOINT)
    ProductDto updateProduct(@RequestBody ProductDto product);

    @PostMapping(PRODUCT_ENDPOINT + "/removeProductFromStore")
    boolean removeProductFromStore(@RequestBody UUID productId);

    @PostMapping(PRODUCT_ENDPOINT + "/quantityState")
    boolean setProductQuantityState(@RequestParam UUID productId, @RequestParam Availability quantityState);
}