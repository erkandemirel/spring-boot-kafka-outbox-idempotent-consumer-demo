package com.example.reliable.inventory;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "processed_events")
class ProcessedEvent {
    @Id
    private UUID eventId;
    private UUID orderId;
    private String consumerName;
    private String topic;
    private int partitionNumber;
    private long offsetNumber;
    private Instant processedAt;

    protected ProcessedEvent() {
    }

    public UUID getEventId() { return eventId; }
    public UUID getOrderId() { return orderId; }
    public String getConsumerName() { return consumerName; }
    public String getTopic() { return topic; }
    public int getPartitionNumber() { return partitionNumber; }
    public long getOffsetNumber() { return offsetNumber; }
    public Instant getProcessedAt() { return processedAt; }
}
