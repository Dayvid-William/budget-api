package br.com.resource;

import br.com.dto.response.ProductResponseDTO;
import br.com.service.ProductService;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductService service;

    @GET
    public List<ProductResponseDTO> getByOwner(
            @HeaderParam("X-Tenant-ID")
            @NotBlank(message = "O ID da loja (X-Tenant-ID) é obrigatório.")
            String ownerId
    ) {
        return service.findByOwner(ownerId);
    }
}