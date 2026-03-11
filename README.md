# Spring Boot Microservices Project

A microservices-based e-commerce backend built with Spring Boot, Spring Cloud, and Netflix Eureka. The system is composed of four independent services that work together to handle product management and order processing.

---

## Architecture Overview

```
Client
  │
  ▼
API Gateway (port 8080)        ← Single entry point for all requests
  │
  ├──▶ Product Service (port 8081)   ← Manages product catalog
  └──▶ Order Service   (port 8082)   ← Manages orders, calls Product Service
              │
              └──▶ Product Service (via Feign Client)

All services register with:
Eureka Server (port 8761)      ← Service discovery & registry
```

---

## Services

### 1. Eureka Server (`eureka-server`) — Port 8761
The service registry. All other services register themselves here, and can discover each other by name rather than hardcoded URLs. Acts as the "phone book" for the system.

### 2. API Gateway (`api-gateway`) — Port 8080
The single entry point for all client requests. Routes incoming traffic to the appropriate service using Spring Cloud Gateway (MVC). Also performs client-side load balancing via the `lb://` URI scheme.

| Route | Forwarded To |
|-------|-------------|
| `/api/products/**` | `product-service` |
| `/api/orders/**` | `order-service` |

### 3. Product Service (`product-service`) — Port 8081
A CRUD service for managing the product catalog. Persists data to a MySQL database (`products_db`).

**Endpoints:**
| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/products` | Create a new product |
| `GET` | `/api/products` | List all products |
| `GET` | `/api/products/{id}` | Get a product by ID |
| `PUT` | `/api/products/{id}` | Update a product |
| `DELETE` | `/api/products/{id}` | Delete a product |

### 4. Order Service (`order-service`) — Port 8082
Handles order placement and management. When an order is placed, it calls the Product Service (via OpenFeign) to look up product details and calculate the total price. Persists data to a MySQL database (`orders_db`).

**Endpoints:**
| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/orders` | Place a new order |
| `GET` | `/api/orders` | List all orders |
| `GET` | `/api/orders/{id}` | Get an order by ID |
| `PUT` | `/api/orders/{id}/cancel` | Cancel an order |

---

## Tech Stack

- **Java 21**
- **Spring Boot 3.5.11**
- **Spring Cloud 2025.0.1**
- **Spring Cloud Gateway (WebMVC)** — API routing
- **Netflix Eureka** — Service discovery
- **OpenFeign** — Declarative inter-service HTTP client
- **Spring Data JPA + Hibernate** — Database ORM
- **MySQL** — Persistent storage
- **Lombok** — Boilerplate reduction
- **Maven** — Build tool

---

## Getting Started

### Prerequisites
- Java 21
- Maven 3.9+
- MySQL (running locally on port 3306)

### Database Setup
Create the two required databases in MySQL:
```sql
CREATE DATABASE products_db;
CREATE DATABASE orders_db;
```

### Running the Services
Start each service in this order (Eureka must be first):

```bash
# 1. Start Eureka Server
cd eureka-server && ./mvnw spring-boot:run

# 2. Start Product Service
cd product-service && ./mvnw spring-boot:run

# 3. Start Order Service
cd order-service && ./mvnw spring-boot:run

# 4. Start API Gateway
cd api-gateway && ./mvnw spring-boot:run
```

Once all services are running, you can verify registration at the Eureka dashboard: http://localhost:8761

All requests should go through the gateway at **http://localhost:8080**.

---

## Example Usage

**Create a product:**
```bash
POST http://localhost:8080/api/products
Content-Type: application/json

{
  "name": "Laptop",
  "description": "A powerful laptop",
  "price": 999.99,
  "stockQuantity": 50
}
```

**Place an order:**
```bash
POST http://localhost:8080/api/orders
Content-Type: application/json

{
  "productId": 1,
  "quantity": 2
}
```

---

## Project Structure

```
├── eureka-server/       # Service registry
├── api-gateway/         # API gateway & routing
├── product-service/     # Product CRUD microservice
└── order-service/       # Order management microservice
```
