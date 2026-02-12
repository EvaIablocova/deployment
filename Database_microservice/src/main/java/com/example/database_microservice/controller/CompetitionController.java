package com.example.database_microservice.controller;

import com.example.database_microservice.DTOs.CompetitionDTO;
import com.example.database_microservice.DTOs.CompetitionParticipantDTO;
import com.example.database_microservice.DTOs.JoinCompetitionDTO;
import com.example.database_microservice.DTOs.UpdateProgressDTO;
import com.example.database_microservice.model.CompetitionStatus;
import com.example.database_microservice.service.CompetitionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/db/competitions")
public class CompetitionController {
    private final CompetitionService competitionService;

    public CompetitionController(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    // ==================== Basic CRUD ====================

    @GetMapping
    public List<CompetitionDTO> getAllCompetitions() {
        return competitionService.getAllCompetitions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetitionDTO> getCompetitionById(@PathVariable Long id) {
        return competitionService.getCompetitionById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/group/{groupId}")
    public List<CompetitionDTO> getCompetitionsByGroupId(@PathVariable Long groupId) {
        return competitionService.getCompetitionsByGroupId(groupId);
    }

    @GetMapping("/status/{status}")
    public List<CompetitionDTO> getCompetitionsByStatus(@PathVariable CompetitionStatus status) {
        return competitionService.getCompetitionsByStatus(status);
    }

    @GetMapping("/group/{groupId}/status/{status}")
    public List<CompetitionDTO> getCompetitionsByGroupIdAndStatus(
            @PathVariable Long groupId,
            @PathVariable CompetitionStatus status) {
        return competitionService.getCompetitionsByGroupIdAndStatus(groupId, status);
    }

    @PostMapping
    public CompetitionDTO createCompetition(@RequestBody CompetitionDTO competitionDTO) {
        return competitionService.createCompetition(competitionDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetitionDTO> updateCompetition(
            @PathVariable Long id,
            @RequestBody CompetitionDTO updatedCompetitionDTO) {
        return competitionService.updateCompetition(id, updatedCompetitionDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompetition(@PathVariable Long id) {
        return competitionService.deleteCompetition(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // ==================== Status Management ====================

    @PatchMapping("/{id}/start")
    public ResponseEntity<CompetitionDTO> startCompetition(@PathVariable Long id) {
        return competitionService.startCompetition(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<CompetitionDTO> completeCompetition(@PathVariable Long id) {
        return competitionService.completeCompetition(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<CompetitionDTO> cancelCompetition(@PathVariable Long id) {
        return competitionService.cancelCompetition(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ==================== Participant Management ====================

    @PostMapping("/{id}/join")
    public ResponseEntity<CompetitionDTO> joinCompetition(
            @PathVariable Long id,
            @RequestBody JoinCompetitionDTO joinDTO) {
        return competitionService.joinCompetition(id, joinDTO.getUserId(), joinDTO.getUsername())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/leave/{userId}")
    public ResponseEntity<CompetitionDTO> leaveCompetition(
            @PathVariable Long id,
            @PathVariable Long userId) {
        return competitionService.leaveCompetition(id, userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ==================== Progress Management ====================

    @PatchMapping("/progress")
    public ResponseEntity<CompetitionParticipantDTO> updateProgress(
            @RequestBody UpdateProgressDTO updateDTO) {
        return competitionService.updateProgress(updateDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ==================== Leaderboard ====================

    @GetMapping("/{id}/leaderboard")
    public List<CompetitionParticipantDTO> getLeaderboard(@PathVariable Long id) {
        return competitionService.getLeaderboard(id);
    }
}
