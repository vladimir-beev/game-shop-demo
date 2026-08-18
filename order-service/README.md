# Order Service
Standalone microservice responsible for creating and managing
user orders in an event‑driven system.
It consumes checkout events from Kafka, persists orders, 
and emits OrderCreated events for downstream services.

---

# Features
- Consumes CartCheckout events
- Consumes StockRejected events when insufficient stock
- Creates orders with associated order items
- Stores orders using JPA with UUID identifiers
- Exposes REST APIs for retrieving user orders
- Publishes OrderCreated events to Kafka
- Supports filtering by order status

---

# Architecture Overview
The Order Service is part of a distributed microservice workflow:

#### Cart → Order → Inventory
Order Service only handles order creation and retrieval.
It does not manage inventory, payment, or delivery — those responsibilities belong to downstream services.

---

# API Endpoints
| Method | 	Path               | 	Description                        |
|--------|---------------------|-------------------------------------|
| GET    | /api/orders/pending | 	Get orders still awaiting delivery |
| GET    | /api/orders/history | 	Get delivered or cancelled orders  |

---

# Data Model
### Order
- `id` (`UUID`)
- `userId` (`UUID`)
- `status` (`OrderStatus`)
- `createdAt` / `updatedAt` (`Instant`)
- `cancellationReason` (`String`)
- `rejectedProductId` (`String`)
- `canceledAt` (`Instant`)
- `items` (`List<OrderItem>`)

### OrderItem
- `id` (`UUID`)
- `productId` (`UUID`)
- `quantity` (`int`)
- `order` (`Order`)

---

# Kafka Integration
The service consumes checkout and stockRejected events and publishes order creation events.

#### Consumes checkout events using:
- Value type: `CartCheckoutEvent`
- JSON deserialization (`JsonDeserializer`)
- Topic configured via `topic.checkout`

#### Consumes stockRejected events using:
- Value type: `StockRejectedEvent`
- JSON deserialization (`JsonDeserializer`)
- Topic configured via `topic.stock-rejected`

#### Publishes order-created events using:
- `KafkaTemplate<String, OrderCreatedEvent>`
- JSON serialization (`JsonSerializer`)
- Topic configured via `topic.order-created`

---

### Produced Event Example (OrderCreatedEvent)
```json
{
  "id": "order123",
  "items": [
    { "id": "111", "productId": "101", "quantity": 2 }, 
    { "id": "222", "productId": "202", "quantity": 1 }
  ]
}
```

---

# Tech Stack
- Java 21
- Spring Boot 4
- Spring Web
- Spring Data JPA
- Spring Kafka