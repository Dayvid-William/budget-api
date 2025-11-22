package br.com.config.exception;

import br.com.dto.response.ErrorResponseDTO;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        int code = 500;
        String message = "Erro interno no servidor";

        if (exception instanceof WebApplicationException webEx) {
            code = webEx.getResponse().getStatus();
            message = webEx.getMessage();
        }
        else if (exception.getMessage() != null) {
            message = exception.getMessage();
        }

        ErrorResponseDTO erro = new ErrorResponseDTO(
                message,
                code,
                LocalDateTime.now()
        );

        return Response.status(code).entity(erro).build();
    }
}