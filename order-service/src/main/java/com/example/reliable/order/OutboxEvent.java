package com.example.reliable.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
class OutboxEvent {
    @Id
    private UUID id;
    private UUID aggregateId;
    private String eventType;
    private String topic;
    @Column(columnDefinition = "text")
    private String payload;
    @Enumerated(EnumType.STRING)
    private OutboxStatus status;
    private int attempts;
    @Column(columnDefinition = "text")
    private String lastError;
    private Instant createdAt;
    private Instant publishedAt;

    protected OutboxEvent() {
    }

    OutboxEvent(UUID id, UUID aggregateId, String eventType, String topic, String payload) {
        this.id = id;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.topic = topic;
        this.payload = payload;
        this.status = OutboxStatus.NEW;
        this.createdAt = Instant.now();
    }

    void markPublished() {
        status = OutboxStatus.PUBLISHED;
        publishedAt = Instant.now();
        lastError = null;
        attempts++;
    }

    void markFailed(Exception exception) {
        status = OutboxStatus.FAILED;
        lastError = exception.getMessage();
        attempts++;
    }

    public UUID getId() { return id; }
    public UUID getAggregateId() { return aggregateId; }
    public String getEventType() { return eventType; }
    public String getTopic() { return topic; }
    public String getPayload() { return payload; }
    public OutboxStatus getStatus() { return status; }
    public int getAttempts() { return attempts; }
    public String getLastError() { return lastError; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getPublishedAt() { return publishedAt; }
}
