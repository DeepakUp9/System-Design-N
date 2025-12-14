package com.stockbrokerage.setup;

import com.stockbrokerage.marketdata.model.Stock;
import com.stockbrokerage.marketdata.repository.StockRepository;
import com.stockbrokerage.user.model.User;
import com.stockbrokerage.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Initializes necessary master data (Stocks) and a test user upon application startup.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final StockRepository stockRepository;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {

        // 1. Initialize Stocks
        if (stockRepository.findBySymbol("AAPL").isEmpty()) {
            stockRepository.save(createStock("AAPL", "Apple Inc.", "NASDAQ", new BigDecimal("150.50")));
            stockRepository.save(createStock("GOOGL", "Alphabet Inc.", "NASDAQ", new BigDecimal("2500.75")));
            stockRepository.save(createStock("TSLA", "Tesla, Inc.", "NASDAQ", new BigDecimal("800.20")));
        }

        // 2. Initialize Test User (if not already done by registration endpoint)
        Optional<User> testUser = userService.userRepository.findByUsername("trader1");
        if (testUser.isEmpty()) {
            userService.registerNewUser("trader1", "trader1@test.com", "password123");
        }

        System.out.println("Setup: Market Data and Test User initialized.");
    }

    private Stock createStock(String symbol, String name, String exchange, BigDecimal price) {
        Stock stock = new Stock();
        stock.setSymbol(symbol);
        stock.setName(name);
        stock.setExchange(exchange);
        stock.setCurrentPrice(price);
        return stock;
    }
}