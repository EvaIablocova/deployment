package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.MealPlan;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class MealPlanDTO {
    private Long id;
    private String name;
    private String description;
    private Long groupId;
    private Long createdBy;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<MealPlanDayDTO> days = new ArrayList<>();

    public MealPlanDTO() {}

    public MealPlanDTO(MealPlan mealPlan) {
        this.id = mealPlan.getId();
        this.name = mealPlan.getName();
        this.description = mealPlan.getDescription();
        this.groupId = mealPlan.getGroupId();
        this.createdBy = mealPlan.getCreatedBy();
        this.startDate = mealPlan.getStartDate();
        this.endDate = mealPlan.getEndDate();
        this.createdAt = mealPlan.getCreatedAt();
        this.updatedAt = mealPlan.getUpdatedAt();

        if (mealPlan.getDays() != null) {
            this.days = mealPlan.getDays().stream()
                    .map(MealPlanDayDTO::new)
                    .toList();
        }
    }
}
