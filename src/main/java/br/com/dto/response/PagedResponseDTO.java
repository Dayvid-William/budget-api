package br.com.dto.response;

import java.util.List;
import java.util.Map;

public record PagedResponseDTO<T>(
        List<T> data,
        int currentPage,
        int pageSize,
        long totalElements,
        int totalPages,
        Map<String, String> _links
) {}