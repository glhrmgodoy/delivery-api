package com.godoy.delivery.dto.response;

import com.godoy.delivery.domain.enums.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        ProductStatus status,
        String restaurantName,
        String categoryName,
        LocalDateTime createdAt
) {
}
