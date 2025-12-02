package com.atm.machine.repository;

import com.atm.machine.entity.Account;
import com.atm.machine.entity.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    /**
     * Finds transaction history for a given account, ordered by timestamp descending.
     * We use Pageable for pagination, which is essential for production-level apps
     * to prevent loading massive history data.
     */
    List<Transaction> findByAccountOrderByTransactionTimestampDesc(Account account, Pageable pageable);
    // ✅ Fixed with @Query
    @Query("SELECT t FROM Transaction t WHERE t.transactionTimestamp BETWEEN :startDate AND :endDate AND t.amount >= :thresholdAmount")
    List<Transaction> findHighValueTransactionsBetween(
            @Param("startDate") ZonedDateTime startDate,
            @Param("endDate") ZonedDateTime endDate,
            @Param("thresholdAmount") BigDecimal thresholdAmount);
}