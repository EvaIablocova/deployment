package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.Recipe;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class RecipeDTO {
    private Long id;
    private String title;
    private String description;
    private Integer prepTimeMinutes;
    private Integer cookTimeMinutes;
    private Integer servings;
    private Long createdBy;
    private Long groupId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<RecipeIngredientDTO> ingredients = new ArrayList<>();
    private List<RecipeStepDTO> steps = new ArrayList<>();

    public RecipeDTO() {}

    public RecipeDTO(Recipe recipe) {
        this.id = recipe.getId();
        this.title = recipe.getTitle();
        this.description = recipe.getDescription();
        this.prepTimeMinutes = recipe.getPrepTimeMinutes();
        this.cookTimeMinutes = recipe.getCookTimeMinutes();
        this.servings = recipe.getServings();
        this.createdBy = recipe.getCreatedBy();
        this.groupId = recipe.getGroupId();
        this.createdAt = recipe.getCreatedAt();
        this.updatedAt = recipe.getUpdatedAt();

        if (recipe.getIngredients() != null) {
            this.ingredients = recipe.getIngredients().stream()
                    .map(RecipeIngredientDTO::new)
                    .toList();
        }

        if (recipe.getSteps() != null) {
            this.steps = recipe.getSteps().stream()
                    .map(RecipeStepDTO::new)
                    .toList();
        }
    }
}
