package com.acme.trading.dto;

import com.acme.trading.domain.OrderSide;
import com.acme.trading.domain.OrderType;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Incoming request to place or amend an order.
 */
public record OrderRequest(
        String accountId,
        String symbol,
        OrderSide side,
        OrderType orderType,
        BigDecimal quantity,
        BigDecimal limitPrice,
        String clientOrderId
) {
    public OrderRequest {
        Objects.requireNonNull(accountId, "accountId is required");
        Objects.requireNonNull(symbol, "symbol is required");
        Objects.requireNonNull(side, "side is required");
        Objects.requireNonNull(orderType, "orderType is required");
        Objects.requireNonNull(quantity, "quantity is required");
    }
}
