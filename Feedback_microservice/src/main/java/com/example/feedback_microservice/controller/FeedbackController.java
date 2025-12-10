package com.example.feedback_microservice.controller;

import com.example.feedback_microservice.DTOs.FeedbackDTO;
import com.example.feedback_microservice.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public List<FeedbackDTO> getAllFeedbacks() {
        List<FeedbackDTO> feedback = feedbackService.getAllFeedbacks();
        return feedbackService.getAllFeedbacks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackDTO> getFeedbackById(@PathVariable Long id) {
        return feedbackService.getFeedbackById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public FeedbackDTO createFeedback(@RequestBody FeedbackDTO FeedbackDTO) {
        return feedbackService.createFeedback(FeedbackDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeedbackDTO> updateFeedback(@PathVariable Long id, @RequestBody FeedbackDTO updatedFeedbackDTO) {
        return feedbackService.updateFeedback(id, updatedFeedbackDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback (@PathVariable Long id) {
        return feedbackService.deleteFeedback(id);
    }


}
