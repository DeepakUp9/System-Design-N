package com.amazon.orderservice.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class ShippingInfo {
    private String recipientName;
    private String streetAddress;
    private String city;
    private String zipCode;
    // Getters and Setters Omitted
}