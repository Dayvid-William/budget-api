package br.com.service;


import br.com.dto.request.ProductRequestDTO;
import br.com.dto.response.ProductResponseDTO;
import br.com.utils.mapper.ProductMapper;
import br.com.model.Product;
import br.com.repository.ProductRepository;
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
                .map(mapper::entitytoResponse )
                .collect(Collectors.toList());
    }

    public void createProduct(ProductRequestDTO dto, String ownerId) {
        Product entity = new Product();
        entity.ownerId = ownerId;

        entity.name = dto.name();
        entity.description = dto.description();
        entity.price = dto.price();
        entity.measurementUnit = dto.measurementUnit();
        entity.active = dto.active();

        repository.persist(entity);
    }
}