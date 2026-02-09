package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.ProductCategory;
import lombok.Data;

@Data
public class ProductCategoryDTO {
    private Long id;
    private String name;
    private String description;
    private String icon;
    private Integer displayOrder;
    private Integer productCount;

    public ProductCategoryDTO() {}

    public ProductCategoryDTO(ProductCategory category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.icon = category.getIcon();
        this.displayOrder = category.getDisplayOrder();
        this.productCount = category.getProducts() != null ? category.getProducts().size() : 0;
    }
}
