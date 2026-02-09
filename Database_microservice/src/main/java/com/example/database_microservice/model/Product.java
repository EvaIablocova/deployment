package com.example.database_microservice.model;

import com.example.database_microservice.DTOs.ProductDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nameOfProduct;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    public Product() {}

    public Product(ProductDTO productDTO) {
        this.id = productDTO.getId();
        this.nameOfProduct = productDTO.getNameOfProduct();
        this.description = productDTO.getDescription();
    }
}
