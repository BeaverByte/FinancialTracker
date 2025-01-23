package com.beaverbyte.financial_tracker_application.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.beaverbyte.financial_tracker_application.exception.RoleNotFoundException;
import com.beaverbyte.financial_tracker_application.mapper.RoleMapper;
import com.beaverbyte.financial_tracker_application.model.RoleType;
import com.beaverbyte.financial_tracker_application.model.Role;
import com.beaverbyte.financial_tracker_application.repository.RoleRepository;
import com.beaverbyte.financial_tracker_application.security.CustomUserDetails;

@Service
public class RoleService {

	private final RoleRepository roleRepository;
	private final RoleMapper roleMapper;

	public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
		this.roleRepository = roleRepository;
		this.roleMapper = roleMapper;
	}

	public List<String> extractRoles(Authentication authentication) {
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		return roleMapper.mapAuthoritiesToRoles(userDetails.getAuthorities());
	}

	public Set<Role> validateAgainstTable(Set<String> roles) {
		if (roles == null || roles.isEmpty()) {
			return Set.of(defaultUser());
		}

		return roles.stream()
				.map(this::validateRoleByName)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}

	private Role defaultUser() {
		return getRole(RoleType.ROLE_USER);
	}

	public static final String EXPECTED_INPUT_ADMIN = "admin";
	public static final String EXPECTED_INPUT_MOD = "mod";

	private Role validateRoleByName(String role) {
		switch (role.toLowerCase()) {
			case EXPECTED_INPUT_ADMIN:
				return getRole(RoleType.ROLE_ADMIN);
			case EXPECTED_INPUT_MOD:
				return getRole(RoleType.ROLE_MODERATOR);
			default:
				return defaultUser();
		}
	}

	private Role getRole(RoleType roleType) {
		return roleRepository.findByName(roleType)
				.orElseThrow(() -> new RoleNotFoundException("Error: " + roleType + " Role is not found."));
	}
}
