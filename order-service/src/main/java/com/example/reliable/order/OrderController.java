package com.example.reliable.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/orders")
class OrderController {
    private final OrderApplicationService service;
    private final OrderRepository repository;

    OrderController(OrderApplicationService service, OrderRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    OrderEntity create(@RequestParam String productId,
                       @RequestParam int quantity,
                       @RequestParam BigDecimal amount) {
        return service.create(productId, quantity, amount);
    }

    @GetMapping
    List<OrderEntity> findAll() {
        return repository.findAll();
    }
}
