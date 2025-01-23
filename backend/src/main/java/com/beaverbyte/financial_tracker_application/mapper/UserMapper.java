package com.beaverbyte.financial_tracker_application.mapper;

import com.beaverbyte.financial_tracker_application.dto.request.SignupRequest;
import com.beaverbyte.financial_tracker_application.model.User;

public class UserMapper {
	private UserMapper() {
	}

    public static User toUser(SignupRequest signUpRequest) {
        return new User(
            signUpRequest.getUsername(),
            signUpRequest.getEmail()
        );
    }
}