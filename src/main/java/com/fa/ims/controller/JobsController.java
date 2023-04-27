package com.fa.ims.controller;

import com.fa.ims.constant.CommonConstants;
import com.fa.ims.dto.JobDto;
import com.fa.ims.entity.Job;
import com.fa.ims.entity.Skill;
import com.fa.ims.enums.JobStatus;
import com.fa.ims.service.ExcelService;
import com.fa.ims.service.JobService;
import com.fa.ims.service.SkillsService;
import com.fa.ims.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/jobs")
public class JobsController {

    @Autowired
    private JobService jobService;
    @Autowired
    private SkillsService skillsService;
    @Autowired
    private ExcelService excelService;

    @InitBinder("valueSearch")
    public void initBinderSearch(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
    }

    @InitBinder({"status"})
    public void initBinderOptional(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping
    public String viewOfferList(Model model,
                                @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                @RequestParam(required = false, defaultValue = "") String valueSearch,
                                @RequestParam(required = false) String status) {

        //List jobs
        JobStatus jobStatus = status != null ? JobStatus.valueOf(status) : null;

        Page<Job> jobDtoList = jobService.getAllJobRecordDto(valueSearch, jobStatus, pageNumber);
        //Paging
        model.addAttribute("jobDtoList", jobDtoList);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", CommonConstants.PAGE_SIZE);
        model.addAttribute("totalPages", jobDtoList != null ? jobDtoList.getTotalPages() : 0);
        model.addAttribute("totalElements", jobDtoList != null ? jobDtoList.getTotalElements() : 0);
        //List to selectBox
        model.addAttribute("statusList", JobStatus.values());
        //Search param return to view
        model.addAttribute("valueSearch", valueSearch);
        model.addAttribute("status", status);

        return "job/list-job";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_RECRUITER','ROLE_ADMIN')")
    @GetMapping("/create")
    public String createJobPage(Model model) {
        model.addAttribute("jobDto", new JobDto());
        model.addAttribute("listSkills", skillsService.getAllSkills());
        return "job/create-job";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_RECRUITER','ROLE_ADMIN')")
    @PostMapping("/create")
    public String createJobs(@ModelAttribute("jobDto") @Valid JobDto jobDto,
                             BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        if (!bindingResult.hasErrors()) {
            Job job = jobService.mapperJobDtoToJob(jobDto);
            job.setJobStatus(JobStatus.OPEN);
            if (jobService.saveJob(job) != null) {
                redirectAttributes.addFlashAttribute("messSuccess", Message.MESSAGE_012);
                return "redirect:/jobs";
            } else {
                model.addAttribute("messFail", Message.MESSAGE_011);
            }
        }
        model.addAttribute("listSkills", skillsService.getAllSkills());
        return "job/create-job";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_RECRUITER','ROLE_ADMIN')")
    @GetMapping("/delete/{jobId}")
    public String deleteJob(@PathVariable("jobId") Long jobId,
                            RedirectAttributes redirectAttributes){
        if(jobService.deleteJob(jobId) == 1){
            redirectAttributes.addFlashAttribute("messSuccess", Message.MESSAGE_017);
        } else {
            redirectAttributes.addFlashAttribute("messFail", Message.MESSAGE_018);
        }
        return "redirect:/jobs";
    }

    @GetMapping("/{jobId}")
    public String viewJob(@PathVariable("jobId") Long jobId, Model model,
                          RedirectAttributes redirectAttributes){

        Optional<Job> job = jobService.findById(jobId);

        if (job.isEmpty()) {
            redirectAttributes.addFlashAttribute("messFail", "This job doesn't exist!");
            return "redirect:/jobs/create";
        } else {

            List<Skill> skills = job.orElseThrow().getSkills();
            String skillsAsString = skills.stream()
                    .map(Skill::getSkillsDesc)
                    .collect(Collectors.joining(", "));

            JobDto jobDto = jobService.mapperJobToJobDto(job.orElseThrow());
            model.addAttribute("jobDto", jobDto);
            model.addAttribute("skills",skillsAsString);
            model.addAttribute("enumJobStatus", JobStatus.values());

            return "job/view-job";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_RECRUITER','ROLE_ADMIN')")
    @GetMapping("/update/{jobId}")
    public String updateJobPage(@PathVariable("jobId") Long jobId, Model model,
                                RedirectAttributes redirectAttributes){

        Optional<Job> job = jobService.findById(jobId);

        if (job.isEmpty()) {
            redirectAttributes.addFlashAttribute("messFail", "This job doesn't exist!");
            return "redirect:/jobs/create";
        } else {
            JobDto jobDto = jobService.mapperJobToJobDto(job.orElseThrow());
        model.addAttribute("jobDto", jobDto);
        model.addAttribute("listSkills", skillsService.getAllSkills());
        model.addAttribute("enumJobStatus", JobStatus.values());

            return "job/update-job";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_RECRUITER','ROLE_ADMIN')")
    @PostMapping("update/{jobId}")
    public String updateJob(@PathVariable("jobId") Long jobId,
                            @ModelAttribute("jobDto") @Valid JobDto jobDto, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes, Model model){
        if (!bindingResult.hasErrors()) {
            Job job = jobService.mapperJobDtoToJob(jobDto);
            job.setJobId(jobId);

            if (jobService.saveJob(job) != null) {
                redirectAttributes.addFlashAttribute("messSuccess", Message.MESSAGE_012);
            } else {
                redirectAttributes.addFlashAttribute("messFail",Message.MESSAGE_011);
            }
            return "redirect:/jobs";
        }
        model.addAttribute("listSkills", skillsService.getAllSkills());
        model.addAttribute("enumJobStatus", JobStatus.values());

        Job job = jobService.findById(jobId).orElseThrow();
        jobDto.setCreatedDate(job.getCreatedDate().toLocalDate());
        jobDto.setLastModifiedBy(job.getLastModifiedBy());
        jobDto.setLastModifiedDate(job.getLastModifiedDate().toLocalDate());

        return "job/update-job";
    }

    @PostMapping("/import")
    public String importJobs(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            excelService.importJobsFromExcel(file);
            redirectAttributes.addFlashAttribute("messSuccess", "Import jobs successfully!");
        }   catch (IllegalArgumentException | IOException e) {
            redirectAttributes.addFlashAttribute("messFail", "Failed to import jobs: " + e.getMessage());
        }
        return "redirect:/jobs";
    }

}
