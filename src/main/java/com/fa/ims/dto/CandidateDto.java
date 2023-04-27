package com.fa.ims.dto;


import com.fa.ims.enums.Gender;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CandidateDto {

    @Length(max = 30, message = "Input correct name format")
    @NotBlank(message = "You have to type a name")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "Input correct name format")
    private String candidateFullname;

    @Length(max = 40, message = "Input correct email format")
    @NotBlank(message = "You have to type an email")
    @Email(message = "Input correct email format")
    private String candidateEmail;

    @NotNull(message = "You have to choose Date Birth")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Input correct Date of Birth")
    private LocalDate candidateBirth;

    @Length(max = 255)
    private String candidateAddress;

    @Pattern(regexp = "^\\d{10}$", message = "Input correct phone number format")
    private String candidatePhone;

    @NotNull(message = "You have to select a gender")
    private Gender candidateGender;

    private MultipartFile candidateCV;

    private String uriPath;

    private String fileName;

    @NotNull(message = "You have to input status")
    private String candidateStatus;

    @NotNull(message = "You have to input position")
    private Long positionCandidate;

    @NotNull(message = "You have to input Year of Experience")
    private Long candidateYoe;

    @NotNull(message = "You have to input skills")
    private List<Long> candidateSkills;

    @NotNull(message = "You have to input highest level")
    private Long highestLevel;

    @NotNull(message = "You have to input recruiter")
    private Long userRecruiter;

    @Length(max = 500)
    private String candidateNote;


}
