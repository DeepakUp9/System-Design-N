package com.atm.machine.repository;

import com.atm.machine.entity.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Finds an account by its unique account number.
     * Used for lookups (e.g., transfers to a known account).
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * CRITICAL: Finds an account by ID and acquires a PESSIMISTIC_WRITE lock (FOR UPDATE).
     * This prevents concurrent updates (like two simultaneous withdrawals) from corrupting the balance.
     * @param id The account ID.
     * @return The Account object, locked until the transaction commits or rolls back.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.accountId = ?1")
    Optional<Account> findById(Long id);
}