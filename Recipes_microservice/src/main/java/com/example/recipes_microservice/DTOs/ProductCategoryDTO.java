package com.example.recipes_microservice.DTOs;

import lombok.Data;

@Data
public class ProductCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private String icon;
    private Integer displayOrder;
    private Integer productCount;
}
