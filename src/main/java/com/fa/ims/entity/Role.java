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
@Table(name = "role")
public class Role  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role_desc")
    private String roleDesc;

    @OneToMany(mappedBy = "userRole", fetch = FetchType.LAZY)
    private List<User> userList;
}
