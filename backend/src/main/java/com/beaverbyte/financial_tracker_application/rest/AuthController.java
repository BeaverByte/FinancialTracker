package com.beaverbyte.financial_tracker_application.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beaverbyte.financial_tracker_application.entity.ERole;
import com.beaverbyte.financial_tracker_application.entity.Role;
import com.beaverbyte.financial_tracker_application.entity.User;
import com.beaverbyte.financial_tracker_application.payload.request.LoginRequest;
import com.beaverbyte.financial_tracker_application.payload.request.SignupRequest;
import com.beaverbyte.financial_tracker_application.payload.response.JwtResponse;
import com.beaverbyte.financial_tracker_application.payload.response.MessageResponse;
import com.beaverbyte.financial_tracker_application.repository.RoleRepository;
import com.beaverbyte.financial_tracker_application.repository.UserRepository;
import com.beaverbyte.financial_tracker_application.security.UserDetailsImpl;
import com.beaverbyte.financial_tracker_application.security.jwt.JwtUtils;
import com.beaverbyte.financial_tracker_application.service.RoleService;
import com.beaverbyte.financial_tracker_application.service.UserService;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserService userService;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  RoleService roleService;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  /**
   * App consumers sign in and have their request authenticated
   * 
   * @param loginRequest
   * @return
   */
  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = jwtUtils.generateJwtToken(authentication);
    
    // User returned back
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt, 
                         userDetails.getId(), 
                         userDetails.getUsername(), 
                         userDetails.getEmail(), 
                         roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userService.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userService.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }
    // Create and validate user
    User user = new User(signUpRequest.getUsername(), 
               signUpRequest.getEmail(),
               encoder.encode(signUpRequest.getPassword()));

    Set<Role> roles = roleService.validateAgainstTable(signUpRequest.getRole());

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
}