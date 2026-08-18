# Cart Service

Standalone microservice responsible for managing user shopping
carts in an event‑driven system.
It provides REST APIs for cart operations and emits
checkout events to Kafka.

---

## Features

- One **active cart per user** (business‑rule enforced)
- Add, update, remove, and clear cart items
- Checkout flow with domain validation
- Kafka event publishing (`CartCheckoutEvent`)
- JPA persistence with UUID identifiers

---

## Architecture Overview

The Cart Service is part of a distributed microservice workflow:
#### Cart → Order → Inventory


Cart only manages cart state and emits checkout events.  
It **does not** reserve stock, decrement inventory, or create orders.

---

## Cart Lifecycle

A user can have multiple carts over time, but only **one ACTIVE cart**.

| Status | Meaning |
|--------|---------|
| ACTIVE | User is adding items |
| COMPLETED | User checked out |

### Lifecycle:
#### ACTIVE → COMPLETED → (new ACTIVE cart created automatically)

---

## API Endpoints

| Method | Path                   | Description                          |
|--------|------------------------|--------------------------------------|
| GET | `/api/cart/items`      | Get active cart with product details |
| POST | `/api/cart/items`      | Add new item                         |
| PUT | `/api/cart/items/{id}` | Update quantity                      |
| DELETE | `/api/cart/items/{id}` | Remove item                          |
| DELETE | `/api/cart`            | Clear cart                           |
| POST | `/api/cart/checkout`   | Checkout cart                        |
---

## Data Model

### Cart

- `id` (`UUID`)
- `userId` (`UUID`)
- `status` (`CartStatus`)
- `createdAt` / `updatedAt` (`Instant`)
- `items` (`List<CartItem>`)

### CartItem

- `id` (`UUID`)
- `productId` (`UUID`)
- `quantity` (`int`)
- `cart` (`Cart`)

---

## Product Service Integration (OpenFeign)
Cart Service enriches cart items with product metadata (title, price, cover image, platform, etc.) by calling the Product Service.
Instead of storing product information locally, Cart Service performs a batch lookup using Spring Cloud OpenFeign.

### How it works
When the frontend requests /api/cart/items, Cart Service:
- Loads the active cart from its own database
- Extracts all productId values
- Calls Product Service via Feign:

```java
@FeignClient(name = "product-service", url = "${product.service.url}")
public interface ProductServiceClient {

    @PostMapping("/api/products/batch")
    List<CartProductDto> getProductsByIds(@RequestBody List<String> productIds);
}
```

- Merges product details into the cart response
- Returns enriched cart items to the client

---

## Kafka Integration

The service publishes checkout events using:

- `KafkaTemplate<String, CartCheckoutEvent>`
- JSON serialization (`JsonSerializer`)
- Topic configured via `topic.checkout`

Event example:

```json
{
  "id": "id123",
  "userId": "u123",
  "items": [
    { "id":"id111", "productId": "101", "quantity": 2 },
    { "id":"id222", "productId": "202", "quantity": 1 }
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
- Spring Cloud OpenFeign
