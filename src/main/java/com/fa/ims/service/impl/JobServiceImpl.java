package com.fa.ims.service.impl;

import com.fa.ims.constant.CommonConstants;
import com.fa.ims.dto.JobDto;
import com.fa.ims.entity.Job;
import com.fa.ims.entity.Skill;
import com.fa.ims.enums.JobStatus;
import com.fa.ims.repository.JobRepository;
import com.fa.ims.service.JobService;
import com.fa.ims.service.SkillsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private SkillsService skillsService;

    @Override
    @Transactional
    public Job saveJob(Job job) {
        return jobRepository.save(job);
    }

    @Override
    @Transactional
    public int deleteJob(Long jobId) {
        return jobRepository.updateDeletedIsFalse(jobId);
    }

    @Override
    public Optional<Job> findById(Long jobId) {
        return jobRepository.findByJobIdAndDeletedIsFalse(jobId);
    }

    @Override
    public Job mapperJobDtoToJob(JobDto jobDto) {
        jobDto.setJobTitle(jobDto.getJobTitle().trim()); // Trim trường jobTitle trước khi map
        Job job = modelMapper.map(jobDto, Job.class);
        List<Skill> skills = new ArrayList<>();

        for (Skill skillId : jobDto.getSkills()) {
            skillsService.findById(skillId.getSkillsId()).ifPresent(skills::add);
        }

        job.setSkills(skills);

        return job;
    }

    @Override
    public JobDto mapperJobToJobDto(Job job) {
        JobDto jobDto = modelMapper.map(job, JobDto.class);
        jobDto.setCreatedDate(job.getCreatedDate().toLocalDate());
        jobDto.setLastModifiedDate(job.getLastModifiedDate().toLocalDate());

        return jobDto;
    }

    @Override
    public Page<Job> getAllJobRecordDto(String valueSearch, JobStatus status, Integer pageNumber) {
        Sort sortByIdDesc = Sort.by("jobId").descending();
        return jobRepository.getAllJobRecordDto(valueSearch, status, PageRequest.of(pageNumber - 1, CommonConstants.PAGE_SIZE,sortByIdDesc));
    }

    @Override
    public int countByJobTitleExcludingJobId(String jobTitle, Long jobId) {
        return jobRepository.countByJobTitleExcludingJobId(jobTitle, jobId);
    }

    @Override
    public int countByJobTitle(String jobTitle) {
        return jobRepository.countByJobTitleAndAndDeletedIsFalse(jobTitle);
    }
}
