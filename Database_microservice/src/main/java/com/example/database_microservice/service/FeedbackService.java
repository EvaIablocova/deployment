package com.example.database_microservice.service;

import com.example.database_microservice.DTOs.FeedbackDTO;
import com.example.database_microservice.model.Feedback;
import com.example.database_microservice.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    // --- Mapping ---
    private FeedbackDTO toDTO(Feedback feedback) {
        return new FeedbackDTO(feedback);
    }

    // --- CRUD ---
    public List<FeedbackDTO> getAllFeedbacks() {
//        var tasks = taskRepository.findAll();
        return feedbackRepository.findAll().stream().map(this::toDTO).toList();
//        return new ArrayList<>();
    }

    public Optional<FeedbackDTO> getFeedbackById(Long id) {
        return feedbackRepository.findById(id).map(this::toDTO);
    }

    public FeedbackDTO createFeedback(FeedbackDTO feedbackDTO) {
        Feedback feedback = new Feedback(feedbackDTO);
        return toDTO(feedbackRepository.save(feedback));
    }

    public Optional<FeedbackDTO> updateFeedback(Long id, FeedbackDTO updatedFeedbackDTO) {
        Feedback updatedFeedback = new Feedback(updatedFeedbackDTO);
        return feedbackRepository.findById(id).map(task -> {
            task.setAspect(updatedFeedback.getAspect());
            task.setSuggestion(updatedFeedback.getSuggestion());
            return toDTO(feedbackRepository.save(task));
        });
    }

    public boolean deleteFeedback(Long id) {
        if (feedbackRepository.existsById(id)) {
            feedbackRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
