package com.fa.ims.entity;


import com.fa.ims.enums.ContractType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "offer")
public class Offer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "offer_id")
    private Long offerId;

    @Column(name = "offer_contract_from")
    private LocalDate offerContFrom;

    @Column(name = "offer_contract_to")
    private LocalDate offerContTo;

    @Column(name = "offer_contract_type")
    @Enumerated(EnumType.STRING)
    private ContractType contractType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_recruiter_id")
    private User userRecruiterId;

    @Column(name = "offer_due_date")
    private LocalDate offerDate;

    @Column(name = "offer_basic_salary")
    private double offerSalary;

    @Column(name = "offer_notes")
    private String offerNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position positionOfferId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_approved_id")
    private User userApprovedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id")
    private JobLevel jobLevelId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department departmentOfferId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_schedule_id")
    private InterviewSchedule interviewScheduleId;
}
