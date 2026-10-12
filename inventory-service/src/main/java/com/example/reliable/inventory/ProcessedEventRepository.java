package com.example.reliable.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.UUID;

interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, UUID> {
    @Modifying
    @Query(value = """
            insert into processed_events
                (event_id, order_id, consumer_name, topic, partition_number, offset_number, processed_at)
            values
                (:eventId, :orderId, :consumerName, :topic, :partitionNumber, :offsetNumber, :processedAt)
            on conflict (event_id) do nothing
            """, nativeQuery = true)
    int insertIfAbsent(@Param("eventId") UUID eventId,
                       @Param("orderId") UUID orderId,
                       @Param("consumerName") String consumerName,
                       @Param("topic") String topic,
                       @Param("partitionNumber") int partitionNumber,
                       @Param("offsetNumber") long offsetNumber,
                       @Param("processedAt") Instant processedAt);
}
