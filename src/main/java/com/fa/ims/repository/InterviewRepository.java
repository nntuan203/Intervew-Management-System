package com.fa.ims.repository;

import com.fa.ims.entity.InterviewSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<InterviewSchedule, Long> {

}
