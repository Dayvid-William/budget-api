package br.com.resource;

import br.com.dto.request.ProductRequestDTO;
import br.com.dto.response.ProductResponseDTO;
import br.com.service.ProductService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
    public List<ProductResponseDTO> getByOwner(
            @HeaderParam("X-Tenant-ID")
            @NotBlank(message = "The store ID (X-Tenant-ID) is required.")
            String ownerId
    ) {
        return service.findByOwner(ownerId);
    }

    @GET
    @Path("{id}")
    public ProductResponseDTO getByIdAndOwner(
            @HeaderParam("X-Tenant-ID")
            @NotBlank(message = "The store ID (X-Tenant-ID) is required.")
            String ownerId,

            @PathParam("id")
            @NotBlank(message = "The ID is required.")
            String id
    ){
        return service.findByIdAndOwnerId(id, ownerId);
    }

    @POST
    public Response createProduct(
            @HeaderParam("X-Tenant-ID")
            @NotBlank(message = "The store ID (X-Tenant-ID) is required.")
            String ownerId,

            @Valid
            ProductRequestDTO request
    ){
        ProductResponseDTO createdProduct = service.createProduct(request, ownerId);

        return Response.status(201).entity(createdProduct).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteByIdAndOwner(
            @HeaderParam("X-Tenant-ID")
            @NotBlank(message = "The store ID (X-Tenant-ID) is required.")
            String ownerId,

            @PathParam("id")
            @NotBlank(message = "The ID is required.")
            String id
    ){
        service.deleteProduct(id, ownerId);
        return Response.noContent().build();
    }

    @PUT
    @Path("{id}")
    public Response updateProduct(
            @HeaderParam("X-Tenant-ID")
            @NotBlank(message = "The store ID (X-Tenant-ID) is required.")
            String ownerId,

            @PathParam("id")
            @NotBlank(message = "The ID is required.")
            String id,

            @Valid
            ProductRequestDTO request
    ){
        ProductResponseDTO updatedProduct = service.updateProduct(id, request, ownerId);

        return Response.status(200).entity(updatedProduct).build();
    }
}