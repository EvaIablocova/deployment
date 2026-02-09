package com.example.mealplan_microservice.repository;

import com.example.mealplan_microservice.DTOs.MealPlanDTO;
import com.example.mealplan_microservice.DTOs.MealPlanDayDTO;
import com.example.mealplan_microservice.DTOs.MealPlanItemDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class MealPlanRepository {

    private final RestTemplate restTemplate;
    private final String externalBase;

    public MealPlanRepository(RestTemplateBuilder builder, @Value("${database.service.url}") String dbServiceUrl) {
        this.restTemplate = builder.build();
        this.externalBase = dbServiceUrl + "/db/meal-plans";
    }

    // --- MealPlan CRUD ---

    public List<MealPlanDTO> getAllMealPlans() {
        try {
            ResponseEntity<MealPlanDTO[]> response = restTemplate.getForEntity(externalBase, MealPlanDTO[].class);
            return Arrays.asList(Optional.ofNullable(response.getBody()).orElse(new MealPlanDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public ResponseEntity<MealPlanDTO> getMealPlanById(Long id) {
        try {
            return restTemplate.getForEntity(externalBase + "/" + id, MealPlanDTO.class);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public List<MealPlanDTO> getMealPlansByGroupId(Long groupId) {
        try {
            ResponseEntity<MealPlanDTO[]> response = restTemplate.getForEntity(
                    externalBase + "/group/" + groupId, MealPlanDTO[].class);
            return Arrays.asList(Optional.ofNullable(response.getBody()).orElse(new MealPlanDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public List<MealPlanDTO> getMealPlansByUserId(Long userId) {
        try {
            ResponseEntity<MealPlanDTO[]> response = restTemplate.getForEntity(
                    externalBase + "/user/" + userId, MealPlanDTO[].class);
            return Arrays.asList(Optional.ofNullable(response.getBody()).orElse(new MealPlanDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public List<MealPlanDTO> getActiveMealPlans(Long groupId, LocalDate date) {
        try {
            ResponseEntity<MealPlanDTO[]> response = restTemplate.getForEntity(
                    externalBase + "/group/" + groupId + "/active?date=" + date, MealPlanDTO[].class);
            return Arrays.asList(Optional.ofNullable(response.getBody()).orElse(new MealPlanDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public ResponseEntity<?> createMealPlan(MealPlanDTO dto) {
        try {
            ResponseEntity<MealPlanDTO> response = restTemplate.postForEntity(externalBase, dto, MealPlanDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to create meal plan"));
        }
    }

    public ResponseEntity<MealPlanDTO> updateMealPlan(Long id, MealPlanDTO dto) {
        try {
            restTemplate.put(externalBase + "/" + id, dto);
            return restTemplate.getForEntity(externalBase + "/" + id, MealPlanDTO.class);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteMealPlan(Long id) {
        try {
            restTemplate.delete(externalBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Day operations ---

    public List<MealPlanDayDTO> getDaysByMealPlanId(Long mealPlanId) {
        try {
            ResponseEntity<MealPlanDayDTO[]> response = restTemplate.getForEntity(
                    externalBase + "/" + mealPlanId + "/days", MealPlanDayDTO[].class);
            return Arrays.asList(Optional.ofNullable(response.getBody()).orElse(new MealPlanDayDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public ResponseEntity<MealPlanDayDTO> getDayById(Long dayId) {
        try {
            return restTemplate.getForEntity(externalBase + "/days/" + dayId, MealPlanDayDTO.class);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<MealPlanDayDTO> getDayByDate(Long mealPlanId, LocalDate date) {
        try {
            return restTemplate.getForEntity(
                    externalBase + "/" + mealPlanId + "/days/date/" + date, MealPlanDayDTO.class);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Item operations ---

    public List<MealPlanItemDTO> getItemsByDayId(Long dayId) {
        try {
            ResponseEntity<MealPlanItemDTO[]> response = restTemplate.getForEntity(
                    externalBase + "/days/" + dayId + "/items", MealPlanItemDTO[].class);
            return Arrays.asList(Optional.ofNullable(response.getBody()).orElse(new MealPlanItemDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public ResponseEntity<?> addItem(Long dayId, MealPlanItemDTO dto) {
        try {
            ResponseEntity<MealPlanItemDTO> response = restTemplate.postForEntity(
                    externalBase + "/days/" + dayId + "/items", dto, MealPlanItemDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to add item"));
        }
    }

    public ResponseEntity<?> updateItem(Long itemId, MealPlanItemDTO dto) {
        try {
            restTemplate.put(externalBase + "/items/" + itemId, dto);
            return ResponseEntity.ok(dto);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteItem(Long itemId) {
        try {
            restTemplate.delete(externalBase + "/items/" + itemId);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
