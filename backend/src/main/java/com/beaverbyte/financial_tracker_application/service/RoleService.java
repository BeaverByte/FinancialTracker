package com.beaverbyte.financial_tracker_application.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.beaverbyte.financial_tracker_application.entity.ERole;
import com.beaverbyte.financial_tracker_application.entity.Role;
import com.beaverbyte.financial_tracker_application.mapper.RoleMapper;
import com.beaverbyte.financial_tracker_application.repository.RoleRepository;
import com.beaverbyte.financial_tracker_application.security.UserDetailsImpl;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RoleMapper roleMapper;

    public RoleService(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public List<String> extractRoles(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return roleMapper.mapAuthoritiesToRoles(userDetails.getAuthorities());
    }

    public Set<Role> validateAgainstTable(Set<String> roles) {
        Set<Role> newRoles = new HashSet<>();

        if (roles == null) {
            // No roles entered so default just User
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            newRoles.add(userRole);
        } else {
            roles.forEach(role -> {
                switch (role) {
                case "admin":
                Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Admin Role is not found."));
                newRoles.add(adminRole);

                break;
                case "mod":
                Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                    .orElseThrow(() -> new RuntimeException("Error: Mod Role is not found."));
                newRoles.add(modRole);

                break;
                default:
                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: User Role is not found."));
                newRoles.add(userRole);
                }
            });
        }
        
        return newRoles;
    } 

    
}
