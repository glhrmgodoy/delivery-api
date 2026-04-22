package com.godoy.delivery.dto.request;

import com.godoy.delivery.domain.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryRequest(
        @NotNull(message = "Nome da categoria é obrigatório")
        CategoryType name,

        @NotBlank(message = "Descrição é obrigatório")
        String description
) {
}
