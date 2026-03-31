package br.com.resource;

import br.com.dto.request.ProductRequestDTO;
import br.com.dto.response.PagedResponseDTO;
import br.com.dto.response.ProductResponseDTO;
import br.com.service.ProductService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductResourceTest {

    @InjectMocks
    private ProductResource productResource;

    @Mock
    private ProductService productService;’

    private final String OWNER_ID = "tenant-123";
    private final String PRODUCT_ID = "prod-456";

    private ProductRequestDTO requestDTO;
    private ProductResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new ProductRequestDTO(
                "Cadeira de Madeira",
                "Cadeira rústica",
                300.00,
                "UN",
                true
        );

        responseDTO = new ProductResponseDTO(
                PRODUCT_ID,
                "Cadeira de Madeira",
                "Cadeira rústica",
                300.00,
                "UN",
                true,
                LocalDateTime.now(),
                LocalDateTime.now(),
                Map.of("self", "http://localhost/products/" + PRODUCT_ID)
        );
    }

    @Test
    void getByOwner_ShouldReturnOkAndPagedResponse() {
        int page = 0;
        int size = 10;

        PagedResponseDTO<ProductResponseDTO> pagedResponse = new PagedResponseDTO<>(
                List.of(responseDTO), page, size, 1L, 1, Map.of()
        );

        when(productService.findByOwner(OWNER_ID, page, size)).thenReturn(pagedResponse);

        Response response = productResource.getByOwner(OWNER_ID, page, size);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(pagedResponse, response.getEntity());
        verify(productService, times(1)).findByOwner(OWNER_ID, page, size);
    }

    @Test
    void getByIdAndOwner_ShouldReturnProductResponseDTO() {
        when(productService.findByIdAndOwnerId(PRODUCT_ID, OWNER_ID)).thenReturn(responseDTO);

        ProductResponseDTO result = productResource.getByIdAndOwner(OWNER_ID, PRODUCT_ID);

        assertNotNull(result);
        assertEquals(PRODUCT_ID, result.id());
        assertEquals(responseDTO.name(), result.name());
        verify(productService, times(1)).findByIdAndOwnerId(PRODUCT_ID, OWNER_ID);
    }

    @Test
    void createProduct_ShouldReturnCreatedStatusAndProductResponseDTO() {
        when(productService.createProduct(requestDTO, OWNER_ID)).thenReturn(responseDTO);

        Response response = productResource.createProduct(OWNER_ID, requestDTO);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(responseDTO, response.getEntity());
        verify(productService, times(1)).createProduct(requestDTO, OWNER_ID);
    }

    @Test
    void updateProduct_ShouldReturnOkStatusAndProductResponseDTO() {
        when(productService.updateProduct(PRODUCT_ID, requestDTO, OWNER_ID)).thenReturn(responseDTO);

        Response response = productResource.updateProduct(OWNER_ID, PRODUCT_ID, requestDTO);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(responseDTO, response.getEntity());
        verify(productService, times(1)).updateProduct(PRODUCT_ID, requestDTO, OWNER_ID);
    }

    @Test
    void deleteByIdAndOwner_ShouldReturnNoContentStatus() {
        Response response = productResource.deleteByIdAndOwner(OWNER_ID, PRODUCT_ID);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(productService, times(1)).deleteProduct(PRODUCT_ID, OWNER_ID);
    }
}