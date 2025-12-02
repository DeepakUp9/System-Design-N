package com.atm.machine.service;

import com.atm.machine.config.FeeConfig;
import com.atm.machine.dto.HistoryResponse;
import com.atm.machine.dto.TransactionRequest;
import com.atm.machine.dto.TransactionResponse;
import com.atm.machine.entity.*;
import com.atm.machine.exception.CardAuthenticationException;
import com.atm.machine.exception.ResourceNotFoundException;
import com.atm.machine.repository.AccountRepository;
import com.atm.machine.repository.CardRepository;
import com.atm.machine.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Service handling all critical financial operations (Withdrawal, Deposit, Transfer, Balance Inquiry).
 * Includes Transaction Limits and Multi-Currency fields.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;
    private final LimitService limitService;
    private final FeeConfig feeConfig;
    private final ForexService forexService;
    private final FraudService fraudService; // INJECTED: Fraud Service
    private final OtpService otpService;     // INJECTED: OTP Service


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TransactionResponse withdraw(TransactionRequest request) {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "ID", String.valueOf(request.getAccountId())));

        if (!account.getIsActive()) {
            throw new CardAuthenticationException("Account is currently inactive or frozen.");
        }

        // --- STEP 11: Fraud Check on Withdrawal (Low Risk generally, but still checked) ---
        int riskScore = fraudService.calculateRiskScore(account, request);
        if (fraudService.requiresOtp(riskScore)) {
            // For withdrawals, high risk typically results in a hard block or a smaller limit
            log.error("Withdrawal blocked due to high fraud risk score: {}", riskScore);
            throw new CardAuthenticationException("Transaction denied. High risk detected.");
        }
        // ----------------------------------------------------------------------------------

        // 1. Calculate Total Debit (Amount + Fee)
        BigDecimal atmFee = feeConfig.getAtmWithdrawalFee(account.getCurrencyCode());
        BigDecimal totalDebitAmount = request.getAmount().add(atmFee);

        // 2. Limit Check
        limitService.enforceAndRecordLimit(account, request, LimitType.WITHDRAWAL);

        // 3. Overdraft Check
        BigDecimal currentBalance = account.getBalance();
        BigDecimal remainingBalance = currentBalance.subtract(totalDebitAmount);

        if (remainingBalance.compareTo(feeConfig.OVERDRAFT_LINE_OF_CREDIT.negate()) < 0) {
            logFailedTransaction(account, request, TransactionType.WITHDRAWAL, "Exceeds Overdraft Credit Line.");
            throw new CardAuthenticationException(String.format("Withdrawal of %s %s exceeds available funds and credit limit.",
                    request.getAmount(), account.getCurrencyCode()));
        }

        // 4. Check for Overdraft Activation and Apply Charge
        if (remainingBalance.compareTo(BigDecimal.ZERO) < 0 && currentBalance.compareTo(BigDecimal.ZERO) >= 0) {
            remainingBalance = remainingBalance.subtract(feeConfig.OVERDRAFT_CHARGE_FLAT_FEE);

            recordTransaction(account, feeConfig.OVERDRAFT_CHARGE_FLAT_FEE.negate(), TransactionType.FEE,
                    TransactionStatus.COMPLETED, request.getAtmIdentifier(), "Overdraft Activation Fee");
        }

        // 5. Update Balance
        account.setBalance(remainingBalance);
        accountRepository.save(account);

        // 6. Record Withdrawal Transaction
        Transaction withdrawalTxn = recordTransaction(
                account,
                request.getAmount().negate(),
                TransactionType.WITHDRAWAL,
                TransactionStatus.COMPLETED,
                request.getAtmIdentifier(),
                String.format("Cash withdrawal successful. ATM Fee: %s %s", atmFee, account.getCurrencyCode())
        );

        // 7. Record ATM Fee Transaction
        recordTransaction(
                account,
                atmFee.negate(),
                TransactionType.FEE,
                TransactionStatus.COMPLETED,
                request.getAtmIdentifier(),
                "ATM Withdrawal Fee"
        );

        log.info("Withdrawal completed for Account ID {} in amount {}. Total Debit: {}. New balance: {}",
                account.getAccountId(), request.getAmount(), totalDebitAmount, remainingBalance);

        return buildResponse(withdrawalTxn, remainingBalance, "Withdrawal successful. Total charge: " + totalDebitAmount);
    }

    // Deposit and Balance Inquiry remain unchanged for brevity

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TransactionResponse deposit(TransactionRequest request) {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "ID", String.valueOf(request.getAccountId())));

        if (!account.getIsActive()) {
            throw new CardAuthenticationException("Account is currently inactive or frozen.");
        }

        BigDecimal newBalance = account.getBalance().add(request.getAmount());
        account.setBalance(newBalance);
        accountRepository.save(account);

        Transaction transaction = recordTransaction(
                account,
                request.getAmount(),
                TransactionType.DEPOSIT,
                TransactionStatus.COMPLETED,
                request.getAtmIdentifier(),
                "Cash deposit successful"
        );

        return buildResponse(transaction, newBalance, "Deposit successful.");
    }

    @Transactional(readOnly = true)
    public TransactionResponse balanceInquiry(Long accountId, String atmIdentifier) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "ID", String.valueOf(accountId)));

        Transaction transaction = recordTransaction(
                account,
                BigDecimal.ZERO,
                TransactionType.BALANCE_INQUIRY,
                TransactionStatus.COMPLETED,
                atmIdentifier,
                "Balance inquiry completed"
        );

        return buildResponse(transaction, account.getBalance(), "Balance inquiry successful.");
    }


    /**
     * Handles the transfer operation atomically, including limits, fees, FX, Fraud, and OTP.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public TransactionResponse transfer(TransactionRequest request) {
        if (request.getReferenceAccount() == null || request.getReferenceAccount().isBlank()) {
            throw new CardAuthenticationException("Destination account number is required for transfer.");
        }

        Account sourceAccount = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Source Account", "ID", String.valueOf(request.getAccountId())));

        Account destAccount = accountRepository.findByAccountNumber(request.getReferenceAccount())
                .orElseThrow(() -> new ResourceNotFoundException("Destination Account", "Number", request.getReferenceAccount()));

        if (sourceAccount.getAccountId().equals(destAccount.getAccountId())) {
            logFailedTransaction(sourceAccount, request, TransactionType.TRANSFER, "Source and destination accounts are the same.");
            throw new CardAuthenticationException("Cannot transfer money to the same account.");
        }

        // --- STEP 11: FRAUD AND OTP CHECK ---
        int riskScore = fraudService.calculateRiskScore(sourceAccount, request);

        if (fraudService.requiresOtp(riskScore)) {
            // If OTP is required but not provided or invalid
            if (request.getOtpCode() == null || request.getOtpCode().isBlank()) {
                // Trigger OTP generation and tell the client to resend with OTP
                otpService.generateOtp(sourceAccount);
                throw new CardAuthenticationException("High risk transfer detected (Score: " + riskScore + "). OTP required. Check your device and resend request with 'otpCode'.");
            }

            // Validate the provided OTP
            if (!otpService.validateOtp(sourceAccount.getAccountId(), request.getOtpCode())) {
                logFailedTransaction(sourceAccount, request, TransactionType.TRANSFER, "Invalid or expired OTP.");
                throw new CardAuthenticationException("Invalid or expired OTP code.");
            }
            // If OTP is successful, the transaction proceeds.
            log.info("OTP validated successfully for high-risk transfer.");
        }
        // ----------------------------------------------------------------------------------

        // 1. Calculate Fees and FX
        BigDecimal principalAmount = request.getAmount().setScale(2, RoundingMode.HALF_UP);
        String sourceCurrency = sourceAccount.getCurrencyCode();
        String destCurrency = destAccount.getCurrencyCode();

        BigDecimal transferFee = principalAmount.multiply(feeConfig.TRANSFER_PERCENTAGE_FEE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal fxFeeInSource = BigDecimal.ZERO;

        BigDecimal amountToDebit = principalAmount.add(transferFee);
        BigDecimal amountToCredit;

        // 2. Handle Cross-Currency (International) Transfer
        if (!sourceCurrency.equals(destCurrency)) {
            amountToCredit = forexService.convert(sourceCurrency, destCurrency, principalAmount);
            fxFeeInSource = forexService.calculateFeeInSource(sourceCurrency, destCurrency, principalAmount);
            amountToDebit = principalAmount.add(transferFee).add(fxFeeInSource).setScale(2, RoundingMode.HALF_UP);
        } else {
            amountToCredit = principalAmount.setScale(2, RoundingMode.HALF_UP);
        }

        // 3. Limit Check
        limitService.enforceAndRecordLimit(sourceAccount, request, LimitType.TRANSFER);

        // 4. Final Balance Check (Source Account)
        if (sourceAccount.getBalance().compareTo(amountToDebit) < 0) {
            logFailedTransaction(sourceAccount, request, TransactionType.TRANSFER, "Insufficient funds for transfer plus fees.");
            throw new CardAuthenticationException("Insufficient funds for transfer and associated fees.");
        }

        // 5. Execute Debit (Source Account: Principal + Transfer Fee + FX Fee)
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amountToDebit));
        accountRepository.save(sourceAccount);

        // 6. Execute Credit (Destination Account: Principal Net of FX Margin)
        destAccount.setBalance(destAccount.getBalance().add(amountToCredit));
        accountRepository.save(destAccount);

        // 7. Record Source Transaction (Debit) - Principal + Fees
        String sourceMessage = String.format("Transfer DEBIT to account %s. Fees: Transfer %s %s, FX %s %s. Credited %s %s.",
                destAccount.getAccountNumber(), transferFee, sourceCurrency, fxFeeInSource, sourceCurrency, amountToCredit, destCurrency);

        Transaction sourceTxn = recordTransaction(
                sourceAccount,
                amountToDebit.negate(),
                TransactionType.TRANSFER,
                TransactionStatus.COMPLETED,
                request.getAtmIdentifier(),
                sourceMessage
        );

        // 8. Record Fee Transactions (Transfer and FX)
        if (transferFee.compareTo(BigDecimal.ZERO) > 0) {
            recordTransaction(sourceAccount, transferFee.negate(), TransactionType.FEE, TransactionStatus.COMPLETED, request.getAtmIdentifier(), "Transfer Percentage Fee");
        }
        if (fxFeeInSource.compareTo(BigDecimal.ZERO) > 0) {
            recordTransaction(sourceAccount, fxFeeInSource.negate(), TransactionType.FEE, TransactionStatus.COMPLETED, request.getAtmIdentifier(), "Foreign Exchange Conversion Fee");
        }

        // 9. Record Destination Transaction (Credit)
        recordTransaction(
                destAccount,
                amountToCredit,
                TransactionType.TRANSFER,
                TransactionStatus.COMPLETED,
                request.getAtmIdentifier(),
                "Transfer CREDIT from account " + sourceAccount.getAccountNumber()
        );

        // Final Response
        String responseMsg = String.format("Transfer successful. Amount credited to %s: %s %s. Total debit from your account: %s %s.",
                destAccount.getAccountNumber(), amountToCredit, destCurrency, amountToDebit, sourceCurrency);

        return buildResponse(sourceTxn, sourceAccount.getBalance(), responseMsg);
    }

    // ... [Utility methods (mapToHistoryResponse, recordTransaction, logFailedTransaction, buildResponse) remain the same] ...

    @Transactional(readOnly = true)
    public List<HistoryResponse> getTransactionHistory(Long accountId, int page, int size) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "ID", String.valueOf(accountId)));

        page = Math.max(0, page);
        size = Math.min(Math.max(1, size), 50);

        Pageable pageable = PageRequest.of(page, size);

        List<Transaction> transactions = transactionRepository.findByAccountOrderByTransactionTimestampDesc(account, pageable);

        return transactions.stream()
                .map(this::mapToHistoryResponse)
                .collect(Collectors.toList());
    }

    private HistoryResponse mapToHistoryResponse(Transaction transaction) {
        boolean isDebit = transaction.getTransactionType() == TransactionType.WITHDRAWAL ||
                transaction.getTransactionType() == TransactionType.FEE ||
                (transaction.getTransactionType() == TransactionType.TRANSFER && transaction.getAmount().compareTo(BigDecimal.ZERO) > 0);

        return HistoryResponse.builder()
                .transactionId(transaction.getTransactionId())
                .type(transaction.getTransactionType().name())
                .status(transaction.getTransactionStatus().name())
                .amount(transaction.getAmount())
                .currencyCode(transaction.getAccount().getCurrencyCode())
                .timestamp(transaction.getTransactionTimestamp())
                .reference(transaction.getReferenceAccount() != null ?
                        transaction.getReferenceAccount() :
                        (transaction.getAtmIdentifier() != null ? "ATM: " + transaction.getAtmIdentifier() : "N/A"))
                .isDebit(isDebit)
                .build();
    }


    private Transaction recordTransaction(Account account, BigDecimal amountChange, TransactionType type,
                                          TransactionStatus status, String atmIdentifier, String message) {

        Card card = cardRepository.findByAccountId(account.getAccountId())
                .orElse(null);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setCard(card);
        transaction.setAmount(amountChange.abs());
        transaction.setTransactionType(type);
        transaction.setTransactionStatus(status);
        transaction.setAtmIdentifier(atmIdentifier);

        if (type == TransactionType.TRANSFER && message.contains("account")) {
            String parts[] = message.split(" ");
            String accountNumber = parts[parts.length - 1].replace(".", "");
            transaction.setReferenceAccount(accountNumber);
        } else if (type == TransactionType.FEE) {
            transaction.setReferenceAccount(message);
        }

        transaction.setTransactionTimestamp(ZonedDateTime.now());

        return transactionRepository.save(transaction);
    }

    private void logFailedTransaction(Account account, TransactionRequest request, TransactionType type, String message) {
        recordTransaction(
                account,
                request.getAmount().abs(),
                type,
                TransactionStatus.FAILED,
                request.getAtmIdentifier(),
                message
        );
        log.warn("Transaction FAILED for Account ID {}. Type: {}. Reason: {}",
                account.getAccountId(), type.name(), message);
    }

    private TransactionResponse buildResponse(Transaction transaction, BigDecimal newBalance, String message) {
        return TransactionResponse.builder()
                .transactionId(transaction.getTransactionId())
                .transactionType(transaction.getTransactionType())
                .currencyCode(transaction.getAccount().getCurrencyCode())
                .amount(transaction.getAmount())
                .status(transaction.getTransactionStatus().name())
                .newBalance(newBalance)
                .timestamp(transaction.getTransactionTimestamp())
                .message(message)
                .build();
    }
}