package com.fa.ims.service;

import com.fa.ims.dto.JobDto;
import com.fa.ims.entity.Job;
import com.fa.ims.enums.JobStatus;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface JobService {
    Job saveJob(Job job);
    int deleteJob(Long jobId);
    Optional<Job> findById(Long jobId);
    Job mapperJobDtoToJob(JobDto jobDto);
    JobDto mapperJobToJobDto(Job job);
    Page<Job> getAllJobRecordDto(String valueSearch, JobStatus status, Integer pageNumber);
    int countByJobTitleExcludingJobId(String jobTitle, Long jobId);

    int countByJobTitle(String jobTitle);
}
