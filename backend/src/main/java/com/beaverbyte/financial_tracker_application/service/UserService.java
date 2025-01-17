package com.beaverbyte.financial_tracker_application.service;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.beaverbyte.financial_tracker_application.dto.api.request.SignupRequest;
import com.beaverbyte.financial_tracker_application.entity.Role;
import com.beaverbyte.financial_tracker_application.entity.User;
import com.beaverbyte.financial_tracker_application.mapper.UserMapper;
import com.beaverbyte.financial_tracker_application.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder encoder;

    public UserService(RoleService roleService, UserRepository userRepository, PasswordEncoder encoder) {
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User createUser(SignupRequest signUpRequest) {
        User user = UserMapper.toUser(signUpRequest);
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        Set<Role> roles = roleService.validateAgainstTable(signUpRequest.getRole());
        user.setRoles(roles);
        return user; 
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
