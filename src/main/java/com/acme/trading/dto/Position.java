package com.acme.trading.dto;

import java.math.BigDecimal;

/**
 * Holdings for a single instrument within a portfolio.
 */
public record Position(
        String symbol,
        BigDecimal quantity,
        BigDecimal averageCost,
        BigDecimal marketPrice,
        BigDecimal marketValue,
        BigDecimal unrealizedPnl
) {}
