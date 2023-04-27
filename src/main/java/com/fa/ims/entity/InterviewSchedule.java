package com.fa.ims.entity;


import com.fa.ims.enums.ScheduleResult;
import com.fa.ims.enums.ScheduleStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "interview_schedule")
public class InterviewSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @Column(name = "schedule_title")
    private String scheduleTitle;

    @Column(name = "schedule_location")
    private String scheduleLocation;

    @Column(name = "schedule_notes")
    private String scheduleNotes;

    @Column(name = "interview_date")
    private LocalDate interviewDate;

    @Column(name = "interview_start_time")
    private LocalTime interviewStartTime;

    @Column(name = "interview_end_time")
    private LocalTime interviewEndTime;

    @Column(name = "schedule_meeting_id")
    private String scheduleMeetId;

    @Column(name = "schedule_status")
    @Enumerated(EnumType.STRING)
    private ScheduleStatus scheduleStatus;

    @Column(name = "schedule_result")
    @Enumerated(EnumType.STRING)
    private ScheduleResult scheduleResult;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "candidate_id")
    private Candidate candidateSchedule;

    @ManyToMany
    @JoinTable(name = "interview_member",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> userInterviewer = new ArrayList<>();


    public void updateStatus() {
        if (interviewDate.isEqual(LocalDate.now())) {
            scheduleStatus = ScheduleStatus.IN_PROGRESS;
        }
    }

    public boolean dueDateInterview() {
        return interviewDate.isEqual(LocalDate.now());
    }
}
