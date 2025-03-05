package com.backend.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "VerifyUserToken")
@Table(name = "tbl_verifyusertoken")
public class VerifyUserToken extends AbstractModel<Long> {

    @Column(name = "token",nullable = false)
    private String token;

    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(name = "expiry_date",nullable = false)
    private LocalDateTime expiryDate;

}
