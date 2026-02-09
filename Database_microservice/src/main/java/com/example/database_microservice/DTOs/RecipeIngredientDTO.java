package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.RecipeIngredient;
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

    public RecipeIngredientDTO() {}

    public RecipeIngredientDTO(RecipeIngredient ingredient) {
        this.id = ingredient.getId();
        this.recipeId = ingredient.getRecipe() != null ? ingredient.getRecipe().getId() : null;
        this.groceryProductId = ingredient.getGroceryProduct() != null ? ingredient.getGroceryProduct().getId() : null;
        this.groceryProductName = ingredient.getGroceryProduct() != null ? ingredient.getGroceryProduct().getNameOfProduct() : null;
        this.customIngredientName = ingredient.getCustomIngredientName();
        this.quantity = ingredient.getQuantity();
        this.unit = ingredient.getUnit();
        this.notes = ingredient.getNotes();
    }
}
