package com.beaverbyte.financial_tracker_application.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beaverbyte.financial_tracker_application.entity.ERole;
import com.beaverbyte.financial_tracker_application.entity.Role;
import com.beaverbyte.financial_tracker_application.repository.RoleRepository;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    // @Override
    // @Transactional
    // public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    //     User user = userRepository.findByUsername(username)
    //         .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    //     return UserDetailsImpl.build(user);
    // }

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
