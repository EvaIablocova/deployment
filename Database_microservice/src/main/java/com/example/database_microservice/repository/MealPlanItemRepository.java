package com.example.database_microservice.repository;

import com.example.database_microservice.model.MealPlanItem;
import com.example.database_microservice.model.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealPlanItemRepository extends JpaRepository<MealPlanItem, Long> {
    List<MealPlanItem> findByMealPlanDayIdOrderByMealTypeAscSortOrderAsc(Long dayId);
    List<MealPlanItem> findByMealPlanDayIdAndMealType(Long dayId, MealType mealType);
}
