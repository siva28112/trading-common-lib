package com.acme.trading.event;

/**
 * Emitted when an open order is cancelled by the client or system.
 */
public class OrderCancelledEvent extends DomainEvent {

    private final String orderId;
    private final String accountId;
    private final String reason;

    public OrderCancelledEvent(String orderId, String accountId, String reason) {
        super();
        this.orderId = orderId;
        this.accountId = accountId;
        this.reason = reason;
    }

    @Override
    public String getEventType() {
        return "ORDER_CANCELLED";
    }

    public String getOrderId() {
        return orderId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getReason() {
        return reason;
    }
}
