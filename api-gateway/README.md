# API Gateway

A centralized entry point for all client requests in the microservice architecture.  
The gateway handles authentication, authorization, routing, request filtering, and path rewriting.  
It validates JWT tokens, extracts identity information, and forwards trusted headers to downstream services.

---

## Features

- Centralized routing for all microservices
- JWT validation and identity extraction
- Role‑based access control via custom `RoleCheck` filter
- Path rewriting and request normalization

---

## Responsibilities

The API Gateway performs:

- **Authentication**  
  Validates incoming JWT tokens using the `JwtAuth` filter.


- **Identity Propagation**  
  Injects trusted headers into downstream requests:
    - `X-User-Id`
    - `X-User-Email`
    - `X-User-Roles`


- **Authorization**  
  Enforces role‑based access using the `RoleCheck` filter.


- **Routing**
  - Forwards requests to the appropriate microservice based on path predicates.
  - Normalizes internal service paths using `RewritePath`.

---

## Custom Filters

### **JwtAuth Filter**
- Validates JWT tokens
- Extracts claims
- Rejects invalid or expired tokens
- Injects identity headers into the request
- Returns `401 Unauthorized` on missing or invalid / expired tokens

### **RoleCheck Filter**
- Reads `X-User-Roles`
- Compares against required roles configured per route
- Supports multiple roles (comma‑separated)
- Returns `403 Forbidden` on insufficient permissions

---

## Environment Variables

- `JWT_SECRET_KEY`
- `JWT_ISSUER`
- `AUTH_SERVICE_URI`
- `USER_SERVICE_URI`
- `FRONTEND_URI`

---
## Example Route Configuration

```yaml
- id: exmpl-service
  uri: ${EXMPL_SERVICE_URI} #ENV Variable
  predicates:
    - Path=/exmpl/**
  filters:
    - JwtAuth
    - name: RoleCheck
      args:
        requiredRoles: ROLE_USER,ROLE_ADMIN
    - RewritePath=/exmpl/(?<segment>.*), /api/exmpl/${segment}
```
---

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Cloud Gateway
- Spring WebFlux
- JWT (JJWT)
- Spring Security (filter chain host for custom JWT + role filters)