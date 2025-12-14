package com.stockbrokerage.user.api;

import com.stockbrokerage.user.model.User;
import com.stockbrokerage.user.service.UserService;
import com.stockbrokerage.user.dto.UserRegistrationRequest;
import com.stockbrokerage.order.exceptions.OrderProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        try {
            User newUser = userService.registerNewUser(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully. Account created with ID: " + newUser.getAccounts().get(0).getAccountNumber());
        } catch (OrderProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint for testing authentication (requires Basic Auth header)
    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser() {
        // In production, you would retrieve the username from the SecurityContext
        return ResponseEntity.ok("Successfully authenticated user.");
    }
}