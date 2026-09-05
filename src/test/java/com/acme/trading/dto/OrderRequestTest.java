package com.acme.trading.dto;

import com.acme.trading.domain.OrderSide;
import com.acme.trading.domain.OrderType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * OrderRequest's compact constructor is the first line of defence: it rejects missing
 * required fields before OrderValidator ever sees the request.
 */
class OrderRequestTest {

    private static final BigDecimal QUANTITY = new BigDecimal("100");

    @Test
    void accountIdIsRequired() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> new OrderRequest(
                null, "AAPL", OrderSide.BUY, OrderType.MARKET, QUANTITY, null, "c1"));
        assertEquals("accountId is required", thrown.getMessage());
    }

    @Test
    void symbolIsRequired() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> new OrderRequest(
                "ACC-001", null, OrderSide.BUY, OrderType.MARKET, QUANTITY, null, "c1"));
        assertEquals("symbol is required", thrown.getMessage());
    }

    @Test
    void sideIsRequired() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> new OrderRequest(
                "ACC-001", "AAPL", null, OrderType.MARKET, QUANTITY, null, "c1"));
        assertEquals("side is required", thrown.getMessage());
    }

    @Test
    void orderTypeIsRequired() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> new OrderRequest(
                "ACC-001", "AAPL", OrderSide.BUY, null, QUANTITY, null, "c1"));
        assertEquals("orderType is required", thrown.getMessage());
    }

    @Test
    void quantityIsRequired() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> new OrderRequest(
                "ACC-001", "AAPL", OrderSide.BUY, OrderType.MARKET, null, null, "c1"));
        assertEquals("quantity is required", thrown.getMessage());
    }

    @Test
    void limitPriceAndClientOrderIdAreOptional() {
        assertDoesNotThrow(() -> new OrderRequest(
                "ACC-001", "AAPL", OrderSide.BUY, OrderType.MARKET, QUANTITY, null, null));
    }

    @Test
    void componentsAreExposedAsGiven() {
        OrderRequest request = new OrderRequest(
                "ACC-001", "aapl", OrderSide.SELL, OrderType.LIMIT,
                QUANTITY, new BigDecimal("101.50"), "client-9");

        assertEquals("ACC-001", request.accountId());
        assertEquals("aapl", request.symbol(), "the record does not normalise case");
        assertEquals(OrderSide.SELL, request.side());
        assertEquals(OrderType.LIMIT, request.orderType());
        assertEquals(QUANTITY, request.quantity());
        assertEquals(new BigDecimal("101.50"), request.limitPrice());
        assertEquals("client-9", request.clientOrderId());
    }

    @Test
    void equalityFollowsRecordComponentsIncludingBigDecimalScale() {
        OrderRequest scaleTwo = new OrderRequest(
                "ACC-001", "AAPL", OrderSide.BUY, OrderType.MARKET, new BigDecimal("100.00"), null, "c1");
        OrderRequest scaleZero = new OrderRequest(
                "ACC-001", "AAPL", OrderSide.BUY, OrderType.MARKET, new BigDecimal("100"), null, "c1");

        // BigDecimal.equals is scale-sensitive, so these are NOT equal even though the
        // amounts match. Anything deduplicating requests must compare with compareTo.
        assertNotEquals(scaleTwo, scaleZero);
        assertEquals(0, scaleTwo.quantity().compareTo(scaleZero.quantity()));
    }
}
