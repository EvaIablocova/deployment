package com.example.feedback_microservice.service;

import com.example.feedback_microservice.DTOs.FeedbackDTO;
import com.example.feedback_microservice.repository.FeedbackRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    @Test
    void getAllFeedbacks_returnsListFromRepository() {
        FeedbackDTO dto = mock(FeedbackDTO.class);
        List<FeedbackDTO> expected = List.of(dto);

        when(feedbackRepository.getAllFeedbacks()).thenReturn(expected);

        List<FeedbackDTO> result = feedbackService.getAllFeedbacks();

        assertSame(expected, result);
        verify(feedbackRepository).getAllFeedbacks();
    }

    @Test
    void getFeedbackById_whenFound_returnsOptionalWithBody() {
        FeedbackDTO dto = mock(FeedbackDTO.class);
        ResponseEntity<FeedbackDTO> resp = ResponseEntity.ok(dto);

        when(feedbackRepository.getFeedbackById(1L)).thenReturn(resp);

        Optional<FeedbackDTO> result = feedbackService.getFeedbackById(1L);

        assertTrue(result.isPresent());
        assertSame(dto, result.get());
        verify(feedbackRepository).getFeedbackById(1L);
    }

    @Test
    void getFeedbackById_whenNotFound_returnsEmptyOptional() {
        ResponseEntity<FeedbackDTO> resp = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        when(feedbackRepository.getFeedbackById(2L)).thenReturn(resp);

        Optional<FeedbackDTO> result = feedbackService.getFeedbackById(2L);

        assertFalse(result.isPresent());
        verify(feedbackRepository).getFeedbackById(2L);
    }

    @Test
    void createFeedback_returnsBodyFromRepository() {
        FeedbackDTO dto = mock(FeedbackDTO.class);
        ResponseEntity<FeedbackDTO> resp = new ResponseEntity<>(dto, HttpStatus.CREATED);

        when(feedbackRepository.createFeedback(dto)).thenReturn(resp);

        FeedbackDTO result = feedbackService.createFeedback(dto);

        assertSame(dto, result);
        verify(feedbackRepository).createFeedback(dto);
    }

    @Test
    void updateFeedback_delegatesAndReturnsRepositoryResponse() {
        FeedbackDTO dto = mock(FeedbackDTO.class);
        ResponseEntity<FeedbackDTO> resp = ResponseEntity.ok(dto);

        when(feedbackRepository.updateFeedback(1L, dto)).thenReturn(resp);

        ResponseEntity<FeedbackDTO> result = feedbackService.updateFeedback(1L, dto);

        assertSame(resp, result);
        verify(feedbackRepository).updateFeedback(1L, dto);
    }

    @Test
    void deleteFeedback_delegatesAndReturnsRepositoryResponse() {
        ResponseEntity<Void> resp = ResponseEntity.noContent().build();

        when(feedbackRepository.deleteFeedback(1L)).thenReturn(resp);

        ResponseEntity<Void> result = feedbackService.deleteFeedback(1L);

        assertSame(resp, result);
        verify(feedbackRepository).deleteFeedback(1L);
    }
}