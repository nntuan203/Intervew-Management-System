package com.fa.ims.service.impl;

import com.fa.ims.dto.InterviewScheduleDto;
import com.fa.ims.entity.Candidate;
import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.enums.InterviewSearchOption;
import com.fa.ims.enums.ScheduleResult;
import com.fa.ims.enums.ScheduleStatus;
import com.fa.ims.repository.CandidateRepository;
import com.fa.ims.repository.CandidateStatusRepository;
import com.fa.ims.repository.InterviewScheduleRepository;
import com.fa.ims.repository.UserRepository;
import com.fa.ims.service.InterviewScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

@Service
public class InterviewScheduleServiceImpl implements InterviewScheduleService {

    @Autowired
    private InterviewScheduleRepository interviewScheduleRepository;

    @Autowired
    private CandidateStatusRepository candidateStatusRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<InterviewSchedule> findAllWithSearchOptionAndStatus(InterviewSearchOption option, ScheduleStatus status, String search, Pageable pageable) {
        Page<InterviewSchedule> schedules;
        if (option == null && status == null) {
            schedules = interviewScheduleRepository.findByScheduleTitleContainsIgnoreCaseAndDeletedIsFalseOrderByCreatedDateDesc(search, pageable);
        } else if (option == null) {
            schedules = interviewScheduleRepository.findByScheduleStatusOrderByCreatedDateDesc(status, pageable);
        } else if (status == null) {
            switch (option) {
                case Candidate:
                    schedules = interviewScheduleRepository.findByCandidateSchedule_CandidateFullnameContainsIgnoreCaseAndDeletedIsFalseOrderByCreatedDateDesc(search, pageable);
                    break;
                case Interviewer:
                    schedules = interviewScheduleRepository.findByUserInterviewer_UserFullnameContainsIgnoreCaseAndDeletedIsFalseOrderByCreatedDateDesc(search, pageable);
                    break;
                default:
                    schedules = interviewScheduleRepository.findByScheduleTitleContainsIgnoreCaseAndDeletedIsFalseOrderByCreatedDateDesc(search, pageable);
                    break;
            }
        } else {
            switch (option) {
                case Candidate:
                    schedules = interviewScheduleRepository.findByCandidateSchedule_CandidateFullnameContainsIgnoreCaseAndScheduleStatusAndDeletedIsFalseOrderByCreatedDateDesc(search, status, pageable);
                    break;
                case Interviewer:
                    schedules = interviewScheduleRepository.findByUserInterviewer_UserFullnameContainsIgnoreCaseAndDeletedFalseAndScheduleStatusOrderByCreatedDateDesc(search, status, pageable);
                    break;
                default:
                    schedules = interviewScheduleRepository.findByScheduleTitleContainsIgnoreCaseAndScheduleStatusAndDeletedIsFalseOrderByCreatedDateDesc(search, status, pageable);
                    break;
            }
        }
        return schedules;
    }

    @Override
    public InterviewSchedule findById(Long id) {
        return interviewScheduleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
    }

    @Override
    public InterviewScheduleDto mapScheduleToScheduleDto(InterviewSchedule interviewSchedule) {
//        InterviewScheduleDto interviewScheduleDto = InterviewScheduleDto.builder()
//                .scheduleId(interviewSchedule.getScheduleId())
//                .scheduleTitle(interviewSchedule.getScheduleTitle())
//                .scheduleLocation(interviewSchedule.getScheduleLocation())
//                .scheduleNotes(interviewSchedule.getScheduleNotes())
//                .candidateSchedule(interviewSchedule.getCandidateSchedule())
//                .interviewDate(interviewSchedule.getInterviewDate())
//                .interviewStartTime(interviewSchedule.getInterviewStartTime())
//                .interviewEndTime(interviewSchedule.getInterviewEndTime())
//                .scheduleMeetId(interviewSchedule.getScheduleMeetId())
//                .scheduleStatus(interviewSchedule.getScheduleStatus())
//                .scheduleResult(interviewSchedule.getScheduleResult())
//                .scheduleRecruiterId(interviewSchedule.getScheduleRecruiterId())
//                .userInterviewer(interviewSchedule.getUserInterviewer())
//                .build();

        return modelMapper.map(interviewSchedule, InterviewScheduleDto.class);
    }

    @Override
    public void update(InterviewScheduleDto interviewScheduleDto) {
        InterviewSchedule interviewSchedule =
                interviewScheduleRepository
                        .findById(interviewScheduleDto.getScheduleId())
                        .orElseThrow(() -> new EntityNotFoundException());
        interviewSchedule.setScheduleTitle(interviewScheduleDto.getScheduleTitle());
        interviewSchedule.setCandidateSchedule(interviewScheduleDto.getCandidateSchedule());
        interviewSchedule.setInterviewDate(interviewScheduleDto.getInterviewDate());
        interviewSchedule.setInterviewStartTime(interviewScheduleDto.getInterviewStartTime());
        interviewSchedule.setInterviewEndTime(interviewScheduleDto.getInterviewEndTime());
        interviewSchedule.setScheduleNotes(interviewScheduleDto.getScheduleNotes());
        interviewSchedule.setUserInterviewer(interviewScheduleDto.getUserInterviewer());
        interviewSchedule.setScheduleLocation(interviewScheduleDto.getScheduleLocation());
        interviewSchedule.setScheduleStatus(interviewScheduleDto.getScheduleStatus());
        interviewSchedule.setScheduleMeetId(interviewScheduleDto.getScheduleMeetId());

        Candidate candidate = interviewScheduleDto.getCandidateSchedule();
        candidate.setUserRecruiter(interviewScheduleDto.getRecruiterOwner());
        candidate.setCandidateStatus(
                candidateStatusRepository
                        .findCandidateStatusByStatusName(interviewScheduleDto.getScheduleStatus().toString()));

        interviewSchedule.setCandidateSchedule(candidate);
        interviewScheduleRepository.save(interviewSchedule);
    }

    @Override
    public InterviewSchedule create(InterviewScheduleDto interviewScheduleDto) {
        InterviewSchedule interviewSchedule = modelMapper.map(interviewScheduleDto, InterviewSchedule.class);
        interviewSchedule.setScheduleResult(ScheduleResult.OPEN);
        interviewSchedule.setScheduleStatus(ScheduleStatus.WAITING_FOR_INTERVIEW);
        // Set candidate status
        Candidate candidate = candidateRepository.findByCandidateIdAndDeletedFalse(interviewScheduleDto.getCandidateSchedule().getCandidateId());
        candidate.setCandidateStatus(candidateStatusRepository.findCandidateStatusByStatusName("Waiting for interview"));
        interviewSchedule.setCandidateSchedule(candidate);

        if (interviewSchedule.getInterviewDate().isEqual(LocalDate.now())) {
            interviewSchedule.setScheduleStatus(ScheduleStatus.IN_PROGRESS);
            candidate.setCandidateStatus(candidateStatusRepository.findCandidateStatusByStatusName("In-progress"));
            interviewSchedule.setCandidateSchedule(candidate);
        }
        return interviewScheduleRepository.save(interviewSchedule);
    }

    @Override
    public InterviewSchedule submitInterviewResult(InterviewScheduleDto interviewScheduleDto) {
        InterviewSchedule interviewSchedule =
                interviewScheduleRepository
                        .findById(interviewScheduleDto.getScheduleId())
                        .orElseThrow(() -> new EntityNotFoundException());
        interviewSchedule.setScheduleResult(interviewScheduleDto.getScheduleResult());
        interviewSchedule.setScheduleNotes(interviewScheduleDto.getScheduleNotes());
        Candidate candidate = candidateRepository.findByCandidateIdAndDeletedFalse(interviewSchedule.getCandidateSchedule().getCandidateId());
        switch (interviewScheduleDto.getScheduleResult()) {
            case PASSED:
                candidate.setCandidateStatus(candidateStatusRepository.findCandidateStatusByStatusName("Passed interview"));
                interviewSchedule.setScheduleStatus(ScheduleStatus.PASSED_INTERVIEW);
                break;
            case CANCEL:
                candidate.setCandidateStatus(candidateStatusRepository.findCandidateStatusByStatusName("Cancelled interview"));
                interviewSchedule.setScheduleStatus(ScheduleStatus.CANCELLED_INTERVIEW);
                break;
            case FAILED:
                candidate.setCandidateStatus(candidateStatusRepository.findCandidateStatusByStatusName("Failed interview"));
                interviewSchedule.setScheduleStatus(ScheduleStatus.FAILED_INTERVIEW);
                break;
            default: break;
        }
        interviewSchedule.setCandidateSchedule(candidate);
        return interviewScheduleRepository.save(interviewSchedule);
    }
}
