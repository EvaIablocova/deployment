package com.example.feedback_microservice.controller;

import com.example.feedback_microservice.DTOs.FeedbackDTO;
import com.example.feedback_microservice.service.FeedbackService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackControllerTest {

    @Mock
    private FeedbackService feedbackService;

    @InjectMocks
    private FeedbackController controller;

    @Test
    void getAllFeedbacks_returnsList_andDelegatesToService() {
        FeedbackDTO dto = mock(FeedbackDTO.class);
        List<FeedbackDTO> expected = List.of(dto);
        when(feedbackService.getAllFeedbacks()).thenReturn(expected);

        List<FeedbackDTO> result = controller.getAllFeedbacks();

        assertSame(expected, result);
        // current controller implementation calls the service twice; assert that to capture behavior
        verify(feedbackService, times(2)).getAllFeedbacks();
    }

    @Test
    void getFeedbackById_whenFound_returnsOkWithBody() {
        FeedbackDTO dto = mock(FeedbackDTO.class);
        when(feedbackService.getFeedbackById(1L)).thenReturn(Optional.of(dto));

        ResponseEntity<FeedbackDTO> resp = controller.getFeedbackById(1L);

        assertEquals(200, resp.getStatusCodeValue());
        assertSame(dto, resp.getBody());
        verify(feedbackService).getFeedbackById(1L);
    }

    @Test
    void getFeedbackById_whenNotFound_returnsNotFound() {
        when(feedbackService.getFeedbackById(2L)).thenReturn(Optional.empty());

        ResponseEntity<FeedbackDTO> resp = controller.getFeedbackById(2L);

        assertEquals(404, resp.getStatusCodeValue());
        assertNull(resp.getBody());
        verify(feedbackService).getFeedbackById(2L);
    }

    @Test
    void createFeedback_delegatesToService_andReturnsCreatedDto() {
        FeedbackDTO dto = mock(FeedbackDTO.class);
        when(feedbackService.createFeedback(dto)).thenReturn(dto);

        FeedbackDTO result = controller.createFeedback(dto);

        assertSame(dto, result);
        verify(feedbackService).createFeedback(dto);
    }

    @Test
    void updateFeedback_delegatesToService_andReturnsResponseEntity() {
        FeedbackDTO dto = mock(FeedbackDTO.class);
        ResponseEntity<FeedbackDTO> entity = ResponseEntity.ok(dto);
        when(feedbackService.updateFeedback(1L, dto)).thenReturn(entity);

        ResponseEntity<FeedbackDTO> resp = controller.updateFeedback(1L, dto);

        assertSame(entity, resp);
        verify(feedbackService).updateFeedback(1L, dto);
    }

    @Test
    void deleteFeedback_delegatesToService_andReturnsResponseEntity() {
        ResponseEntity<Void> entity = ResponseEntity.noContent().build();
        when(feedbackService.deleteFeedback(1L)).thenReturn(entity);

        ResponseEntity<Void> resp = controller.deleteFeedback(1L);

        assertSame(entity, resp);
        verify(feedbackService).deleteFeedback(1L);
    }
}