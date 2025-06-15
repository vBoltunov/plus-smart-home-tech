package ru.yandex.practicum.commerce.warehouse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interaction_api.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.interaction_api.dto.AddressDto;
import ru.yandex.practicum.commerce.interaction_api.dto.BookedProductsDto;
import ru.yandex.practicum.commerce.interaction_api.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.interaction_api.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interaction_api.exceptions.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.interaction_api.exceptions.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.interaction_api.exceptions.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.commerce.warehouse.model.Product;
import ru.yandex.practicum.commerce.warehouse.repository.ProductRepository;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private static final String[] ADDRESSES = new String[]{"ADDRESS_1", "ADDRESS_2"};
    private static final String CURRENT_ADDRESS = ADDRESSES[Random.from(
            new SecureRandom()).nextInt(0, 2)];

    private final ProductRepository productRepository;

    @Transactional
    public void newProductInWarehouse(NewProductInWarehouseRequest request) {
        Optional<Product> existingProduct = productRepository.findById(request.getProductId());
        if (existingProduct.isPresent()) {
            throw new SpecifiedProductAlreadyInWarehouseException(
                    "Product with ID " + request.getProductId() + " already exists in warehouse");
        }

        Product product = new Product();
        product.setProductId(request.getProductId());
        product.setWidth(request.getDimension().getWidth());
        product.setHeight(request.getDimension().getHeight());
        product.setDepth(request.getDimension().getDepth());
        product.setWeight(request.getWeight());
        product.setFragile(request.getFragile() != null && request.getFragile());
        product.setQuantity(0L);

        productRepository.save(product);
    }

    @Transactional
    public void addProductToWarehouse(AddProductToWarehouseRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                        "Product with ID " + request.getProductId() + " not found in warehouse"));

        product.setQuantity(product.getQuantity() + request.getQuantity());
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto cart) {
        double totalWeight = 0.0;
        double totalVolume = 0.0;
        boolean hasFragile = false;

        for (Map.Entry<String, Long> entry : cart.getProducts().entrySet()) {
            UUID productId = UUID.fromString(entry.getKey());
            Long requestedQuantity = entry.getValue();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                            "Product with ID " + productId + " not found in warehouse"));

            if (product.getQuantity() < requestedQuantity) {
                throw new ProductInShoppingCartLowQuantityInWarehouse(
                        "Insufficient quantity for product ID " + productId + ": requested " +
                                requestedQuantity + ", available " + product.getQuantity());
            }

            totalWeight += product.getWeight() * requestedQuantity;
            totalVolume += product.getWidth() * product.getHeight() * product.getDepth() * requestedQuantity;
            hasFragile = hasFragile || product.getFragile();
        }

        return new BookedProductsDto(totalWeight, totalVolume, hasFragile);
    }

    public AddressDto getWarehouseAddress() {
        return new AddressDto(CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS);
    }
}