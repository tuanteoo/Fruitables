package com.backend.Service.Implement;

import com.backend.Configuration.Translator;
import com.backend.DTO.Request.UserRequestDTO;
import com.backend.Exception.ResourceNotFoundException;
import com.backend.Model.User;
import com.backend.Model.VerifyUserToken;
import com.backend.Repository.UserRepository;
import com.backend.Repository.VerifyUserTokenRepository;
import com.backend.Service.MailService;
import com.backend.Service.UserService;
import com.backend.Util.UserStatus;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {

    private final UserRepository userRepository;
    private final VerifyUserTokenRepository verifyUserTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Transactional
    @Override
    public String signUpUser(UserRequestDTO userRequestDTO) throws MessagingException, UnsupportedEncodingException {
        Optional<User> optionalUser = userRepository.findByEmail(userRequestDTO.getEmail());
        if (optionalUser.isEmpty()){
            User user = User.builder()
                    .email(userRequestDTO.getEmail())
                    .fullName(userRequestDTO.getFullname())
                    .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                    .dateOfBirth(userRequestDTO.getDateOfBirth())
                    .phone(userRequestDTO.getPhone())
                    .gender(userRequestDTO.getGender())
                    .type(userRequestDTO.getType())
                    .status(userRequestDTO.getStatus())
                    .build();
            userRepository.save(user);

            String token = UUID.randomUUID().toString();
            LocalDateTime expiryDate = LocalDateTime.now().plusHours(3);
            VerifyUserToken verifyUserToken = new VerifyUserToken(token, user, expiryDate);
            verifyUserTokenRepository.save(verifyUserToken);

            String confirmationUrl = "http://localhost:85/user/confirm-user?token=" + token;

            // Create context to pass data to template
            Context context = new Context();
            context.setVariable("fullName", user.getFullName());
            context.setVariable("confirmationUrl", confirmationUrl);

            // Send email verify
            mailService.sendVerificationEmail(
                    user.getEmail(), // Email Receiver
                    "Xác thực tài khoản", // Email Subject
                    "verification-email", // Template Name(file HTML in folder templates)
                    context // Context contain data
            );

            log.info("Email verification account have been sending... to {}", user.getEmail());
            return "Kiểm tra hòm thư email để xác thực tài khoản";
        }
        else {
            User existingUser  = optionalUser.get();

            if (existingUser .getStatus() == UserStatus.INACTIVE){
                String token = UUID.randomUUID().toString();
                LocalDateTime expiryDate = LocalDateTime.now().plusHours(3);
                VerifyUserToken verifyUserToken = new VerifyUserToken(token, existingUser , expiryDate);
                verifyUserTokenRepository.save(verifyUserToken);

                String confirmationUrl = "http://localhost:85/user/confirm-user?token=" + token;

                // Create context to pass data to template
                Context context = new Context();
                context.setVariable("fullName", existingUser .getFullName());
                context.setVariable("confirmationUrl", confirmationUrl);

                // Send email verify
                mailService.sendVerificationEmail(
                        existingUser .getEmail(), // Email Receiver
                        "Xác thực tài khoản", // Email Subject
                        "verification-email", // Template Name(file HTML in folder templates)
                        context // Context contain data
                );

                log.info("Email verify account have been sending... to {}", existingUser .getEmail());
                return "Kiểm tra hòm thư email để xác thực tài khoản";
            }
            else {
                return "Email này đã được sử dụng đăng ký";
            }

        }
    }

    @Override
    public boolean confirmUser(String token) {
        Optional<VerifyUserToken> verifyUserTokenOpt = verifyUserTokenRepository.findByToken(token);

        // Check if token exists ?
        if (verifyUserTokenOpt.isEmpty()) {
            log.warn("Token không hợp lệ: {}", token);
            return false;
        }

        VerifyUserToken verifyUserToken = verifyUserTokenOpt.get();

        // Check if token is still valid ?
        if (verifyUserToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            log.warn("Token đã hết hạn: {}", token);
            return false;
        }

        // Active Account
        User user = verifyUserToken.getUser();
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        // Xóa token sau khi xác thực thành công
        verifyUserTokenRepository.delete(verifyUserToken);

        log.info("Verify Account Successfully");

        return true;
    }

    @Override
    public boolean signInUser(UserRequestDTO userRequestDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(userRequestDTO.getEmail());

        if (optionalUser.isEmpty()){
            log.info("Tài khoản không tồn tại {}", userRequestDTO.getEmail());
            return false;
        }
        else {
            User user = optionalUser.get();
            return passwordEncoder.matches(userRequestDTO.getPassword(), user.getPassword());
        }
    }
}
