package com.example.mealplan_microservice.service;

import com.example.mealplan_microservice.DTOs.MealPlanDTO;
import com.example.mealplan_microservice.DTOs.MealPlanDayDTO;
import com.example.mealplan_microservice.DTOs.MealPlanItemDTO;
import com.example.mealplan_microservice.repository.MealPlanRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MealPlanService {

    private final MealPlanRepository mealPlanRepository;

    public MealPlanService(MealPlanRepository mealPlanRepository) {
        this.mealPlanRepository = mealPlanRepository;
    }

    // --- MealPlan CRUD ---

    public List<MealPlanDTO> getAllMealPlans() {
        return mealPlanRepository.getAllMealPlans();
    }

    public Optional<MealPlanDTO> getMealPlanById(Long id) {
        ResponseEntity<MealPlanDTO> response = mealPlanRepository.getMealPlanById(id);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return Optional.of(response.getBody());
        }
        return Optional.empty();
    }

    public List<MealPlanDTO> getMealPlansByGroupId(Long groupId) {
        return mealPlanRepository.getMealPlansByGroupId(groupId);
    }

    public List<MealPlanDTO> getMealPlansByUserId(Long userId) {
        return mealPlanRepository.getMealPlansByUserId(userId);
    }

    public List<MealPlanDTO> getActiveMealPlans(Long groupId, LocalDate date) {
        return mealPlanRepository.getActiveMealPlans(groupId, date);
    }

    public ResponseEntity<?> createMealPlan(MealPlanDTO dto) {
        return mealPlanRepository.createMealPlan(dto);
    }

    public ResponseEntity<MealPlanDTO> updateMealPlan(Long id, MealPlanDTO dto) {
        return mealPlanRepository.updateMealPlan(id, dto);
    }

    public ResponseEntity<Void> deleteMealPlan(Long id) {
        return mealPlanRepository.deleteMealPlan(id);
    }

    // --- Day operations ---

    public List<MealPlanDayDTO> getDaysByMealPlanId(Long mealPlanId) {
        return mealPlanRepository.getDaysByMealPlanId(mealPlanId);
    }

    public Optional<MealPlanDayDTO> getDayById(Long dayId) {
        ResponseEntity<MealPlanDayDTO> response = mealPlanRepository.getDayById(dayId);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return Optional.of(response.getBody());
        }
        return Optional.empty();
    }

    public Optional<MealPlanDayDTO> getDayByDate(Long mealPlanId, LocalDate date) {
        ResponseEntity<MealPlanDayDTO> response = mealPlanRepository.getDayByDate(mealPlanId, date);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return Optional.of(response.getBody());
        }
        return Optional.empty();
    }

    // --- Item operations ---

    public List<MealPlanItemDTO> getItemsByDayId(Long dayId) {
        return mealPlanRepository.getItemsByDayId(dayId);
    }

    public ResponseEntity<?> addItem(Long dayId, MealPlanItemDTO dto) {
        return mealPlanRepository.addItem(dayId, dto);
    }

    public ResponseEntity<?> updateItem(Long itemId, MealPlanItemDTO dto) {
        return mealPlanRepository.updateItem(itemId, dto);
    }

    public ResponseEntity<Void> deleteItem(Long itemId) {
        return mealPlanRepository.deleteItem(itemId);
    }
}
