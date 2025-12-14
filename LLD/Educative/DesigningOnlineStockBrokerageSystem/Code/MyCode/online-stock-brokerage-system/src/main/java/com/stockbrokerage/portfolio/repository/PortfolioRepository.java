package com.stockbrokerage.portfolio.repository;

import com.stockbrokerage.portfolio.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Optional<Portfolio> findByAccountIdAndSymbol(Long accountId, String symbol);
    List<Portfolio> findByAccountId(Long accountId);
}