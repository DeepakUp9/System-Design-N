package com.amazon.paymentservice.api.controller;

import com.amazon.paymentservice.api.dto.PaymentRequestDTO;
import com.amazon.paymentservice.api.dto.PaymentResponseDTO;
import com.amazon.paymentservice.api.mapper.PaymentMapper;
import com.amazon.paymentservice.domain.model.PaymentRequest;
import com.amazon.paymentservice.domain.model.PaymentTransaction;
import com.amazon.paymentservice.domain.service.PaymentProcessor;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentProcessor paymentProcessor;

    public PaymentController(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    /**
     * POST /api/v1/payments
     * Executes a payment request using the LLD Strategy Pattern.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // Authorization: Should be restricted to the internal Order Service (System-to-System communication)
    // or an authenticated user making a direct call.
    @PreAuthorize("hasAnyRole('ORDER_SERVICE', 'USER')")
    public PaymentResponseDTO executePayment(@Valid @RequestBody PaymentRequestDTO requestDTO) {
        // 1. Map DTO to LLD Domain Request
        PaymentRequest domainRequest = PaymentMapper.toDomainRequest(requestDTO);

        // 2. Delegate to the Strategy Context (PaymentProcessor)
        PaymentTransaction transaction = paymentProcessor.executePayment(domainRequest);

        // 3. Determine HTTP response based on transaction status (production standard)
        HttpStatus status = switch (transaction.getStatus()) {
            case SUCCESS -> HttpStatus.CREATED;
            case PENDING -> HttpStatus.ACCEPTED; // Payment requires async verification (e.g., webhook)
            case FAILED -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return ResponseEntity.status(status).body(PaymentMapper.toDTO(transaction)).getBody();
    }

    // Additional endpoints for refunds, transaction lookup, etc., would follow the same pattern.
}