package br.com.service;

import br.com.config.exception.ResourceNotFoundException;
import br.com.dto.request.ProductRequestDTO;
import br.com.dto.response.ProductResponseDTO;
import br.com.model.Product;
import br.com.repository.ProductRepository;
import br.com.utils.mapper.ProductMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository repository;
    @Inject
    ProductMapper mapper;

    public List<ProductResponseDTO> findByOwner(String ownerId) {
        try {
            return repository.listByOwner(ownerId).stream()
                    .filter(product -> product.active)
                    .map(mapper::entitytoResponse)
                    .collect(Collectors.toList());
        }catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    public ProductResponseDTO findByIdAndOwnerId(String id,  String ownerId) {
        try{
            Product product = repository.findByIdAndOwner(id, ownerId);

            if(product == null){ throw new ResourceNotFoundException("Product not found"); }

            return mapper.entitytoResponse(product);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ProductResponseDTO createProduct(ProductRequestDTO request, String ownerId) {
        Product entity = mapper.requestToEntity(request);
        entity.ownerId = ownerId;
        repository.persist(entity);
        return mapper.entitytoResponse(entity);
    }

    public void deleteProduct(String id, String ownerId) {
        try{
            Product product = repository.findByIdAndOwner(id, ownerId);

            if(product == null) throw new ResourceNotFoundException("Product not found");

            product.active = false;
            product.updatedAt = LocalDateTime.now();

            repository.update(product);
        }catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting product: " + e.getMessage());
        }
    }

    public ProductResponseDTO updateProduct(String id, ProductRequestDTO request, String ownerId) {
        try{
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
        }catch (ResourceNotFoundException e) {
            throw e;
        }catch (Exception e) {
            throw new RuntimeException("Error updating product: " + e.getMessage());
        }
    }
}