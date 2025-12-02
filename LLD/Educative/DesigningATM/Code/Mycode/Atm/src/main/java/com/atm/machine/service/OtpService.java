package com.atm.machine.service;

import com.atm.machine.entity.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service to manage One-Time Passwords (OTP) for enhanced transaction security.
 * Uses an in-memory store for simulation.
 */
@Service
@Slf4j
public class OtpService {

    // Key: Account ID, Value: OtpData (OTP code and expiration time)
    private final Map<Long, OtpData> otpStore = new ConcurrentHashMap<>();
    private static final int OTP_LENGTH = 6;
    private static final long OTP_VALIDITY_SECONDS = 120; // 2 minutes

    private static class OtpData {
        String code;
        Instant expiryTime;
    }

    /**
     * Generates a new OTP and stores it for the given account.
     * In a real system, this triggers an SMS/Email.
     * @param account The target account.
     */
    public String generateOtp(Account account) {
        Random random = new Random();
        String code = String.format("%0" + OTP_LENGTH + "d", random.nextInt((int) Math.pow(10, OTP_LENGTH)));
        Instant expiryTime = Instant.now().plusSeconds(OTP_VALIDITY_SECONDS);

        OtpData data = new OtpData();
        data.code = code;
        data.expiryTime = expiryTime;
        otpStore.put(account.getAccountId(), data);

        log.warn("Generated OTP for Account {}: {}. Expires in {} seconds.",
                account.getAccountId(), code, OTP_VALIDITY_SECONDS);

        // Return the code for mock client/testing purposes
        return code;
    }

    /**
     * Validates the provided OTP code against the stored value.
     * @param accountId The account ID.
     * @param otpCode The code provided by the client.
     * @return true if valid and not expired.
     */
    public boolean validateOtp(Long accountId, String otpCode) {
        OtpData storedOtp = otpStore.get(accountId);

        if (storedOtp == null) {
            log.warn("OTP validation failed for Account {}: No OTP generated.", accountId);
            return false;
        }

        if (storedOtp.expiryTime.isBefore(Instant.now())) {
            otpStore.remove(accountId); // Clear expired OTP
            log.warn("OTP validation failed for Account {}: Expired.", accountId);
            return false;
        }

        if (!storedOtp.code.equals(otpCode)) {
            log.warn("OTP validation failed for Account {}: Mismatch.", accountId);
            // In a real system, this would increment a failed attempt counter
            return false;
        }

        // OTP is valid, consume it
        otpStore.remove(accountId);
        return true;
    }
}