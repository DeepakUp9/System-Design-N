package com.atm.machine.repository;

import com.atm.machine.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    /**
     * Finds a customer by their email address.
     * Often used for internal user management/login.
     */
    Optional<Customer> findByEmail(String email);
}