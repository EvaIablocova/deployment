package com.example.recipes_microservice.DTOs;

import lombok.Data;

@Data
public class RecipeStepDTO {
    private Long id;
    private Long recipeId;
    private Integer stepNumber;
    private String instruction;
    private Integer durationMinutes;
}
