package com.airline.pricing.domain;

import com.airline.reservation.domain.SeatClass;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class PricingContext {
    private final BigDecimal basePrice;
    private final SeatClass seatClass;
    private final LocalDateTime departureTime;
    private final LocalDateTime bookingTime;
    private final boolean isLoyaltyMember;
    private final boolean isHolidaySeason;
}