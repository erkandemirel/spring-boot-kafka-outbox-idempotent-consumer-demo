package com.example.reliable.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface InventoryReservationRepository extends JpaRepository<InventoryReservation, UUID> {
}
