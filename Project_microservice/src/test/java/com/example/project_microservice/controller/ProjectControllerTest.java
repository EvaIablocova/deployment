package com.example.project_microservice.controller;

import com.example.project_microservice.DTOs.ProjectDTO;
import com.example.project_microservice.service.ProjectService;
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
class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    @Test
    void getAllProjects_returnsList() {
        ProjectDTO dto1 = mock(ProjectDTO.class);
        ProjectDTO dto2 = mock(ProjectDTO.class);
        List<ProjectDTO> expected = Arrays.asList(dto1, dto2);

        when(projectService.getAllProjects()).thenReturn(expected);

        List<ProjectDTO> result = projectController.getAllProjects();

        assertNotNull(result);
        assertEquals(expected, result);
        verify(projectService, times(1)).getAllProjects();
    }

    @Test
    void getProjectById_found_returnsOkWithBody() {
        Long id = 1L;
        ProjectDTO dto = mock(ProjectDTO.class);

        when(projectService.getProjectById(id)).thenReturn(Optional.of(dto));

        ResponseEntity<ProjectDTO> response = projectController.getProjectById(id);

        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(dto, response.getBody());
        verify(projectService, times(1)).getProjectById(id);
    }

    @Test
    void getProjectById_notFound_returnsNotFound() {
        Long id = 2L;

        when(projectService.getProjectById(id)).thenReturn(Optional.empty());

        ResponseEntity<ProjectDTO> response = projectController.getProjectById(id);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(projectService, times(1)).getProjectById(id);
    }

    @Test
    void createProject_returnsCreatedDto() {
        ProjectDTO input = mock(ProjectDTO.class);
        ProjectDTO created = mock(ProjectDTO.class);

        when(projectService.createProject(input)).thenReturn(created);

        ProjectDTO result = projectController.createProject(input);

        assertNotNull(result);
        assertEquals(created, result);
        verify(projectService, times(1)).createProject(input);
    }

    @Test
    void updateProject_delegatesToService_andReturnsServiceResponse() {
        Long id = 3L;
        ProjectDTO updated = mock(ProjectDTO.class);
        ResponseEntity<ProjectDTO> serviceResponse = ResponseEntity.ok(updated);

        when(projectService.updateProject(id, updated)).thenReturn(serviceResponse);

        ResponseEntity<ProjectDTO> response = projectController.updateProject(id, updated);

        assertNotNull(response);
        assertEquals(serviceResponse, response);
        verify(projectService, times(1)).updateProject(id, updated);
    }

    @Test
    void deleteProject_delegatesToService_andReturnsServiceResponse() {
        Long id = 4L;
        ResponseEntity<Void> serviceResponse = ResponseEntity.noContent().build();

        when(projectService.deleteProject(id)).thenReturn(serviceResponse);

        ResponseEntity<Void> response = projectController.deleteProject(id);

        assertNotNull(response);
        assertEquals(serviceResponse, response);
        verify(projectService, times(1)).deleteProject(id);
    }
}
