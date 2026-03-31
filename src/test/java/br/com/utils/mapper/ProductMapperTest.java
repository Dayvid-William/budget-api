package br.com.utils.mapper;

import br.com.dto.request.ProductRequestDTO;
import br.com.dto.response.ProductResponseDTO;
import br.com.model.Product;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ProductMapperTest {

    @InjectMocks
    private ProductMapper productMapper;

    @Mock
    private UriInfo uriInfo;

    @Mock
    private UriBuilder uriBuilder;

    private Product product;
    private ProductRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.id = "12345";
        product.name = "Mesa de Madeira";
        product.description = "Mesa rústica para sala de jantar";
        product.price = 1500.00;
        product.measurementUnit = "UN";
        product.active = true;
        product.createdAt = LocalDateTime.now();
        product.updatedAt = LocalDateTime.now();

        requestDTO = new ProductRequestDTO(
                "Cadeira de Madeira",
                "Cadeira rústica",
                300.00,
                "UN",
                true
        );
    }

    @Test
    void entityToResponse_ShouldReturnNull_WhenEntityIsNull() {
        ProductResponseDTO response = productMapper.entitytoResponse(null);

        assertNull(response, "O retorno deve ser nulo quando a entidade for nula");
    }

    @Test
    void entityToResponse_ShouldReturnResponseDTO_WhenEntityIsValid() {
        URI mockUri = URI.create("http://localhost:8080/products/12345");
        when(uriInfo.getBaseUriBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.path(anyString())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(mockUri);

        ProductResponseDTO response = productMapper.entitytoResponse(product);

        assertNotNull(response);
        assertEquals(product.id, response.id());
        assertEquals(product.name, response.name());
        assertEquals(product.description, response.description());
        assertEquals(product.price, response.price());
        assertEquals(product.measurementUnit, response.measurementUnit());
        assertEquals(product.active, response.active());
        assertEquals(product.createdAt, response.createdAt());
        assertEquals(product.updatedAt, response.updatedAt());

        assertNotNull(response._links());
        assertEquals(3, response._links().size());
        assertEquals("http://localhost:8080/products/12345", response._links().get("self"));
        assertEquals("http://localhost:8080/products/12345", response._links().get("update"));
        assertEquals("http://localhost:8080/products/12345", response._links().get("delete"));
    }

    @Test
    void requestToEntity_ShouldReturnNull_WhenDtoIsNull() {
        Product entity = productMapper.requestToEntity(null);

        assertNull(entity, "O retorno deve ser nulo quando o DTO for nulo");
    }

    @Test
    void requestToEntity_ShouldReturnEntity_WhenDtoIsValid() {
        Product entity = productMapper.requestToEntity(requestDTO);

        assertNotNull(entity);
        assertEquals(requestDTO.name(), entity.name);
        assertEquals(requestDTO.description(), entity.description);
        assertEquals(requestDTO.price(), entity.price);
        assertEquals(requestDTO.measurementUnit(), entity.measurementUnit);
        assertEquals(requestDTO.active(), entity.active);

        assertNull(entity.id);
        assertNotNull(entity.createdAt);
        assertNull(entity.updatedAt);
    }
}