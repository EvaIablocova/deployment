package com.example.api_gateway.DTOs;

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
}
