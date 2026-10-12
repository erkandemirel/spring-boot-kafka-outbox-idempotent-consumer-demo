package com.example.reliable.inventory;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "inventory_reservations")
class InventoryReservation {
    @Id
    private UUID orderId;
    private UUID eventId;
    private String productId;
    private int quantity;
    private Instant reservedAt;

    protected InventoryReservation() {
    }

    InventoryReservation(UUID orderId, UUID eventId, String productId, int quantity) {
        this.orderId = orderId;
        this.eventId = eventId;
        this.productId = productId;
        this.quantity = quantity;
        this.reservedAt = Instant.now();
    }

    public UUID getOrderId() { return orderId; }
    public UUID getEventId() { return eventId; }
    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public Instant getReservedAt() { return reservedAt; }
}
