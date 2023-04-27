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
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "department_name")
    private String departmentName;

    @OneToMany(mappedBy = "departmentOfferId",fetch = FetchType.LAZY)
    private List<Offer> offers;

    @OneToMany(mappedBy = "userDepartment",fetch = FetchType.LAZY)
    private List<User> userList;
}
