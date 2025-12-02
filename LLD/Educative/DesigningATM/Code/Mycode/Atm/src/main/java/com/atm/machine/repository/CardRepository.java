package com.atm.machine.repository;

import com.atm.machine.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    /**
     * ABSOLUTELY CRITICAL: Finds a card by its unique card number.
     * This is the entry point for all ATM transactions.
     *
     * @param cardNumber The 16-digit card number.
     * @return An Optional containing the Card entity if found.
     */
    Optional<Card> findByCardNumber(String cardNumber);

    /**
     * Added for transaction recording utility.
     * Retrieves the primary card linked to a specific account ID.
     */
    // ✅ Clear and explicit
    @Query("SELECT c FROM Card c WHERE c.account.accountId = :accountId")
    Optional<Card> findByAccountId(@Param("accountId") Long accountId);
}