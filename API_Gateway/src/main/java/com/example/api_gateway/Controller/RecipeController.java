package com.example.api_gateway.Controller;

import com.example.api_gateway.DTOs.RecipeDTO;
import com.example.api_gateway.DTOs.RecipeIngredientDTO;
import com.example.api_gateway.DTOs.RecipeStepDTO;
import com.example.api_gateway.config.ServiceUrlsConfig;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api_gateway/recipes")
public class RecipeController {

    private final RestTemplate restTemplate;
    private final String externalBase;

    public RecipeController(RestTemplateBuilder builder, ServiceUrlsConfig serviceUrls) {
        this.restTemplate = builder.build();
        this.externalBase = serviceUrls.getRecipesServiceUrl() + "/api/recipes";
    }

    // --- Recipe CRUD ---

    @GetMapping
    public List<RecipeDTO> getAllRecipes() {
        try {
            ResponseEntity<RecipeDTO[]> response = restTemplate.getForEntity(externalBase, RecipeDTO[].class);
            RecipeDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new RecipeDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable Long id) {
        try {
            ResponseEntity<RecipeDTO> response =
                    restTemplate.getForEntity(externalBase + "/" + id, RecipeDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/group/{groupId}")
    public List<RecipeDTO> getRecipesByGroupId(@PathVariable Long groupId) {
        try {
            ResponseEntity<RecipeDTO[]> response = restTemplate.getForEntity(
                    externalBase + "/group/" + groupId, RecipeDTO[].class);
            RecipeDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new RecipeDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/user/{userId}")
    public List<RecipeDTO> getRecipesByUserId(@PathVariable Long userId) {
        try {
            ResponseEntity<RecipeDTO[]> response = restTemplate.getForEntity(
                    externalBase + "/user/" + userId, RecipeDTO[].class);
            RecipeDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new RecipeDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @PostMapping
    public ResponseEntity<?> createRecipe(@RequestBody RecipeDTO recipeDTO) {
        try {
            ResponseEntity<RecipeDTO> response =
                    restTemplate.postForEntity(externalBase, recipeDTO, RecipeDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Failed to create recipe"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeDTO> updateRecipe(@PathVariable Long id, @RequestBody RecipeDTO recipeDTO) {
        try {
            restTemplate.put(externalBase + "/" + id, recipeDTO);
            ResponseEntity<RecipeDTO> response = restTemplate.getForEntity(externalBase + "/" + id, RecipeDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        try {
            restTemplate.delete(externalBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Ingredient Management ---

    @GetMapping("/{recipeId}/ingredients")
    public ResponseEntity<List<RecipeIngredientDTO>> getIngredientsByRecipeId(@PathVariable Long recipeId) {
        try {
            ResponseEntity<RecipeIngredientDTO[]> response = restTemplate.getForEntity(
                    externalBase + "/" + recipeId + "/ingredients", RecipeIngredientDTO[].class);
            RecipeIngredientDTO[] body = response.getBody();
            return ResponseEntity.ok(Arrays.asList(Optional.ofNullable(body).orElse(new RecipeIngredientDTO[0])));
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{recipeId}/ingredients")
    public ResponseEntity<?> addIngredient(
            @PathVariable Long recipeId,
            @RequestBody RecipeIngredientDTO ingredientDTO) {
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

    @PutMapping("/ingredients/{ingredientId}")
    public ResponseEntity<?> updateIngredient(
            @PathVariable Long ingredientId,
            @RequestBody RecipeIngredientDTO ingredientDTO) {
        try {
            restTemplate.put(externalBase + "/ingredients/" + ingredientId, ingredientDTO);
            return ResponseEntity.ok(ingredientDTO);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/ingredients/{ingredientId}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long ingredientId) {
        try {
            restTemplate.delete(externalBase + "/ingredients/" + ingredientId);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Step Management ---

    @GetMapping("/{recipeId}/steps")
    public ResponseEntity<List<RecipeStepDTO>> getStepsByRecipeId(@PathVariable Long recipeId) {
        try {
            ResponseEntity<RecipeStepDTO[]> response = restTemplate.getForEntity(
                    externalBase + "/" + recipeId + "/steps", RecipeStepDTO[].class);
            RecipeStepDTO[] body = response.getBody();
            return ResponseEntity.ok(Arrays.asList(Optional.ofNullable(body).orElse(new RecipeStepDTO[0])));
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{recipeId}/steps")
    public ResponseEntity<?> addStep(
            @PathVariable Long recipeId,
            @RequestBody RecipeStepDTO stepDTO) {
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

    @PutMapping("/steps/{stepId}")
    public ResponseEntity<?> updateStep(
            @PathVariable Long stepId,
            @RequestBody RecipeStepDTO stepDTO) {
        try {
            restTemplate.put(externalBase + "/steps/" + stepId, stepDTO);
            return ResponseEntity.ok(stepDTO);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/steps/{stepId}")
    public ResponseEntity<Void> deleteStep(@PathVariable Long stepId) {
        try {
            restTemplate.delete(externalBase + "/steps/" + stepId);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
