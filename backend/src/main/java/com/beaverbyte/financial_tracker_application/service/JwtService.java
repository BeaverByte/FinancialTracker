package com.beaverbyte.financial_tracker_application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.beaverbyte.financial_tracker_application.dto.api.response.JwtResponse;
import com.beaverbyte.financial_tracker_application.mapper.JwtResponseMapper;
import com.beaverbyte.financial_tracker_application.security.UserDetailsImpl;

@Service
public class JwtService {
    private final JwtResponseMapper jwtResponseMapper;

    public JwtService(JwtResponseMapper jwtResponseMapper) {
        this.jwtResponseMapper = jwtResponseMapper;
    }

    public JwtResponse createJwtResponse(String jwt, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

        // Use the mapper to create JwtResponse
        return jwtResponseMapper.toJwtResponse(jwt, userDetails, roles);
    }
}
