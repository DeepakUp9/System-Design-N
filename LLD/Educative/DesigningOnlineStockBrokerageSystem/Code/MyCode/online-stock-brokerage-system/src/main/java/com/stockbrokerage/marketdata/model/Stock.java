package com.stockbrokerage.marketdata.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity for Master Instrument Data (What can be traded).
 */
@Entity
@Data
@NoArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String symbol; // Ticker (e.g., AAPL)

    @Column(nullable = false)
    private String name; // Company Name

    @Column(nullable = false)
    private String exchange; // NASDAQ, NYSE

    // Current Price (Volatile data, often cached or sourced externally)
    @Column(nullable = false)
    private BigDecimal currentPrice;

    private BigDecimal dayHigh = BigDecimal.ZERO;
    private BigDecimal dayLow = BigDecimal.ZERO;
    private BigDecimal volume = BigDecimal.ZERO;

    private LocalDateTime lastUpdated = LocalDateTime.now();
}