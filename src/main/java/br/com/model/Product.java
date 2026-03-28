package br.com.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.NotBlank;
import org.bson.codecs.pojo.annotations.BsonId;

import java.time.LocalDateTime;

@MongoEntity(collection = "products")
public class Product {
    @BsonId
    public String id;
    @NotNull
    public String ownerId;
    @NotBlank(message = "O nome é obrigatório")
    public String name;
    @NotBlank(message = "A descrição é obrigatório")
    public String description;
    public Double price;
    public String measurementUnit;
    public Integer minOrderQuantity = 1;
    public Integer stockQuantity;
    public boolean active = true;
    public LocalDateTime createdAt = LocalDateTime.now();
    public LocalDateTime updatedAt;
}