package com.fa.ims.entity;

import com.fa.ims.enums.Gender;
import com.fa.ims.enums.UserStatus;
import lombok.*;

import javax.persistence.*;
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
@ToString
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_fullname")
    private String userFullname;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_date_birth")
    private LocalDate userDateBirth;

    @Column(name = "user_address")
    private String userAddress;

    @Column(name = "user_phone")
    private String userPhone;

    @Column(name = "user_gender")
    @Enumerated(EnumType.STRING)
    private Gender userGender;

    @Column(name = "user_status")
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column(name = "user_note")
    private String userNote;

    @Column(name = "user_username")
    private String userName;

    @Column(name = "user_password")
    private String userPassword;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role userRole;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    @ToString.Exclude
    private Department userDepartment;

    @OneToMany(mappedBy = "userApprovedId", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Offer> userApprovedOffer;

    @OneToMany(mappedBy = "userRecruiterId", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Offer> userRecruiterOffer;

    @OneToMany(mappedBy = "userRecruiter", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Candidate> candidates;

    @ToString.Exclude
    @ManyToMany(mappedBy = "userInterviewer")
    private List<InterviewSchedule> interviewSchedules = new ArrayList<>();

}



