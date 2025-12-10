package com.example.feedback_microservice.service;

import com.example.feedback_microservice.DTOs.FeedbackDTO;
import com.example.feedback_microservice.repository.FeedbackRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {

        this.feedbackRepository = feedbackRepository;
    }

    public List<FeedbackDTO> getAllFeedbacks() {
        return feedbackRepository.getAllFeedbacks();
    }

   public Optional<FeedbackDTO> getFeedbackById(Long id) {
       return Optional.ofNullable(feedbackRepository.getFeedbackById(id))
                      .map(response -> response.getBody());
   }

    public FeedbackDTO createFeedback(FeedbackDTO feedbackDTO) {
        return feedbackRepository.createFeedback(feedbackDTO).getBody();
    }

    public ResponseEntity<FeedbackDTO> updateFeedback(Long id, FeedbackDTO updatedFeedbackDTO) {
            return feedbackRepository.updateFeedback(id, updatedFeedbackDTO);
    }

    public ResponseEntity<Void> deleteFeedback(Long id) {
        return feedbackRepository.deleteFeedback(id);
    }


}
