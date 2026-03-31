package br.com.utils.mapper;

import br.com.dto.request.ProductRequestDTO;
import br.com.dto.response.ProductResponseDTO;
import br.com.model.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriInfo;
import java.util.Map;

@ApplicationScoped
public class ProductMapper {

    @Inject
    UriInfo uriInfo;

    public ProductResponseDTO entitytoResponse(Product entity) {
        if (entity == null) return null;

        String selfLink = uriInfo.getBaseUriBuilder()
                .path("/products")
                .path(entity.id)
                .build().toString();

        Map<String, String> links = Map.of(
                "self", selfLink,
                "update", selfLink,
                "delete", selfLink
        );

        return new ProductResponseDTO(
                entity.id,
                entity.name,
                entity.description,
                entity.price,
                entity.measurementUnit,
                entity.active,
                entity.createdAt,
                entity.updatedAt,
                links
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