package com.example.database_microservice.repository;

import com.example.database_microservice.model.Competition;
import com.example.database_microservice.model.CompetitionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {

    List<Competition> findByGroupId(Long groupId);

    List<Competition> findByCreatedBy(Long userId);

    List<Competition> findByStatus(CompetitionStatus status);

    List<Competition> findByGroupIdAndStatus(Long groupId, CompetitionStatus status);

    @Query("SELECT c FROM Competition c WHERE c.groupId = :groupId ORDER BY c.updatedAt DESC")
    List<Competition> findByGroupIdOrderByUpdatedAtDesc(@Param("groupId") Long groupId);

    @Query("SELECT c FROM Competition c WHERE c.status = 'ACTIVE' ORDER BY c.endDate ASC")
    List<Competition> findActiveCompetitions();

    @Query("SELECT c FROM Competition c WHERE c.status = 'PENDING' AND c.startDate <= CURRENT_DATE")
    List<Competition> findPendingCompetitionsToStart();

    @Query("SELECT c FROM Competition c WHERE c.status = 'ACTIVE' AND c.endDate < CURRENT_DATE")
    List<Competition> findActiveCompetitionsToComplete();
}
