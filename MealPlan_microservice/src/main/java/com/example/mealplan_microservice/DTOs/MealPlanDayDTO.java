package com.example.mealplan_microservice.DTOs;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class MealPlanDayDTO {
    private Long id;
    private Long mealPlanId;
    private LocalDate date;
    private Integer dayOfWeek;
    private String dayName;
    private List<MealPlanItemDTO> items = new ArrayList<>();
}
