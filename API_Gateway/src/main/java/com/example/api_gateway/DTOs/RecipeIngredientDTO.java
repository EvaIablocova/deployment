package com.example.api_gateway.DTOs;

import lombok.Data;

@Data
public class RecipeIngredientDTO {
    private Long id;
    private Long recipeId;
    private Long groceryProductId;
    private String groceryProductName;
    private String customIngredientName;
    private Double quantity;
    private String unit;
    private String notes;
}
