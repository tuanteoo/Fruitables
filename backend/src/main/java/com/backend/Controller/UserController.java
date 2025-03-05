package com.backend.Controller;

import com.backend.Configuration.Translator;
import com.backend.DTO.Request.UserRequestDTO;
import com.backend.DTO.Response.ResponseData;
import com.backend.DTO.Response.ResponseError;
import com.backend.Service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
@Tag(name = "User Controller")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private static final String ERROR_MESSAGE = "errorMessage={}";

    @PostMapping("/add-user")
    public ResponseData<?> addUser(@Valid @RequestBody UserRequestDTO userRequestDTO){
        log.info("Request add user, {}", userRequestDTO.getEmail());

        try {
            String message = userService.signUpUser(userRequestDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocale("user.add.success"), message);
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add user fail");
        }
    }

    @GetMapping("/confirm-user")
    public ResponseData<?> verifyUser(@RequestParam("token") String token) {
        log.info("Request verify user user");

        try {
            boolean verify = userService.confirmUser(token);
            return new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocale("user.verify.account"), verify ? "Xác thực tài khoản thành công" : "Token không hợp lệ hoặc hết hạn");
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Verify Account Fail");
        }
    }

    @GetMapping("/signin")
    public ResponseData<?> signin(@RequestBody UserRequestDTO userRequestDTO) {
        log.info("Check account User: {}", userRequestDTO.getEmail());

        try {
            boolean signIn = userService.signInUser(userRequestDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocale("user.signin.account"), signIn);
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Sign In Fail");
        }
    }
}
