package com.example.database_microservice.service;

import com.example.database_microservice.DTOs.CompetitionDTO;
import com.example.database_microservice.DTOs.CompetitionParticipantDTO;
import com.example.database_microservice.DTOs.UpdateProgressDTO;
import com.example.database_microservice.model.Competition;
import com.example.database_microservice.model.CompetitionParticipant;
import com.example.database_microservice.model.CompetitionStatus;
import com.example.database_microservice.repository.CompetitionParticipantRepository;
import com.example.database_microservice.repository.CompetitionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final CompetitionParticipantRepository participantRepository;

    public CompetitionService(CompetitionRepository competitionRepository,
                              CompetitionParticipantRepository participantRepository) {
        this.competitionRepository = competitionRepository;
        this.participantRepository = participantRepository;
    }

    private CompetitionDTO toDTO(Competition competition) {
        return new CompetitionDTO(competition);
    }

    // ==================== Basic CRUD ====================

    public List<CompetitionDTO> getAllCompetitions() {
        return competitionRepository.findAll().stream().map(this::toDTO).toList();
    }

    public Optional<CompetitionDTO> getCompetitionById(Long id) {
        return competitionRepository.findById(id).map(this::toDTO);
    }

    public List<CompetitionDTO> getCompetitionsByGroupId(Long groupId) {
        return competitionRepository.findByGroupIdOrderByUpdatedAtDesc(groupId)
                .stream().map(this::toDTO).toList();
    }

    public List<CompetitionDTO> getCompetitionsByStatus(CompetitionStatus status) {
        return competitionRepository.findByStatus(status)
                .stream().map(this::toDTO).toList();
    }

    public List<CompetitionDTO> getCompetitionsByGroupIdAndStatus(Long groupId, CompetitionStatus status) {
        return competitionRepository.findByGroupIdAndStatus(groupId, status)
                .stream().map(this::toDTO).toList();
    }

    public CompetitionDTO createCompetition(CompetitionDTO competitionDTO) {
        Competition competition = new Competition(competitionDTO);
        return toDTO(competitionRepository.save(competition));
    }

    public Optional<CompetitionDTO> updateCompetition(Long id, CompetitionDTO updatedCompetitionDTO) {
        return competitionRepository.findById(id).map(competition -> {
            if (updatedCompetitionDTO.getName() != null) {
                competition.setName(updatedCompetitionDTO.getName());
            }
            if (updatedCompetitionDTO.getDescription() != null) {
                competition.setDescription(updatedCompetitionDTO.getDescription());
            }
            if (updatedCompetitionDTO.getCompetitionType() != null) {
                competition.setCompetitionType(updatedCompetitionDTO.getCompetitionType());
            }
            if (updatedCompetitionDTO.getTargetValue() != null) {
                competition.setTargetValue(updatedCompetitionDTO.getTargetValue());
            }
            if (updatedCompetitionDTO.getStartDate() != null) {
                competition.setStartDate(updatedCompetitionDTO.getStartDate());
            }
            if (updatedCompetitionDTO.getEndDate() != null) {
                competition.setEndDate(updatedCompetitionDTO.getEndDate());
            }
            // Legacy fields
            if (updatedCompetitionDTO.getGoal() != null) {
                competition.setGoal(updatedCompetitionDTO.getGoal());
            }
            if (updatedCompetitionDTO.getType() != null) {
                competition.setType(updatedCompetitionDTO.getType());
            }
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

    // ==================== Status Management ====================

    @Transactional
    public Optional<CompetitionDTO> startCompetition(Long id) {
        return competitionRepository.findById(id).map(competition -> {
            if (competition.getStatus() == CompetitionStatus.PENDING) {
                competition.setStatus(CompetitionStatus.ACTIVE);
                if (competition.getStartDate() == null) {
                    competition.setStartDate(LocalDate.now());
                }
                return toDTO(competitionRepository.save(competition));
            }
            return toDTO(competition);
        });
    }

    @Transactional
    public Optional<CompetitionDTO> completeCompetition(Long id) {
        return competitionRepository.findById(id).map(competition -> {
            if (competition.getStatus() == CompetitionStatus.ACTIVE) {
                competition.setStatus(CompetitionStatus.COMPLETED);
                if (competition.getEndDate() == null) {
                    competition.setEndDate(LocalDate.now());
                }
                // Determine winners
                updateRankings(competition);
                markWinners(competition);
                return toDTO(competitionRepository.save(competition));
            }
            return toDTO(competition);
        });
    }

    @Transactional
    public Optional<CompetitionDTO> cancelCompetition(Long id) {
        return competitionRepository.findById(id).map(competition -> {
            if (competition.getStatus() != CompetitionStatus.COMPLETED) {
                competition.setStatus(CompetitionStatus.CANCELLED);
                return toDTO(competitionRepository.save(competition));
            }
            return toDTO(competition);
        });
    }

    // ==================== Participant Management ====================

    @Transactional
    public Optional<CompetitionDTO> joinCompetition(Long competitionId, Long userId, String username) {
        return competitionRepository.findById(competitionId).map(competition -> {
            // Check if already a participant
            if (participantRepository.existsByCompetitionIdAndUserId(competitionId, userId)) {
                return toDTO(competition);
            }

            CompetitionParticipant participant = new CompetitionParticipant(userId, username);
            competition.addParticipant(participant);
            return toDTO(competitionRepository.save(competition));
        });
    }

    @Transactional
    public Optional<CompetitionDTO> leaveCompetition(Long competitionId, Long userId) {
        return competitionRepository.findById(competitionId).map(competition -> {
            competition.getParticipants().removeIf(p -> p.getUserId().equals(userId));
            return toDTO(competitionRepository.save(competition));
        });
    }

    // ==================== Progress Management ====================

    @Transactional
    public Optional<CompetitionParticipantDTO> updateProgress(UpdateProgressDTO updateDTO) {
        return participantRepository.findByCompetitionIdAndUserId(
                updateDTO.getCompetitionId(), updateDTO.getUserId()
        ).map(participant -> {
            if (updateDTO.isIncrement()) {
                participant.setCurrentValue(participant.getCurrentValue() + updateDTO.getValue());
            } else {
                participant.setCurrentValue(updateDTO.getValue());
            }

            // Update rankings for the competition
            Competition competition = participant.getCompetition();
            updateRankings(competition);

            return new CompetitionParticipantDTO(participantRepository.save(participant));
        });
    }

    // ==================== Leaderboard ====================

    public List<CompetitionParticipantDTO> getLeaderboard(Long competitionId) {
        List<CompetitionParticipant> participants =
                participantRepository.findByCompetitionIdOrderByCurrentValueDesc(competitionId);

        // Set ranks
        for (int i = 0; i < participants.size(); i++) {
            participants.get(i).setRank(i + 1);
        }

        return participants.stream()
                .map(CompetitionParticipantDTO::new)
                .collect(Collectors.toList());
    }

    // ==================== Helper Methods ====================

    private void updateRankings(Competition competition) {
        List<CompetitionParticipant> participants = competition.getParticipants();
        // Sort by currentValue descending
        participants.sort((a, b) -> Double.compare(b.getCurrentValue(), a.getCurrentValue()));

        for (int i = 0; i < participants.size(); i++) {
            participants.get(i).setRank(i + 1);
        }
    }

    private void markWinners(Competition competition) {
        if (competition.getParticipants().isEmpty()) {
            return;
        }

        // Reset all winners
        competition.getParticipants().forEach(p -> p.setWinner(false));

        // Find the highest score
        double maxValue = competition.getParticipants().stream()
                .mapToDouble(CompetitionParticipant::getCurrentValue)
                .max()
                .orElse(0.0);

        // Mark all participants with max value as winners (handle ties)
        competition.getParticipants().stream()
                .filter(p -> p.getCurrentValue() == maxValue)
                .forEach(p -> p.setWinner(true));
    }
}
