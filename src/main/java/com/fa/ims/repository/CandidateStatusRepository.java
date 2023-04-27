package com.fa.ims.repository;

import com.fa.ims.entity.CandidateStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CandidateStatusRepository extends JpaRepository<CandidateStatus, Long> {
    @Query("select c from CandidateStatus c where upper(c.stage) = upper(?1)")
    List<CandidateStatus> findByStageIgnoreCase(String stage);

    CandidateStatus findCandidateStatusByStatusName(String statusName);
}
