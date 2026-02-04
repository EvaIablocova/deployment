package com.example.project_microservice.service;

import com.example.project_microservice.DTOs.ProjectDTO;
import com.example.project_microservice.repository.ProjectRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void getAllProjects_returnsRepositoryResult() {
        ProjectDTO dto1 = mock(ProjectDTO.class);
        ProjectDTO dto2 = mock(ProjectDTO.class);
        List<ProjectDTO> expected = Arrays.asList(dto1, dto2);

        when(projectRepository.getAllProjects()).thenReturn(expected);

        List<ProjectDTO> result = projectService.getAllProjects();

        assertNotNull(result);
        assertEquals(expected, result);
        verify(projectRepository, times(1)).getAllProjects();
    }

    @Test
    void getProjectById_whenRepositoryReturnsResponseWithBody_returnsOptionalWithBody() {
        Long id = 1L;
        ProjectDTO dto = mock(ProjectDTO.class);
        when(projectRepository.getProjectById(id)).thenReturn(ResponseEntity.ok(dto));

        Optional<ProjectDTO> result = projectService.getProjectById(id);

        assertTrue(result.isPresent());
        assertEquals(dto, result.get());
        verify(projectRepository, times(1)).getProjectById(id);
    }

    @Test
    void getProjectById_whenRepositoryReturnsNullResponse_returnsEmptyOptional() {
        Long id = 2L;
        when(projectRepository.getProjectById(id)).thenReturn(null);

        Optional<ProjectDTO> result = projectService.getProjectById(id);

        assertFalse(result.isPresent());
        verify(projectRepository, times(1)).getProjectById(id);
    }

    @Test
    void getProjectById_whenRepositoryReturnsResponseWithNullBody_returnsEmptyOptional() {
        Long id = 3L;
        when(projectRepository.getProjectById(id)).thenReturn(ResponseEntity.ok(null));

        Optional<ProjectDTO> result = projectService.getProjectById(id);

        assertFalse(result.isPresent());
        verify(projectRepository, times(1)).getProjectById(id);
    }

    @Test
    void createProject_returnsBodyFromRepositoryResponse() {
        ProjectDTO input = mock(ProjectDTO.class);
        ProjectDTO created = mock(ProjectDTO.class);

        when(projectRepository.createProject(input)).thenReturn(ResponseEntity.ok(created));

        ProjectDTO result = projectService.createProject(input);

        assertNotNull(result);
        assertEquals(created, result);
        verify(projectRepository, times(1)).createProject(input);
    }

    @Test
    void createProject_whenRepositoryReturnsResponseWithNullBody_returnsNull() {
        ProjectDTO input = mock(ProjectDTO.class);
        when(projectRepository.createProject(input)).thenReturn(ResponseEntity.ok(null));

        ProjectDTO result = projectService.createProject(input);

        assertNull(result);
        verify(projectRepository, times(1)).createProject(input);
    }

    @Test
    void updateProject_delegatesToRepository_andReturnsResponse() {
        Long id = 4L;
        ProjectDTO updated = mock(ProjectDTO.class);
        ResponseEntity<ProjectDTO> repoResponse = ResponseEntity.ok(updated);

        when(projectRepository.updateProject(id, updated)).thenReturn(repoResponse);

        ResponseEntity<ProjectDTO> result = projectService.updateProject(id, updated);

        assertEquals(repoResponse, result);
        verify(projectRepository, times(1)).updateProject(id, updated);
    }

    @Test
    void deleteProject_delegatesToRepository_andReturnsResponse() {
        Long id = 5L;
        ResponseEntity<Void> repoResponse = ResponseEntity.noContent().build();

        when(projectRepository.deleteProject(id)).thenReturn(repoResponse);

        ResponseEntity<Void> result = projectService.deleteProject(id);

        assertEquals(repoResponse, result);
        verify(projectRepository, times(1)).deleteProject(id);
    }
}
