package com.example.reliable.order;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "orders")
class OrderEntity {
    @Id
    private UUID id;
    private String productId;
    private int quantity;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private Instant createdAt;

    protected OrderEntity() {
    }

    OrderEntity(UUID id, String productId, int quantity, BigDecimal amount) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.amount = amount;
        this.status = OrderStatus.CREATED;
        this.createdAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public BigDecimal getAmount() { return amount; }
    public OrderStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
