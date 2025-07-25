// Description: This code defines a REST controller for user authentication in a Spring Boot application.
package com.cardchess.auth.controller;

import com.cardchess.auth.entity.User;
import com.cardchess.auth.repository.UserRepository;
import com.cardchess.auth.security.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")

// Controller for user authentication
public class UserController {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    // Endpoint for user registration
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already registered.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    // Endpoint for user login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest login) {
        User user = userRepository.findByEmail(login.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}

// Data Transfer Objects for Request and Response
@Data class LoginRequest {
    private String email;
    private String password;
}

// Response object for authentication
@Data class AuthResponse {
    private final String token;
}