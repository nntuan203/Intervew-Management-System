package com.fa.ims.repository;

import com.fa.ims.entity.InterviewSchedule;
import com.fa.ims.enums.ScheduleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;


public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Long> {
    Page<InterviewSchedule> findByScheduleStatusOrderByCreatedDateDesc(ScheduleStatus scheduleStatus, Pageable pageable);

    Page<InterviewSchedule> findByScheduleTitleContainsIgnoreCaseAndScheduleStatusAndDeletedIsFalseOrderByCreatedDateDesc(String scheduleTitle, ScheduleStatus scheduleStatus, Pageable pageable);

    Page<InterviewSchedule> findByCandidateSchedule_CandidateFullnameContainsIgnoreCaseAndScheduleStatusAndDeletedIsFalseOrderByCreatedDateDesc(String fullName, ScheduleStatus scheduleStatus, Pageable pageable);

    Page<InterviewSchedule> findByUserInterviewer_UserFullnameContainsIgnoreCaseAndDeletedFalseAndScheduleStatusOrderByCreatedDateDesc(@Nullable String userFullname, @Nullable ScheduleStatus scheduleStatus, Pageable pageable);


    Page<InterviewSchedule> findByCandidateSchedule_CandidateFullnameContainsIgnoreCaseAndDeletedIsFalseOrderByCreatedDateDesc(String keyword, Pageable pageable);

    Page<InterviewSchedule> findByUserInterviewer_UserFullnameContainsIgnoreCaseAndDeletedIsFalseOrderByCreatedDateDesc(String keyword, Pageable pageable);

    Page<InterviewSchedule> findByScheduleTitleContainsIgnoreCaseAndDeletedIsFalseOrderByCreatedDateDesc(String keyword, Pageable pageable);


}
