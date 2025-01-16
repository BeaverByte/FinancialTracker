package com.beaverbyte.financial_tracker_application.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.beaverbyte.financial_tracker_application.dto.api.request.SignupRequest;
import com.beaverbyte.financial_tracker_application.entity.User;

public class UserMapper {
    public static User toEntity(SignupRequest signUpRequest, PasswordEncoder encoder) {
        return new User(
            signUpRequest.getUsername(),
            signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword())
        );
    }
}