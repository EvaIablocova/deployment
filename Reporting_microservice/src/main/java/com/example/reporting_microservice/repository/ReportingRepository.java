package com.example.reporting_microservice.repository;

import com.example.reporting_microservice.DTOs.CompetitionDTO;
import com.example.reporting_microservice.DTOs.TaskDTO;
import com.example.reporting_microservice.DTOs.UserDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class ReportingRepository {

    private final RestTemplate restTemplate;
    private final String externalBaseUser = "http://usermicroservice:9015/api/users";
    private final String externalBaseTask = "http://taskmicroservice:9010/api/tasks";
    private final String externalBaseCompetition = "http://competitionmicroservice:9011/api/competitions";

    public ReportingRepository(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }


    public List<UserDTO> getAllUsers() {
        try {
            ResponseEntity<UserDTO[]> response = restTemplate.getForEntity(externalBaseUser, UserDTO[].class);
            UserDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new UserDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }



    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        try {
            ResponseEntity<UserDTO> response =
                    restTemplate.getForEntity(externalBaseUser + "/" + id, UserDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public List<TaskDTO> getAllTasks() {
        try {
            ResponseEntity<TaskDTO[]> response = restTemplate.getForEntity(externalBaseTask, TaskDTO[].class);
            TaskDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new TaskDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }


    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        try {
            ResponseEntity<TaskDTO> response =
                    restTemplate.getForEntity(externalBaseTask + "/" + id, TaskDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public List<TaskDTO> getTasksByUserId(@PathVariable Long id) {
        try {
            ResponseEntity<TaskDTO[]> response =
                    restTemplate.getForEntity(externalBaseTask + "/user/" + id, TaskDTO[].class);
            TaskDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new TaskDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

    public List<CompetitionDTO> getAllCompetitions() {
        try {
            ResponseEntity<CompetitionDTO[]> response = restTemplate.getForEntity(externalBaseCompetition, CompetitionDTO[].class);
            CompetitionDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new CompetitionDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }


    public ResponseEntity<CompetitionDTO> getCompetitionById(@PathVariable Long id) {
        try {
            ResponseEntity<CompetitionDTO> response =
                    restTemplate.getForEntity(externalBaseCompetition + "/" + id, CompetitionDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public List<CompetitionDTO> getCompetitionsByUserId(@PathVariable Long id) {
        try {
            ResponseEntity<CompetitionDTO[]> response =
                    restTemplate.getForEntity(externalBaseCompetition + "/user/" + id, CompetitionDTO[].class);
            CompetitionDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new CompetitionDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }

}