# Accounts Microservice

A Spring Boot microservice for managing various types of financial accounts.

## Overview

The Accounts Microservice is responsible for managing different types of financial accounts:
- Savings / Current Bank Accounts
- Credit Card Accounts
- Loan Accounts
- Investment Accounts (e.g., stocks, bonds)
- Digital Wallets

## Tech Stack

- **Language**: Java 17+
- **Framework**: Spring Boot (Spring MVC)
- **Database**: PostgreSQL
- **Messaging**: Apache Kafka
- **Cache**: Spring Cache + Redis
- **API Security**: JWT + HTTPS
- **Configuration**: Spring Cloud Config
- **Secret Management**: HashiCorp Vault
- **Observability**: Prometheus + Grafana + Loki
- **API Documentation**: SpringDoc OpenAPI (Swagger)
- **Code Style**: Checkstyle + Google Java Format
- **Linting/Static Analysis**: PMD + SpotBugs
- **Containerization**: Docker
- **Orchestration**: Kubernetes
- **Unit Testing**: JUnit 5 + Mockito + Testcontainers

## Getting Started

### Prerequisites

- Java 17+
- Maven
- Docker and Docker Compose
- PostgreSQL (or use the Docker Compose setup)

### Running Locally

1. Clone the repository
2. Set up environment variables:
   ```
   export DB_USERNAME=postgres
   export DB_PASSWORD=postgres
   export VAULT_TOKEN=dev-token
   export JWT_SECRET=your-jwt-secret-key
   ```

3. Start the dependencies using Docker Compose:
   ```
   docker-compose up -d postgres redis kafka zookeeper
   ```

4. Run the application:
   ```
   ./mvnw spring-boot:run
   ```

### Running with Docker Compose

Start the entire application and its dependencies:
```
docker-compose up -d
```

## API Documentation

When the application is running, you can access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## API Endpoints

| Method | Endpoint                    | Description                   | Auth |
| ------ | --------------------------- | ----------------------------- | ---- |
| POST   | `/accounts`                 | Create a new account          | ✅    |
| GET    | `/accounts/{accountNumber}` | Fetch account by number       | ✅    |
| GET    | `/accounts/user/{userId}`   | Paginated accounts for a user | ✅    |
| PUT    | `/accounts/{accountNumber}` | Update account                | ✅    |
| DELETE | `/accounts/{accountNumber}` | Soft delete account           | ✅    |

## Database Schema

The service uses the following database tables:
- `accounts`: Main account information
- `account_type`: Types of accounts
- `account_details`: Additional details specific to account types

## Monitoring

- Prometheus metrics are exposed at `/actuator/prometheus`
- A Grafana dashboard is available at `http://localhost:3000` when running with Docker Compose

## Testing

Run unit tests:
```
./mvnw test
```

Run integration tests:
```
./mvnw verify
```

## Code Quality

Check code style:
```
./mvnw checkstyle:check
```

Run static analysis:
```
./mvnw pmd:check spotbugs:check
```

## Building for Production

Build the JAR file:
```
./mvnw clean package
```

Build the Docker image:
```
docker build -t accounts-service .
```
