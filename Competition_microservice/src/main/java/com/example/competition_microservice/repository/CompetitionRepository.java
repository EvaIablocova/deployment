package com.example.competition_microservice.repository;

import com.example.competition_microservice.DTOs.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class CompetitionRepository {

    private final RestTemplate restTemplate;
    private final String externalBase = "http://dbmicroservice:9009/db/competitions";

    public CompetitionRepository(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    // ==================== Basic CRUD ====================

    public List<CompetitionDTO> getAllCompetitions() {
        try {
            ResponseEntity<CompetitionDTO[]> response = restTemplate.getForEntity(externalBase, CompetitionDTO[].class);
            CompetitionDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new CompetitionDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public ResponseEntity<CompetitionDTO> getCompetitionById(Long id) {
        try {
            ResponseEntity<CompetitionDTO> response =
                    restTemplate.getForEntity(externalBase + "/" + id, CompetitionDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public List<CompetitionDTO> getCompetitionsByGroupId(Long groupId) {
        try {
            ResponseEntity<CompetitionDTO[]> response =
                    restTemplate.getForEntity(externalBase + "/group/" + groupId, CompetitionDTO[].class);
            CompetitionDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new CompetitionDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public List<CompetitionDTO> getCompetitionsByStatus(CompetitionStatus status) {
        try {
            ResponseEntity<CompetitionDTO[]> response =
                    restTemplate.getForEntity(externalBase + "/status/" + status, CompetitionDTO[].class);
            CompetitionDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new CompetitionDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public List<CompetitionDTO> getCompetitionsByGroupIdAndStatus(Long groupId, CompetitionStatus status) {
        try {
            ResponseEntity<CompetitionDTO[]> response =
                    restTemplate.getForEntity(externalBase + "/group/" + groupId + "/status/" + status, CompetitionDTO[].class);
            CompetitionDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new CompetitionDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public ResponseEntity<CompetitionDTO> createCompetition(CompetitionDTO competitionDTO) {
        try {
            ResponseEntity<CompetitionDTO> response =
                    restTemplate.postForEntity(externalBase, competitionDTO, CompetitionDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public ResponseEntity<CompetitionDTO> updateCompetition(Long id, CompetitionDTO updatedCompetitionDTO) {
        try {
            restTemplate.put(externalBase + "/" + id, updatedCompetitionDTO);
            return getCompetitionById(id);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteCompetition(Long id) {
        try {
            restTemplate.delete(externalBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== Status Management ====================

    public ResponseEntity<CompetitionDTO> startCompetition(Long id) {
        try {
            ResponseEntity<CompetitionDTO> response =
                    restTemplate.patchForObject(externalBase + "/" + id + "/start", null, ResponseEntity.class);
            return getCompetitionById(id);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<CompetitionDTO> completeCompetition(Long id) {
        try {
            restTemplate.patchForObject(externalBase + "/" + id + "/complete", null, Void.class);
            return getCompetitionById(id);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<CompetitionDTO> cancelCompetition(Long id) {
        try {
            restTemplate.patchForObject(externalBase + "/" + id + "/cancel", null, Void.class);
            return getCompetitionById(id);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== Participant Management ====================

    public ResponseEntity<CompetitionDTO> joinCompetition(Long id, JoinCompetitionDTO joinDTO) {
        try {
            ResponseEntity<CompetitionDTO> response =
                    restTemplate.postForEntity(externalBase + "/" + id + "/join", joinDTO, CompetitionDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<CompetitionDTO> leaveCompetition(Long id, Long userId) {
        try {
            restTemplate.delete(externalBase + "/" + id + "/leave/" + userId);
            return getCompetitionById(id);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== Progress Management ====================

    public ResponseEntity<CompetitionParticipantDTO> updateProgress(UpdateProgressDTO updateDTO) {
        try {
            ResponseEntity<CompetitionParticipantDTO> response =
                    restTemplate.patchForObject(externalBase + "/progress", updateDTO, ResponseEntity.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== Leaderboard ====================

    public List<CompetitionParticipantDTO> getLeaderboard(Long id) {
        try {
            ResponseEntity<CompetitionParticipantDTO[]> response =
                    restTemplate.getForEntity(externalBase + "/" + id + "/leaderboard", CompetitionParticipantDTO[].class);
            CompetitionParticipantDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new CompetitionParticipantDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }
}
