package com.example.database_microservice.controller;

import com.example.database_microservice.DTOs.CompetitionDTO;
import com.example.database_microservice.service.CompetitionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/db/competitions")
public class CompetitionController {
    private final CompetitionService competitionService;

    public CompetitionController(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    @GetMapping
    public List<CompetitionDTO> getAllCompetitions() {
        List<CompetitionDTO> tasks = competitionService.getAllCompetitions();
        return competitionService.getAllCompetitions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetitionDTO> getCompetitionById(@PathVariable Long id) {
        return competitionService.getCompetitionById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public CompetitionDTO createCompetition(@RequestBody CompetitionDTO CompetitionDTO) {
        return competitionService.createCompetition(CompetitionDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetitionDTO> updateCompetition(@PathVariable Long id, @RequestBody CompetitionDTO updatedCompetitionDTO) {
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

}
