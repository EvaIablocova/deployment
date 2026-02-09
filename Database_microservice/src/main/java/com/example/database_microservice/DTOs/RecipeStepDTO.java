package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.RecipeStep;
import lombok.Data;

@Data
public class RecipeStepDTO {
    private Long id;
    private Long recipeId;
    private Integer stepNumber;
    private String instruction;
    private Integer durationMinutes;

    public RecipeStepDTO() {}

    public RecipeStepDTO(RecipeStep step) {
        this.id = step.getId();
        this.recipeId = step.getRecipe() != null ? step.getRecipe().getId() : null;
        this.stepNumber = step.getStepNumber();
        this.instruction = step.getInstruction();
        this.durationMinutes = step.getDurationMinutes();
    }
}
