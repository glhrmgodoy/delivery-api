package com.godoy.delivery.service;

import com.godoy.delivery.domain.entity.Restaurant;
import com.godoy.delivery.domain.enums.ProductStatus;
import com.godoy.delivery.domain.enums.RestaurantStatus;
import com.godoy.delivery.dto.request.RestaurantRequest;
import com.godoy.delivery.dto.response.RestaurantResponse;
import com.godoy.delivery.exception.BusinessException;
import com.godoy.delivery.exception.NotFoundException;
import com.godoy.delivery.mapper.RestaurantMapper;
import com.godoy.delivery.messaging.event.ProductEvent;
import com.godoy.delivery.messaging.producer.ProductEventProducer;
import com.godoy.delivery.repository.ProductRepository;
import com.godoy.delivery.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ProductRepository productRepository;
    private final RestaurantMapper restaurantMapper;
    private final ProductEventProducer productEventProducer;

    public RestaurantResponse create(RestaurantRequest request) {
        if (restaurantRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email já cadastrado");
        }

        if (restaurantRepository.existsByCnpj(request.cnpj())) {
            throw new BusinessException("CNPJ já cadastrado");
        }

        Restaurant restaurant = restaurantMapper.toEntity(request);
        Restaurant saved = restaurantRepository.save(restaurant);

        return restaurantMapper.toResponse(saved);
    }

    public List<RestaurantResponse> findAll() {
        return restaurantRepository.findAll()
                .stream()
                .map(restaurantMapper::toResponse)
                .toList();
    }

    public RestaurantResponse findById(UUID id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurante não encontrado"));
        return restaurantMapper.toResponse(restaurant);
    }

    public RestaurantResponse update(UUID id, RestaurantRequest request) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurante não encontrado"));

        if (!restaurant.getEmail().equals(request.email()) &&
                restaurantRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email já cadastrado");
        }

        if (!restaurant.getCnpj().equals(request.cnpj()) &&
                restaurantRepository.existsByCnpj(request.cnpj())) {
            throw new BusinessException("CNPJ já cadastrado");
        }

        restaurant.setName(request.name());
        restaurant.setEmail(request.email());
        restaurant.setPhone(request.phone());
        restaurant.setCnpj(request.cnpj());
        restaurant.setAddress(request.address());
        restaurant.setStatus(request.status());

        Restaurant updated = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponse(updated);
    }

    public RestaurantResponse updateStatus(UUID id, RestaurantStatus status) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurante não encontrado"));

        restaurant.setStatus(status);

        if (status.equals(RestaurantStatus.CLOSED)) {
            productRepository.findAllByRestaurantId(id)
                    .forEach(product -> {
                        product.setStatus(ProductStatus.UNAVAILABLE);
                        productRepository.save(product);
                    });
        }

        Restaurant update = restaurantRepository.save(restaurant);

        ProductEvent event = ProductEvent.builder()
                .restaurantName(update.getName())
                .status(null)
                .eventType(status.equals(RestaurantStatus.OPEN) ? "RESTAURANT_OPENED" : "RESTAURANT_CLOSED")
                .occurredAt(LocalDateTime.now())
                .build();

        productEventProducer.sendRestaurantStatus(event);

        return restaurantMapper.toResponse(update);
    }

    public void delete(UUID id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurante não encontrado"));
        restaurant.setActive(false);
        restaurantRepository.save(restaurant);
    }
}
