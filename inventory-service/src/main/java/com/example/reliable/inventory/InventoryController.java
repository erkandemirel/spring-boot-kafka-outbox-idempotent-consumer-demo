package com.example.reliable.inventory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventory")
class InventoryController {
    private final InventoryReservationRepository reservationRepository;
    private final ProcessedEventRepository processedEventRepository;

    InventoryController(InventoryReservationRepository reservationRepository,
                        ProcessedEventRepository processedEventRepository) {
        this.reservationRepository = reservationRepository;
        this.processedEventRepository = processedEventRepository;
    }

    @GetMapping("/reservations")
    List<InventoryReservation> reservations() {
        return reservationRepository.findAll();
    }

    @GetMapping("/processed-events")
    List<ProcessedEvent> processedEvents() {
        return processedEventRepository.findAll();
    }
}
