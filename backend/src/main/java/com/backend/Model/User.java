package com.backend.Model;

import com.backend.Util.Gender;
import com.backend.Util.UserStatus;
import com.backend.Util.UserType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "User")
@Table(name = "tbl_user")
public class User extends AbstractModel<Long>{

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "dateofbirth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "type")
    private UserType type;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status")
    private UserStatus status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private Set<VerifyUserToken> verifyUserTokens = new HashSet<>();

    public void saveToken(VerifyUserToken verifyUserToken) {
        if (verifyUserToken != null) {
            if (verifyUserTokens == null) {
                verifyUserTokens = new HashSet<>();
            }
            verifyUserTokens.add(verifyUserToken);
            verifyUserToken.setUser(this);
        }
    }

}
