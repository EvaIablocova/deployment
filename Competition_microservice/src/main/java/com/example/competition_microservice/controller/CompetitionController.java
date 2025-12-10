package com.example.competition_microservice.controller;

import com.example.competition_microservice.DTOs.CompetitionDTO;
import com.example.competition_microservice.service.CompetitionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/competitions")
public class CompetitionController {

    private final CompetitionService competitionService;

    public CompetitionController(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    @GetMapping
    public List<CompetitionDTO> getAllCompetitions() {
        List<CompetitionDTO> competitions = competitionService.getAllCompetitions();
        return competitions;
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
        return competitionService.updateCompetition(id, updatedCompetitionDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompetition(@PathVariable Long id) {
        return competitionService.deleteCompetition(id);
    }


    @GetMapping("/user/{id}")
    public List<CompetitionDTO> getAllCompetitionsByUserId(@PathVariable Long id) {
        List<CompetitionDTO> compet = competitionService.getAllCompetitionsByUserId(id);
        return competitionService.getAllCompetitionsByUserId(id);
    }

}
