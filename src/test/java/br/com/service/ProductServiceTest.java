package br.com.service;

import br.com.config.exception.ResourceNotFoundException;
import br.com.dto.request.ProductRequestDTO;
import br.com.dto.response.PagedResponseDTO;
import br.com.dto.response.ProductResponseDTO;
import br.com.model.Product;
import br.com.repository.ProductRepository;
import br.com.utils.mapper.ProductMapper;
import io.quarkus.mongodb.panache.PanacheQuery;
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
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @Mock
    private UriInfo uriInfo;

    @Mock
    private UriBuilder uriBuilder;

    @Mock
    private PanacheQuery<Product> panacheQuery;

    private final String OWNER_ID = "tenant-123";
    private final String PRODUCT_ID = "prod-456";

    private Product product;
    private ProductRequestDTO requestDTO;
    private ProductResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.id = PRODUCT_ID;
        product.ownerId = OWNER_ID;
        product.name = "Mesa";
        product.description = "Mesa de jantar";
        product.price = 500.00;
        product.measurementUnit = "UN";
        product.active = true;

        requestDTO = new ProductRequestDTO(
                "Mesa Nova",
                "Mesa de jantar nova",
                600.00,
                "UN",
                true
        );

        responseDTO = new ProductResponseDTO(
                PRODUCT_ID,
                "Mesa Nova",
                "Mesa de jantar nova",
                600.00,
                "UN",
                true,
                LocalDateTime.now(),
                LocalDateTime.now(),
                Map.of()
        );
    }

    @Test
    void findByOwner_ShouldReturnPagedResponse_WithNextLinkOnly_WhenFirstPage() {
        int page = 0;
        int size = 10;

        when(repository.findActiveByOwner(OWNER_ID)).thenReturn(panacheQuery);
        when(panacheQuery.page(page, size)).thenReturn(panacheQuery);
        when(panacheQuery.list()).thenReturn(List.of(product));
        when(panacheQuery.count()).thenReturn(25L);
        when(panacheQuery.pageCount()).thenReturn(3);
        when(mapper.entitytoResponse(product)).thenReturn(responseDTO);

        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.queryParam(anyString(), any())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(URI.create("http://localhost/products"));

        PagedResponseDTO<ProductResponseDTO> result = productService.findByOwner(OWNER_ID, page, size);

        assertNotNull(result);
        assertEquals(1, result.data().size());
        assertEquals(25L, result.totalElements());

        assertTrue(result._links().containsKey("self"));
        assertTrue(result._links().containsKey("next"));
        assertFalse(result._links().containsKey("prev"));
    }

    @Test
    void findByOwner_ShouldReturnPagedResponse_WithPrevAndNextLinks_WhenMiddlePage() {
        int page = 1;
        int size = 10;

        when(repository.findActiveByOwner(OWNER_ID)).thenReturn(panacheQuery);
        when(panacheQuery.page(page, size)).thenReturn(panacheQuery);
        when(panacheQuery.list()).thenReturn(List.of(product));
        when(panacheQuery.count()).thenReturn(25L);
        when(panacheQuery.pageCount()).thenReturn(3);
        when(mapper.entitytoResponse(product)).thenReturn(responseDTO);

        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.queryParam(anyString(), any())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(URI.create("http://localhost/products"));

        PagedResponseDTO<ProductResponseDTO> result = productService.findByOwner(OWNER_ID, page, size);

        assertNotNull(result);
        assertTrue(result._links().containsKey("self"));
        assertTrue(result._links().containsKey("prev"));
        assertTrue(result._links().containsKey("next"));
    }

    @Test
    void findByIdAndOwnerId_ShouldReturnProduct_WhenFound() {
        when(repository.findByIdAndOwner(PRODUCT_ID, OWNER_ID)).thenReturn(product);
        when(mapper.entitytoResponse(product)).thenReturn(responseDTO);

        ProductResponseDTO result = productService.findByIdAndOwnerId(PRODUCT_ID, OWNER_ID);

        assertNotNull(result);
        assertEquals(PRODUCT_ID, result.id());
    }

    @Test
    void findByIdAndOwnerId_ShouldThrowException_WhenNotFound() {
        when(repository.findByIdAndOwner(PRODUCT_ID, OWNER_ID)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () ->
                productService.findByIdAndOwnerId(PRODUCT_ID, OWNER_ID)
        );
    }

    @Test
    void createProduct_ShouldPersistAndReturnProduct() {
        when(mapper.requestToEntity(requestDTO)).thenReturn(product);
        when(mapper.entitytoResponse(any(Product.class))).thenReturn(responseDTO);

        ProductResponseDTO result = productService.createProduct(requestDTO, OWNER_ID);

        assertNotNull(result);
        verify(repository, times(1)).persist(any(Product.class));
        assertNotNull(product.id);
        assertEquals(OWNER_ID, product.ownerId);
    }

    @Test
    void updateProduct_ShouldUpdateAndReturnProduct_WhenFound() {
        when(repository.findByIdAndOwner(PRODUCT_ID, OWNER_ID)).thenReturn(product);
        when(mapper.entitytoResponse(product)).thenReturn(responseDTO);

        ProductResponseDTO result = productService.updateProduct(PRODUCT_ID, requestDTO, OWNER_ID);

        assertNotNull(result);
        assertEquals(requestDTO.name(), product.name);
        assertEquals(requestDTO.price(), product.price);
        assertNotNull(product.updatedAt);
        verify(repository, times(1)).update(product);
    }

    @Test
    void updateProduct_ShouldThrowException_WhenNotFound() {
        when(repository.findByIdAndOwner(PRODUCT_ID, OWNER_ID)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () ->
                productService.updateProduct(PRODUCT_ID, requestDTO, OWNER_ID)
        );
        verify(repository, never()).update(any(Product.class));
    }

    @Test
    void deleteProduct_ShouldThrowException_WhenNotFound() {
        when(repository.findByIdAndOwner(PRODUCT_ID, OWNER_ID)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () ->
                productService.deleteProduct(PRODUCT_ID, OWNER_ID)
        );
        verify(repository, never()).update(any(Product.class));
    }

    @Test
    void deleteProduct_ShouldSoftDelete_WhenFound() {
        when(repository.findByIdAndOwner(PRODUCT_ID, OWNER_ID)).thenReturn(product);

        productService.deleteProduct(PRODUCT_ID, OWNER_ID);

        assertFalse(product.active);
        assertNotNull(product.updatedAt);
        verify(repository, times(1)).update(product);
    }

    @Test
    void findByOwner_ShouldReturnPagedResponse_WithPrevLinkOnly_WhenLastPage() {
        int page = 2;
        int totalPages = 3;
        int size = 10;

        when(repository.findActiveByOwner(OWNER_ID)).thenReturn(panacheQuery);
        when(panacheQuery.page(page, size)).thenReturn(panacheQuery);
        when(panacheQuery.list()).thenReturn(List.of(product));
        when(panacheQuery.count()).thenReturn(25L);
        when(panacheQuery.pageCount()).thenReturn(totalPages);
        when(mapper.entitytoResponse(product)).thenReturn(responseDTO);

        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        when(uriBuilder.queryParam(anyString(), any())).thenReturn(uriBuilder);
        when(uriBuilder.build()).thenReturn(URI.create("http://localhost/products"));

        PagedResponseDTO<ProductResponseDTO> result = productService.findByOwner(OWNER_ID, page, size);

        assertNotNull(result);

        assertTrue(result._links().containsKey("self"), "Deve ter o link self");
        assertTrue(result._links().containsKey("prev"), "Deve ter o link prev na última página");
        assertFalse(result._links().containsKey("next"), "NÃO deve ter o link next na última página");
    }
}