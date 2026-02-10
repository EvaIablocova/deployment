package com.example.database_microservice.repository;

import com.example.database_microservice.model.ListType;
import com.example.database_microservice.model.Lists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListRepository extends JpaRepository<Lists, Long> {

    List<Lists> findByGroupId(Long groupId);

    List<Lists> findByCreatedBy(Long createdBy);

    List<Lists> findByListType(ListType listType);

    List<Lists> findByGroupIdAndListType(Long groupId, ListType listType);

    @Query("SELECT l FROM Lists l WHERE l.groupId = :groupId ORDER BY l.updatedAt DESC")
    List<Lists> findByGroupIdOrderByUpdatedAtDesc(@Param("groupId") Long groupId);

}


