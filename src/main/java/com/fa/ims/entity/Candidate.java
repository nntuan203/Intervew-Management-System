package com.fa.ims.entity;


import com.fa.ims.enums.Gender;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "candidate")
public class Candidate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_Id")
    private Long candidateId;

    @Column(name = "candidate_fullname")
    private String candidateFullname;

    @Column(name = "candidate_gender")
    @Enumerated(EnumType.STRING)
    private Gender candidateGender;

    @Column(name = "candidate_phone_number")
    private String candidatePhone;

    @Column(name = "candidate_birth")
    private LocalDate candidateBirth;

    @Column(name = "candidate_email")
    private String candidateEmail;

    @Column(name = "candidate_address")
    private String candidateAddress;

    @Column(name = "candidate_cv")
    private String candidateCv;

    @Column(name = "candidate_yoe")
    private Long candidateYoe;

    @Column(name = "candidate_note")
    private String candidateNote;

    @OneToOne(mappedBy = "candidateSchedule", fetch = FetchType.EAGER)
    private InterviewSchedule interviewSchedule;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "position_id")
    private Position positionCandidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private CandidateStatus candidateStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "highest_id")
    private HighestLevel highestLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userRecruiter;

    @ManyToMany
    @JoinTable(name = "candidate_skills",
            joinColumns = @JoinColumn(name = "candidate_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills = new ArrayList<>();



}
