package com.godoy.delivery.repository;

import com.godoy.delivery.domain.entity.Product;
import com.godoy.delivery.domain.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findAllByRestaurantId(UUID restaurantId);

    List<Product> findAllByRestaurantIdAndStatus(UUID restaurantId, ProductStatus status);

    boolean existsByCategoryIdAndActiveTrue(UUID categoryId);
}
