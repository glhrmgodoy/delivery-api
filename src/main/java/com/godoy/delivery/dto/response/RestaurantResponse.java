package com.godoy.delivery.dto.response;

import com.godoy.delivery.domain.enums.RestaurantStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record RestaurantResponse(
        UUID id,
        String name,
        String email,
        String phone,
        String cnpj,
        String address,
        RestaurantStatus status,
        LocalDateTime createdAt

) {
}
