package com.beaverbyte.financial_tracker_application.mapper;

import java.util.Map;

import com.beaverbyte.financial_tracker_application.dto.api.request.SignupRequest;

public class SignupRequestMapper {

    public static Map<String, Object> toMap(SignupRequest signupRequest) {
        return Map.of(
                "username", signupRequest.getUsername(),
                "email", signupRequest.getEmail(),
                "password", signupRequest.getPassword(),
                "role", signupRequest.getRole()
        );
    }
}