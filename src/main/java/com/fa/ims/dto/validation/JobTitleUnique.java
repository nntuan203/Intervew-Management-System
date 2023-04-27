package com.fa.ims.dto.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { JobTitleUniqueValidator.class })
public @interface JobTitleUnique {

    String message() default "Job title already exists in database";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
