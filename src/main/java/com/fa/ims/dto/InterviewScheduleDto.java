package com.fa.ims.dto;

import com.fa.ims.entity.Candidate;
import com.fa.ims.entity.CandidateStatus;
import com.fa.ims.entity.User;
import com.fa.ims.enums.ScheduleResult;
import com.fa.ims.enums.ScheduleStatus;
import com.fa.ims.util.Message;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class InterviewScheduleDto {

    private Long scheduleId;

    @NotEmpty(message = Message.MESSAGE_003)
    @Length(min = 0, max = 255, message = "Max length is 255 characters!")
    private String scheduleTitle;

    @Length(min = 0, max = 255, message = "Max length is 255 characters!")
    private String scheduleLocation;

    @Length(min = 0, max = 255, message = "Max length is 255 characters!")
    private String scheduleNotes;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = Message.MESSAGE_003)
    private LocalDate interviewDate;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = Message.MESSAGE_003)
    private LocalTime interviewStartTime;
    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = Message.MESSAGE_003)
    private LocalTime interviewEndTime;

    private String scheduleMeetId;

    @NotNull(message = Message.MESSAGE_003)
    private ScheduleStatus scheduleStatus;
    
    private CandidateStatus candidateStatus;

    private ScheduleResult scheduleResult;

    @NotNull(message = Message.MESSAGE_003)
    private Candidate candidateSchedule;

    @NotEmpty(message = Message.MESSAGE_003)
    private List<User> userInterviewer = new ArrayList<>();

    @NotNull(message = Message.MESSAGE_003)
    private User recruiterOwner;

    @AssertTrue(message = "Interview date must in present or future!")
    public boolean isInterviewDateInPresentOrFuture() {
        if (interviewDate == null) {
            return true;
        }
        return interviewDate.isEqual(LocalDate.now()) || interviewDate.isAfter(LocalDate.now());
    }

    @AssertTrue(message = "Interview start time must before end time!")
    public boolean isInterviewStartTimeBeforeEndTime() {
        if (interviewStartTime == null || interviewEndTime == null) {
            return true;
        }
        return interviewStartTime.isBefore(interviewEndTime);
    }

}
