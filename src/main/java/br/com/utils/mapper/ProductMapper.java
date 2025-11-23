package br.com.utils.mapper;

import br.com.dto.request.ProductRequestDTO;
import br.com.dto.response.ProductResponseDTO;
import br.com.model.Product;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductMapper {

    public ProductResponseDTO entitytoResponse(Product entity) {
        if (entity == null) return null;

        return new ProductResponseDTO(
                entity.id.toString(),
                entity.name,
                entity.description,
                entity.price,
                entity.measurementUnit,
                entity.active,
                entity.createdAt,
                entity.updatedAt
        );
    }

    public Product requestToEntity(ProductRequestDTO dto) {
        if (dto == null) return null;

        Product entity = new Product();
        entity.name = dto.name();
        entity.description = dto.description();
        entity.price = dto.price();
        entity.measurementUnit = dto.measurementUnit();
        entity.active = dto.active();

        return entity;
    }
}