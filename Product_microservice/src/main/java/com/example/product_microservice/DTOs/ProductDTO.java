package com.example.product_microservice.DTOs;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String nameOfProduct;
    private String description;
}
