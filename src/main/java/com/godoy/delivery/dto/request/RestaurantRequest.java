package com.godoy.delivery.dto.request;

import com.godoy.delivery.domain.enums.RestaurantStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RestaurantRequest(
        @NotBlank(message = "Nome é obrigatório")
        String name,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "Telefone é obrigatório")
        String phone,

        @NotBlank(message = "CNPJ é obrigatório")
        @Size(min = 14, max = 14, message = "CNPJ deve ter 14 caracteres")
        String cnpj,

        @NotBlank(message = "Endereço é obrigatório")
        String address,

        @NotNull(message = "Status do restaurante é obrigatório")
        RestaurantStatus status
) {
}
