package com.example.database_microservice.repository;

import com.example.database_microservice.model.MealPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {
    List<MealPlan> findByGroupId(Long groupId);
    List<MealPlan> findByCreatedBy(Long userId);
    List<MealPlan> findByGroupIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long groupId, LocalDate endDate, LocalDate startDate);
    List<MealPlan> findByGroupIdOrderByStartDateDesc(Long groupId);
}
