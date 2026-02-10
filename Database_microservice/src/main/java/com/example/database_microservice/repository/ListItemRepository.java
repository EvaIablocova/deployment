package com.example.database_microservice.repository;

import com.example.database_microservice.model.ListItem;
import com.example.database_microservice.model.ListItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListItemRepository extends JpaRepository<ListItem, Long> {

    List<ListItem> findByListId(Long listId);

    List<ListItem> findByListIdOrderBySortOrderAsc(Long listId);

    List<ListItem> findByListIdAndDone(Long listId, boolean done);

    List<ListItem> findByListIdAndItemType(Long listId, ListItemType itemType);

    List<ListItem> findBySourceMealPlanId(Long mealPlanId);

    @Modifying
    @Query("DELETE FROM ListItem li WHERE li.list.id = :listId")
    void deleteByListId(@Param("listId") Long listId);

    @Query("SELECT COALESCE(MAX(li.sortOrder), 0) FROM ListItem li WHERE li.list.id = :listId")
    Integer findMaxSortOrderByListId(@Param("listId") Long listId);

}


