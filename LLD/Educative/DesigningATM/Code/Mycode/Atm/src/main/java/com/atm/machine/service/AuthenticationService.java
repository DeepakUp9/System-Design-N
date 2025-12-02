package com.atm.machine.service;

import com.atm.machine.entity.Card;
import com.atm.machine.entity.CardStatus;
import com.atm.machine.exception.CardAuthenticationException;
import com.atm.machine.exception.ResourceNotFoundException;
import com.atm.machine.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Service responsible for all card and PIN authentication logic.
 * This service handles security-critical tasks and edge cases like card status and PIN lockout.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final CardRepository cardRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Main authentication method.
     * Edge Cases Covered: Card existence, status, expiration, and PIN failure count.
     *
     * @param cardNumber The card number provided by the user.
     * @param pin The plaintext PIN provided by the user.
     * @return The authenticated Card object.
     * @throws CardAuthenticationException If the card is invalid, blocked, expired, or the PIN is incorrect.
     */
    @Transactional
    public Card authenticateCard(String cardNumber, String pin) {
        log.info("Attempting authentication for card: {}", cardNumber);

        // 1. Retrieve Card
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "number", cardNumber));

        // 2. Check Card Status (Edge Case)
        if (card.getStatus() != CardStatus.ACTIVE) {
            log.warn("Card {} is not active. Current status: {}", cardNumber, card.getStatus());
            throw new CardAuthenticationException("Card is currently " + card.getStatus().name() + ".");
        }

        // 3. Check Expiry Date (Edge Case)
        if (card.getExpiryDate().isBefore(LocalDate.now())) {
            // Note: In a full system, you would update the status to EXPIRED here.
            card.setStatus(CardStatus.EXPIRED);
            cardRepository.save(card);
            log.warn("Card {} is expired. Expiry date: {}", cardNumber, card.getExpiryDate());
            throw new CardAuthenticationException("Card has expired.");
        }

        // 4. Validate PIN
        if (passwordEncoder.matches(pin, card.getPinHash())) {
            // PIN is correct: Reset the failure count (CRITICAL)
            if (card.getPinFailCount() > 0) {
                card.setPinFailCount(0);
                cardRepository.save(card);
                log.info("Authentication successful. PIN failure count reset for card {}.", cardNumber);
            }
            return card;
        } else {
            // PIN is incorrect: Increment the failure count
            card.setPinFailCount(card.getPinFailCount() + 1);

            log.warn("Authentication failed for card {}. Attempts left: {}",
                    cardNumber, (card.getMaxPinFailCount() - card.getPinFailCount()));

            // 5. Check for Lockout (CRITICAL Edge Case)
            if (card.getPinFailCount() >= card.getMaxPinFailCount()) {
                card.setStatus(CardStatus.BLOCKED);
                log.error("Card {} BLOCKED due to too many failed PIN attempts.", cardNumber);
                cardRepository.save(card);
                throw new CardAuthenticationException("Too many failed attempts. Card has been blocked.");
            }

            cardRepository.save(card);
            throw new CardAuthenticationException("Invalid PIN.");
        }
    }
}