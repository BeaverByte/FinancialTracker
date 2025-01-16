package com.beaverbyte.financial_tracker_application.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.beaverbyte.financial_tracker_application.dto.api.response.JwtResponse;
import com.beaverbyte.financial_tracker_application.security.UserDetailsImpl;

@Mapper(componentModel = "spring")
public interface JwtResponseMapper {

    // JwtResponseMapper INSTANCE = Mappers.getMapper(JwtResponseMapper.class);

    @Mapping(source = "jwt", target = "accessToken")
    @Mapping(source = "userDetails.id", target = "id")
    @Mapping(source = "userDetails.username", target = "username")
    @Mapping(source = "userDetails.email", target = "email")
    @Mapping(source = "roles", target = "roles")

    JwtResponse toJwtResponse(String jwt, UserDetailsImpl userDetails, List<String> roles);
}