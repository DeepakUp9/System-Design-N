package com.stockbrokerage.finance.service;

import com.stockbrokerage.finance.model.Transaction;
import com.stockbrokerage.finance.model.TransactionType;
import com.stockbrokerage.finance.repository.TransactionRepository;
import com.stockbrokerage.user.model.Account;
import com.stockbrokerage.user.repository.AccountRepository;
import com.stockbrokerage.order.exceptions.OrderProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Service Layer: Central authority for all ledger updates (money movement).
 */
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    /**
     * Core: Creates a new transaction and updates the account balance atomically.
     * Use REQUIRES_NEW propagation to ensure the transaction persists even if the main trade rolls back.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Transaction createAndApplyTransaction(Long accountId, TransactionType type, BigDecimal netAmount, String relatedOrderId, String description) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new OrderProcessingException("Account not found for transaction."));

        // 1. Calculate new balance
        BigDecimal oldBalance = account.getBalance().setScale(2, RoundingMode.HALF_UP);
        BigDecimal newBalance = oldBalance.add(netAmount).setScale(2, RoundingMode.HALF_UP);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new OrderProcessingException("Insufficient funds. Transaction would result in negative balance.");
        }

        // 2. Create ledger entry
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType(type);
        transaction.setAmount(netAmount);
        transaction.setBalanceBefore(oldBalance);
        transaction.setBalanceAfter(newBalance);
        transaction.setRelatedOrderId(relatedOrderId);
        transaction.setDescription(description);

        transactionRepository.save(transaction);

        // 3. Update account model
        account.setBalance(newBalance);
        account.setAvailableBalance(newBalance); // Simplified for now; proper handling in RiskService
        accountRepository.save(account);

        System.out.printf("[TXN] Applied %s: %s. New Balance: %s%n", type, netAmount, newBalance);
        return transaction;
    }
}