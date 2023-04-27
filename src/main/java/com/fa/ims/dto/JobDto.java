package com.fa.ims.dto;

import com.fa.ims.dto.validation.JobTitleUnique;
import com.fa.ims.entity.Skill;
import com.fa.ims.enums.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDto {

    @JobTitleUnique
    @NotBlank(message = "Job Title is required")
    private String jobTitle;
    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be the current or future date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate jobStart;
    @NotNull(message = "End date is required")
    @Future(message = "Start date must be the future date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate jobEnd;

    @Pattern(regexp = "^$|^(0|[1-9]\\d{0,9})(\\.\\d{1,3})?$", message = "Salary must be a positive with max 10 digits before the decimal point and 3 digits after.")
    private String jobSalaryFrom;
    @NotBlank(message = "Salary to is required")
    @Pattern(regexp = "^$|^(0|[1-9]\\d{0,9})(\\.\\d{1,3})?$", message = "Salary must be a positive with max 10 digits before the decimal point and 3 digits after.")
    private String jobSalaryTo;

    private String jobAddress;

    private JobStatus jobStatus;

    private String jobDescription;

    private LocalDate createdDate;

    private String createdBy;

    private String lastModifiedBy;

    private LocalDate lastModifiedDate;

    private Long jobId;

    @NotNull(message = "Skills is required")
    @Size(min = 1, max = 6, message = "Please select 1 to 6 skills")
    private List<Skill> skills;

    @AssertTrue(message = "Start date must be before end date")
    public boolean isStartBeforeEnd() {
        if (jobStart == null || jobEnd == null) {
            return true;
        }
        return jobStart.isBefore(jobEnd);
    }

    @AssertTrue(message = "Unreasonable salary range")
    public boolean isSalaryFromLessThanSalaryTo() {
        if (jobSalaryTo == null || jobSalaryFrom == null || jobSalaryFrom.equals("")) {
            return true;
        }
        try {
            NumberFormat format = NumberFormat.getInstance(Locale.US);
            Number from = format.parse(jobSalaryFrom);
            Number to = format.parse(jobSalaryTo);
            return to.doubleValue() > from.doubleValue();
        } catch (ParseException e) {
            return false; // nếu không thể chuyển đổi chuỗi thành số, trả về false
        }
    }
}

