package com.fa.ims.repository;

import com.fa.ims.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartRepository extends JpaRepository<Department, Long> {
}
