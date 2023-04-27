package com.fa.ims.controller.api;

import com.fa.ims.dto.InterviewOfferDto;
import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.entity.User;
import com.fa.ims.repository.CandidateStatusRepository;
import com.fa.ims.repository.InterviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/offer")
public class OfferControllerApi {

    @Autowired
    private InterviewRepository interviewRepository;

    @GetMapping("/{interviewSchedule}")
    public InterviewOfferDto handleRequest(@PathVariable Long interviewSchedule) {

        InterviewSchedule interview = interviewRepository.findById(interviewSchedule).orElseThrow();

        return InterviewOfferDto.builder()
                .interview(interview.getUserInterviewer().stream()
                        .map(User::getUserName)
                        .collect(Collectors.joining(" , ")))
                .statusInterview(interview.getCandidateSchedule().getCandidateStatus().getStatusName())
                .note(interview.getScheduleNotes())
        .build();
    }
}
