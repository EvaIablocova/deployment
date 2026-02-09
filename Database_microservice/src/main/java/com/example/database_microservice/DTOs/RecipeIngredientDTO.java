package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.RecipeIngredient;
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

    public RecipeIngredientDTO() {}

    public RecipeIngredientDTO(RecipeIngredient ingredient) {
        this.id = ingredient.getId();
        this.recipeId = ingredient.getRecipe() != null ? ingredient.getRecipe().getId() : null;
        this.productId = ingredient.getProduct() != null ? ingredient.getProduct().getId() : null;
        this.productName = ingredient.getProduct() != null ? ingredient.getProduct().getNameOfProduct() : null;
        this.customIngredientName = ingredient.getCustomIngredientName();
        this.quantity = ingredient.getQuantity();
        this.unit = ingredient.getUnit();
        this.notes = ingredient.getNotes();
        this.isCustom = ingredient.isCustomIngredient();
    }

    public String getIngredientDisplayName() {
        if (productName != null) {
            return productName;
        }
        return customIngredientName;
    }
}
