package com.example.competition_microservice.service;

import com.example.competition_microservice.DTOs.CompetitionDTO;
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

    public List<CompetitionDTO> getAllCompetitions() {
        return competitionRepository.getAllCompetitions();
    }

   public Optional<CompetitionDTO> getCompetitionById(Long id) {
       return Optional.ofNullable(competitionRepository.getCompetitionById(id))
                      .map(response -> response.getBody());
   }

    public CompetitionDTO createCompetition(CompetitionDTO CompetitionDTO) {
        return competitionRepository.createCompetition(CompetitionDTO).getBody();
    }

    public ResponseEntity<CompetitionDTO> updateCompetition(Long id, CompetitionDTO updatedCompetitionDTO) {
            return competitionRepository.updateCompetition(id, updatedCompetitionDTO);
    }

    public ResponseEntity<Void> deleteCompetition(Long id) {
        return competitionRepository.deleteCompetition(id);
    }

    public List<CompetitionDTO> getAllCompetitionsByUserId(Long id) {
        List<CompetitionDTO> competitions = competitionRepository.getAllCompetitions();
        return competitions.stream()
                .filter(competition -> competition.getUserId().contains(id))
                .toList();

    }

}
