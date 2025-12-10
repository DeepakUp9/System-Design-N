package com.stackclonell.stackclone.web;

import com.stackclonell.stackclone.core.model.User;
import com.stackclonell.stackclone.repository.UserRepository;
import com.stackclonell.stackclone.security.SecurityConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * API layer for user authentication (registration and login).
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Endpoint for user registration
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            // Edge Case: Username already taken
            return new ResponseEntity<>("Username already exists.", HttpStatus.BAD_REQUEST);
        }

        // Security: Hash the password before saving
        String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(hashedPassword);

        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully.", HttpStatus.CREATED);
    }

    // Production Note: The login endpoint would be handled by Spring Security's default filters
    // or a custom JWT controller which generates a token upon successful authentication.
}