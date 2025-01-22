package com.beaverbyte.financial_tracker_application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.beaverbyte.financial_tracker_application.dto.response.UserInfoResponse;
import com.beaverbyte.financial_tracker_application.security.CustomUserDetails;

@Service
public class UserInfoResponseService {

    public UserInfoResponse createUserInfoResponse(CustomUserDetails userDetails) {
        List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .toList();

        return new UserInfoResponse(
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles
        );
    }
}
