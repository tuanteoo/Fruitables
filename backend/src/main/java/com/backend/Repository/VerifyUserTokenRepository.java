package com.backend.Repository;

import com.backend.Model.User;
import com.backend.Model.VerifyUserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerifyUserTokenRepository extends JpaRepository<VerifyUserToken, Long> {
    Optional<VerifyUserToken> findByToken(String token);
    Optional<VerifyUserToken> findByUser(User user);
}
