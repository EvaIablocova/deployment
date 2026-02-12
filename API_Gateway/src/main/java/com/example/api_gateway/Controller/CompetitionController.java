package com.example.api_gateway.Controller;

import com.example.api_gateway.DTOs.*;
import com.example.api_gateway.config.ServiceUrlsConfig;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api_gateway/competitions")
public class CompetitionController {

    private final RestTemplate restTemplate;
    private final String externalBase;

    public CompetitionController(RestTemplateBuilder builder, ServiceUrlsConfig serviceUrls) {
        this.restTemplate = builder.build();
        this.externalBase = serviceUrls.getCompetitionServiceUrl() + "/api/competitions";
    }

    // ==================== Basic CRUD ====================

    @GetMapping
    public List<CompetitionDTO> getAllCompetitions() {
        try {
            ResponseEntity<CompetitionDTO[]> response = restTemplate.getForEntity(externalBase, CompetitionDTO[].class);
            CompetitionDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new CompetitionDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetitionDTO> getCompetitionById(@PathVariable Long id) {
        try {
            ResponseEntity<CompetitionDTO> response =
                    restTemplate.getForEntity(externalBase + "/" + id, CompetitionDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/group/{groupId}")
    public List<CompetitionDTO> getCompetitionsByGroupId(@PathVariable Long groupId) {
        try {
            ResponseEntity<CompetitionDTO[]> response =
                    restTemplate.getForEntity(externalBase + "/group/" + groupId, CompetitionDTO[].class);
            CompetitionDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new CompetitionDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/status/{status}")
    public List<CompetitionDTO> getCompetitionsByStatus(@PathVariable CompetitionStatus status) {
        try {
            ResponseEntity<CompetitionDTO[]> response =
                    restTemplate.getForEntity(externalBase + "/status/" + status, CompetitionDTO[].class);
            CompetitionDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new CompetitionDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @GetMapping("/group/{groupId}/status/{status}")
    public List<CompetitionDTO> getCompetitionsByGroupIdAndStatus(
            @PathVariable Long groupId,
            @PathVariable CompetitionStatus status) {
        try {
            ResponseEntity<CompetitionDTO[]> response =
                    restTemplate.getForEntity(externalBase + "/group/" + groupId + "/status/" + status, CompetitionDTO[].class);
            CompetitionDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new CompetitionDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    @PostMapping
    public ResponseEntity<CompetitionDTO> createCompetition(@RequestBody CompetitionDTO competitionDTO) {
        try {
            ResponseEntity<CompetitionDTO> response =
                    restTemplate.postForEntity(externalBase, competitionDTO, CompetitionDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetitionDTO> updateCompetition(
            @PathVariable Long id,
            @RequestBody CompetitionDTO updatedCompetitionDTO) {
        try {
            restTemplate.put(externalBase + "/" + id, updatedCompetitionDTO);
            return getCompetitionById(id);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompetition(@PathVariable Long id) {
        try {
            restTemplate.delete(externalBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== Status Management ====================

    @PatchMapping("/{id}/start")
    public ResponseEntity<CompetitionDTO> startCompetition(@PathVariable Long id) {
        try {
            restTemplate.patchForObject(externalBase + "/" + id + "/start", null, Void.class);
            return getCompetitionById(id);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<CompetitionDTO> completeCompetition(@PathVariable Long id) {
        try {
            restTemplate.patchForObject(externalBase + "/" + id + "/complete", null, Void.class);
            return getCompetitionById(id);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<CompetitionDTO> cancelCompetition(@PathVariable Long id) {
        try {
            restTemplate.patchForObject(externalBase + "/" + id + "/cancel", null, Void.class);
            return getCompetitionById(id);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== Participant Management ====================

    @PostMapping("/{id}/join")
    public ResponseEntity<CompetitionDTO> joinCompetition(
            @PathVariable Long id,
            @RequestBody JoinCompetitionDTO joinDTO) {
        try {
            ResponseEntity<CompetitionDTO> response =
                    restTemplate.postForEntity(externalBase + "/" + id + "/join", joinDTO, CompetitionDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/leave/{userId}")
    public ResponseEntity<CompetitionDTO> leaveCompetition(
            @PathVariable Long id,
            @PathVariable Long userId) {
        try {
            restTemplate.delete(externalBase + "/" + id + "/leave/" + userId);
            return getCompetitionById(id);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== Progress Management ====================

    @PatchMapping("/progress")
    public ResponseEntity<CompetitionParticipantDTO> updateProgress(
            @RequestBody UpdateProgressDTO updateDTO) {
        try {
            ResponseEntity<CompetitionParticipantDTO> response = restTemplate.patchForObject(
                    externalBase + "/progress", updateDTO, ResponseEntity.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== Leaderboard ====================

    @GetMapping("/{id}/leaderboard")
    public List<CompetitionParticipantDTO> getLeaderboard(@PathVariable Long id) {
        try {
            ResponseEntity<CompetitionParticipantDTO[]> response =
                    restTemplate.getForEntity(externalBase + "/" + id + "/leaderboard", CompetitionParticipantDTO[].class);
            CompetitionParticipantDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new CompetitionParticipantDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    // ==================== Legacy Endpoints ====================

    @GetMapping("/user/{id}")
    public List<CompetitionDTO> getAllCompetitionsByUserId(@PathVariable Long id) {
        try {
            ResponseEntity<CompetitionDTO[]> response =
                    restTemplate.getForEntity(externalBase + "/user/" + id, CompetitionDTO[].class);
            CompetitionDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new CompetitionDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }
}
