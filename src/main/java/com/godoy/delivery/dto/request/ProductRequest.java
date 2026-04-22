package com.godoy.delivery.dto.request;

import com.godoy.delivery.domain.enums.ProductStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRequest(
        @NotBlank(message = "Nome é obrigatório")
        String name,

        String description,

        @NotNull(message = "Preço é obrigatório")
        @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
        BigDecimal price,

        @NotNull(message = "Status do produto é obrigatório")
        ProductStatus status,

        @NotNull(message = "ID do restaurante é obrigatório")
        UUID restaurantId,

        @NotNull(message = "ID da categoria é obrigatório")
        UUID categoryId
) {
}
