package com.fa.ims.dto.validation;


import com.fa.ims.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class JobTitleUniqueValidator implements ConstraintValidator<JobTitleUnique, String> {

    @Autowired
    private JobService jobService;


    @Override
    public void initialize(JobTitleUnique constraintAnnotation) {
    }

    @Override
    public boolean isValid(String jobTitle, ConstraintValidatorContext context) {
        if (jobTitle == null || jobTitle.isEmpty()) {
            return true;
        }
        return jobService.countByJobTitle(jobTitle.trim()) == 0;
    }
}
