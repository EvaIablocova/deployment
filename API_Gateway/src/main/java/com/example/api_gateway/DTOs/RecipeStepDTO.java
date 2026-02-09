package com.example.api_gateway.DTOs;

import lombok.Data;

@Data
public class RecipeStepDTO {
    private Long id;
    private Long recipeId;
    private Integer stepNumber;
    private String instruction;
    private Integer durationMinutes;
}
