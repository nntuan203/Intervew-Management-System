package com.fa.ims.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "skill")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private Long skillsId;

    @Column(name = "skill_desc")
    private String skillsDesc;

    @ToString.Exclude
    @ManyToMany(mappedBy = "skills")
    private List<Job> jobs = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany(mappedBy = "skills")
    private List<Candidate> candidates = new ArrayList<>();

}
