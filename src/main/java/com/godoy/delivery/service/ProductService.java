package com.godoy.delivery.service;

import com.godoy.delivery.domain.entity.Category;
import com.godoy.delivery.domain.entity.Product;
import com.godoy.delivery.domain.entity.Restaurant;
import com.godoy.delivery.domain.enums.ProductStatus;
import com.godoy.delivery.domain.enums.RestaurantStatus;
import com.godoy.delivery.dto.request.ProductRequest;
import com.godoy.delivery.dto.response.ProductResponse;
import com.godoy.delivery.exception.BusinessException;
import com.godoy.delivery.exception.NotFoundException;
import com.godoy.delivery.mapper.ProductMapper;
import com.godoy.delivery.messaging.event.ProductEvent;
import com.godoy.delivery.messaging.producer.ProductEventProducer;
import com.godoy.delivery.repository.CategoryRepository;
import com.godoy.delivery.repository.ProductRepository;
import com.godoy.delivery.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ProductEventProducer productEventProducer;

    public ProductResponse create(ProductRequest request) {
        Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurante não encontrado"));

        if (!restaurant.getActive()) {
            throw new BusinessException("Restaurante inavtivo não pode ter produtos");
        }

        if (restaurant.getStatus().equals(RestaurantStatus.CLOSED) &&
                request.status().equals(ProductStatus.AVAILABLE)) {
            throw  new BusinessException("Restaurante fechado não pode ter produtos disponíveis");
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        if (!category.getActive()) {
            throw new BusinessException("Categoria inativa não pode ser utilizada");
        }

        Product product = productMapper.toEntity(request);
        product.setRestaurant(restaurant);
        product.setCategory(category);

        Product saved = productRepository.save(product);

        ProductEvent event = ProductEvent.builder()
                .productId(saved.getId())
                .productName(saved.getName())
                .price(saved.getPrice())
                .status(saved.getStatus())
                .restaurantName(restaurant.getName())
                .categoryName(category.getName().name())
                .eventType("CREATED")
                .occurredAt(LocalDateTime.now())
                .build();

        productEventProducer.sendProductCreated(event);

        return productMapper.toResponse(saved);
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public ProductResponse findById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));
        return productMapper.toResponse(product);
    }

    public List<ProductResponse> findAllByRestaurantId(UUID restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new BusinessException("Restaurante não encontrado");
        }

        return productRepository.findAllByRestaurantId(restaurantId)
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public List<ProductResponse> findAllAvailableByRestaurantId(UUID restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new BusinessException("Restaurante não encontrado");
        }

        return productRepository.findAllByRestaurantIdAndStatus(restaurantId, ProductStatus.AVAILABLE)
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public ProductResponse update(UUID id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurante não encontrado"));

        if (restaurant.getStatus().equals(RestaurantStatus.CLOSED) &&
                request.status().equals(ProductStatus.AVAILABLE)) {
            throw new BusinessException("Restaurante fechado não pode ter produtod disponíveis");
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));

        if (!category.getActive()) {
            throw new BusinessException("Categoria inativa não pode ser utilizada");
        }

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setRestaurant(restaurant);
        product.setCategory(category);

        Product updated = productRepository.save(product);

        ProductEvent event = ProductEvent.builder()
                .productId(updated.getId())
                .productName(updated.getName())
                .price(updated.getPrice())
                .status(updated.getStatus())
                .restaurantName(restaurant.getName())
                .categoryName(category.getName().name())
                .eventType("UPDATED")
                .occurredAt(LocalDateTime.now())
                .build();

        productEventProducer.sendProductUpdated(event);

        return productMapper.toResponse(updated);
    }

    public ProductResponse updateStatus(UUID id, ProductStatus status) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        if (product.getRestaurant().getStatus().equals(RestaurantStatus.CLOSED) &&
                status.equals(ProductStatus.AVAILABLE)) {
            throw new BusinessException("Restaurante fechado não pode ter produtos disponíveis");
        }

        product.setStatus(status);
        Product updated = productRepository.save(product);
        return productMapper.toResponse(updated);
    }

    public void delete(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        product.setActive(false);
        productRepository.save(product);
    }
}
