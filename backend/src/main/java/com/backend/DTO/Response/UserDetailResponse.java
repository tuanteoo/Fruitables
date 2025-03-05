package com.backend.DTO.Response;

import com.backend.Util.Gender;
import com.backend.Util.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.Date;

@Builder
@Getter
@AllArgsConstructor
public class UserDetailResponse implements Serializable {

    private Long id;

    private String email;

    private String fullName;

    private Date dateOfBirth;

    private Gender gender;

    private String phone;

    private String password;
}
