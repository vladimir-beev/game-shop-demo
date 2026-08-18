## 🚀 Setup in 60 Seconds

Get the entire stack running with Docker Compose.  
Just prepare your `.env` file and start the containers.

### 1. Copy the content of the example environment file into a new `.env` file

`.env.example`

### 2. Generate a secure JWT secret

You must replace JWT_SECRET_KEY with a real HS256 key.   
Use the KeyGen utility inside the auth-service:
```java
public class KeyGen {
    public static void main(String[] args) {
        String key = Encoders.BASE64.encode(
            Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded()
        );
        System.out.println(key);
    }
}
```

Run it, copy the generated key, and paste it into:

```

JWT_SECRET_KEY=<your_generated_key>

```
This key is required for issuing and validating JWT access tokens.

### 3. Start the full stack
```

docker compose up --build -d

```
This launches:
- Kafka (KRaft mode)
- API Gateway
- Auth, User, Product, Cart, Order, Inventory services
- Frontend

### 4. Varify Kafka topics
Use `list-topics.bat` located in the scripts folder
- If topic auto-creation fails, manually create them using `create-topics.bat`


---

### Your environment is now fully operational.
#### By default client is available at `http:/localhost:3000`
#### A default admin user is created on startup with the following default credentials:
- email: `admin@mail.com`
- password: `admin123`

---

---

## Architecture Overview
A fully decoupled microservice architecture:
- API Gateway — entry point for all clients
- Auth Service — JWT authentication, admin bootstrap
- User Service — user profiles and management
- Product Service — product catalog
- Cart Service — shopping cart logic
- Order Service — order creation and orchestration
- Inventory Service — stock reservation and rejection
- Kafka Broker — event backbone (KRaft mode)

Communication patterns:
- REST between `frontend` <-> `api-gateway` <-> `services`
- Kafka events between `cart` <-> `order` <-> `inventory` 

---

## Kafka Topics
The system uses four core topics:

|    Topic     |	       Purpose                 |
|--------------|---------------------------------|
|cart.checkout | Checkout event from Cart Service| 
|order.created |	    Order created event        |
| order.cancelled |	Order cancellation event     |
| stock.rejected | 	Inventory rejection event    |

Scripts for managing topics:
- **Create:** scripts/create-topics.bat
- **Delete:** scripts/delete-topics.bat
- **List:** scripts/list-topics.bat

---

## JWT Refresh Flow

#### The platform uses short‑lived access tokens and longer‑lived refresh tokens to maintain secure and stateless authentication across the services.

### Flow Overview:
#### 1. Login / Authentication
- The client sends credentials to the Auth Service.
- Auth Service issues:
  - an access token (JWT, stored in-memory, short TTL)
  - a refresh token (HTTP‑only cookie, long TTL)

#### 2. Access Token Usage
- The client includes the access token in requests to the API Gateway.
- The gateway validates the token signature and expiration.
- If valid, the request is forwarded to downstream services.

#### 3. Access Token Expiration
- When the access token expires, the gateway returns a 401 Unauthorized.
- The client detects this and triggers a refresh request.

#### 4. Refresh Request
- The client sends the refresh token to the Auth Service (never to other services).
- Auth Service validates:
  - token signature
  - token expiration

#### 5. Token Rotation
- If valid, Auth Service issues:
  - a new access token
  - a new refresh token
- The old refresh token is removed to prevent replay attacks.

#### 6. Failure Handling
- If the refresh token is invalid, expired, or revoked:
  - Auth Service returns 401
  - The client logs the user out and redirects to the login page

## Security Characteristics
- Refresh tokens are never exposed to JavaScript (HTTP‑only cookie).
- Tokens are stateless, validated via signature (HS256).
- Rotation prevents reuse of stolen refresh tokens.
- API Gateway centralizes all token validation logic.
