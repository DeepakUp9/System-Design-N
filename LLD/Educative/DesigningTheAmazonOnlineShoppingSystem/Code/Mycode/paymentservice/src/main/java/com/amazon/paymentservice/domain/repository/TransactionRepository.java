package com.amazon.paymentservice.domain.repository;

import com.amazon.paymentservice.domain.model.PaymentTransaction;
import java.util.Optional;

public interface TransactionRepository {
    PaymentTransaction save(PaymentTransaction transaction);
    Optional<PaymentTransaction> findByOrderId(Long orderId);
}
// JpaRepository and Implementation omitted for brevity (similar structure to previous services)