# Payment API

A minimal Spring Boot **payment service** demonstrating idempotent payment processing, validation, global error‑handling and full test coverage.

---

## ✨ Features

* **POST /payments** endpoint with an `Idempotency-Key` header to prevent duplicate charges
* Bean Validation (Jakarta) on incoming payloads
* Global exception handling (`@RestControllerAdvice`) with clear JSON error responses
* H2 in‑memory database via Spring Data JPA
* Maven Wrapper for reproducible builds
* Unit & Web‑layer tests (JUnit 5, Mockito, Spring Test)

---

## 🛠 Tech stack

| Layer       | Tech                          |
| ----------- | ----------------------------- |
| Runtime     | Java 21                       |
| Framework   | Spring Boot 3.5               |
| Persistence | Spring Data JPA + H2          |
| Build       | Maven 4 (wrapper)             |
| Testing     | JUnit 5, Mockito, Spring Test |
| Code Gen    | Lombok 1.18.30                |

---

## 🚀 Getting started

### Prerequisites

* **Java 21** JDK
* **Maven** (or use the included `mvnw` wrapper)

### Run locally

```bash
# build & run
./mvnw spring-boot:run

# or build a jar
./mvnw clean package
java -jar target/payment-api-0.0.1-SNAPSHOT.jar
```

App starts on **`http://localhost:8080`**.

---

## 📑 API

### Create / fetch a payment

```
POST /payments
Headers:
  Content-Type: application/json
  Idempotency-Key: <uuid>
Body:
{
  "clientId": "abc123",
  "amount": 99.99,
  "currency": "USD"
}
```

* **200 OK** – returns existing or newly‑created payment
* **400 Bad Request** – validation error or missing header
* **409 Conflict** – duplicate idempotency key (business rule)

#### Sample `curl`

```bash
curl -X POST http://localhost:8080/payments \
  -H "Idempotency-Key: 9f3a34b2-d45b-4d3f-a7e1-82ce2ea4589a" \
  -H "Content-Type: application/json" \
  -d '{"clientId":"abc","amount":49.99,"currency":"USD"}'
```

---

## 🧪 Testing

```bash
./mvnw test          # run unit & web-layer tests
```

Generated coverage focuses on:

* `PaymentServiceImplTest` – business logic
* `PaymentControllerTest` – REST interface & validation

---

## 🗄 Database

Uses an **in‑memory H2** database by default (no setup). Connection details in `src/main/resources/application.properties`.

To connect while the app is running:

```
http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:testdb
```

---

## 🧩 Project structure

```
src/main/java/com/example/paymentapi
├── controller     # REST controllers
├── service        # Business logic (interface + impl)
├── repository     # Spring Data JPA repositories
├── model          # Entities & DTOs
└── exception      # Custom & global error handling
```

---

## 🤝 Contributing

1. Fork the repo & create a feature branch.
2. Run `./mvnw test` – all tests must pass.
3. Submit a PR. Please follow conventional commit messages.

---

## 📝 License

Ahmed Sherif © 2025
