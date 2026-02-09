package com.example.api_gateway.DTOs;

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
}
