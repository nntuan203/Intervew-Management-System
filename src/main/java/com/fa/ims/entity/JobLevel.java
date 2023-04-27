package com.fa.ims.entity;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "joblevel")
public class JobLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_id")
    private Long levelId;

    @Column(name = "level_desc")
    private String levelDesc;

    @OneToMany(mappedBy = "jobLevelId", fetch = FetchType.LAZY)
    private List<Offer> offer;
}
