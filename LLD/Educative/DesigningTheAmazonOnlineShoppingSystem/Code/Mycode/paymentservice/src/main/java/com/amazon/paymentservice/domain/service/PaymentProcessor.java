package com.amazon.paymentservice.domain.service;

import com.amazon.paymentservice.domain.model.PaymentRequest;
import com.amazon.paymentservice.domain.model.PaymentTransaction;
import com.amazon.paymentservice.domain.repository.TransactionRepository;
import com.amazon.paymentservice.domain.strategy.PaymentStrategy;
import com.amazon.paymentservice.domain.strategy.PaymentStrategyFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// The Context in the Strategy Pattern
@Service
public class PaymentProcessor {

    private final PaymentStrategyFactory strategyFactory;
    private final TransactionRepository transactionRepository;

    // Spring DI for Factory and Repository
    public PaymentProcessor(
            PaymentStrategyFactory strategyFactory,
            TransactionRepository transactionRepository) {
        this.strategyFactory = strategyFactory;
        this.transactionRepository = transactionRepository;
    }

    /**
     * LLD Core Method: Executes payment by selecting and using the correct strategy.
     */
    @Transactional
    public PaymentTransaction executePayment(PaymentRequest request) {
        // 1. Strategy Selection (Decoupled using the Factory)
        PaymentStrategy strategy = strategyFactory.getStrategy(request.getPaymentMethod());

        // 2. Strategy Execution (The Context delegates the work)
        PaymentTransaction transaction = strategy.processPayment(request);

        // 3. Persistence (The Context handles the storage of the result)
        PaymentTransaction savedTransaction = transactionRepository.save(transaction);

        // Business Logic: If SUCCESS, notify Order Service asynchronously via Message Broker
        System.out.println("Transaction " + savedTransaction.getId() + " completed with status: " + savedTransaction.getStatus());

        return savedTransaction;
    }
}