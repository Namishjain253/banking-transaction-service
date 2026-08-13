# Banking Transaction Service — Architecture

## 1. Overview

The Banking Transaction Service is a Spring Boot REST API application designed to provide banking transaction-related functionality.

The application follows a layered architecture that separates:

- API/HTTP handling
- Security and authentication
- Business logic
- Data access
- Database persistence
- Configuration
- Validation
- Exception handling

The application is built using Java 21 and Spring Boot 3.5.4.

---

# 2. Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Application runtime and development |
| Spring Boot 3.5.4 | Application framework |
| Spring MVC | REST API implementation |
| Spring Data JPA | Data access |
| Hibernate | ORM |
| MySQL | Relational database |
| Spring Security | Authentication and authorization |
| JJWT 0.12.6 | JWT creation/validation |
| Jakarta Validation | Request validation |
| Lombok | Boilerplate reduction |
| Springdoc OpenAPI | Swagger/API documentation |
| Maven | Build and dependency management |

---

# 3. High-Level Architecture

The application follows a layered architecture.

```text
                    ┌───────────────────────┐
                    │       API Client      │
                    │ Postman / Frontend /  │
                    │ Other REST Clients    │
                    └───────────┬───────────┘
                                │
                                │ HTTP/HTTPS
                                ▼
                    ┌───────────────────────┐
                    │    Spring Security    │
                    │                       │
                    │ JWT Authentication    │
                    │ Authorization         │
                    └───────────┬───────────┘
                                │
                                ▼
                    ┌───────────────────────┐
                    │      Controllers      │
                    │                       │
                    │ REST API Layer        │
                    └───────────┬───────────┘
                                │
                                ▼
                    ┌───────────────────────┐
                    │       Services        │
                    │                       │
                    │ Business Logic        │
                    └───────────┬───────────┘
                                │
                                ▼
                    ┌───────────────────────┐
                    │     Repositories      │
                    │                       │
                    │ Spring Data JPA       │
                    └───────────┬───────────┘
                                │
                                ▼
                    ┌───────────────────────┐
                    │        MySQL          │
                    │                       │
                    │ Persistent Data       │
                    └───────────────────────┘
