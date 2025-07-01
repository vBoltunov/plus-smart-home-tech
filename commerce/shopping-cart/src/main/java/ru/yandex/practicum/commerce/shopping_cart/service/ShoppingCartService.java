package ru.yandex.practicum.commerce.shopping_cart.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.interaction_api.client.WarehouseClient;
import ru.yandex.practicum.commerce.interaction_api.requests.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.interaction_api.dto.ShoppingCartDto;
import ru.yandex.practicum.commerce.interaction_api.enums.Status;
import ru.yandex.practicum.commerce.interaction_api.exceptions.NoProductsInShoppingCartException;
import ru.yandex.practicum.commerce.interaction_api.exceptions.NotAuthorizedUserException;
import ru.yandex.practicum.commerce.interaction_api.exceptions.ServiceUnavailableException;
import ru.yandex.practicum.commerce.shopping_cart.model.CartItem;
import ru.yandex.practicum.commerce.shopping_cart.model.ShoppingCart;
import ru.yandex.practicum.commerce.shopping_cart.repository.CartItemRepository;
import ru.yandex.practicum.commerce.shopping_cart.repository.ShoppingCartRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {
    private final ShoppingCartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper cartMapper;
    private final WarehouseClient warehouseClient;
    private final CartFactory cartFactory;

    @Transactional(readOnly = true)
    public ShoppingCartDto getShoppingCart(String username) {
        validateUsername(username);
        ShoppingCart cart = cartRepository.findByUsernameAndStatus(username, Status.ACTIVE)
                .orElseGet(() -> cartFactory.createNewCart(username));
        return cartMapper.toDto(cart);
    }

    @Transactional
    @CircuitBreaker(name = "warehouse", fallbackMethod = "addProductFallback")
    public ShoppingCartDto addProductToShoppingCart(String username, Map<String, Long> products) {
        validateUsername(username);
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Products map cannot be null or empty");
        }
        for (Map.Entry<String, Long> entry : products.entrySet()) {
            String productId = entry.getKey();
            Long quantity = entry.getValue();
            validateProductId(productId);
            if (quantity == null || quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be positive for product ID: " + productId);
            }
        }

        ShoppingCart cart = cartRepository.findByUsernameAndStatus(username, Status.ACTIVE)
                .orElseGet(() -> cartFactory.createNewCart(username));

        ShoppingCartDto cartDto = ShoppingCartDto.builder()
                .shoppingCartId(cart.getId().toString())
                .products(products)
                .build();
        warehouseClient.checkProductQuantityEnoughForShoppingCart(cartDto);

        for (Map.Entry<String, Long> entry : products.entrySet()) {
            String productId = entry.getKey();
            Long quantity = entry.getValue();
            UUID productUuid = UUID.fromString(productId);
            CartItem item = cartItemRepository.findByCartAndProductId(cart, productUuid)
                    .orElseGet(() -> CartItem.builder()
                            .cart(cart)
                            .productId(productUuid)
                            .quantity(0L)
                            .build());
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
            cart.getItems().add(item);
        }
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    private UUID validateProductId(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("Product ID cannot be null or empty");
        }
        try {
            return UUID.fromString(productId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format for product ID: " + productId);
        }
    }

    /**
     * Fallback method for addProductToShoppingCart when warehouse service is unavailable.
     * @param username the username
     * @param products the products to add
     * @param t the throwable cause
     * @return throws ServiceUnavailableException
     */
    @SuppressWarnings("unused")
    public ShoppingCartDto addProductFallback(String username, Map<String, Long> products, Throwable t) {
        throw new ServiceUnavailableException("Warehouse service is unavailable: " + t.getMessage(), t);
    }

    @Transactional
    public void deactivateCurrentShoppingCart(String username) {
        validateUsername(username);
        ShoppingCart cart = getActiveCart(username);
        cart.setStatus(Status.DEACTIVATE);
        cartRepository.save(cart);
    }

    @Transactional
    public ShoppingCartDto removeFromShoppingCart(String username, List<String> productIds) {
        validateUsername(username);
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Product IDs list cannot be null or empty");
        }

        List<UUID> uuidProductIds = productIds.stream()
                .map(this::validateProductId)
                .toList();

        ShoppingCart cart = getActiveCart(username);
        for (UUID productId : uuidProductIds) {
            CartItem item = cartItemRepository.findByCartAndProductId(cart, productId)
                    .orElseThrow(() -> new NoProductsInShoppingCartException("Product not found in cart: " + productId));
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
        }
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    @Transactional
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        validateUsername(username);
        ShoppingCart cart = getActiveCart(username);
        UUID productId = request.getProductId();
        Long newQuantity = request.getNewQuantity();
        if (newQuantity < 0) {
            throw new IllegalArgumentException("New quantity cannot be negative");
        }
        CartItem item = cartItemRepository.findByCartAndProductId(cart, productId)
                .orElseThrow(() -> new NoProductsInShoppingCartException("Product not found in cart: " + productId));
        if (newQuantity == 0) {
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
        }
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new NotAuthorizedUserException("Username cannot be empty");
        }
    }

    private ShoppingCart getActiveCart(String username) {
        return cartRepository.findByUsernameAndStatus(username, Status.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("No active cart found for user: " + username));
    }
}