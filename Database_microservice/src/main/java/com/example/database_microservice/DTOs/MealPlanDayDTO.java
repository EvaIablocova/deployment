package com.example.database_microservice.DTOs;

import com.example.database_microservice.model.MealPlanDay;
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

    public MealPlanDayDTO() {}

    public MealPlanDayDTO(MealPlanDay day) {
        this.id = day.getId();
        this.mealPlanId = day.getMealPlan() != null ? day.getMealPlan().getId() : null;
        this.date = day.getDate();
        this.dayOfWeek = day.getDayOfWeek();
        this.dayName = day.getDate() != null ? day.getDate().getDayOfWeek().name() : null;

        if (day.getItems() != null) {
            this.items = day.getItems().stream()
                    .map(MealPlanItemDTO::new)
                    .toList();
        }
    }
}
