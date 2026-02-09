package com.example.database_microservice.service;

import com.example.database_microservice.DTOs.MealPlanDTO;
import com.example.database_microservice.DTOs.MealPlanDayDTO;
import com.example.database_microservice.DTOs.MealPlanItemDTO;
import com.example.database_microservice.model.MealPlan;
import com.example.database_microservice.model.MealPlanDay;
import com.example.database_microservice.model.MealPlanItem;
import com.example.database_microservice.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MealPlanService {
    private final MealPlanRepository mealPlanRepository;
    private final MealPlanDayRepository dayRepository;
    private final MealPlanItemRepository itemRepository;
    private final RecipeRepository recipeRepository;
    private final ProductRepository productRepository;

    public MealPlanService(MealPlanRepository mealPlanRepository,
                           MealPlanDayRepository dayRepository,
                           MealPlanItemRepository itemRepository,
                           RecipeRepository recipeRepository,
                           ProductRepository productRepository) {
        this.mealPlanRepository = mealPlanRepository;
        this.dayRepository = dayRepository;
        this.itemRepository = itemRepository;
        this.recipeRepository = recipeRepository;
        this.productRepository = productRepository;
    }

    private MealPlanDTO toDTO(MealPlan mealPlan) {
        return new MealPlanDTO(mealPlan);
    }

    // --- MealPlan CRUD ---

    public List<MealPlanDTO> getAllMealPlans() {
        return mealPlanRepository.findAll().stream().map(this::toDTO).toList();
    }

    public Optional<MealPlanDTO> getMealPlanById(Long id) {
        return mealPlanRepository.findById(id).map(this::toDTO);
    }

    public List<MealPlanDTO> getMealPlansByGroupId(Long groupId) {
        return mealPlanRepository.findByGroupIdOrderByStartDateDesc(groupId)
                .stream().map(this::toDTO).toList();
    }

    public List<MealPlanDTO> getMealPlansByUserId(Long userId) {
        return mealPlanRepository.findByCreatedBy(userId)
                .stream().map(this::toDTO).toList();
    }

    public List<MealPlanDTO> getActiveMealPlans(Long groupId, LocalDate date) {
        return mealPlanRepository.findByGroupIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                groupId, date, date).stream().map(this::toDTO).toList();
    }

    @Transactional
    public MealPlanDTO createMealPlan(MealPlanDTO dto) {
        MealPlan mealPlan = new MealPlan(dto);
        MealPlan saved = mealPlanRepository.save(mealPlan);

        // Create days for the date range
        LocalDate current = dto.getStartDate();
        while (!current.isAfter(dto.getEndDate())) {
            MealPlanDay day = new MealPlanDay();
            day.setMealPlan(saved);
            day.setDate(current);
            day.setDayOfWeek(current.getDayOfWeek().getValue());
            dayRepository.save(day);
            current = current.plusDays(1);
        }

        return getMealPlanById(saved.getId()).orElse(null);
    }

    @Transactional
    public Optional<MealPlanDTO> updateMealPlan(Long id, MealPlanDTO dto) {
        return mealPlanRepository.findById(id).map(mealPlan -> {
            mealPlan.setName(dto.getName());
            mealPlan.setDescription(dto.getDescription());
            return toDTO(mealPlanRepository.save(mealPlan));
        });
    }

    @Transactional
    public boolean deleteMealPlan(Long id) {
        if (mealPlanRepository.existsById(id)) {
            mealPlanRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // --- MealPlanDay operations ---

    public List<MealPlanDayDTO> getDaysByMealPlanId(Long mealPlanId) {
        return dayRepository.findByMealPlanIdOrderByDateAsc(mealPlanId)
                .stream().map(MealPlanDayDTO::new).toList();
    }

    public Optional<MealPlanDayDTO> getDayById(Long dayId) {
        return dayRepository.findById(dayId).map(MealPlanDayDTO::new);
    }

    public Optional<MealPlanDayDTO> getDayByDate(Long mealPlanId, LocalDate date) {
        return dayRepository.findByMealPlanIdAndDate(mealPlanId, date)
                .map(MealPlanDayDTO::new);
    }

    // --- MealPlanItem operations ---

    public List<MealPlanItemDTO> getItemsByDayId(Long dayId) {
        return itemRepository.findByMealPlanDayIdOrderByMealTypeAscSortOrderAsc(dayId)
                .stream().map(MealPlanItemDTO::new).toList();
    }

    @Transactional
    public Optional<MealPlanItemDTO> addItem(Long dayId, MealPlanItemDTO dto) {
        return dayRepository.findById(dayId).map(day -> {
            MealPlanItem item = new MealPlanItem(dto);
            item.setMealPlanDay(day);

            if (dto.getRecipeId() != null) {
                recipeRepository.findById(dto.getRecipeId())
                        .ifPresent(item::setRecipe);
            }
            if (dto.getProductId() != null) {
                productRepository.findById(dto.getProductId())
                        .ifPresent(item::setProduct);
            }

            return new MealPlanItemDTO(itemRepository.save(item));
        });
    }

    @Transactional
    public Optional<MealPlanItemDTO> updateItem(Long itemId, MealPlanItemDTO dto) {
        return itemRepository.findById(itemId).map(item -> {
            item.setMealType(dto.getMealType());
            item.setCustomMeal(dto.getCustomMeal());
            item.setNotes(dto.getNotes());
            item.setServings(dto.getServings());
            item.setSortOrder(dto.getSortOrder());

            if (dto.getRecipeId() != null) {
                recipeRepository.findById(dto.getRecipeId())
                        .ifPresent(item::setRecipe);
                item.setProduct(null);
                item.setCustomMeal(null);
            } else if (dto.getProductId() != null) {
                productRepository.findById(dto.getProductId())
                        .ifPresent(item::setProduct);
                item.setRecipe(null);
                item.setCustomMeal(null);
            } else {
                item.setRecipe(null);
                item.setProduct(null);
            }

            return new MealPlanItemDTO(itemRepository.save(item));
        });
    }

    @Transactional
    public boolean deleteItem(Long itemId) {
        if (itemRepository.existsById(itemId)) {
            itemRepository.deleteById(itemId);
            return true;
        }
        return false;
    }
}
