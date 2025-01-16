package com.beaverbyte.financial_tracker_application.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.beaverbyte.financial_tracker_application.mapper.AuthenticationMapper;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthenticationService {

    private final AuthenticationMapper authenticationMapper;

    public AuthenticationService(AuthenticationMapper authenticationMapper) {
        this.authenticationMapper = authenticationMapper;
    }

    public UsernamePasswordAuthenticationToken createAuthenticationToken(UserDetails userDetails, HttpServletRequest request) {
        return authenticationMapper.toAuthenticationToken(userDetails, request);
    }
}