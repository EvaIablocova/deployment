package com.example.reporting_microservice.controller;

import com.example.reporting_microservice.DTOs.CompetitionDTO;
import com.example.reporting_microservice.DTOs.ReportDTO;
import com.example.reporting_microservice.DTOs.TaskDTO;
import com.example.reporting_microservice.DTOs.UserDTO;
import com.example.reporting_microservice.service.ReportingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ReportingController.class)
class ReportingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportingService reportingService;

    @Autowired
    private ObjectMapper objectMapper;

    private ReportDTO sampleReport() {
        ReportDTO r = new ReportDTO();
        r.setUserId(1L);
        r.setUsername("alice");
        CompetitionDTO c = new CompetitionDTO();
        c.setGoal("win");
        c.setType("weekly");
        r.setCompetitions(List.of(c));
        TaskDTO t = new TaskDTO();
        t.setTitle("t1");
        t.setDescription("desc1");
        r.setTasks(List.of(t));
        return r;
    }

    private UserDTO sampleUser() {
        UserDTO u = new UserDTO();
        u.setId(1L);
        u.setUsername("alice");
        return u;
    }

    private TaskDTO sampleTask() {
        TaskDTO t = new TaskDTO();
        t.setId(10L);
        t.setTitle("clean");
        t.setDescription("clean kitchen");
        return t;
    }

    private CompetitionDTO sampleCompetition() {
        CompetitionDTO c = new CompetitionDTO();
        c.setGoal("save");
        c.setType("monthly");
        return c;
    }

    @Test
    void getAllReportsByUsers_returnsOkAndReportList() throws Exception {
        ReportDTO r = sampleReport();
        when(reportingService.getAllReportsByUsers()).thenReturn(List.of(r));

        mockMvc.perform(get("/api/reports/reports/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[0].username", is("alice")))
                .andExpect(jsonPath("$[0].competitions[0].goal", is("win")));
    }

    @Test
    void getAllUsers_returnsOkAndUserList() throws Exception {
        UserDTO u = sampleUser();
        when(reportingService.getAllUsers()).thenReturn(List.of(u));

        mockMvc.perform(get("/api/reports/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].username", is("alice")));
    }

    @Test
    void getUserById_foundReturnsOk() throws Exception {
        UserDTO u = sampleUser();
        when(reportingService.getUserById(1L)).thenReturn(Optional.of(u));

        mockMvc.perform(get("/api/reports/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("alice")));
    }

    @Test
    void getUserById_notFoundReturns404() throws Exception {
        when(reportingService.getUserById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reports/users/99").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllTasks_returnsOkAndTaskList() throws Exception {
        TaskDTO t = sampleTask();
        when(reportingService.getAllTasks()).thenReturn(List.of(t));

        mockMvc.perform(get("/api/reports/tasks").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(10)))
                .andExpect(jsonPath("$[0].title", is("clean")));
    }

    @Test
    void getAllCompetitions_returnsOkAndCompetitionList() throws Exception {
        CompetitionDTO c = sampleCompetition();
        when(reportingService.getAllCompetitions()).thenReturn(List.of(c));

        mockMvc.perform(get("/api/reports/competitions").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].goal", is("save")))
                .andExpect(jsonPath("$[0].type", is("monthly")));
    }
}