package com.backend.Service;

import com.backend.DTO.Request.UserRequestDTO;
import com.backend.DTO.Response.UserDetailResponse;
import com.backend.Model.User;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface UserService {

    String signUpUser(UserRequestDTO userRequestDTO) throws MessagingException, UnsupportedEncodingException;

    boolean confirmUser(String token);

    boolean signInUser(UserRequestDTO userRequestDTO);
}
