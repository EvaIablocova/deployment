package com.example.database_microservice.model;

import com.example.database_microservice.DTOs.MealPlanDayDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "meal_plan_day")
public class MealPlanDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_plan_id", nullable = false)
    @JsonIgnore
    private MealPlan mealPlan;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "day_of_week")
    private Integer dayOfWeek;

    @OneToMany(mappedBy = "mealPlanDay", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("mealType ASC, sortOrder ASC")
    private List<MealPlanItem> items = new ArrayList<>();

    public MealPlanDay() {}

    public MealPlanDay(MealPlanDayDTO dto) {
        this.id = dto.getId();
        this.date = dto.getDate();
        this.dayOfWeek = dto.getDayOfWeek();
    }

    @PrePersist
    @PreUpdate
    protected void setDayOfWeek() {
        if (date != null) {
            this.dayOfWeek = date.getDayOfWeek().getValue();
        }
    }
}
