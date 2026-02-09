package com.example.recipes_microservice.repository;

import com.example.recipes_microservice.DTOs.RecipeDTO;
import com.example.recipes_microservice.DTOs.RecipeIngredientDTO;
import com.example.recipes_microservice.DTOs.RecipeStepDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class RecipeRepository {

    private final RestTemplate restTemplate;
    private final String externalBase;

    public RecipeRepository(RestTemplateBuilder builder, @Value("${database.service.url}") String dbServiceUrl) {
        this.restTemplate = builder.build();
        this.externalBase = dbServiceUrl + "/db/recipes";
    }

    // --- Recipe CRUD ---

    public List<RecipeDTO> getAllRecipes() {
        try {
            ResponseEntity<RecipeDTO[]> response = restTemplate.getForEntity(externalBase, RecipeDTO[].class);
            RecipeDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new RecipeDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public ResponseEntity<RecipeDTO> getRecipeById(Long id) {
        try {
            ResponseEntity<RecipeDTO> response = restTemplate.getForEntity(externalBase + "/" + id, RecipeDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public List<RecipeDTO> getRecipesByGroupId(Long groupId) {
        try {
            ResponseEntity<RecipeDTO[]> response = restTemplate.getForEntity(
                    externalBase + "/group/" + groupId, RecipeDTO[].class);
            RecipeDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new RecipeDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public List<RecipeDTO> getRecipesByUserId(Long userId) {
        try {
            ResponseEntity<RecipeDTO[]> response = restTemplate.getForEntity(
                    externalBase + "/user/" + userId, RecipeDTO[].class);
            RecipeDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new RecipeDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public ResponseEntity<?> createRecipe(RecipeDTO recipeDTO) {
        try {
            ResponseEntity<RecipeDTO> response = restTemplate.postForEntity(externalBase, recipeDTO, RecipeDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Failed to create recipe"));
        }
    }

    public ResponseEntity<RecipeDTO> updateRecipe(Long id, RecipeDTO recipeDTO) {
        try {
            restTemplate.put(externalBase + "/" + id, recipeDTO);
            ResponseEntity<RecipeDTO> response = restTemplate.getForEntity(externalBase + "/" + id, RecipeDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteRecipe(Long id) {
        try {
            restTemplate.delete(externalBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Ingredient Management ---

    public List<RecipeIngredientDTO> getIngredientsByRecipeId(Long recipeId) {
        try {
            ResponseEntity<RecipeIngredientDTO[]> response = restTemplate.getForEntity(
                    externalBase + "/" + recipeId + "/ingredients", RecipeIngredientDTO[].class);
            RecipeIngredientDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new RecipeIngredientDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public ResponseEntity<?> addIngredient(Long recipeId, RecipeIngredientDTO ingredientDTO) {
        try {
            ResponseEntity<RecipeIngredientDTO> response = restTemplate.postForEntity(
                    externalBase + "/" + recipeId + "/ingredients", ingredientDTO, RecipeIngredientDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to add ingredient"));
        }
    }

    public ResponseEntity<?> updateIngredient(Long ingredientId, RecipeIngredientDTO ingredientDTO) {
        try {
            restTemplate.put(externalBase + "/ingredients/" + ingredientId, ingredientDTO);
            return ResponseEntity.ok(ingredientDTO);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteIngredient(Long ingredientId) {
        try {
            restTemplate.delete(externalBase + "/ingredients/" + ingredientId);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Step Management ---

    public List<RecipeStepDTO> getStepsByRecipeId(Long recipeId) {
        try {
            ResponseEntity<RecipeStepDTO[]> response = restTemplate.getForEntity(
                    externalBase + "/" + recipeId + "/steps", RecipeStepDTO[].class);
            RecipeStepDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new RecipeStepDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public ResponseEntity<?> addStep(Long recipeId, RecipeStepDTO stepDTO) {
        try {
            ResponseEntity<RecipeStepDTO> response = restTemplate.postForEntity(
                    externalBase + "/" + recipeId + "/steps", stepDTO, RecipeStepDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to add step"));
        }
    }

    public ResponseEntity<?> updateStep(Long stepId, RecipeStepDTO stepDTO) {
        try {
            restTemplate.put(externalBase + "/steps/" + stepId, stepDTO);
            return ResponseEntity.ok(stepDTO);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteStep(Long stepId) {
        try {
            restTemplate.delete(externalBase + "/steps/" + stepId);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
