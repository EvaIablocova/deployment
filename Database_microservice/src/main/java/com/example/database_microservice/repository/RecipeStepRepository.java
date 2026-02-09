package com.example.database_microservice.repository;

import com.example.database_microservice.model.RecipeStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {
    List<RecipeStep> findByRecipeIdOrderByStepNumberAsc(Long recipeId);
    void deleteByRecipeId(Long recipeId);
}
