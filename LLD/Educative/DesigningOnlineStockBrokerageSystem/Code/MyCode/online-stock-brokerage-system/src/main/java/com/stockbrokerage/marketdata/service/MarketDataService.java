package com.stockbrokerage.marketdata.service;

import com.stockbrokerage.marketdata.model.Stock;
import com.stockbrokerage.marketdata.repository.StockRepository;
import com.stockbrokerage.order.exceptions.OrderProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Service Layer: Provides real-time pricing and instrument lookup.
 * This is the interface that ALL execution strategies and portfolio valuation logic rely on.
 */
@Service
@RequiredArgsConstructor
public class MarketDataService {

    private final StockRepository stockRepository;

    /**
     * Production Detail: Retrieves the latest price for a symbol.
     * This method would eventually be @Cacheable for low-latency.
     */
    public BigDecimal getCurrentPrice(String symbol) {
        // In production, this would try Redis cache first, then fall back to the DB/External Feed.
        return stockRepository.findBySymbol(symbol)
                .map(Stock::getCurrentPrice)
                .orElseThrow(() -> new OrderProcessingException("Symbol not found or market data unavailable for: " + symbol));
    }

    public Stock getStockBySymbol(String symbol) {
        return stockRepository.findBySymbol(symbol)
                .orElseThrow(() -> new OrderProcessingException("Instrument not found: " + symbol));
    }

    /**
     * Simulation of external feed update.
     */
    public void updatePrice(String symbol, BigDecimal newPrice) {
        stockRepository.findBySymbol(symbol).ifPresentOrElse(stock -> {
            System.out.printf("[MARKET DATA] Price update for %s: %s -> %s%n", symbol, stock.getCurrentPrice(), newPrice);
            stock.setCurrentPrice(newPrice);
            stock.setLastUpdated(LocalDateTime.now());
            // Update day high/low logic would go here
            stockRepository.save(stock);
        }, () -> {
            System.err.println("Cannot update price: Symbol not found: " + symbol);
        });
    }
}