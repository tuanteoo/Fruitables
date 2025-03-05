package com.backend.DTO.Request;

import com.backend.Util.Gender;
import com.backend.Util.UserStatus;
import com.backend.Util.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Getter
public class UserRequestDTO implements Serializable {
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Fullname is mandatory")
    @Size(max = 255, message = "Fullname should not exceed 255 characters")
    private String fullname;

    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "password must be valid")
    private String password;

    @NotNull(message = "User type is mandatory")
    private UserType type = UserType.USER;

    @NotNull(message = "User Status is mandatory")
    private UserStatus status = UserStatus.INACTIVE;

    @Pattern(regexp = "^(0|\\\\+84)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-5]|9[0-4|6-9])[0-9]{7}$", message = "phone must be valid")
    private String phone;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private Date dateOfBirth;

    private Gender gender;
}
