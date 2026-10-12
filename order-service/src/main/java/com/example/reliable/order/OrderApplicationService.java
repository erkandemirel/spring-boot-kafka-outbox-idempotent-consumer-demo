package com.example.reliable.order;

import com.example.reliable.messages.EventTopics;
import com.example.reliable.messages.OrderCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
class OrderApplicationService {
    private final OrderRepository orderRepository;
    private final OutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;

    OrderApplicationService(OrderRepository orderRepository,
                            OutboxEventRepository outboxRepository,
                            ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    OrderEntity create(String productId, int quantity, BigDecimal amount) {
        if (productId == null || productId.isBlank() || quantity <= 0 || amount.signum() <= 0) {
            throw new IllegalArgumentException("productId, quantity and amount must be valid");
        }

        UUID orderId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        OrderEntity order = orderRepository.save(new OrderEntity(orderId, productId, quantity, amount));
        OrderCreatedEvent event = new OrderCreatedEvent(
                eventId, orderId, productId, quantity, amount, Instant.now());

        try {
            outboxRepository.save(new OutboxEvent(
                    eventId,
                    orderId,
                    OrderCreatedEvent.class.getSimpleName(),
                    EventTopics.ORDER_CREATED,
                    objectMapper.writeValueAsString(event)));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Could not serialize order event", exception);
        }
        return order;
    }
}
