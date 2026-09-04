package com.acme.trading.dto;

import com.acme.trading.domain.OrderSide;
import com.acme.trading.domain.OrderStatus;
import com.acme.trading.domain.OrderType;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Order representation returned by trading services.
 */
public record OrderResponse(
        String orderId,
        String accountId,
        String symbol,
        OrderSide side,
        OrderType orderType,
        OrderStatus status,
        BigDecimal quantity,
        BigDecimal filledQuantity,
        BigDecimal limitPrice,
        BigDecimal averageFillPrice,
        BigDecimal estimatedFee,
        Instant createdAt,
        Instant updatedAt
) {}
