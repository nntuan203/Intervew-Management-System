package com.fa.ims.controller;

import com.fa.ims.dto.InterviewScheduleDto;
import com.fa.ims.entity.Candidate;
import com.fa.ims.entity.User;
import com.fa.ims.enums.ScheduleResult;
import com.fa.ims.enums.ScheduleStatus;
import com.fa.ims.service.InterviewScheduleService;
import org.aspectj.lang.annotation.Before;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class InterviewScheduleControllerMvcTest {
    @Mock
    private InterviewScheduleService interviewScheduleService;
    @InjectMocks
    private InterviewScheduleController interviewScheduleController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "recruiter1", roles = "RECRUITER")
    void testShowCreateInterviewSchedule_WithRecruiterRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/interview-schedule/create"))
                .andExpect(view().name("interview-schedule/create"))
                .andExpect(model().attributeExists("interviewScheduleDto"))
                .andExpect(model().attribute("interviewScheduleDto", Matchers.instanceOf(InterviewScheduleDto.class)));
    }

    @Test
    @WithMockUser(username = "manager1", roles = "MANAGER")
    void testShowCreateInterviewSchedule_WithManagerRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/interview-schedule/create"))
                .andExpect(view().name("interview-schedule/create"))
                .andExpect(model().attributeExists("interviewScheduleDto"))
                .andExpect(model().attribute("interviewScheduleDto", Matchers.instanceOf(InterviewScheduleDto.class)));
    }

    @Test
    @WithMockUser(username = "interviewer1", roles = "INTERVIEWER")
    void testShowCreateInterviewSchedule_WithInterviewerRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/interview-schedule/create"))
                .andExpect(redirectedUrl("/accessDenied"));
    }


}
