package com.atm.machine.utility;

import com.atm.machine.entity.*;
import com.atm.machine.repository.AccountRepository;
import com.atm.machine.repository.CardRepository;
import com.atm.machine.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Utility class to load initial secure data into the database upon application startup.
 * Implements CommandLineRunner to execute code after the Spring context is fully loaded.
 */
@Profile("!prod") // Only run in non-production environments
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final PasswordEncoder passwordEncoder;

    // Defined Test Variables (Account 1 - USD - Verified)
    private static final String TEST_CARD_NUMBER_USD = "1234567890123456";
    private static final String TEST_PIN = "1234";
    private static final String TEST_ACCOUNT_NUMBER_USD = "9876543210";
    private static final BigDecimal INITIAL_BALANCE_USD = new BigDecimal("5000.75");
    private static final String CURRENCY_USD = "USD";

    // Defined Test Variables (Account 2 - EUR - Pending KYC)
    private static final String TEST_ACCOUNT_NUMBER_EUR = "1122334455667788";
    private static final BigDecimal INITIAL_BALANCE_EUR = new BigDecimal("1000.00");
    private static final String CURRENCY_EUR = "EUR";

    @Override
    public void run(String... args) throws Exception {
        log.info("--- Starting DataLoader: Creating initial test data ---");

        if (cardRepository.findByCardNumber(TEST_CARD_NUMBER_USD).isPresent()) {
            log.warn("Test data already loaded. Skipping initialization.");
            return;
        }

        // --- CUSTOMER 1: Alice (VERIFIED KYC) ---
        Customer customer1 = new Customer(
                null,
                "Alice",
                "Smith",
                "alice.smith@example.com",
                "555-0101",
                "123 Main St, Anytown",
                // KYC FIELDS
                LocalDate.of(1990, 5, 15),
                DocumentType.PASSPORT,
                "A12345678",
                KycStatus.VERIFIED, // ALLOWS LOGIN
                // END KYC FIELDS
                ZonedDateTime.now(),
                null,
                null
        );
        customer1 = customerRepository.save(customer1);

        Account account1 = new Account(
                null,
                customer1,
                TEST_ACCOUNT_NUMBER_USD,
                AccountType.CHECKING,
                CURRENCY_USD,
                INITIAL_BALANCE_USD,
                true,
                ZonedDateTime.now()
        );
        account1 = accountRepository.save(account1);

        String pinHash = passwordEncoder.encode(TEST_PIN);
        Card card1 = new Card(
                null,
                customer1,
                account1,
                TEST_CARD_NUMBER_USD,
                pinHash,
                LocalDate.now().plusYears(4),
                passwordEncoder.encode(UUID.randomUUID().toString().substring(0, 3)),
                CardStatus.ACTIVE,
                0,
                3,
                ZonedDateTime.now()
        );
        cardRepository.save(card1);

        log.info("Created VERIFIED USD Account: ID {}, Number {}", account1.getAccountId(), TEST_ACCOUNT_NUMBER_USD);

        // --- CUSTOMER 2: Bob (PENDING KYC - Should be blocked on login) ---
        Customer customer2 = new Customer(
                null,
                "Bob",
                "Jones",
                "bob.jones@example.com",
                "555-0102",
                "456 Other Rd, Anytown",
                // KYC FIELDS
                LocalDate.of(1985, 1, 20),
                DocumentType.DRIVERS_LICENSE,
                "B98765432",
                KycStatus.PENDING, // BLOCKS LOGIN
                // END KYC FIELDS
                ZonedDateTime.now(),
                null,
                null
        );
        customer2 = customerRepository.save(customer2);

        Account account2 = new Account(
                null,
                customer2,
                TEST_ACCOUNT_NUMBER_EUR,
                AccountType.SAVINGS,
                CURRENCY_EUR,
                INITIAL_BALANCE_EUR,
                true,
                ZonedDateTime.now()
        );
        account2 = accountRepository.save(account2);

        // Add a second card/PIN for testing failed KYC block
        Card card2 = new Card(
                null,
                customer2,
                account2,
                "9999999999999999",
                pinHash,
                LocalDate.now().plusYears(4),
                passwordEncoder.encode(UUID.randomUUID().toString().substring(0, 3)),
                CardStatus.ACTIVE,
                0,
                3,
                ZonedDateTime.now()
        );
        cardRepository.save(card2);


        log.info("Created PENDING KYC EUR Account: ID {}, Number {}. Card: 9999...", account2.getAccountId(), TEST_ACCOUNT_NUMBER_EUR);

        log.info("--- DataLoader completed successfully ---");
    }
}