package com.example.mealplan_microservice.DTOs;

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
}
