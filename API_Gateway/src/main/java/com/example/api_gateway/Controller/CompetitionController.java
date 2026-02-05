package com.example.api_gateway.Controller;

import com.example.api_gateway.DTOs.CompetitionDTO;
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

    @PostMapping
    public ResponseEntity<CompetitionDTO> createCompetition(@RequestBody CompetitionDTO CompetitionDTO) {
        try {
            ResponseEntity<CompetitionDTO> response =
                    restTemplate.postForEntity(externalBase, CompetitionDTO, CompetitionDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetitionDTO> updateCompetition(@PathVariable Long id, @RequestBody CompetitionDTO updatedCompetitionDTO) {
        try {
            restTemplate.put(externalBase + "/" + id, updatedCompetitionDTO);
            return ResponseEntity.ok(updatedCompetitionDTO);
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




}
