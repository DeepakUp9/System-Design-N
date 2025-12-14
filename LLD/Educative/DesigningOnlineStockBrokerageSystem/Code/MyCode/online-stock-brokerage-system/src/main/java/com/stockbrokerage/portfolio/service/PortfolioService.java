package com.stockbrokerage.portfolio.service;

import com.stockbrokerage.marketdata.service.MarketDataService;
import com.stockbrokerage.order.exceptions.OrderProcessingException;
import com.stockbrokerage.portfolio.model.Portfolio;
import com.stockbrokerage.portfolio.repository.PortfolioRepository;
import com.stockbrokerage.user.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Service Layer: Manages user holdings and calculates portfolio metrics.
 */
@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final MarketDataService marketDataService; // Dependency for valuation

    public List<Portfolio> getHoldingsByAccountId(Long accountId) {
        return portfolioRepository.findByAccountId(accountId);
    }

    /**
     * Production Core: Calculates the current market value and P&L for a portfolio.
     */
    @Transactional
    public void revaluePortfolio(Long accountId) {
        List<Portfolio> holdings = getHoldingsByAccountId(accountId);

        for (Portfolio holding : holdings) {
            if (holding.getQuantity().compareTo(BigDecimal.ZERO) > 0) {
                try {
                    BigDecimal currentPrice = marketDataService.getCurrentPrice(holding.getSymbol());
                    BigDecimal marketValue = holding.getQuantity().multiply(currentPrice).setScale(2, RoundingMode.HALF_UP);

                    BigDecimal costBasis = holding.getQuantity().multiply(holding.getAverageCost());
                    BigDecimal pnl = marketValue.subtract(costBasis).setScale(2, RoundingMode.HALF_UP);

                    holding.setCurrentMarketValue(marketValue);
                    holding.setUnrealizedProfitLoss(pnl);

                    portfolioRepository.save(holding);
                } catch (OrderProcessingException e) {
                    System.err.printf("[PORTFOLIO ERROR] Could not revalue %s for account %d: %s%n",
                            holding.getSymbol(), accountId, e.getMessage());
                    // Log error but continue with other holdings
                }
            }
        }
        System.out.printf("[PORTFOLIO] Revaluation complete for account %d.%n", accountId);
    }

    // NOTE: The most complex method, updateHoldings(Order order), will be integrated in a later step
    // when we update the OrderExecutionService to handle the post-trade steps (settlement/commission).
}