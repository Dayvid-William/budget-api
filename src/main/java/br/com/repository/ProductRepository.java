package br.com.repository;

import br.com.model.Product;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ProductRepository implements PanacheMongoRepository<Product> {

    public List<Product> listByOwner(String ownerId) {
        return list("ownerId", ownerId);
    }
}