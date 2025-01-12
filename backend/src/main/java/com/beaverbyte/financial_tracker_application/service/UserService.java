package com.beaverbyte.financial_tracker_application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beaverbyte.financial_tracker_application.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
