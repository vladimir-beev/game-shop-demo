# User Service

A dedicated microservice responsible for serving authenticated user information, profile data, and user‑related operations.  
It runs behind an API Gateway, which validates JWT tokens and forwards identity headers to this service.

---

## Features

- Access user profile and account data
- Secure access enforced through the API Gateway (the service does not handle authentication)

---

## Data Model

The service maintains:
- **UserProfile** — profile data (bio, email, phone number, avatar, etc.) 
---

## Identity Propagation

The API Gateway forwards:

- `X-User-Id`
- `X-User-Email`
- `X-User-Roles`

The service trusts only the gateway, not external clients.

---

## Endpoints

| Method | Endpoint            | Description              |
|--------|---------------------|--------------------------|
| `GET` | `/api/user/profile` | Fetch user profile info  |
| `PUT` | `/api/user/profile` | Update user profile info |

---

## Tech Stack

- Java 21
- Spring Boot 4
- Spring Web
- Spring Data JPA
- H2 (dev) or PostgreSQL / MySQL
