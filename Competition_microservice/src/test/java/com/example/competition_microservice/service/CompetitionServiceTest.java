package com.example.competition_microservice.service;

import com.example.competition_microservice.DTOs.CompetitionDTO;
import com.example.competition_microservice.repository.CompetitionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompetitionServiceTest {

    @Mock
    private CompetitionRepository competitionRepository;

    @InjectMocks
    private CompetitionService competitionService;

    @Test
    void getAllCompetitions_delegatesToRepository() {
        CompetitionDTO a = mock(CompetitionDTO.class);
        CompetitionDTO b = mock(CompetitionDTO.class);
        List<CompetitionDTO> list = Arrays.asList(a, b);

        when(competitionRepository.getAllCompetitions()).thenReturn(list);

        List<CompetitionDTO> result = competitionService.getAllCompetitions();

        assertSame(list, result);
        verify(competitionRepository, times(1)).getAllCompetitions();
    }

    @Test
    void getCompetitionById_whenFound_returnsOptionalWithBody() {
        Long id = 1L;
        CompetitionDTO dto = mock(CompetitionDTO.class);
        ResponseEntity<CompetitionDTO> resp = ResponseEntity.ok(dto);

        when(competitionRepository.getCompetitionById(id)).thenReturn(resp);

        Optional<CompetitionDTO> result = competitionService.getCompetitionById(id);

        assertTrue(result.isPresent());
        assertSame(dto, result.get());
        verify(competitionRepository, times(1)).getCompetitionById(id);
    }

    @Test
    void getCompetitionById_whenNotFound_returnsEmptyOptional() {
        Long id = 2L;
        ResponseEntity<CompetitionDTO> resp = ResponseEntity.notFound().build();

        when(competitionRepository.getCompetitionById(id)).thenReturn(resp);

        Optional<CompetitionDTO> result = competitionService.getCompetitionById(id);

        assertFalse(result.isPresent());
        verify(competitionRepository, times(1)).getCompetitionById(id);
    }

    @Test
    void createCompetition_returnsBodyFromRepository() {
        CompetitionDTO input = mock(CompetitionDTO.class);
        CompetitionDTO created = mock(CompetitionDTO.class);
        ResponseEntity<CompetitionDTO> repoResp = ResponseEntity.ok(created);

        when(competitionRepository.createCompetition(input)).thenReturn(repoResp);

        CompetitionDTO result = competitionService.createCompetition(input);

        assertSame(created, result);
        verify(competitionRepository, times(1)).createCompetition(input);
    }

    @Test
    void updateCompetition_delegatesAndReturnsRepositoryResponse() {
        Long id = 3L;
        CompetitionDTO update = mock(CompetitionDTO.class);
        CompetitionDTO updated = mock(CompetitionDTO.class);
        ResponseEntity<CompetitionDTO> repoResp = ResponseEntity.ok(updated);

        when(competitionRepository.updateCompetition(id, update)).thenReturn(repoResp);

        ResponseEntity<CompetitionDTO> result = competitionService.updateCompetition(id, update);

        assertSame(repoResp, result);
        verify(competitionRepository, times(1)).updateCompetition(id, update);
    }

    @Test
    void deleteCompetition_delegatesAndReturnsRepositoryResponse() {
        Long id = 4L;
        ResponseEntity<Void> repoResp = ResponseEntity.noContent().build();

        when(competitionRepository.deleteCompetition(id)).thenReturn(repoResp);

        ResponseEntity<Void> result = competitionService.deleteCompetition(id);

        assertSame(repoResp, result);
        verify(competitionRepository, times(1)).deleteCompetition(id);
    }

    @Test
    void getAllCompetitionsByUserId_filtersCompetitionsByUserId() {
        Long userId = 10L;

        CompetitionDTO match = mock(CompetitionDTO.class);
        when(match.getUserId()).thenReturn(Collections.singletonList(userId));

        CompetitionDTO noMatch = mock(CompetitionDTO.class);
        when(noMatch.getUserId()).thenReturn(Collections.singletonList(999L));

        List<CompetitionDTO> repoList = Arrays.asList(match, noMatch);
        when(competitionRepository.getAllCompetitions()).thenReturn(repoList);

        List<CompetitionDTO> result = competitionService.getAllCompetitionsByUserId(userId);

        assertEquals(1, result.size());
        assertSame(match, result.get(0));
        verify(competitionRepository, times(1)).getAllCompetitions();
    }
}