# Transactional Outbox and Idempotent Consumer Demo

Spring Boot, Kafka and PostgreSQL demo combining two reliability patterns:

- Transactional Outbox prevents an order from being committed without its event.
- Idempotent Consumer prevents the same event from creating duplicate business effects.

## Run

```bash
docker compose up --build -d
```

Create an order:

```bash
curl -X POST "http://localhost:8092/orders?productId=laptop&quantity=2&amount=1499.90"
```

Inspect the result:

```bash
curl http://localhost:8092/outbox
curl http://localhost:8093/inventory/reservations
curl http://localhost:8093/inventory/processed-events
```

Copy the outbox event ID and publish the same event again:

```bash
curl -X POST http://localhost:8092/outbox/{eventId}/replay
```

The consumer logs the event as a duplicate. The reservation and processed-event counts remain `1`.

Kafka UI: http://localhost:8089
