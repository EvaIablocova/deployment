package com.example.list_microservice.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class AddFromMealPlanDTO {
    private Long listId;           // Target list (null to create new)
    private String newListTitle;   // Title for new list (if listId is null)
    private Long groupId;          // Group for new list
    private Long createdBy;        // User creating the list
    private Long mealPlanId;       // Source meal plan
    private List<Long> recipeIds;  // Specific recipes to add (null/empty for all)
}
