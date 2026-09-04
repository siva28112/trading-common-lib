package com.acme.trading.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Base type for domain events emitted by trading services.
 */
public abstract class DomainEvent {

    private final String eventId;
    private final Instant occurredAt;

    protected DomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.occurredAt = Instant.now();
    }

    public String getEventId() {
        return eventId;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public abstract String getEventType();
}
