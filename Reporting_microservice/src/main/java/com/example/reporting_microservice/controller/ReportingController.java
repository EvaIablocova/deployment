package com.example.reporting_microservice.controller;

import com.example.reporting_microservice.DTOs.CompetitionDTO;
import com.example.reporting_microservice.DTOs.ReportDTO;
import com.example.reporting_microservice.DTOs.TaskDTO;
import com.example.reporting_microservice.DTOs.UserDTO;
import com.example.reporting_microservice.service.ReportingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/reports/users")
    public List<ReportDTO> getAllReportsByUsers() {
        List<ReportDTO> reports = reportingService.getAllReportsByUsers();
        return reportingService.getAllReportsByUsers();
    }

//    @GetMapping("/users/{id}")
//    public ResponseEntity<ReportDTO> getReportByUserId(@PathVariable Long id) {
//        return reportingService.getReportByUserId(id)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = reportingService.getAllUsers();
        return reportingService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return reportingService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/tasks")
    public List<TaskDTO> getAllTasks() {
        List<TaskDTO> tasks = reportingService.getAllTasks();
        return reportingService.getAllTasks();
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        return reportingService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/competitions")
    public List<CompetitionDTO> getAllCompetitions() {
        List<CompetitionDTO> competitions = reportingService.getAllCompetitions();
        return competitions;
    }

    @GetMapping("/competitions/{id}")
    public ResponseEntity<CompetitionDTO> getCompetitionById(@PathVariable Long id) {
        return reportingService.getCompetitionById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}