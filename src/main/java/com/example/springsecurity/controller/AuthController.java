package com.example.springsecurity.controller;

import com.example.springsecurity.dto.SignUpRequest;
import com.example.springsecurity.entity.ERole;
import com.example.springsecurity.entity.Role;
import com.example.springsecurity.entity.User;
import com.example.springsecurity.repository.RoleRepository;
import com.example.springsecurity.repository.UserRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthController(UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/signup")
  public ResponseEntity<String> signup2(@RequestBody SignUpRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username is already taken");
    }
    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email is already taken");
    }
    String hashedPassword = passwordEncoder.encode(signUpRequest.getPassword());
    Set<Role> roles = new HashSet<>();
    Optional<Role> userRole = roleRepository.findByName(ERole.ROLE_USER);
    if (userRole.isEmpty()) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("role not found");
    }
    roles.add(userRole.get());
    User user = new User();
    user.setUsername(signUpRequest.getUsername());
    user.setEmail(signUpRequest.getEmail());
    user.setPassword(hashedPassword);
    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok("User registered success");
  }
}
