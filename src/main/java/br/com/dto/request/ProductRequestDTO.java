package br.com.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ProductRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String name,
        @NotBlank(message = "Descrição é obrigatória")
        String description,
        Double price,
        String measurementUnit,
        boolean active
) {
}