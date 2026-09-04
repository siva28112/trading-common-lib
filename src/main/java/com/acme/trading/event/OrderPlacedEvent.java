package com.acme.trading.event;

import com.acme.trading.domain.OrderSide;
import com.acme.trading.domain.OrderType;

import java.math.BigDecimal;

/**
 * Emitted when a new order is accepted by the order service.
 */
public class OrderPlacedEvent extends DomainEvent {

    private final String orderId;
    private final String accountId;
    private final String symbol;
    private final OrderSide side;
    private final OrderType orderType;
    private final BigDecimal quantity;

    public OrderPlacedEvent(String orderId, String accountId, String symbol,
                            OrderSide side, OrderType orderType, BigDecimal quantity) {
        super();
        this.orderId = orderId;
        this.accountId = accountId;
        this.symbol = symbol;
        this.side = side;
        this.orderType = orderType;
        this.quantity = quantity;
    }

    @Override
    public String getEventType() {
        return "ORDER_PLACED";
    }

    public String getOrderId() {
        return orderId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getSymbol() {
        return symbol;
    }

    public OrderSide getSide() {
        return side;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }
}
