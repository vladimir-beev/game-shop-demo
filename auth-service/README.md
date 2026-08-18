# AuthService — Central Identity Provider for Microservices

A Spring Boot–based authentication and identity provider designed for modern microservice architectures.  
This service issues **JWT access tokens**, manages **single‑session refresh tokens**, and stores **user credentials and roles**.  
It is intended to run behind an API Gateway, acting as the **source of truth** for authentication and identity.

---

## Purpose

In a distributed system, authentication must be **centralized**, while microservices remain **stateless** and focused on business logic.  
This AuthService provides a unified identity layer that integrates with a Spring Cloud Gateway.

It handles:

- User authentication  
- Token issuing  
- Refresh token rotation  
- Role-based identity  

Downstream microservices never parse JWTs—they trust the gateway, which trusts this service.

---

## Responsibilities

- Register new users  
- Authenticate existing users  
- Issue short-lived **access tokens**  
- Issue and rotate **refresh tokens**  
- Enforce **single-session** refresh token policy  
- Store and manage **roles**  
- Provide identity information to the API Gateway  

---

## Not Responsible For

This service intentionally does **not** handle:

- Authorization of requests to other microservices  
- User profile data (e.g., avatar, preferences)  
- Multi-device session management  
- Business logic for any domain  

These belong to the API Gateway and other microservices (e.g., UserService).

---

## Endpoints

| Endpoint        | Method | Description                                      |
|-----------------|--------|--------------------------------------------------|
| `/auth/register` | POST   | Register a new user                              |
| `/auth/login`    | POST   | Authenticate and receive access + refresh tokens |
| `/auth/refresh`  | POST   | Rotate refresh token and issue new access token  |
| `/auth/logout`   | POST   | Invalidate the current refresh token             |

---

## Token Model

### Access Token
- Short-lived  
- Contains email + roles  
- Used by the API Gateway for authorization  
- Never stored in the database  

### Refresh Token
- Stored in the database  
- **Single-session**: logging in again invalidates the previous token  
- Used to obtain new access tokens without re-authenticating  

---

## Security Flow

1. User logs in via `/auth/login`  
2. AuthService returns:  
   - Access token  
   - Refresh token  
3. API Gateway validates the access token  
4. Gateway forwards identity headers (email, roles) to microservices  
5. User refreshes tokens via `/auth/refresh`  
6. Old refresh token is invalidated and replaced

---

## Data Model

The service maintains:

- **User** — credentials, email, password hash  
- **UserRole** — role definitions (`ROLE_USER`, `ROLE_ADMIN`, etc.)  
- **RefreshToken** — single active token per user  

Roles and the default admin account are initialized at startup.

---

## Environment Variables

- **JWT_SECRET_KEY**
- **ADMIN_EMAIL**
- **ADMIN_USERNAME**
- **ADMIN_PASSWORD**

---

## Tech Stack

- Java 21  
- Spring Boot 4  
- Spring Security  
- Spring Data JPA  
- JWT (jjwt)  
- H2 (dev) or PostgreSQL / MySQL  
