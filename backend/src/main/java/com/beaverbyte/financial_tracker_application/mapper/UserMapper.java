package com.beaverbyte.financial_tracker_application.mapper;

import org.mapstruct.*;

import com.beaverbyte.financial_tracker_application.entity.Role;
import com.beaverbyte.financial_tracker_application.entity.User;
import com.beaverbyte.financial_tracker_application.payload.request.SignupRequest;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // @Mapping(target = "roles", source = "role", qualifiedByName = "stringToRoleEntity")
    // User signupRequestToUserEntity(SignupRequest signupRequest);

    // @Named("stringToRoleEntity")
    // default Set<Role> stringToRoleEntity(Set<String> roles) {
    //     return roles.stream()
    //             .map(role -> {
    //                 RoleEntity roleEntity = new RoleEntity();
    //                 roleEntity.setName(role);
    //                 return roleEntity;
    //             })
    //             .collect(Collectors.toSet());
    // }
}