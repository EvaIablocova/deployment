package com.example.recipes_microservice.DTOs;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String nameOfProduct;
    private String description;
    private Long categoryId;
    private String categoryName;
}
