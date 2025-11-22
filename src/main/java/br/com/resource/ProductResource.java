package br.com.resource;


import br.com.dto.request.ProductRequestDTO;
import br.com.dto.response.ProductResponseDTO;
import br.com.service.ProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductService service;

    @GET
    public List<ProductResponseDTO> list(@HeaderParam("X-Tenant-ID") String ownerId) {
        if (ownerId == null) throw new WebApplicationException("ID da loja obrigatório", 400);
        return service.listMyProducts(ownerId);
    }

    @POST
    public Response create(@HeaderParam("X-Tenant-ID") String ownerId, ProductRequestDTO dto) {
        if (ownerId == null) throw new WebApplicationException("ID da loja obrigatório", 400);

        service.createProduct(dto, ownerId);

        return Response.status(201).build();
    }
}