package com.example.database_microservice.service;

import com.example.database_microservice.DTOs.CompetitionDTO;
import com.example.database_microservice.model.Competition;
import com.example.database_microservice.repository.CompetitionRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service

public class CompetitionService {

    private final CompetitionRepository competitionRepository;

    public CompetitionService(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
    }

    // --- Mapping ---
    private CompetitionDTO toDTO(Competition competition) {
        return new CompetitionDTO(competition);
    }

    public List<CompetitionDTO> getAllCompetitions() {
        return competitionRepository.findAll().stream().map(this::toDTO).toList();
    }

    public Optional<CompetitionDTO> getCompetitionById(Long id) {
        return competitionRepository.findById(id).map(this::toDTO);
    }

    public CompetitionDTO createCompetition(CompetitionDTO CompetitionDTO) {
        Competition Competition = new Competition(CompetitionDTO);
        return toDTO(competitionRepository.save(Competition));
    }

    public Optional<CompetitionDTO> updateCompetition(Long id, CompetitionDTO updatedCompetitionDTO) {
        Competition updatedCompetition = new Competition(updatedCompetitionDTO);
        return competitionRepository.findById(id).map(competition -> {
            competition.setGoal(updatedCompetition.getGoal());
            competition.setType(updatedCompetition.getType());
//            competition.setStartDate(updatedCompetition.getStartDate());
//            competition.setEndDate(updatedCompetition.getEndDate());
//            competition.setStatus(updatedCompetition.getStatus());
            return toDTO(competitionRepository.save(competition));
        });
    }

    public boolean deleteCompetition(Long id) {
        if (competitionRepository.existsById(id)) {
            competitionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
