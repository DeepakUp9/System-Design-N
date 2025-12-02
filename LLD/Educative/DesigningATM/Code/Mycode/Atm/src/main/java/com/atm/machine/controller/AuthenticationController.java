package com.atm.machine.controller;

import com.atm.machine.dto.AuthRequest;
import com.atm.machine.dto.AuthResponse;
import com.atm.machine.entity.Account;
import com.atm.machine.entity.Card;
import com.atm.machine.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for the initial card authentication process.
 * This simulates the card swipe/insertion and PIN entry.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    /**
     * Endpoint to authenticate a card and PIN.
     * @param request DTO containing cardNumber and pin.
     * @return AuthResponse DTO with account details upon success.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {

        // 1. Authenticate the card (handles all validation, blocking, and existence checks)
        Card authenticatedCard = authService.authenticateCard(
                request.getCardNumber(),
                request.getPin()
        );

        // 2. Fetch linked Account and Customer data
        Account account = authenticatedCard.getAccount();

        // 3. Map to AuthResponse DTO
        AuthResponse response = AuthResponse.builder()
                .isAuthenticated(true)
                .accountId(account.getAccountId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType().name())
                .currentBalance(account.getBalance()) // Fetching the balance is safe here, as it's part of the session
                .customerName(authenticatedCard.getCustomer().getFirstName() + " " +
                        authenticatedCard.getCustomer().getLastName())
                .build();

        // HTTP 200 OK with the session data
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Future steps will require a /logout endpoint to simulate session termination,
    // but for now, successful login is the focus.
}