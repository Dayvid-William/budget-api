package br.com.service;

import br.com.config.exception.ResourceNotFoundException;
import br.com.dto.request.ProductRequestDTO;
import br.com.dto.response.PagedResponseDTO;
import br.com.dto.response.ProductResponseDTO;
import br.com.model.Product;
import br.com.repository.ProductRepository;
import br.com.utils.mapper.ProductMapper;
import io.quarkus.mongodb.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.UriInfo;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository repository;

    @Inject
    ProductMapper mapper;

    @Inject
    UriInfo uriInfo;

    public PagedResponseDTO<ProductResponseDTO> findByOwner(String ownerId, int page, int size) {
        PanacheQuery<Product> query = repository.findActiveByOwner(ownerId).page(page, size);

        List<ProductResponseDTO> content = query.list().stream()
                .map(mapper::entitytoResponse)
                .toList();

        return new PagedResponseDTO<>(
                content,
                page,
                size,
                query.count(),
                query.pageCount(),
                generatePaginationLinks(page, query.pageCount(), size)
        );
    }

    public ProductResponseDTO findByIdAndOwnerId(String id, String ownerId) {
        Product product = repository.findByIdAndOwner(id, ownerId);
        if(product == null) throw new ResourceNotFoundException("Product not found");
        return mapper.entitytoResponse(product);
    }

    public ProductResponseDTO createProduct(ProductRequestDTO request, String ownerId) {
        Product entity = mapper.requestToEntity(request);
        entity.id = UUID.randomUUID().toString();
        entity.ownerId = ownerId;
        repository.persist(entity);
        return mapper.entitytoResponse(entity);
    }

    public ProductResponseDTO updateProduct(String id, ProductRequestDTO request, String ownerId) {
        Product product = repository.findByIdAndOwner(id, ownerId);
        if(product == null) throw new ResourceNotFoundException("Product not found");

        product.name = request.name();
        product.description = request.description();
        product.price = request.price();
        product.measurementUnit = request.measurementUnit();
        product.active = request.active();
        product.updatedAt = LocalDateTime.now();

        repository.update(product);
        return mapper.entitytoResponse(product);
    }

    public void deleteProduct(String id, String ownerId) {
        Product product = repository.findByIdAndOwner(id, ownerId);
        if(product == null) throw new ResourceNotFoundException("Product not found");
        product.active = false;
        product.updatedAt = LocalDateTime.now();
        repository.update(product);
    }

    private Map<String, String> generatePaginationLinks(int page, int totalPages, int size) {
        Map<String, String> links = new HashMap<>();
        links.put("self", buildPaginationUrl(page, size));

        if (page > 0) links.put("prev", buildPaginationUrl(page - 1, size));

        if (page < totalPages - 1) links.put("next", buildPaginationUrl(page + 1, size));

        return links;
    }

    private String buildPaginationUrl(int page, int size) {
        return uriInfo.getAbsolutePathBuilder()
                .queryParam("page", page)
                .queryParam("size", size)
                .build()
                .toString();
    }
}