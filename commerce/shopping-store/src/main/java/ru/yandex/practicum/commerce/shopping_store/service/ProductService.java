package ru.yandex.practicum.commerce.shopping_store.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.interaction_api.dto.ProductDto;
import ru.yandex.practicum.commerce.interaction_api.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.interaction_api.enums.Category;
import ru.yandex.practicum.commerce.interaction_api.enums.Status;
import ru.yandex.practicum.commerce.interaction_api.exceptions.ProductNotFoundException;
import ru.yandex.practicum.commerce.shopping_store.model.Product;
import ru.yandex.practicum.commerce.shopping_store.repository.ProductRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private static final String NOT_FOUND = "Product not found: ";

    public Page<ProductDto> getProducts(String category, Pageable pageable) {
        try {
            Category cat = Category.valueOf(category.toUpperCase());
            return productRepository.findByCategoryAndStatus(cat, Status.ACTIVE, pageable)
                    .map(productMapper::toDto);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category: " + category);
        }
    }

    public ProductDto getProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(NOT_FOUND + productId));
        return productMapper.toDto(product);
    }

    public ProductDto createNewProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        if (product.getStatus() == null) {
            product.setStatus(Status.ACTIVE);
        }
        Product saved = productRepository.save(product);
        return productMapper.toDto(saved);
    }

    public ProductDto updateProduct(ProductDto productDto) {
        UUID id = productDto.getProductId();
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(NOT_FOUND + id));
        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setImageSrc(productDto.getImageSrc());
        product.setCategory(productDto.getProductCategory());
        product.setAvailability(productDto.getQuantityState());
        product.setPrice(productDto.getPrice());
        product.setStatus(productDto.getProductState());
        Product updated = productRepository.save(product);
        return productMapper.toDto(updated);
    }

    public boolean removeProductFromStore(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(NOT_FOUND + productId));
        product.setStatus(Status.DEACTIVATE);
        productRepository.save(product);
        return true;
    }

    public boolean setProductQuantityState(SetProductQuantityStateRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND + request.getProductId()));
        product.setAvailability(request.getQuantityState());
        productRepository.save(product);
        return true;
    }
}