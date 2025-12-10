package com.example.feedback_microservice.repository;

import com.example.feedback_microservice.DTOs.FeedbackDTO;
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
public class FeedbackRepository {

    private final RestTemplate restTemplate;
    private final String externalBase = "http://dbmicroservice:9009/db/feedback";

    public FeedbackRepository(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }


    public List<FeedbackDTO> getAllFeedbacks() {
        try {
            ResponseEntity<FeedbackDTO[]> response = restTemplate.getForEntity(externalBase, FeedbackDTO[].class);
            FeedbackDTO[] body = response.getBody();
            return Arrays.asList(Optional.ofNullable(body).orElse(new FeedbackDTO[0]));
        } catch (RestClientException e) {
            return List.of();
        }
    }


    public ResponseEntity<FeedbackDTO> getFeedbackById(@PathVariable Long id) {
        try {
            ResponseEntity<FeedbackDTO> response =
                    restTemplate.getForEntity(externalBase + "/" + id, FeedbackDTO.class);
            return ResponseEntity.ok(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }


    public ResponseEntity<FeedbackDTO> createFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        try {
            ResponseEntity<FeedbackDTO> response =
                    restTemplate.postForEntity(externalBase, feedbackDTO, FeedbackDTO.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(response.getBody());
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    public ResponseEntity<FeedbackDTO> updateFeedback(@PathVariable Long id, @RequestBody FeedbackDTO updatedFeedbackDTO) {
        try {
            restTemplate.put(externalBase + "/" + id, updatedFeedbackDTO);
            return ResponseEntity.ok(updatedFeedbackDTO);
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        try {
            restTemplate.delete(externalBase + "/" + id);
            return ResponseEntity.noContent().build();
        } catch (RestClientException e) {
            return ResponseEntity.notFound().build();
        }
    }


}

