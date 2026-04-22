package com.godoy.delivery.dto.response;

import com.godoy.delivery.domain.enums.CategoryType;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponse(
        UUID id,
        CategoryType name,
        String description,
        LocalDateTime createdAt
) {
}
