package com.example.recipes_microservice.service;

import com.example.recipes_microservice.DTOs.RecipeDTO;
import com.example.recipes_microservice.DTOs.RecipeIngredientDTO;
import com.example.recipes_microservice.DTOs.RecipeStepDTO;
import com.example.recipes_microservice.repository.RecipeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    // --- Recipe CRUD ---

    public List<RecipeDTO> getAllRecipes() {
        return recipeRepository.getAllRecipes();
    }

    public Optional<RecipeDTO> getRecipeById(Long id) {
        ResponseEntity<RecipeDTO> response = recipeRepository.getRecipeById(id);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return Optional.of(response.getBody());
        }
        return Optional.empty();
    }

    public List<RecipeDTO> getRecipesByGroupId(Long groupId) {
        return recipeRepository.getRecipesByGroupId(groupId);
    }

    public List<RecipeDTO> getRecipesByUserId(Long userId) {
        return recipeRepository.getRecipesByUserId(userId);
    }

    public ResponseEntity<?> createRecipe(RecipeDTO recipeDTO) {
        return recipeRepository.createRecipe(recipeDTO);
    }

    public ResponseEntity<RecipeDTO> updateRecipe(Long id, RecipeDTO recipeDTO) {
        return recipeRepository.updateRecipe(id, recipeDTO);
    }

    public ResponseEntity<Void> deleteRecipe(Long id) {
        return recipeRepository.deleteRecipe(id);
    }

    // --- Ingredient Management ---

    public List<RecipeIngredientDTO> getIngredientsByRecipeId(Long recipeId) {
        return recipeRepository.getIngredientsByRecipeId(recipeId);
    }

    public ResponseEntity<?> addIngredient(Long recipeId, RecipeIngredientDTO ingredientDTO) {
        return recipeRepository.addIngredient(recipeId, ingredientDTO);
    }

    public ResponseEntity<?> updateIngredient(Long ingredientId, RecipeIngredientDTO ingredientDTO) {
        return recipeRepository.updateIngredient(ingredientId, ingredientDTO);
    }

    public ResponseEntity<Void> deleteIngredient(Long ingredientId) {
        return recipeRepository.deleteIngredient(ingredientId);
    }

    // --- Step Management ---

    public List<RecipeStepDTO> getStepsByRecipeId(Long recipeId) {
        return recipeRepository.getStepsByRecipeId(recipeId);
    }

    public ResponseEntity<?> addStep(Long recipeId, RecipeStepDTO stepDTO) {
        return recipeRepository.addStep(recipeId, stepDTO);
    }

    public ResponseEntity<?> updateStep(Long stepId, RecipeStepDTO stepDTO) {
        return recipeRepository.updateStep(stepId, stepDTO);
    }

    public ResponseEntity<Void> deleteStep(Long stepId) {
        return recipeRepository.deleteStep(stepId);
    }
}
