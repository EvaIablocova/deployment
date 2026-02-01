package com.example.user_microservice.repository;

import com.example.user_microservice.DTOs.UserDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final RestTemplate restTemplate;
    private final String externalBase = "http://dbmicroservice:9009/db/users";

    public UserRepository(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }


    public List<UserDTO> getAllUsers() {
        try {
            ResponseEntity<UserDTO[]> response = restTemplate.getForEntity(externalBase, UserDTO[].class);
            UserDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new UserDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }


    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        try {
            ResponseEntity<UserDTO> response =
                    restTemplate.getForEntity(externalBase + "/" + id, UserDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }


    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO taskDTO) {
        try {
            ResponseEntity<UserDTO> response =
                    restTemplate.postForEntity(externalBase, taskDTO, UserDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public boolean recalculateUserPointsScore(Long userId, int points, boolean isDone) {
        try {
            String url = externalBase + "/recalculateUserPointsScore/" + userId + "/" + points + "/" + isDone;
            restTemplate.put(url, null); // null because no request body needed
            return true;
        } catch (RestClientException e) {
            return false;
        }
    }


    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO updatedUserDTO) {
        try {
            restTemplate.put(externalBase + "/" + id, updatedUserDTO);
            return ResponseEntity.ok(updatedUserDTO);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            restTemplate.delete(externalBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }


}