package com.stockbrokerage.order.execution.strategy;

import com.stockbrokerage.finance.model.TransactionType;
import com.stockbrokerage.finance.service.CommissionCalculator;
import com.stockbrokerage.finance.service.SettlementService;
import com.stockbrokerage.finance.service.TransactionService;
import com.stockbrokerage.marketdata.service.MarketDataService;
import com.stockbrokerage.order.model.Order;
import com.stockbrokerage.order.enums.OrderStatus;
import com.stockbrokerage.portfolio.service.PortfolioService;
import com.stockbrokerage.user.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * Concrete Strategy 1: Handles the immediate execution of a Market Order.
 */
@Service("marketOrderStrategy") // Use a specific qualifier for easy retrieval
@RequiredArgsConstructor
public class MarketOrderStrategy implements OrderExecutionStrategy {

    // Dependency Injection (e.g., MarketDataService, AccountService) would be injected here
    private final MarketDataService marketDataService;
    private final TransactionService transactionService;
    private final CommissionCalculator commissionCalculator;
    private final PortfolioService portfolioService;
    private final SettlementService settlementService;
    private final AccountRepository accountRepository; // Needed for account type

    @Override
    public Order execute(Order order) {

        // 1. Finalize Execution Price (Get real-time market price)
        BigDecimal executionPrice = marketDataService.getCurrentPrice(order.getSymbol());
        order.setCurrentPrice(executionPrice); // Update the order with the actual fill price

        // 2. Identify Financial Action and Calculate Trade Value
        boolean isBuy = order.getType().name().contains("BUY");
        BigDecimal grossTradeValue = order.getQuantity().multiply(executionPrice).setScale(2, RoundingMode.HALF_UP);

        // 3. Calculate Fees/Commissions
        // Fetch Account details for commission logic
        var account = accountRepository.findById(order.getAccountId())
                .orElseThrow(() -> new IllegalStateException("Account not found during execution."));

        // Total fees will be a negative BigDecimal
        BigDecimal fees = commissionCalculator.calculateCommissionAndFees(order, account.getType());

        // 4. Calculate Net Cash Impact
        // BUY: Cash impact = -(Gross Value + Fees)
        // SELL: Cash impact = +(Gross Value - Fees) (Fees are deducted from proceeds)
        BigDecimal netCashImpact = isBuy
                ? grossTradeValue.negate().add(fees) // fees is already negative
                : grossTradeValue.add(fees);

        // 5. Apply Financial Transaction (Cash Leg)
        // This transaction debits/credits the account's cash balance
        transactionService.createAndApplyTransaction(
                order.getAccountId(),
                isBuy ? TransactionType.BUY : TransactionType.SELL,
                netCashImpact, // Net amount applied to balance
                order.getOrderReferenceId(),
                String.format("Executed %s %s shares of %s at %s.",
                        order.getQuantity(), isBuy ? "BUY" : "SELL", order.getSymbol(), executionPrice)
        );

        // 6. Apply Commission Transaction (Fees Leg)
        if (fees.compareTo(BigDecimal.ZERO) < 0) {
            transactionService.createAndApplyTransaction(
                    order.getAccountId(),
                    TransactionType.COMMISSION,
                    fees, // Fees is negative, which correctly debits the account
                    order.getOrderReferenceId(),
                    "Brokerage and regulatory fees deducted."
            );
        }

        // 7. Update Portfolio Holdings (Security Leg)
        // This method handles average cost and quantity updates
        portfolioService.updateHoldings(order, isBuy);

        // 8. Final Status Update
        order.setStatus(OrderStatus.EXECUTED);
        order.setExecutedAt(LocalDateTime.now());

        // 9. Mark for Settlement
        settlementService.markTradeForSettlement(order);

        System.out.printf("--- [EXECUTION SUCCESS] --- Order %s: Net Impact %s%n", order.getOrderReferenceId(), netCashImpact);

        return order;
    }
}