package com.fa.ims.service;

import com.fa.ims.dto.InterviewScheduleDto;
import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.enums.InterviewSearchOption;
import com.fa.ims.enums.ScheduleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InterviewScheduleService{
    Page<InterviewSchedule> findAllWithSearchOptionAndStatus(InterviewSearchOption option, ScheduleStatus status, String search, Pageable pageable);

    InterviewSchedule findById(Long id);

    InterviewScheduleDto mapScheduleToScheduleDto(InterviewSchedule interviewSchedule);

    void update(InterviewScheduleDto interviewScheduleDto);

    InterviewSchedule create(InterviewScheduleDto interviewScheduleDto);

    InterviewSchedule submitInterviewResult(InterviewScheduleDto interviewScheduleDto);
}
