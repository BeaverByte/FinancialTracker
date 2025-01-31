package com.beaverbyte.financial_tracker_application.mapper;

import org.mapstruct.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
	default List<String> mapAuthoritiesToRoles(Collection<? extends GrantedAuthority> authorities) {
		return authorities.stream()
				.map(GrantedAuthority::getAuthority)
				.toList();
	}
}