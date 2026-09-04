package com.acme.trading.validation;

import com.acme.trading.domain.OrderType;
import com.acme.trading.dto.OrderRequest;
import com.acme.trading.exception.ValidationException;

import java.math.BigDecimal;

/**
 * Validates incoming order requests before submission.
 */
public final class OrderValidator {

    private static final BigDecimal MAX_QUANTITY = new BigDecimal("1_000_000".replace("_", ""));

    private OrderValidator() {
    }

    public static void validate(OrderRequest request) {
        if (request.symbol().isBlank()) {
            throw new ValidationException("Symbol must not be blank");
        }
        if (request.quantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Quantity must be positive");
        }
        if (request.quantity().compareTo(MAX_QUANTITY) > 0) {
            throw new ValidationException("Quantity exceeds maximum allowed size");
        }
        if (request.orderType() == OrderType.LIMIT || request.orderType() == OrderType.STOP_LIMIT) {
            if (request.limitPrice() == null || request.limitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidationException("Limit price is required for limit orders");
            }
        }
    }
}
