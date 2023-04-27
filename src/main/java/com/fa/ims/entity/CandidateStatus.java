package com.fa.ims.entity;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "candidatestatus")
public class CandidateStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long statusId;

    @Column(name = "status_name")
    private String statusName;

    @Column(name = "stage")
    private String  stage;

    @OneToMany(mappedBy = "candidateStatus", fetch = FetchType.LAZY)
    private List<Candidate> candidateList;
}
