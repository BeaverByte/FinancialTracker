package com.beaverbyte.financial_tracker_application.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.beaverbyte.financial_tracker_application.exception.RoleNotFoundException;
import com.beaverbyte.financial_tracker_application.model.RoleType;
import com.beaverbyte.financial_tracker_application.model.Role;
import com.beaverbyte.financial_tracker_application.repository.RoleRepository;

@Service
public class RoleService {

	private final RoleRepository roleRepository;

	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public Set<Role> validate(Set<String> roles) {
		if (roles == null || roles.isEmpty()) {
			return Set.of(getRole(RoleType.ROLE_USER));
		}

		Set<RoleType> roleTypes = roles.stream()
				.map(this::parseRoleType)
				.collect(Collectors.toSet());

		Set<Role> foundRoles = roleRepository.findByNameIn(roleTypes);

		if (foundRoles.size() != roleTypes.size()) {
			throw new RoleNotFoundException("One or more roles do not exist.");
		}

		return foundRoles;
	}

	private RoleType parseRoleType(String role) {
		try {
			return RoleType.valueOf("ROLE_" + role.toUpperCase()); // Ensure correct prefix
		} catch (IllegalArgumentException e) {
			throw new RoleNotFoundException("Invalid role: " + role);
		}
	}

	private Role getRole(RoleType roleType) {
		return roleRepository.findByName(roleType)
				.orElseThrow(() -> new RoleNotFoundException("Error: " + roleType + " Role is not found."));
	}
}
