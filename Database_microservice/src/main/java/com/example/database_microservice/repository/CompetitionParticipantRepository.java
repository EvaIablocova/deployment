package com.example.database_microservice.repository;

import com.example.database_microservice.model.CompetitionParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompetitionParticipantRepository extends JpaRepository<CompetitionParticipant, Long> {

    List<CompetitionParticipant> findByCompetitionId(Long competitionId);

    List<CompetitionParticipant> findByUserId(Long userId);

    Optional<CompetitionParticipant> findByCompetitionIdAndUserId(Long competitionId, Long userId);

    @Query("SELECT p FROM CompetitionParticipant p WHERE p.competition.id = :competitionId ORDER BY p.currentValue DESC")
    List<CompetitionParticipant> findByCompetitionIdOrderByCurrentValueDesc(@Param("competitionId") Long competitionId);

    boolean existsByCompetitionIdAndUserId(Long competitionId, Long userId);

    void deleteByCompetitionIdAndUserId(Long competitionId, Long userId);

    @Query("SELECT COUNT(p) FROM CompetitionParticipant p WHERE p.competition.id = :competitionId")
    int countByCompetitionId(@Param("competitionId") Long competitionId);
}
