package br.com.config.exception;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void toResponse_ShouldReturn404AndErrorDetails_WhenExceptionIsThrown() {
        String errorMessage = "Product not found for the given owner";
        ResourceNotFoundException exception = new ResourceNotFoundException(errorMessage);

        Response response = exceptionHandler.toResponse(exception);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus(), "O status HTTP deve ser 404");
        assertTrue(response.getEntity() instanceof Map, "A entidade de resposta deve ser um Map");

        @SuppressWarnings("unchecked")
        Map<String, Object> errorDetails = (Map<String, Object>) response.getEntity();

        assertEquals(404, errorDetails.get("status"), "O status no body deve ser 404");
        assertEquals("Not Found", errorDetails.get("error"), "O erro no body deve ser 'Not Found'");
        assertEquals(errorMessage, errorDetails.get("message"), "A mensagem no body deve bater com a da exceção");
    }
}