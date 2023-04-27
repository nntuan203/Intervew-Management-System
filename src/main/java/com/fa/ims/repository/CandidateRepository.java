package com.fa.ims.repository;

import com.fa.ims.entity.Candidate;
import com.fa.ims.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Page<Candidate> findAllByDeletedIsFalse(Pageable pageable);

    List<Candidate> findAllByDeletedFalse();

    Candidate findByCandidateIdAndDeletedFalse(Long candidateId);

    @Query("SELECT count(c.candidateId) " +
            "FROM Candidate c " +
            "WHERE c.candidateEmail = :email AND c.deleted = false")
    Long isEmailExists(@Param("email") String email);

    @Query("SELECT c " +
            "FROM Candidate c " +
            "JOIN c.positionCandidate pt " +
            "JOIN c.userRecruiter ur " +
            "JOIN c.candidateStatus cs " +
            "WHERE (CONCAT(c.candidateFullname,' ', c.candidateEmail,' ', c.candidatePhone,' ', pt.positionDesc,' ', ur.userName)) LIKE %:keyword% " +
            "AND (:status = '' OR cs.statusName = :status) " +
            "AND c.deleted = false")
    Page<Candidate> search(@Param("keyword") String keyword, @Param("status") String status, Pageable pageable);
}
