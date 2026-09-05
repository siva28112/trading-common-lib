package com.acme.trading.event;

import com.acme.trading.domain.OrderSide;
import com.acme.trading.domain.OrderType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DomainEventTest {

    private static OrderPlacedEvent placed() {
        return new OrderPlacedEvent("ORD-1", "ACC-001", "AAPL",
                OrderSide.BUY, OrderType.MARKET, new BigDecimal("100"));
    }

    @Test
    void eventIdIsAUniqueUuidPerInstance() {
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < 500; i++) {
            String eventId = placed().getEventId();
            assertDoesNotThrow(() -> UUID.fromString(eventId), "eventId should be a UUID: " + eventId);
            assertTrue(ids.add(eventId), "duplicate eventId generated: " + eventId);
        }
    }

    @Test
    void occurredAtIsStampedAtConstructionAndDoesNotMove() {
        Instant before = Instant.now();
        OrderPlacedEvent event = placed();
        Instant after = Instant.now();

        Instant occurredAt = event.getOccurredAt();
        assertNotNull(occurredAt);
        assertFalse(occurredAt.isBefore(before), "occurredAt precedes construction");
        assertFalse(occurredAt.isAfter(after), "occurredAt follows construction");
        assertEquals(occurredAt, event.getOccurredAt(), "occurredAt must be stable across reads");
    }

    @Test
    void orderPlacedEventCarriesItsPayloadAndType() {
        OrderPlacedEvent event = placed();

        assertEquals("ORDER_PLACED", event.getEventType());
        assertEquals("ORD-1", event.getOrderId());
        assertEquals("ACC-001", event.getAccountId());
        assertEquals("AAPL", event.getSymbol());
        assertEquals(OrderSide.BUY, event.getSide());
        assertEquals(OrderType.MARKET, event.getOrderType());
        assertEquals(new BigDecimal("100"), event.getQuantity());
    }

    @Test
    void orderCancelledEventCarriesItsPayloadAndType() {
        OrderCancelledEvent event =
                new OrderCancelledEvent("ORD-2", "ACC-002", "Client requested cancellation");

        assertEquals("ORDER_CANCELLED", event.getEventType());
        assertEquals("ORD-2", event.getOrderId());
        assertEquals("ACC-002", event.getAccountId());
        assertEquals("Client requested cancellation", event.getReason());
    }

    @Test
    void eventTypesAreDistinctSoConsumersCanRouteOnThem() {
        assertFalse(placed().getEventType()
                .equals(new OrderCancelledEvent("ORD-3", "ACC-003", "reason").getEventType()));
    }
}
