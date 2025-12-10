package com.example.reporting_microservice.service;

import com.example.reporting_microservice.DTOs.CompetitionDTO;
import com.example.reporting_microservice.DTOs.ReportDTO;
import com.example.reporting_microservice.DTOs.TaskDTO;
import com.example.reporting_microservice.DTOs.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.reporting_microservice.repository.ReportingRepository;

@Service
public class ReportingService {

    private final ReportingRepository reportingRepository;

    public ReportingService(ReportingRepository reportingRepository) {

        this.reportingRepository = reportingRepository;
    }

    public List<ReportDTO> getAllReportsByUsers() {
        List<ReportDTO> reports = new ArrayList<>();

        List<UserDTO> users = reportingRepository.getAllUsers();
//        List<TaskDTO> tasks = reportingRepository.getAllTasks();
//        List<CompetitionDTO> competitions = reportingRepository.getAllCompetitions();

        for (UserDTO user : users) {
            List<TaskDTO> tasksByUserId = reportingRepository.getTasksByUserId(user.getId());
            List<CompetitionDTO> competitionsByUserId = reportingRepository.getCompetitionsByUserId(user.getId());

            ReportDTO reportDTO = new ReportDTO(user.getId(), user.getUsername(), tasksByUserId, competitionsByUserId);
            reports.add(reportDTO);
        }


        return reports;
    }

//    public Optional<ReportDTO> getReportByUserId(Long id) {
//        return Optional.ofNullable(reportingRepository.getReportByUserId(id))
//                .map(response -> response.getBody());
//    }

    public List<UserDTO> getAllUsers() {
        return reportingRepository.getAllUsers();
    }

    public Optional<UserDTO> getUserById(Long id) {
        return Optional.ofNullable(reportingRepository.getUserById(id))
                .map(response -> response.getBody());
    }

    public List<TaskDTO> getAllTasks() {
        return reportingRepository.getAllTasks();
    }

    public Optional<TaskDTO> getTaskById(Long id) {
        return Optional.ofNullable(reportingRepository.getTaskById(id))
                .map(response -> response.getBody());
    }

    public List<CompetitionDTO> getAllCompetitions() {
        return reportingRepository.getAllCompetitions();
    }

    public Optional<CompetitionDTO> getCompetitionById(Long id) {
        return Optional.ofNullable(reportingRepository.getCompetitionById(id))
                .map(response -> response.getBody());
    }




}