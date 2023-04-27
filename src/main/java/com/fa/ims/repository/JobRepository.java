package com.fa.ims.repository;


import com.fa.ims.entity.Job;
import com.fa.ims.entity.Skill;
import com.fa.ims.enums.JobStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job,Long> {
    @Modifying
    @Query("update Job j set j.deleted = true where j.jobId = ?1 and j.deleted = false ")
    int updateDeletedIsFalse(Long jobId);
    Optional<Job> findByJobIdAndDeletedIsFalse(Long jobId);

    @Query("SELECT DISTINCT j " +
            "FROM Job j " +
            "LEFT JOIN j.skills s " +
            "WHERE (:status IS NULL OR j.jobStatus = :status) " +
            "AND ((j.jobTitle LIKE %:value% OR CAST(j.jobEnd AS string) LIKE %:value%) OR CAST(j.jobStart AS string) LIKE %:value% OR s.skillsDesc LIKE %:value%) " +
            "AND j.deleted = false")
    Page<Job> getAllJobRecordDto(@Param("value") String value, @Param("status") JobStatus status, Pageable pageable);



    @Query("SELECT s FROM Job j JOIN j.skills s WHERE j.jobId = :jobId")
    List<Skill> findAllSkillsByJobId(@Param("jobId") Long jobId);

    @Query("SELECT COUNT(j) FROM Job j WHERE LOWER(j.jobTitle) = LOWER(:jobTitle) AND j.jobId <> :jobId AND j.deleted IS FALSE")
    int countByJobTitleExcludingJobId(@Param("jobTitle") String jobTitle, @Param("jobId") Long jobId);

    @Query("SELECT COUNT(j) FROM Job j WHERE j.jobTitle = :jobTitle AND j.deleted IS FALSE")
    int countByJobTitle(@Param("jobTitle") String jobTitle);

    int countByJobTitleAndAndDeletedIsFalse(String jobTitle);
}
