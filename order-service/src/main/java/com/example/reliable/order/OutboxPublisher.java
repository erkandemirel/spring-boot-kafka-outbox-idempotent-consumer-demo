package com.example.reliable.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
class OutboxPublisher {
    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);
    private static final List<OutboxStatus> PENDING = List.of(OutboxStatus.NEW, OutboxStatus.FAILED);

    private final OutboxEventRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    OutboxPublisher(OutboxEventRepository repository, KafkaTemplate<String, String> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelayString = "${outbox.publisher.fixed-delay:2000}")
    @Transactional
    public void publishPending() {
        repository.findTop50ByStatusInOrderByCreatedAtAsc(PENDING).forEach(this::publish);
    }

    void replay(UUID eventId) {
        OutboxEvent event = repository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Outbox event not found"));
        send(event);
        log.info("Replayed outbox event {} to demonstrate idempotency", eventId);
    }

    private void publish(OutboxEvent event) {
        try {
            send(event);
            event.markPublished();
            log.info("Published outbox event {}", event.getId());
        } catch (Exception exception) {
            event.markFailed(exception);
            log.warn("Outbox event {} will be retried", event.getId());
        }
    }

    private void send(OutboxEvent event) {
        try {
            kafkaTemplate.send(event.getTopic(), event.getAggregateId().toString(), event.getPayload())
                    .get(10, TimeUnit.SECONDS);
        } catch (Exception exception) {
            throw new IllegalStateException("Could not publish outbox event", exception);
        }
    }
}
