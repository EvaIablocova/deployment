package com.example.product_microservice.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class ProductDTO {
    private Long id;
    private String nameOfProduct;
    private String description;
    private Long categoryId;
    private String categoryName;
    private Long imageId;
    private List<ImageDTO> images;
}
