package com.example.database_microservice.model;

import com.example.database_microservice.DTOs.RecipeDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "recipe")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "prep_time_minutes")
    private Integer prepTimeMinutes;

    @Column(name = "cook_time_minutes")
    private Integer cookTimeMinutes;

    @Column
    private Integer servings;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("stepNumber ASC")
    private List<RecipeStep> steps = new ArrayList<>();

    public Recipe() {}

    public Recipe(RecipeDTO recipeDTO) {
        this.id = recipeDTO.getId();
        this.title = recipeDTO.getTitle();
        this.description = recipeDTO.getDescription();
        this.prepTimeMinutes = recipeDTO.getPrepTimeMinutes();
        this.cookTimeMinutes = recipeDTO.getCookTimeMinutes();
        this.servings = recipeDTO.getServings();
        this.createdBy = recipeDTO.getCreatedBy();
        this.groupId = recipeDTO.getGroupId();
        this.createdAt = recipeDTO.getCreatedAt();
        this.updatedAt = recipeDTO.getUpdatedAt();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
