package com.amazon.orderservice.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ShippingInfoDTO(
        @NotBlank(message = "Recipient name is required")
        String recipientName,

        @NotBlank(message = "Street address is required")
        String streetAddress,

        @NotBlank(message = "City is required")
        String city,

        @NotBlank(message = "Zip code is required")
        String zipCode
) {}