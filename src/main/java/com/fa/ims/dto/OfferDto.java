package com.fa.ims.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferDto {

    private Long offerId;

    @NotNull(message = "Candidate is required")
    private Long interviewScheduleId;

    @NotNull(message = "Position is required")
    private Long positionOffer;

    @NotNull(message = "Contract type is required")
    private String contractType;

    @NotNull(message = "Level is required")
    private Long jobLevelId;

    @NotNull(message = "Department is required")
    private Long departmentOfferId;

    @NotNull(message = "Recruited is required")
    private Long userRecruiterId;

    @NotNull(message = "Approved is required")
    private Long userApprovedId;

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be the future date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate offerDate;
    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be the current or future date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate offerContFrom;
    @NotNull(message = "End date is required")
    @Future(message = "Start date must be the future date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate offerContTo;

    @Pattern(regexp = "^$|^(0|[1-9]\\d{0,9})(\\.\\d{1,3})?$", message = "Salary must be a positive with max 10 digits before the decimal point and 3 digits after.")
    private String offerSalary;

    private String offerNotes;

    private String status;

    private LocalDate createdDate;

    private String createdBy;

    private String lastModifiedBy;

    private LocalDate lastModifiedDate;

    private String candidate;

    private String notes;

    private String interviews;

    @AssertTrue(message = "Start date must be before end date")
    public boolean isStartBeforeEnd() {
        if (offerContFrom == null || offerContTo == null) {
            return true;
        }
        return offerContFrom.isBefore(offerContTo);
    }
}
