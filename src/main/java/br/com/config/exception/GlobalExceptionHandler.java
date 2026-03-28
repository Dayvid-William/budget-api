package br.com.config.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<ResourceNotFoundException> {

    @Override
    public Response toResponse(ResourceNotFoundException exception) {
        Map<String, Object> errorDetails = Map.of(
                "status", 404,
                "error", "Not Found",
                "message", exception.getMessage()
        );

        return Response.status(Response.Status.NOT_FOUND)
                .entity(errorDetails)
                .build();
    }
}