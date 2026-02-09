package com.example.database_microservice.controller;

import com.example.database_microservice.DTOs.RecipeDTO;
import com.example.database_microservice.DTOs.RecipeIngredientDTO;
import com.example.database_microservice.DTOs.RecipeStepDTO;
import com.example.database_microservice.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/db/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    // --- Recipe CRUD ---

    @GetMapping
    public List<RecipeDTO> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/group/{groupId}")
    public List<RecipeDTO> getRecipesByGroupId(@PathVariable Long groupId) {
        return recipeService.getRecipesByGroupId(groupId);
    }

    @GetMapping("/user/{userId}")
    public List<RecipeDTO> getRecipesByUserId(@PathVariable Long userId) {
        return recipeService.getRecipesByUserId(userId);
    }

    @PostMapping
    public RecipeDTO createRecipe(@RequestBody RecipeDTO recipeDTO) {
        return recipeService.createRecipe(recipeDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeDTO> updateRecipe(@PathVariable Long id, @RequestBody RecipeDTO recipeDTO) {
        return recipeService.updateRecipe(id, recipeDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        return recipeService.deleteRecipe(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // --- Ingredient Management ---

    @GetMapping("/{recipeId}/ingredients")
    public List<RecipeIngredientDTO> getIngredientsByRecipeId(@PathVariable Long recipeId) {
        return recipeService.getIngredientsByRecipeId(recipeId);
    }

    @PostMapping("/{recipeId}/ingredients")
    public ResponseEntity<RecipeIngredientDTO> addIngredient(
            @PathVariable Long recipeId,
            @RequestBody RecipeIngredientDTO ingredientDTO) {
        return recipeService.addIngredient(recipeId, ingredientDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/ingredients/{ingredientId}")
    public ResponseEntity<RecipeIngredientDTO> updateIngredient(
            @PathVariable Long ingredientId,
            @RequestBody RecipeIngredientDTO ingredientDTO) {
        return recipeService.updateIngredient(ingredientId, ingredientDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/ingredients/{ingredientId}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long ingredientId) {
        return recipeService.deleteIngredient(ingredientId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // --- Step Management ---

    @GetMapping("/{recipeId}/steps")
    public List<RecipeStepDTO> getStepsByRecipeId(@PathVariable Long recipeId) {
        return recipeService.getStepsByRecipeId(recipeId);
    }

    @PostMapping("/{recipeId}/steps")
    public ResponseEntity<RecipeStepDTO> addStep(
            @PathVariable Long recipeId,
            @RequestBody RecipeStepDTO stepDTO) {
        return recipeService.addStep(recipeId, stepDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/steps/{stepId}")
    public ResponseEntity<RecipeStepDTO> updateStep(
            @PathVariable Long stepId,
            @RequestBody RecipeStepDTO stepDTO) {
        return recipeService.updateStep(stepId, stepDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/steps/{stepId}")
    public ResponseEntity<Void> deleteStep(@PathVariable Long stepId) {
        return recipeService.deleteStep(stepId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
