package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.MealPlanItem;
import com.example.database_microservice.model.MealType;
import lombok.Data;

@Data
public class MealPlanItemDTO {
    private Long id;
    private Long mealPlanDayId;
    private MealType mealType;
    private Long recipeId;
    private String recipeName;
    private Long productId;
    private String productName;
    private String customMeal;
    private String notes;
    private Integer servings;
    private Integer sortOrder;
    private String itemType;

    public MealPlanItemDTO() {}

    public MealPlanItemDTO(MealPlanItem item) {
        this.id = item.getId();
        this.mealPlanDayId = item.getMealPlanDay() != null ? item.getMealPlanDay().getId() : null;
        this.mealType = item.getMealType();
        this.recipeId = item.getRecipe() != null ? item.getRecipe().getId() : null;
        this.recipeName = item.getRecipe() != null ? item.getRecipe().getTitle() : null;
        this.productId = item.getProduct() != null ? item.getProduct().getId() : null;
        this.productName = item.getProduct() != null ? item.getProduct().getNameOfProduct() : null;
        this.customMeal = item.getCustomMeal();
        this.notes = item.getNotes();
        this.servings = item.getServings();
        this.sortOrder = item.getSortOrder();

        if (this.recipeId != null) {
            this.itemType = "RECIPE";
        } else if (this.productId != null) {
            this.itemType = "PRODUCT";
        } else {
            this.itemType = "CUSTOM";
        }
    }

    public String getDisplayName() {
        if (recipeName != null) return recipeName;
        if (productName != null) return productName;
        return customMeal;
    }
}
