package com.example.competition_microservice.controller;

import com.example.competition_microservice.DTOs.CompetitionDTO;
import com.example.competition_microservice.service.CompetitionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompetitionControllerTest {

    @Mock
    private CompetitionService competitionService;

    @InjectMocks
    private CompetitionController controller;

    private CompetitionDTO sampleDto(Long id, String name) {
        CompetitionDTO dto = new CompetitionDTO();
        try {
            // try common setters if available
            dto.getClass().getMethod("setId", Long.class).invoke(dto, id);
            dto.getClass().getMethod("setName", String.class).invoke(dto, name);
        } catch (Exception ignored) {
            // ignore if setters are absent; tests will still use object identity
        }
        return dto;
    }

    @Test
    void getAllCompetitions_returnsListFromService() {
        CompetitionDTO a = sampleDto(1L, "A");
        CompetitionDTO b = sampleDto(2L, "B");
        List<CompetitionDTO> list = Arrays.asList(a, b);

        when(competitionService.getAllCompetitions()).thenReturn(list);

        List<CompetitionDTO> result = controller.getAllCompetitions();

        assertSame(list, result);
        verify(competitionService).getAllCompetitions();
    }

    @Test
    void getCompetitionById_found_returnsOk() {
        CompetitionDTO dto = sampleDto(1L, "Test");
        when(competitionService.getCompetitionById(1L)).thenReturn(Optional.of(dto));

        ResponseEntity<CompetitionDTO> resp = controller.getCompetitionById(1L);

        assertEquals(200, resp.getStatusCodeValue());
        assertSame(dto, resp.getBody());
        verify(competitionService).getCompetitionById(1L);
    }

    @Test
    void getCompetitionById_notFound_returnsNotFound() {
        when(competitionService.getCompetitionById(99L)).thenReturn(Optional.empty());

        ResponseEntity<CompetitionDTO> resp = controller.getCompetitionById(99L);

        assertEquals(404, resp.getStatusCodeValue());
        assertNull(resp.getBody());
        verify(competitionService).getCompetitionById(99L);
    }

    @Test
    void createCompetition_delegatesToService_returnsCreatedDto() {
        CompetitionDTO input = sampleDto(null, "New");
        CompetitionDTO created = sampleDto(5L, "New");
        when(competitionService.createCompetition(input)).thenReturn(created);

        CompetitionDTO resp = controller.createCompetition(input);

        assertSame(created, resp);
        verify(competitionService).createCompetition(input);
    }

    @Test
    void updateCompetition_delegatesToService_returnsServiceResponse() {
        CompetitionDTO update = sampleDto(null, "Updated");
        CompetitionDTO updated = sampleDto(2L, "Updated");

        ResponseEntity<CompetitionDTO> serviceResp = ResponseEntity.ok(updated);
        when(competitionService.updateCompetition(2L, update)).thenReturn(serviceResp);

        ResponseEntity<CompetitionDTO> resp = controller.updateCompetition(2L, update);

        assertEquals(200, resp.getStatusCodeValue());
        assertSame(updated, resp.getBody());
        verify(competitionService).updateCompetition(2L, update);
    }

    @Test
    void deleteCompetition_delegatesToService_returnsServiceResponse() {
        ResponseEntity<Void> serviceResp = ResponseEntity.noContent().build();
        when(competitionService.deleteCompetition(3L)).thenReturn(serviceResp);

        ResponseEntity<Void> resp = controller.deleteCompetition(3L);

        assertEquals(204, resp.getStatusCodeValue());
        verify(competitionService).deleteCompetition(3L);
    }

}