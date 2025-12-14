package com.stockbrokerage.risk.service;

import com.stockbrokerage.marketdata.service.MarketDataService;
import com.stockbrokerage.order.model.Order;
import com.stockbrokerage.order.enums.OrderType;
import com.stockbrokerage.order.exceptions.OrderProcessingException;
import com.stockbrokerage.user.model.Account;
import com.stockbrokerage.user.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service Layer: Performs all pre-trade compliance, liquidity, and balance checks.
 */
@Service
@RequiredArgsConstructor
public class RiskService {

    private final AccountRepository accountRepository;
    private final MarketDataService marketDataService;

    private static final BigDecimal MAX_ORDER_VALUE = new BigDecimal("100000.00"); // Example circuit breaker

    /**
     * Core: Performs comprehensive pre-trade risk checks.
     * @param order The order to check.
     */
    public void runPreTradeChecks(Order order) {

        Account account = accountRepository.findById(order.getAccountId())
                .orElseThrow(() -> new OrderProcessingException("Risk check failed: Account not found."));

        // 1. Financial Risk Check: Available Funds (Essential Check)
        validateAvailableFunds(order, account);

        // 2. Compliance Risk Check: Circuit Breakers
        validateOrderSize(order);

        // 3. Market Data Check
        validateSymbol(order.getSymbol());

        // Production Detail: More complex checks like Pattern Day Trader, Margin Requirements, and Position Limits would go here.
        System.out.printf("[RISK CHECK] All pre-trade risk checks passed for order %s.%n", order.getOrderReferenceId());
    }

    private void validateAvailableFunds(Order order, Account account) {
        BigDecimal requiredCash = BigDecimal.ZERO;

        if (order.getType().name().contains("BUY")) {
            // Estimate trade cost: Quantity * Price
            BigDecimal estimatedCost = order.getQuantity().multiply(order.getCurrentPrice());

            // NOTE: Must include estimated commission/fees in the check for production!
            // CommissionCalculator commissionCalculator = ... // Inject/use calculator
            // estimatedCost = estimatedCost.add(commissionCalculator.calculateCommission(order, account.getType()).abs());

            requiredCash = estimatedCost;

            if (account.getAvailableBalance().compareTo(requiredCash) < 0) {
                throw new OrderProcessingException(
                        String.format("Insufficient available funds. Required: %s, Available: %s.", requiredCash, account.getAvailableBalance())
                );
            }
        }

        // For SELL orders, we check if the user has the shares (Position Limit check)
        // (This relies on the PortfolioService, which we will integrate next)
    }

    private void validateOrderSize(Order order) {
        BigDecimal orderValue = order.getQuantity().multiply(order.getCurrentPrice());
        if (orderValue.compareTo(MAX_ORDER_VALUE) > 0) {
            throw new OrderProcessingException("Order value exceeds maximum allowed limit of " + MAX_ORDER_VALUE);
        }
    }

    private void validateSymbol(String symbol) {
        try {
            marketDataService.getStockBySymbol(symbol);
        } catch (OrderProcessingException e) {
            throw new OrderProcessingException("Risk check failed: Trading symbol is invalid or inactive.");
        }
    }
}