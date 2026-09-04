package com.acme.trading.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Aggregated portfolio view for an account.
 */
public record PortfolioSummary(
        String accountId,
        BigDecimal totalMarketValue,
        BigDecimal totalUnrealizedPnl,
        BigDecimal totalRealizedPnl,
        BigDecimal cashBalance,
        List<Position> positions,
        Instant asOf
) {}
