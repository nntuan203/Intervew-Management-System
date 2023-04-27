package com.fa.ims.entity;


import com.fa.ims.enums.JobStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
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
@Table(name = "joblist")
public class Job extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long jobId;

    @Column(name = "job_title")
    private String jobTitle;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "job_start_date")
    private LocalDate jobStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "job_end_date")
    private LocalDate jobEnd;

    @Column(name = "job_salary_from")
    private double jobSalaryFrom;

    @Column(name = "job_salary_to")
    private double jobSalaryTo;

    @Column(name = "job_address")
    private String jobAddress;

    @Column(name = "job_description")
    private String jobDescription;

    @Column(name = "job_status",columnDefinition = "varchar(255) default 'OPEN'")
    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus = JobStatus.OPEN;

    @ManyToMany
    @JoinTable(name = "job_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills = new ArrayList<>();


}
