package com.example.mealplan_microservice.controller;

import com.example.mealplan_microservice.DTOs.MealPlanDTO;
import com.example.mealplan_microservice.DTOs.MealPlanDayDTO;
import com.example.mealplan_microservice.DTOs.MealPlanItemDTO;
import com.example.mealplan_microservice.service.MealPlanService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/meal-plans")
public class MealPlanController {

    private final MealPlanService mealPlanService;

    public MealPlanController(MealPlanService mealPlanService) {
        this.mealPlanService = mealPlanService;
    }

    // --- MealPlan CRUD ---

    @GetMapping
    public List<MealPlanDTO> getAllMealPlans() {
        return mealPlanService.getAllMealPlans();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealPlanDTO> getMealPlanById(@PathVariable Long id) {
        return mealPlanService.getMealPlanById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/group/{groupId}")
    public List<MealPlanDTO> getMealPlansByGroupId(@PathVariable Long groupId) {
        return mealPlanService.getMealPlansByGroupId(groupId);
    }

    @GetMapping("/user/{userId}")
    public List<MealPlanDTO> getMealPlansByUserId(@PathVariable Long userId) {
        return mealPlanService.getMealPlansByUserId(userId);
    }

    @GetMapping("/group/{groupId}/active")
    public List<MealPlanDTO> getActiveMealPlans(
            @PathVariable Long groupId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return mealPlanService.getActiveMealPlans(groupId, date);
    }

    @PostMapping
    public ResponseEntity<?> createMealPlan(@RequestBody MealPlanDTO mealPlanDTO) {
        return mealPlanService.createMealPlan(mealPlanDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MealPlanDTO> updateMealPlan(@PathVariable Long id, @RequestBody MealPlanDTO mealPlanDTO) {
        return mealPlanService.updateMealPlan(id, mealPlanDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMealPlan(@PathVariable Long id) {
        return mealPlanService.deleteMealPlan(id);
    }

    // --- Day operations ---

    @GetMapping("/{mealPlanId}/days")
    public List<MealPlanDayDTO> getDaysByMealPlanId(@PathVariable Long mealPlanId) {
        return mealPlanService.getDaysByMealPlanId(mealPlanId);
    }

    @GetMapping("/days/{dayId}")
    public ResponseEntity<MealPlanDayDTO> getDayById(@PathVariable Long dayId) {
        return mealPlanService.getDayById(dayId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{mealPlanId}/days/date/{date}")
    public ResponseEntity<MealPlanDayDTO> getDayByDate(
            @PathVariable Long mealPlanId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return mealPlanService.getDayByDate(mealPlanId, date)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // --- Item operations ---

    @GetMapping("/days/{dayId}/items")
    public List<MealPlanItemDTO> getItemsByDayId(@PathVariable Long dayId) {
        return mealPlanService.getItemsByDayId(dayId);
    }

    @PostMapping("/days/{dayId}/items")
    public ResponseEntity<?> addItem(
            @PathVariable Long dayId,
            @RequestBody MealPlanItemDTO itemDTO) {
        return mealPlanService.addItem(dayId, itemDTO);
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<?> updateItem(
            @PathVariable Long itemId,
            @RequestBody MealPlanItemDTO itemDTO) {
        return mealPlanService.updateItem(itemId, itemDTO);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        return mealPlanService.deleteItem(itemId);
    }
}
