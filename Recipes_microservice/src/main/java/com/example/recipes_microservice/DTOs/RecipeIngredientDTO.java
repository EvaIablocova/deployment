package com.example.recipes_microservice.DTOs;

import lombok.Data;

@Data
public class RecipeIngredientDTO {
    private Long id;
    private Long recipeId;
    private Long productId;
    private String productName;
    private String customIngredientName;
    private Double quantity;
    private String unit;
    private String notes;
    private boolean isCustom;

    public String getIngredientDisplayName() {
        if (productName != null) {
            return productName;
        }
        return customIngredientName;
    }
}
