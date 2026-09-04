package com.acme.trading.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Real-time or delayed market quote for a symbol.
 */
public record Quote(
        String symbol,
        BigDecimal bid,
        BigDecimal ask,
        BigDecimal last,
        BigDecimal volume,
        Instant timestamp
) {}
