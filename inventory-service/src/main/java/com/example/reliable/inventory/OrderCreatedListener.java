package com.example.reliable.inventory;

import com.example.reliable.messages.EventTopics;
import com.example.reliable.messages.OrderCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
class OrderCreatedListener {
    private static final Logger log = LoggerFactory.getLogger(OrderCreatedListener.class);
    private static final String CONSUMER_NAME = "inventory-service";

    private final ProcessedEventRepository processedEventRepository;
    private final InventoryReservationRepository reservationRepository;
    private final ObjectMapper objectMapper;

    OrderCreatedListener(ProcessedEventRepository processedEventRepository,
                         InventoryReservationRepository reservationRepository,
                         ObjectMapper objectMapper) {
        this.processedEventRepository = processedEventRepository;
        this.reservationRepository = reservationRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = EventTopics.ORDER_CREATED)
    @Transactional
    public void onOrderCreated(ConsumerRecord<String, String> record) throws JsonProcessingException {
        OrderCreatedEvent event = objectMapper.readValue(record.value(), OrderCreatedEvent.class);

        int claimed = processedEventRepository.insertIfAbsent(
                event.eventId(), event.orderId(), CONSUMER_NAME,
                record.topic(), record.partition(), record.offset(), Instant.now());

        if (claimed == 0) {
            log.info("Duplicate event {} ignored", event.eventId());
            return;
        }

        reservationRepository.save(new InventoryReservation(
                event.orderId(), event.eventId(), event.productId(), event.quantity()));
        log.info("Event {} processed and inventory reserved", event.eventId());
    }
}
