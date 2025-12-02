package com.atm.machine.controller;

import com.atm.machine.dto.HistoryResponse;
import com.atm.machine.dto.TransactionRequest;
import com.atm.machine.dto.TransactionResponse;
import com.atm.machine.service.FinancialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for financial transactions (Withdrawal, Deposit, Inquiry, Transfer, History).
 * Assumes the client has already authenticated via the AuthController and holds a valid session (accountId).
 */
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final FinancialService financialService;

    /**
     * Endpoint for cash withdrawal.
     */
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = financialService.withdraw(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Endpoint for cash deposit.
     */
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = financialService.deposit(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Endpoint for balance inquiry.
     */
    @GetMapping("/balance/{accountId}")
    public ResponseEntity<TransactionResponse> balanceInquiry(
            @PathVariable Long accountId,
            @RequestParam String atmIdentifier) {

        TransactionResponse response = financialService.balanceInquiry(accountId, atmIdentifier);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Endpoint for transferring funds to another account.
     * This requires a destination account number in the TransactionRequest.
     */
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransactionRequest request) {
        // The service handles fetching and locking both source and destination accounts.
        TransactionResponse response = financialService.transfer(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve the transaction history for an account.
     * Implements pagination (page and size) which is crucial for high-volume systems.
     * @param accountId The ID of the authenticated account.
     * @param page The page number (default 0).
     * @param size The number of records per page (default 10).
     * @return List of transaction history entries.
     */
    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<HistoryResponse>> getHistory(
            @PathVariable Long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<HistoryResponse> history = financialService.getTransactionHistory(accountId, page, size);
        return new ResponseEntity<>(history, HttpStatus.OK);
    }
}