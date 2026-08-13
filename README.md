# Banking Transaction Service

A Spring Boot–based banking transaction service that provides REST APIs for managing banking transactions with secure JWT-based authentication, MySQL persistence, request validation, and OpenAPI/Swagger documentation.

## Technology Stack

| Technology        | Version / Details           |
| ----------------- | --------------------------- |
| Java              | 21                          |
| Spring Boot       | 3.5.4                       |
| Build Tool        | Maven                       |
| REST API          | Spring Web / Spring MVC     |
| Persistence       | Spring Data JPA / Hibernate |
| Database          | MySQL                       |
| Security          | Spring Security             |
| Authentication    | JWT                         |
| JWT Library       | JJWT 0.12.6                 |
| Validation        | Jakarta Bean Validation     |
| API Documentation | Springdoc OpenAPI 2.8.9     |
| Testing           | Spring Boot Starter Test    |
| Utilities         | Lombok                      |
| Development       | Spring Boot DevTools        |

## Project Structure

```text
banking-transaction-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   └── test/
├── pom.xml
├── .gitignore
└── README.md
```

The detailed package structure should be documented after reviewing the application's source code.

## Prerequisites

Install the following before running the application:

* Java 21
* Maven
* MySQL
* Git

Verify Java:

```bash
java -version
```

Verify Maven:

```bash
mvn -version
```

## Configuration

The application uses environment variables for sensitive configuration.

For example, the JWT secret is configured using:

```properties
jwt.secret=${JWT_SECRET}
```

Set the required environment variables before starting the application.

Example:

```bash
export JWT_SECRET="your-local-development-secret"
```

**Do not commit real secrets, passwords, API keys, or tokens to Git.**

## Database Configuration

The application uses MySQL with Spring Data JPA/Hibernate.

Database credentials should be supplied through environment-specific configuration or environment variables rather than hard-coded into the source code.

Example configuration pattern:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/banking
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Use the actual database name and configuration from the project's application configuration when setting up a local environment.

## Build the Application

From the project root:

```bash
mvn clean install
```

To skip tests during a build:

```bash
mvn clean install -DskipTests
```

## Run the Application

Using Maven:

```bash
mvn spring-boot:run
```

Or build the JAR and run it:

```bash
mvn clean package
java -jar target/banking-transaction-service-1.0-SNAPSHOT.jar
```

The exact application port is determined by the Spring Boot configuration.

## API Documentation

The project uses Springdoc OpenAPI.

After starting the application, Swagger UI is normally available at:

```text
/swagger-ui/index.html
```

The OpenAPI specification is normally available at:

```text
/v3/api-docs
```

The exact URLs and available endpoints should be confirmed from the running application configuration.

## Authentication

The application uses Spring Security with JWT-based authentication.

The general authentication flow is:

```text
Client
  |
  | Authentication Request
  v
Authentication API
  |
  | Validate Credentials
  v
JWT Generation
  |
  | JWT
  v
Client
  |
  | Authorization: Bearer <JWT>
  v
Protected API
  |
  v
JWT Validation
  |
  v
Business Logic
```

JWT secrets must be provided through environment configuration and must never be committed to source control.

## Validation

The project includes Spring Boot Validation for validating incoming API requests.

Invalid requests should be rejected before reaching the core business logic.

## Persistence

The application uses:

```text
Spring Data JPA
       |
       v
Hibernate
       |
       v
MySQL
```

Entity and repository details should be documented based on the actual source implementation.

## Testing

The project includes Spring Boot's testing framework.

Run tests with:

```bash
mvn test
```

Run the complete verification lifecycle with:

```bash
mvn verify
```

## Development

For local development:

1. Clone the repository.
2. Configure Java 21.
3. Configure Maven.
4. Configure MySQL.
5. Set required environment variables.
6. Start the application.
7. Open Swagger UI to test the APIs.

## Git Workflow

Before committing changes:

```bash
git status
```

Review changes:

```bash
git diff
```

Stage changes:

```bash
git add .
```

Review staged changes:

```bash
git diff --cached
```

Commit:

```bash
git commit -m "Describe your change"
```

Push:

```bash
git push
```

Never commit:

* JWT secrets
* Database passwords
* API keys
* Access tokens
* Private keys
* Local environment files containing credentials

## Security

This project uses JWT authentication and Spring Security.

Security-sensitive values should always be supplied through environment variables or an appropriate external secret-management mechanism.

Example:

```properties
jwt.secret=${JWT_SECRET}
```

Do not replace the environment-variable reference with a real secret in committed configuration.

## Project Information

**Artifact:** `banking-transaction-service`

**Group:** `org.hsbc`

**Version:** `1.0-SNAPSHOT`

**Description:** Banking Transaction Service

## License

No license has currently been specified for this repository.
