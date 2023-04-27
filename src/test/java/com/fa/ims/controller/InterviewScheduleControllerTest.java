package com.fa.ims.controller;

import com.fa.ims.dto.InterviewScheduleDto;
import com.fa.ims.repository.CandidateRepository;
import com.fa.ims.repository.UserRepository;
import com.fa.ims.service.InterviewScheduleService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class InterviewScheduleControllerTest {
    @InjectMocks
    private InterviewScheduleController interviewScheduleController;

    @Mock
    private Model model;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private InterviewScheduleService interviewScheduleService;

    @Mock
    private RedirectAttributes redirectAttributes;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private InterviewScheduleDto interviewScheduleDto;

    @Test
    void testShowCreateInterviewScheduleView() {
        // Arrange
        when(userRepository.findAllUserWhoIsInterviewer()).thenReturn(new ArrayList<>());
        when(userRepository.findAllUserWhoIsRecruiter()).thenReturn(new ArrayList<>());
        when(candidateRepository.findAllByDeletedFalse()).thenReturn(new ArrayList<>());

        // Act
        String result = interviewScheduleController.showCreateInterviewSchedule(model);

        // Assert
        assertEquals("interview-schedule/create", result);
        verify(model, times(1)).addAttribute(eq("interviewScheduleDto"), any(InterviewScheduleDto.class));

    }

    @Test
    public void testDoCreateInterviewSchedule_WithNoErrors() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String result = interviewScheduleController.doCreateInterviewSchedule(interviewScheduleDto, bindingResult, model, redirectAttributes);

        // Assert
        assertEquals("redirect:/interview-schedule/view-list", result);
        verify(interviewScheduleService, times(1)).create(interviewScheduleDto);
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("successMsg"), eq("Create new interview schedule successfully!"));
    }

    @Test
    public void testDoCreateInterviewSchedule_WithErrors() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String result = interviewScheduleController.doCreateInterviewSchedule(interviewScheduleDto, bindingResult, model, redirectAttributes);

        // Assert
        assertEquals("interview-schedule/create", result);
    }


}
