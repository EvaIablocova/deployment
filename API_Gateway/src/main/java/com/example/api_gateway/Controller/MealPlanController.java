package com.example.api_gateway.Controller;

import com.example.api_gateway.DTOs.MealPlanDTO;
import com.example.api_gateway.DTOs.MealPlanDayDTO;
import com.example.api_gateway.DTOs.MealPlanItemDTO;
import com.example.api_gateway.config.ServiceUrlsConfig;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api_gateway/meal-plans")
public class MealPlanController {

    private final RestTemplate restTemplate;
    private final String externalBase;

    public MealPlanController(RestTemplateBuilder builder, ServiceUrlsConfig serviceUrls) {
        this.restTemplate = builder.build();
        this.externalBase = serviceUrls.getMealPlanServiceUrl() + "/api/meal-plans";
    }

    // --- MealPlan CRUD ---

    @GetMapping
    public List<MealPlanDTO> getAllMealPlans() {
        try {
            ResponseEntity<MealPlanDTO[]> response = restTemplate.getForEntity(externalBase, MealPlanDTO[].class);
            return Arrays.asList(Optional.ofNullable(response.getBody()).orElse(new MealPlanDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealPlanDTO> getMealPlanById(@PathVariable Long id) {
        try {
            return restTemplate.getForEntity(externalBase + "/" + id, MealPlanDTO.class);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/group/{groupId}")
    public List<MealPlanDTO> getMealPlansByGroupId(@PathVariable Long groupId) {
        try {
            ResponseEntity<MealPlanDTO[]> response = restTemplate.getForEntity(
                    externalBase + "/group/" + groupId, MealPlanDTO[].class);
            return Arrays.asList(Optional.ofNullable(response.getBody()).orElse(new MealPlanDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/user/{userId}")
    public List<MealPlanDTO> getMealPlansByUserId(@PathVariable Long userId) {
        try {
            ResponseEntity<MealPlanDTO[]> response = restTemplate.getForEntity(
                    externalBase + "/user/" + userId, MealPlanDTO[].class);
            return Arrays.asList(Optional.ofNullable(response.getBody()).orElse(new MealPlanDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/group/{groupId}/active")
    public List<MealPlanDTO> getActiveMealPlans(
            @PathVariable Long groupId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            ResponseEntity<MealPlanDTO[]> response = restTemplate.getForEntity(
                    externalBase + "/group/" + groupId + "/active?date=" + date, MealPlanDTO[].class);
            return Arrays.asList(Optional.ofNullable(response.getBody()).orElse(new MealPlanDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @PostMapping
    public ResponseEntity<?> createMealPlan(@RequestBody MealPlanDTO mealPlanDTO) {
        try {
            ResponseEntity<MealPlanDTO> response = restTemplate.postForEntity(externalBase, mealPlanDTO, MealPlanDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to create meal plan"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MealPlanDTO> updateMealPlan(@PathVariable Long id, @RequestBody MealPlanDTO mealPlanDTO) {
        try {
            restTemplate.put(externalBase + "/" + id, mealPlanDTO);
            return restTemplate.getForEntity(externalBase + "/" + id, MealPlanDTO.class);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMealPlan(@PathVariable Long id) {
        try {
            restTemplate.delete(externalBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Day operations ---

    @GetMapping("/{mealPlanId}/days")
    public List<MealPlanDayDTO> getDaysByMealPlanId(@PathVariable Long mealPlanId) {
        try {
            ResponseEntity<MealPlanDayDTO[]> response = restTemplate.getForEntity(
                    externalBase + "/" + mealPlanId + "/days", MealPlanDayDTO[].class);
            return Arrays.asList(Optional.ofNullable(response.getBody()).orElse(new MealPlanDayDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/days/{dayId}")
    public ResponseEntity<MealPlanDayDTO> getDayById(@PathVariable Long dayId) {
        try {
            return restTemplate.getForEntity(externalBase + "/days/" + dayId, MealPlanDayDTO.class);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{mealPlanId}/days/date/{date}")
    public ResponseEntity<MealPlanDayDTO> getDayByDate(
            @PathVariable Long mealPlanId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            return restTemplate.getForEntity(
                    externalBase + "/" + mealPlanId + "/days/date/" + date, MealPlanDayDTO.class);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Item operations ---

    @GetMapping("/days/{dayId}/items")
    public List<MealPlanItemDTO> getItemsByDayId(@PathVariable Long dayId) {
        try {
            ResponseEntity<MealPlanItemDTO[]> response = restTemplate.getForEntity(
                    externalBase + "/days/" + dayId + "/items", MealPlanItemDTO[].class);
            return Arrays.asList(Optional.ofNullable(response.getBody()).orElse(new MealPlanItemDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @PostMapping("/days/{dayId}/items")
    public ResponseEntity<?> addItem(
            @PathVariable Long dayId,
            @RequestBody MealPlanItemDTO itemDTO) {
        try {
            ResponseEntity<MealPlanItemDTO> response = restTemplate.postForEntity(
                    externalBase + "/days/" + dayId + "/items", itemDTO, MealPlanItemDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to add item"));
        }
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<?> updateItem(
            @PathVariable Long itemId,
            @RequestBody MealPlanItemDTO itemDTO) {
        try {
            restTemplate.put(externalBase + "/items/" + itemId, itemDTO);
            return ResponseEntity.ok(itemDTO);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        try {
            restTemplate.delete(externalBase + "/items/" + itemId);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
