package com.acme.trading.dto;

import com.acme.trading.domain.InstrumentType;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Static reference data for a tradable instrument.
 */
public record InstrumentMetadata(
        String symbol,
        String name,
        InstrumentType instrumentType,
        String exchange,
        Currency currency,
        BigDecimal tickSize,
        BigDecimal lotSize,
        boolean tradable
) {}
