package com.acme.trading.domain;

/**
 * Execution style for an order.
 */
public enum OrderType {
    MARKET,
    LIMIT,
    STOP,
    STOP_LIMIT
}
