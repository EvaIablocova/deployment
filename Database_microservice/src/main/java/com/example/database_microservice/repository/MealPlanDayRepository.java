package com.example.database_microservice.repository;

import com.example.database_microservice.model.MealPlanDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MealPlanDayRepository extends JpaRepository<MealPlanDay, Long> {
    List<MealPlanDay> findByMealPlanIdOrderByDateAsc(Long mealPlanId);
    Optional<MealPlanDay> findByMealPlanIdAndDate(Long mealPlanId, LocalDate date);
}
