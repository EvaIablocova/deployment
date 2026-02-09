package com.example.database_microservice.service;

import com.example.database_microservice.DTOs.RecipeDTO;
import com.example.database_microservice.DTOs.RecipeIngredientDTO;
import com.example.database_microservice.DTOs.RecipeStepDTO;
import com.example.database_microservice.model.Recipe;
import com.example.database_microservice.model.RecipeIngredient;
import com.example.database_microservice.model.RecipeStep;
import com.example.database_microservice.repository.ProductRepository;
import com.example.database_microservice.repository.RecipeIngredientRepository;
import com.example.database_microservice.repository.RecipeRepository;
import com.example.database_microservice.repository.RecipeStepRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository ingredientRepository;
    private final RecipeStepRepository stepRepository;
    private final ProductRepository productRepository;

    public RecipeService(RecipeRepository recipeRepository,
                         RecipeIngredientRepository ingredientRepository,
                         RecipeStepRepository stepRepository,
                         ProductRepository productRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.stepRepository = stepRepository;
        this.productRepository = productRepository;
    }

    private RecipeDTO toDTO(Recipe recipe) {
        return new RecipeDTO(recipe);
    }

    public List<RecipeDTO> getAllRecipes() {
        return recipeRepository.findAll().stream().map(this::toDTO).toList();
    }

    public Optional<RecipeDTO> getRecipeById(Long id) {
        return recipeRepository.findById(id).map(this::toDTO);
    }

    public List<RecipeDTO> getRecipesByGroupId(Long groupId) {
        return recipeRepository.findByGroupId(groupId).stream().map(this::toDTO).toList();
    }

    public List<RecipeDTO> getRecipesByUserId(Long userId) {
        return recipeRepository.findByCreatedBy(userId).stream().map(this::toDTO).toList();
    }

    @Transactional
    public RecipeDTO createRecipe(RecipeDTO recipeDTO) {
        Recipe recipe = new Recipe(recipeDTO);
        Recipe savedRecipe = recipeRepository.save(recipe);

        if (recipeDTO.getIngredients() != null) {
            for (RecipeIngredientDTO ingredientDTO : recipeDTO.getIngredients()) {
                RecipeIngredient ingredient = new RecipeIngredient(ingredientDTO);
                ingredient.setRecipe(savedRecipe);
                if (ingredientDTO.getProductId() != null) {
                    productRepository.findById(ingredientDTO.getProductId())
                            .ifPresent(ingredient::setProduct);
                }
                ingredientRepository.save(ingredient);
            }
        }

        if (recipeDTO.getSteps() != null) {
            for (RecipeStepDTO stepDTO : recipeDTO.getSteps()) {
                RecipeStep step = new RecipeStep(stepDTO);
                step.setRecipe(savedRecipe);
                stepRepository.save(step);
            }
        }

        return getRecipeById(savedRecipe.getId()).orElse(null);
    }

    @Transactional
    public Optional<RecipeDTO> updateRecipe(Long id, RecipeDTO recipeDTO) {
        return recipeRepository.findById(id).map(recipe -> {
            recipe.setTitle(recipeDTO.getTitle());
            recipe.setDescription(recipeDTO.getDescription());
            recipe.setPrepTimeMinutes(recipeDTO.getPrepTimeMinutes());
            recipe.setCookTimeMinutes(recipeDTO.getCookTimeMinutes());
            recipe.setServings(recipeDTO.getServings());
            recipe.setGroupId(recipeDTO.getGroupId());

            recipe.getIngredients().clear();
            recipe.getSteps().clear();
            recipeRepository.save(recipe);

            if (recipeDTO.getIngredients() != null) {
                for (RecipeIngredientDTO ingredientDTO : recipeDTO.getIngredients()) {
                    RecipeIngredient ingredient = new RecipeIngredient(ingredientDTO);
                    ingredient.setRecipe(recipe);
                    if (ingredientDTO.getProductId() != null) {
                        productRepository.findById(ingredientDTO.getProductId())
                                .ifPresent(ingredient::setProduct);
                    }
                    recipe.getIngredients().add(ingredient);
                }
            }

            if (recipeDTO.getSteps() != null) {
                for (RecipeStepDTO stepDTO : recipeDTO.getSteps()) {
                    RecipeStep step = new RecipeStep(stepDTO);
                    step.setRecipe(recipe);
                    recipe.getSteps().add(step);
                }
            }

            return toDTO(recipeRepository.save(recipe));
        });
    }

    @Transactional
    public boolean deleteRecipe(Long id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // --- Ingredient Management ---

    public List<RecipeIngredientDTO> getIngredientsByRecipeId(Long recipeId) {
        return ingredientRepository.findByRecipeId(recipeId).stream()
                .map(RecipeIngredientDTO::new)
                .toList();
    }

    @Transactional
    public Optional<RecipeIngredientDTO> addIngredient(Long recipeId, RecipeIngredientDTO ingredientDTO) {
        return recipeRepository.findById(recipeId).map(recipe -> {
            RecipeIngredient ingredient = new RecipeIngredient(ingredientDTO);
            ingredient.setRecipe(recipe);
            if (ingredientDTO.getProductId() != null) {
                productRepository.findById(ingredientDTO.getProductId())
                        .ifPresent(ingredient::setProduct);
            }
            return new RecipeIngredientDTO(ingredientRepository.save(ingredient));
        });
    }

    @Transactional
    public Optional<RecipeIngredientDTO> updateIngredient(Long ingredientId, RecipeIngredientDTO ingredientDTO) {
        return ingredientRepository.findById(ingredientId).map(ingredient -> {
            ingredient.setQuantity(ingredientDTO.getQuantity());
            ingredient.setUnit(ingredientDTO.getUnit());
            ingredient.setNotes(ingredientDTO.getNotes());
            ingredient.setCustomIngredientName(ingredientDTO.getCustomIngredientName());
            if (ingredientDTO.getProductId() != null) {
                productRepository.findById(ingredientDTO.getProductId())
                        .ifPresent(ingredient::setProduct);
            } else {
                ingredient.setProduct(null);
            }
            return new RecipeIngredientDTO(ingredientRepository.save(ingredient));
        });
    }

    @Transactional
    public boolean deleteIngredient(Long ingredientId) {
        if (ingredientRepository.existsById(ingredientId)) {
            ingredientRepository.deleteById(ingredientId);
            return true;
        }
        return false;
    }

    // --- Step Management ---

    public List<RecipeStepDTO> getStepsByRecipeId(Long recipeId) {
        return stepRepository.findByRecipeIdOrderByStepNumberAsc(recipeId).stream()
                .map(RecipeStepDTO::new)
                .toList();
    }

    @Transactional
    public Optional<RecipeStepDTO> addStep(Long recipeId, RecipeStepDTO stepDTO) {
        return recipeRepository.findById(recipeId).map(recipe -> {
            RecipeStep step = new RecipeStep(stepDTO);
            step.setRecipe(recipe);
            return new RecipeStepDTO(stepRepository.save(step));
        });
    }

    @Transactional
    public Optional<RecipeStepDTO> updateStep(Long stepId, RecipeStepDTO stepDTO) {
        return stepRepository.findById(stepId).map(step -> {
            step.setStepNumber(stepDTO.getStepNumber());
            step.setInstruction(stepDTO.getInstruction());
            step.setDurationMinutes(stepDTO.getDurationMinutes());
            return new RecipeStepDTO(stepRepository.save(step));
        });
    }

    @Transactional
    public boolean deleteStep(Long stepId) {
        if (stepRepository.existsById(stepId)) {
            stepRepository.deleteById(stepId);
            return true;
        }
        return false;
    }
}
