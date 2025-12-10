package com.example.reporting_microservice.service;

import com.example.reporting_microservice.DTOs.CompetitionDTO;
import com.example.reporting_microservice.DTOs.ReportDTO;
import com.example.reporting_microservice.DTOs.TaskDTO;
import com.example.reporting_microservice.DTOs.UserDTO;
import com.example.reporting_microservice.repository.ReportingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportingServiceTest {

    @Mock
    private ReportingRepository reportingRepository;

    @InjectMocks
    private ReportingService reportingService;

    private UserDTO sampleUser(long id, String username) {
        UserDTO u = new UserDTO();
        u.setId(id);
        u.setUsername(username);
        return u;
    }

    private TaskDTO sampleTask(long id, String title, String description) {
        TaskDTO t = new TaskDTO();
        t.setId(id);
        t.setTitle(title);
        t.setDescription(description);
        return t;
    }

    private CompetitionDTO sampleCompetition(String goal, String type) {
        CompetitionDTO c = new CompetitionDTO();
        c.setGoal(goal);
        c.setType(type);
        return c;
    }

    @Test
    void getAllReportsByUsers_buildsReportsFromUsersTasksAndCompetitions() {
        UserDTO u = sampleUser(1L, "alice");
        TaskDTO t = sampleTask(10L, "clean", "clean kitchen");
        CompetitionDTO c = sampleCompetition("win", "weekly");

        when(reportingRepository.getAllUsers()).thenReturn(List.of(u));
        when(reportingRepository.getTasksByUserId(1L)).thenReturn(List.of(t));
        when(reportingRepository.getCompetitionsByUserId(1L)).thenReturn(List.of(c));

        List<ReportDTO> reports = reportingService.getAllReportsByUsers();

        assertNotNull(reports);
        assertEquals(1, reports.size());

        ReportDTO report = reports.get(0);
        assertEquals(1L, report.getUserId());
        assertEquals("alice", report.getUsername());
        assertNotNull(report.getTasks());
        assertEquals(1, report.getTasks().size());
        assertEquals("clean", report.getTasks().get(0).getTitle());
        assertNotNull(report.getCompetitions());
        assertEquals(1, report.getCompetitions().size());
        assertEquals("win", report.getCompetitions().get(0).getGoal());
    }

    @Test
    void getAllUsers_returnsAllUsersFromRepository() {
        UserDTO u1 = sampleUser(1L, "alice");
        UserDTO u2 = sampleUser(2L, "bob");

        when(reportingRepository.getAllUsers()).thenReturn(List.of(u1, u2));

        List<UserDTO> users = reportingService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("alice", users.get(0).getUsername());
        assertEquals("bob", users.get(1).getUsername());
    }

    @Test
    void getUserById_returnsOptionalUserWhenPresent_andEmptyWhenMissing() {
        UserDTO u = sampleUser(1L, "alice");
        when(reportingRepository.getUserById(1L)).thenReturn(ResponseEntity.ok(u));
        when(reportingRepository.getUserById(99L)).thenReturn(null);

        Optional<UserDTO> found = reportingService.getUserById(1L);
        Optional<UserDTO> missing = reportingService.getUserById(99L);

        assertTrue(found.isPresent());
        assertEquals("alice", found.get().getUsername());
        assertFalse(missing.isPresent());
    }

    @Test
    void getAllTasks_and_getTaskById() {
        TaskDTO t = sampleTask(10L, "clean", "clean kitchen");
        when(reportingRepository.getAllTasks()).thenReturn(List.of(t));
        when(reportingRepository.getTaskById(10L)).thenReturn(ResponseEntity.ok(t));
        when(reportingRepository.getTaskById(99L)).thenReturn(null);

        List<TaskDTO> tasks = reportingService.getAllTasks();
        assertEquals(1, tasks.size());
        assertEquals("clean", tasks.get(0).getTitle());

        Optional<TaskDTO> found = reportingService.getTaskById(10L);
        Optional<TaskDTO> missing = reportingService.getTaskById(99L);
        assertTrue(found.isPresent());
        assertEquals(10L, found.get().getId());
        assertFalse(missing.isPresent());
    }

    @Test
    void getAllCompetitions_and_getCompetitionById() {
        CompetitionDTO c = sampleCompetition("save", "monthly");
        when(reportingRepository.getAllCompetitions()).thenReturn(List.of(c));
        when(reportingRepository.getCompetitionById(1L)).thenReturn(ResponseEntity.ok(c));
        when(reportingRepository.getCompetitionById(99L)).thenReturn(null);

        List<CompetitionDTO> competitions = reportingService.getAllCompetitions();
        assertEquals(1, competitions.size());
        assertEquals("save", competitions.get(0).getGoal());

        Optional<CompetitionDTO> found = reportingService.getCompetitionById(1L);
        Optional<CompetitionDTO> missing = reportingService.getCompetitionById(99L);
        assertTrue(found.isPresent());
        assertEquals("monthly", found.get().getType());
        assertFalse(missing.isPresent());
    }
}