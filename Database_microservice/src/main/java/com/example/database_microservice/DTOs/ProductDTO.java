package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.Product;
import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String nameOfProduct;
    private String description;
    private Long categoryId;
    private String categoryName;

    public ProductDTO() {}

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.nameOfProduct = product.getNameOfProduct();
        this.description = product.getDescription();
        if (product.getCategory() != null) {
            this.categoryId = product.getCategory().getId();
            this.categoryName = product.getCategory().getName();
        }
    }
}
