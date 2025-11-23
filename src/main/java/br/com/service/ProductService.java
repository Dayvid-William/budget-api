package br.com.service;

import br.com.dto.response.ProductResponseDTO;
import br.com.repository.ProductRepository;
import br.com.utils.mapper.ProductMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository repository;
    @Inject
    ProductMapper mapper;

    public List<ProductResponseDTO> findByOwner(String ownerId) {
        return repository.listByOwner(ownerId).stream()
                .map(mapper::entitytoResponse)
                .collect(Collectors.toList());
    }
}