package com.stockbrokerage.portfolio.model;

import com.stockbrokerage.user.model.Account;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity for tracking user holdings (Shares owned in a specific stock).
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "portfolio", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"account_id", "symbol"}) // Ensures only one row per account/stock
})
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private String symbol;

    // Core metrics
    @Column(nullable = false)
    private BigDecimal quantity = BigDecimal.ZERO; // Current shares owned

    @Column(nullable = false)
    private BigDecimal averageCost = BigDecimal.ZERO; // Cost basis calculation (crucial for taxes/P&L)

    private BigDecimal currentMarketValue = BigDecimal.ZERO;
    private BigDecimal unrealizedProfitLoss = BigDecimal.ZERO;

    private LocalDateTime lastTradeDate = LocalDateTime.now();
}