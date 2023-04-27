package com.fa.ims.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity extends AbstractAuditingEntity {

    private boolean deleted;
}
