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
@Table(name = "highestlevel")
public class HighestLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "highest_id")
    private Long highestId;

    @Column(name = "highest_desc")
    private String highestDesc;

    @OneToMany(mappedBy = "highestLevel",fetch = FetchType.LAZY)
    private List<Candidate> candidate;

}
