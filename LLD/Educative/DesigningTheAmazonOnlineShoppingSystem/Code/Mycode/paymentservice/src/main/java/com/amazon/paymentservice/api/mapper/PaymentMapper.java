package com.amazon.paymentservice.api.mapper;

import com.amazon.paymentservice.api.dto.PaymentRequestDTO;
import com.amazon.paymentservice.api.dto.PaymentResponseDTO;
import com.amazon.paymentservice.domain.model.PaymentRequest;
import com.amazon.paymentservice.domain.model.PaymentTransaction;

public final class PaymentMapper {

    private PaymentMapper() {}

    // Maps DTO to the clean LLD PaymentRequest domain object for the Strategy Context
    public static PaymentRequest toDomainRequest(PaymentRequestDTO dto) {
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(dto.orderId());
        request.setAmount(dto.amount());
        request.setPaymentMethod(dto.paymentMethod());
        request.setPaymentDetails(dto.paymentDetails());
        return request;
    }

    // Maps the resulting PaymentTransaction entity to the DTO for response
    public static PaymentResponseDTO toDTO(PaymentTransaction transaction) {
        return new PaymentResponseDTO(
                transaction.getId(),
                transaction.getOrderId(),
                transaction.getAmount(),
                transaction.getPaymentMethod(),
                transaction.getStatus(),
                transaction.getGatewayTransactionId(),
                transaction.getTransactionDate()
        );
    }
}