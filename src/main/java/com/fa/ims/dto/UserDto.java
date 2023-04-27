package com.fa.ims.dto;


import com.fa.ims.enums.Gender;
import com.fa.ims.enums.UserStatus;
import com.fa.ims.service.UserService;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDto {

    @Length(max = 30, message = "Input correct name format")
    @NotBlank(message = "You have to type a name")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "Input correct name format")
    private String userFullname;

    @Length(max = 40, message = "Input correct name format")
    @NotBlank(message = "You have to type an email")
    @Email(message = "Input correct email format")
    private String userEmail;

    @NotNull(message = "You have to choose Date Birth")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Input correct Date of Birth")
    private LocalDate userDateBirth;

    private String userAddress;

    @Pattern(regexp = "^\\d{10}$", message = "Input correct phone number format")
    private String userPhone;

    @NotNull(message = "You have to select a gender")
    private Gender userGender;

    @NotNull(message = "You have to select a position")
    private Long userRole;

    @NotNull(message = "You have to select a department")
    private Long userDepartment;

    @NotNull(message = "You have to select a status")
    private UserStatus userStatus;

    private String userNote;


}
