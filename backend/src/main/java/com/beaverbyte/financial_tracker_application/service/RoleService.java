package com.beaverbyte.financial_tracker_application.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	// public Set<Role> validateAgainstTable(Set<String> roles) {
	// Set<Role> newRoles = new HashSet<>();

	// if (roles == null) {
	// // No roles entered so default just User
	// Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
	// .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
	// newRoles.add(userRole);
	// } else {
	// roles.forEach(role -> {
	// switch (role) {
	// case "admin":
	// Role adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN)
	// .orElseThrow(() -> new RuntimeException("Error: Admin Role is not found."));
	// newRoles.add(adminRole);

	// break;
	// case "mod":
	// Role modRole = roleRepository.findByName(RoleType.ROLE_MODERATOR)
	// .orElseThrow(() -> new RuntimeException("Error: Mod Role is not found."));
	// newRoles.add(modRole);

	// break;
	// default:
	// Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
	// .orElseThrow(() -> new RuntimeException("Error: User Role is not found."));
	// newRoles.add(userRole);
	// }
	// });
	// }

	// return newRoles;
	// }

	public Set<Role> validateAgainstTable(Set<String> roles) {
		Set<Role> newRoles = new HashSet<>();

		if (roles == null || roles.isEmpty()) {
			// Default to User if no roles are provided
			newRoles.add(getRole(RoleType.ROLE_USER));
		} else {
			roles.forEach(role -> {
				Role roleEntity = getRoleEntityByName(role);
				if (roleEntity != null) {
					newRoles.add(roleEntity);
				}
			});
		}

		return newRoles;
	}

	private Role getRoleEntityByName(String role) {
		switch (role.toLowerCase()) {
			case "admin":
				return getRole(RoleType.ROLE_ADMIN);
			case "mod":
				return getRole(RoleType.ROLE_MODERATOR);
			default:
				return getRole(RoleType.ROLE_USER); // Default to USER if unrecognized role
		}
	}

	private Role getRole(RoleType roleType) {
		return roleRepository.findByName(roleType)
				.orElseThrow(() -> new RoleNotFoundException("Error: " + roleType + " Role is not found."));
	}
}
