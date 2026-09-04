package com.acme.trading.domain;

/**
 * Lifecycle state of an order.
 */
public enum OrderStatus {
    PENDING,
    OPEN,
    PARTIALLY_FILLED,
    FILLED,
    CANCELLED,
    REJECTED,
    EXPIRED
}
