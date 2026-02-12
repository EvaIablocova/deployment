package com.example.competition_microservice.service;

import com.example.competition_microservice.DTOs.*;
import com.example.competition_microservice.repository.CompetitionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompetitionService {

    private final CompetitionRepository competitionRepository;

    public CompetitionService(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
    }

    // ==================== Basic CRUD ====================

    public List<CompetitionDTO> getAllCompetitions() {
        return competitionRepository.getAllCompetitions();
    }

    public Optional<CompetitionDTO> getCompetitionById(Long id) {
        return Optional.ofNullable(competitionRepository.getCompetitionById(id))
                .map(ResponseEntity::getBody);
    }

    public List<CompetitionDTO> getCompetitionsByGroupId(Long groupId) {
        return competitionRepository.getCompetitionsByGroupId(groupId);
    }

    public List<CompetitionDTO> getCompetitionsByStatus(CompetitionStatus status) {
        return competitionRepository.getCompetitionsByStatus(status);
    }

    public List<CompetitionDTO> getCompetitionsByGroupIdAndStatus(Long groupId, CompetitionStatus status) {
        return competitionRepository.getCompetitionsByGroupIdAndStatus(groupId, status);
    }

    public CompetitionDTO createCompetition(CompetitionDTO competitionDTO) {
        return competitionRepository.createCompetition(competitionDTO).getBody();
    }

    public ResponseEntity<CompetitionDTO> updateCompetition(Long id, CompetitionDTO updatedCompetitionDTO) {
        return competitionRepository.updateCompetition(id, updatedCompetitionDTO);
    }

    public ResponseEntity<Void> deleteCompetition(Long id) {
        return competitionRepository.deleteCompetition(id);
    }

    // ==================== Status Management ====================

    public ResponseEntity<CompetitionDTO> startCompetition(Long id) {
        return competitionRepository.startCompetition(id);
    }

    public ResponseEntity<CompetitionDTO> completeCompetition(Long id) {
        return competitionRepository.completeCompetition(id);
    }

    public ResponseEntity<CompetitionDTO> cancelCompetition(Long id) {
        return competitionRepository.cancelCompetition(id);
    }

    // ==================== Participant Management ====================

    public ResponseEntity<CompetitionDTO> joinCompetition(Long id, JoinCompetitionDTO joinDTO) {
        return competitionRepository.joinCompetition(id, joinDTO);
    }

    public ResponseEntity<CompetitionDTO> leaveCompetition(Long id, Long userId) {
        return competitionRepository.leaveCompetition(id, userId);
    }

    // ==================== Progress Management ====================

    public ResponseEntity<CompetitionParticipantDTO> updateProgress(UpdateProgressDTO updateDTO) {
        return competitionRepository.updateProgress(updateDTO);
    }

    // ==================== Leaderboard ====================

    public List<CompetitionParticipantDTO> getLeaderboard(Long id) {
        return competitionRepository.getLeaderboard(id);
    }

    // ==================== Legacy Methods ====================

    public List<CompetitionDTO> getAllCompetitionsByUserId(Long id) {
        List<CompetitionDTO> competitions = competitionRepository.getAllCompetitions();
        return competitions.stream()
                .filter(competition -> competition.getUserId() != null && competition.getUserId().contains(id))
                .toList();
    }
}
