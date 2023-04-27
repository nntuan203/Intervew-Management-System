package com.fa.ims.controller;

import com.fa.ims.constant.CommonConstants;
import com.fa.ims.dto.InterviewScheduleDto;
import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.enums.InterviewSearchOption;
import com.fa.ims.enums.ScheduleResult;
import com.fa.ims.enums.ScheduleStatus;
import com.fa.ims.repository.CandidateRepository;
import com.fa.ims.repository.CandidateStatusRepository;
import com.fa.ims.repository.UserRepository;
import com.fa.ims.service.InterviewScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/interview-schedule")
public class InterviewScheduleController {
    @Autowired
    private InterviewScheduleService interviewScheduleService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CandidateStatusRepository candidateStatusRepository;


    @GetMapping("/view-list")
    public String showInterviewScheduleList(Model model, @RequestParam(defaultValue = "", required = false) String search, @RequestParam(required = false) InterviewSearchOption option, @RequestParam(required = false) ScheduleStatus status, @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex) {
        Page<InterviewSchedule> interviewSchedulePage = interviewScheduleService.findAllWithSearchOptionAndStatus(option, status, search, PageRequest.of(pageIndex - 1, CommonConstants.PAGE_SIZE));

        int totalPages = interviewSchedulePage.getTotalPages();
        int currentPage = 0;
        if (interviewSchedulePage.toList().size() == CommonConstants.PAGE_SIZE) {
            currentPage = CommonConstants.PAGE_SIZE * pageIndex;
        } else {
            currentPage = CommonConstants.PAGE_SIZE * (pageIndex - 1) + interviewSchedulePage.toList().size();
        }

        model.addAttribute("statusList", Arrays.stream(ScheduleStatus.values()).collect(Collectors.toList()));
        model.addAttribute("searchOptions", Arrays.stream(InterviewSearchOption.values()).collect(Collectors.toList()));
        model.addAttribute("scheduleList", interviewSchedulePage.toList());
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("option", option);
        model.addAttribute("status", status);
        model.addAttribute("search", search);
        model.addAttribute("totalRows", interviewSchedulePage.getTotalElements());
        model.addAttribute("currentPage", currentPage);

        return "interview-schedule/view";
    }


    @GetMapping("/view-list/{id}")
    public String showDetailInterviewSchedule(Model model, @PathVariable("id") Long id) {
        model.addAttribute("interviewSchedule", interviewScheduleService.findById(id));

        return "interview-schedule/view-detail";
    }

    @GetMapping("/edit/{id}")
    public String showEditInterviewSchedule(Model model, @PathVariable("id") Long id) {
        getAllAttributeForInterview(model);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        InterviewSchedule interviewSchedule = interviewScheduleService.findById(id);
        InterviewScheduleDto interviewScheduleDto = interviewScheduleService.mapScheduleToScheduleDto(interviewSchedule);
        model.addAttribute("interviewScheduleDto", interviewScheduleDto);
        model.addAttribute("interviewSchedule", interviewSchedule);
        if (Objects.equals(interviewSchedule.getCandidateSchedule().getUserRecruiter().getUserName(), authentication.getName())
                || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))
                || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "interview-schedule/edit";
        } else {
            return "error/403";
        }
    }


    @PostMapping("/edit/{id}")
    public String doEditInterviewSchedule(@ModelAttribute("interviewScheduleDto") @Valid InterviewScheduleDto interviewScheduleDto,
                                          BindingResult bindingResult,
                                          RedirectAttributes redirectAttributes,
                                          Model model,
                                          @PathVariable("id") Long id,
                                          @SessionAttribute(CommonConstants.USERNAME_SESSION) String userInSession) {
        model.addAttribute("interviewSchedule", interviewScheduleService.findById(id));
        if (bindingResult.hasErrors()) {
            getAllAttributeForInterview(model);
            return "interview-schedule/edit";
        } else {
            interviewScheduleService.update(interviewScheduleDto);
            redirectAttributes.addFlashAttribute("successMsg", "Update interview schedule success!");
            return "redirect:/interview-schedule/view-list";
        }
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_RECRUITER')")
    public String showCreateInterviewSchedule(Model model) {
        model.addAttribute("interviewScheduleDto", new InterviewScheduleDto());
        getAllAttributeForInterview(model);
        return "interview-schedule/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_RECRUITER')")
    public String doCreateInterviewSchedule(@ModelAttribute @Valid InterviewScheduleDto interviewScheduleDto,
                                           BindingResult bindingResult,
                                           Model model,
                                           RedirectAttributes redirectAttributes) {
        getAllAttributeForInterview(model);
        if (bindingResult.hasErrors()) {
            return "interview-schedule/create";
        } else {
            interviewScheduleService.create(interviewScheduleDto);
            redirectAttributes.addFlashAttribute("successMsg", "Create new interview schedule successfully!");
            return "redirect:/interview-schedule/view-list";
        }
    }

    @GetMapping("/submit-result/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER','ROLE_RECRUITER')")
    public String showSubmitResult(Model model, @PathVariable("id") Long id) {
        getAllAttributeForInterview(model);
        InterviewSchedule interviewSchedule = interviewScheduleService.findById(id);
        InterviewScheduleDto interviewScheduleDto = interviewScheduleService.mapScheduleToScheduleDto(interviewSchedule);
        model.addAttribute("interviewScheduleDto", interviewScheduleDto);
        model.addAttribute("interviewSchedule", interviewSchedule);
        return "interview-schedule/submit-result";

    }

    @PostMapping("/submit-result/{id}")
    public String doSubmitResult(@ModelAttribute("interviewScheduleDto") InterviewScheduleDto interviewScheduleDto,
                                 RedirectAttributes redirectAttributes,
                                 Model model,
                                 @PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        InterviewSchedule interviewSchedule = interviewScheduleService.findById(id);
        if (authentication.getName().equals(interviewSchedule.getCandidateSchedule().getUserRecruiter().getUserName())
                || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))
                || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            interviewScheduleService.submitInterviewResult(interviewScheduleDto);
            redirectAttributes.addFlashAttribute("successMsg", "Update interview schedule success!");
            return "redirect:/interview-schedule/view-list";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "You don't have permission to submit this interview result!");
            return "redirect:/accessDenied";
        }
    }


    public void getAllAttributeForInterview(Model model) {
        model.addAttribute("interviewers", userRepository.findAllUserWhoIsInterviewer());
        model.addAttribute("recruiters", userRepository.findAllUserWhoIsRecruiter());
        model.addAttribute("statusList", Arrays.stream(ScheduleStatus.values()).collect(Collectors.toList()));
        model.addAttribute("resultList", Arrays.stream(ScheduleResult.values()).collect(Collectors.toList()));
        model.addAttribute("candidateList", candidateRepository.findAllByDeletedFalse());
    }

}
