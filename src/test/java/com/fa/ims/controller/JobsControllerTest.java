package com.fa.ims.controller;

import com.fa.ims.dto.JobDto;
import com.fa.ims.entity.Job;
import com.fa.ims.entity.Skill;
import com.fa.ims.service.JobService;
import com.fa.ims.service.SkillsService;
import com.fa.ims.util.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class JobsControllerTest {

    @InjectMocks
    private JobsController jobsController;

    @Mock
    private Model model;

    @Mock
    private SkillsService skillsService;

    @Mock
    private JobService jobService;

    @Test
    void shouldReturnCreateJobView() {
        // arrange
        List<Skill> skillsList = new ArrayList<>();
        skillsList.add(new Skill());
        when(skillsService.getAllSkills()).thenReturn(skillsList);
        // act
        String viewName = jobsController.createJobPage(model);

        // assert
        assertEquals("job/create-job", viewName);
        verify(model, times(1)).addAttribute(eq("jobDto"), any(JobDto.class));
        verify(model, times(1)).addAttribute(eq("listSkills"), eq(skillsList));
    }

    @Test
    void shouldRedirectToJobsPage_whenBindingResultHasNoErrorsAndJobIsSaved() {
        // arrange
        JobDto jobDto = JobDto.builder()
                .jobTitle("Software Engineer")
                .build();

        BindingResult bindingResult = new BeanPropertyBindingResult(jobDto, "jobDto");

        Job job = Job.builder()
                .jobTitle("Software Engineer")
                .build();
        when(jobService.mapperJobDtoToJob(any(JobDto.class))).thenReturn(job);
        when(jobService.saveJob(any(Job.class))).thenReturn(job);

        // act
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();
        String viewName = jobsController.createJobs(jobDto, bindingResult, model, redirectAttributes);

        // assert
        assertEquals("redirect:/jobs", viewName);
        Map<String, Object> flashAttributes = (Map<String, Object>) redirectAttributes.getFlashAttributes();
        assertNotNull(flashAttributes.get("messSuccess"));
        assertEquals(Message.MESSAGE_012, flashAttributes.get("messSuccess"));
    }

    @Test
    void shouldRedirectToJobsPage_whenBindingResultHasNoErrorsAndJobIsNotSaved() {
        // arrange
        JobDto jobDto = JobDto.builder()
                .jobTitle("Software Engineer")
                .build();
        BindingResult bindingResult = new BeanPropertyBindingResult(jobDto, "jobDto");

        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();


        Job job = Job.builder()
                .jobTitle("Software Engineer")
                .build();
        when(jobService.mapperJobDtoToJob(any(JobDto.class))).thenReturn(job);
        when(jobService.saveJob(any(Job.class))).thenReturn(null); // returning null indicates the job was not saved
        List<Skill> skillsList = new ArrayList<>();
        skillsList.add(new Skill());
        when(skillsService.getAllSkills()).thenReturn(skillsList);

        // act
        String viewName = jobsController.createJobs(jobDto, bindingResult, model, redirectAttributes);

        // assert
        assertEquals("job/create-job", viewName);
        verify(model, times(1)).addAttribute(eq("listSkills"), eq(skillsList));
    }

    @Test
    public void shouldReturnCreateJobView_whenBindingResultHasErrors() {
        // arrange
        JobDto jobDto = JobDto.builder()
                .jobTitle("Software Engineer")
                .build();

        BindingResult bindingResult = new BeanPropertyBindingResult(jobDto, "jobDto");
        bindingResult.rejectValue("jobTitle", "Job title already exists in database");

        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        List<Skill> skillsList = new ArrayList<>();
        skillsList.add(new Skill());
        when(skillsService.getAllSkills()).thenReturn(skillsList);

        // act
        String viewName = jobsController.createJobs(jobDto, bindingResult, model, redirectAttributes);

        // assert
        assertEquals("job/create-job", viewName);
        verify(model, times(1)).addAttribute(eq("listSkills"), eq(skillsList));
    }
}
