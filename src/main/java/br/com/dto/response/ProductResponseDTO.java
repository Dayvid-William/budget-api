package br.com.dto.response;

import java.time.LocalDateTime;

public record ProductResponseDTO(
        String id,
        String name,
        String description,
        Double price,
        String measurementUnit,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}