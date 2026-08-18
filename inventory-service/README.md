# Inventory Service
Standalone microservice responsible for validating and
updating product stock in an event‑driven system.
It consumes order‑related events from Kafka,
checks inventory availability, updates quantities,
and emits StockRejected events when items cannot be fulfilled.

---

# Features
- Consumes OrderCreated events
- Validates stock for each ordered item
- Deducts inventory atomically using JPA
- Publishes StockRejected events when insufficient stock
- Stores products with current quantities
- Exposes REST APIs for retrieving product availability

---

# Architecture Overview
The Inventory Service is part of a distributed microservice workflow:

#### Cart → Order → Inventory → (StockRejected → Order)
Inventory Service only handles stock validation and deduction.

---

# API Endpoints
| Method | Path                                      | 	Description             |
|--------|-------------------------------------------|--------------------------|
| GET    | 	/api/inventory/{productId}/availability	 | Get product availability |

---

# Data Model
### ProductStock
- `productId` (`UUID`)
- `availableQuantity` (`int`)
- `updatedAt` (`Instant`)

---

# Kafka Integration
The service consumes order-created events and publishes stock-rejected events.

### Consumes order-created events using:
- Value type: `OrderCreatedEvent`
- JSON deserialization (`JsonDeserializer`)
- Topic configured via `topic.order-created`

### Publishes stock-rejected events using:
- `KafkaTemplate<String, StockRejectedEvent>`
- JSON serialization (`JsonSerializer`)
- Topic configured via `topic.stock-rejected`

---

# Produced Event Example (StockRejectedEvent)
```json
{
  "orderId": "order123", 
  "productId": "101"
}
```

---

# Tech Stack
- Java 21
- Spring Boot 4
- Spring Web
- Spring Data JPA
- Spring Kafka