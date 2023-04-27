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
@Table(name = "position")
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private Long positionId;

    @Column(name = "position_name")
    private String positionDesc;

    @OneToMany(mappedBy = "positionCandidate", fetch = FetchType.LAZY)
    private List<Candidate> candidateList;

    @OneToMany(mappedBy = "positionOfferId", fetch = FetchType.LAZY)
    private List<Offer> offers;
}
