package com.example.reliable.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/outbox")
class OutboxController {
    private final OutboxEventRepository repository;
    private final OutboxPublisher publisher;

    OutboxController(OutboxEventRepository repository, OutboxPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @GetMapping
    List<OutboxEvent> findAll() {
        return repository.findAll();
    }

    @PostMapping("/{eventId}/replay")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void replay(@PathVariable UUID eventId) {
        publisher.replay(eventId);
    }
}
