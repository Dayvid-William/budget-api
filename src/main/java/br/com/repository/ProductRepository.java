package br.com.repository;

import br.com.model.Product;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductRepository implements PanacheMongoRepository<Product> {
    public PanacheQuery<Product> findActiveByOwner(String ownerId) {
        return find("ownerId = ?1 and active = ?2", ownerId, true);
    }

    public Product findByIdAndOwner(String id, String ownerId) {
        return find("_id = ?1 and ownerId = ?2 and active = ?3", id, ownerId, true)
                .firstResult();
    }
}