# Banking Transaction Service — Local Setup Guide

## 1. Purpose

This document explains how to set up and run the **Banking Transaction Service** on a new development system.

The goal is to take a clean machine from:

```text
GitHub Repository
      ↓
Install Prerequisites
      ↓
Clone Project
      ↓
Configure MySQL
      ↓
Configure Environment Variables
      ↓
Build Application
      ↓
Run Application
      ↓
Test APIs
```

---

# 2. Prerequisites

The following software is required.

| Software      | Required Version                 |
| ------------- | -------------------------------- |
| Java          | 21                               |
| Maven         | 3.x                              |
| Git           | Latest stable version            |
| MySQL         | Compatible MySQL 8.x recommended |
| Postman       | Optional                         |
| IntelliJ IDEA | Optional                         |

The application itself uses:

* Java 21
* Spring Boot 3.5.4
* Maven
* Spring Web
* Spring Data JPA
* Hibernate
* MySQL
* Spring Security
* JWT
* Spring Validation
* Springdoc OpenAPI

---

# 3. Verify Java

Open Terminal.

Run:

```bash
java -version
```

The project requires Java 21.

Expected output should contain:

```text
21
```

Also verify the Java compiler:

```bash
javac -version
```

---

# 4. Verify Maven

Run:

```bash
mvn -version
```

Verify that Maven is using Java 21.

Example:

```text
Apache Maven ...
Java version: 21
```

---

# 5. Verify Git

Run:

```bash
git --version
```

Git is required to clone and update the repository.

---

# 6. Verify MySQL

Make sure MySQL is installed and running.

Check the MySQL client:

```bash
mysql --version
```

Log in:

```bash
mysql -u root -p
```

Use the appropriate MySQL username for your local environment.

---

# 7. Clone the Repository

Clone the project:

```bash
git clone https://github.com/Namishjain253/banking-transaction-service.git
```

Move into the project directory:

```bash
cd banking-transaction-service
```

Verify the repository:

```bash
git status
```

You should see the current Git branch and working-tree status.

---

# 8. Open the Project

The project can be opened using IntelliJ IDEA or another Java IDE.

For IntelliJ IDEA:

1. Open IntelliJ IDEA.
2. Select **Open**.
3. Select the `banking-transaction-service` directory.
4. Allow IntelliJ to import the Maven project.
5. Verify that Java 21 is selected as the project SDK.

---

# 9. Configure MySQL

The application uses MySQL through Spring Data JPA/Hibernate.

Create a local database using the database name configured for the application.

Example:

```sql
CREATE DATABASE banking;
```

The actual database name should match the application's configuration.

Do not use production databases for local development.

---

# 10. Configure Database Credentials

Database credentials should not be hard-coded into Git-tracked source files.

Use environment variables or local configuration.

Example:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Example local environment values:

```text
DB_URL=jdbc:mysql://localhost:3306/banking
DB_USERNAME=your-local-username
DB_PASSWORD=your-local-password
```

Use the actual property names configured by the application.

---

# 11. Configure JWT Secret

The application uses JWT authentication.

The JWT secret should be provided through an environment variable.

Example:

```properties
jwt.secret=${JWT_SECRET}
```

Set the variable locally.

macOS/Linux:

```bash
export JWT_SECRET="your-local-development-secret"
```

Verify that it is available:

```bash
echo $JWT_SECRET
```

**Never commit the actual JWT secret to GitHub.**

---

# 12. IntelliJ Environment Variables

If the application is started from IntelliJ IDEA, environment variables must be available to the application's run configuration.

Open:

```text
Run
 → Edit Configurations
 → Spring Boot Application
 → Environment variables
```

Add the required local variables.

Example:

```text
JWT_SECRET=your-local-development-secret
DB_USERNAME=your-local-username
DB_PASSWORD=your-local-password
DB_URL=jdbc:mysql://localhost:3306/banking
```

Use local values only.

Do not commit these values to Git.

---

# 13. Build the Project

From the project root:

```bash
mvn clean install
```

This will:

1. Clean previous build output.
2. Compile the application.
3. Execute tests.
4. Package the application.

If successful, Maven should report:

```text
BUILD SUCCESS
```

---

# 14. Run Tests

Run:

```bash
mvn clean test
```

All automated tests should pass before starting development.

For full Maven verification:

```bash
mvn clean verify
```

---

# 15. Start the Application

Run:

```bash
mvn spring-boot:run
```

Alternatively:

```bash
mvn clean package
```

Then:

```bash
java -jar target/banking-transaction-service-1.0-SNAPSHOT.jar
```

---

# 16. Verify Application Startup

Check the application console.

A successful startup should indicate that Spring Boot has started successfully and that the application is listening on its configured port.

If startup fails, check:

1. Java version.
2. MySQL availability.
3. Database URL.
4. Database username.
5. Database password.
6. JWT secret.
7. Application configuration.
8. Port availability.

---

# 17. Swagger / OpenAPI

The project includes Springdoc OpenAPI.

Once the application is running, open:

```text
http://localhost:8080/swagger-ui/index.html
```

The OpenAPI specification is normally available at:

```text
http://localhost:8080/v3/api-docs
```

If the application uses a different port or context path, adjust the URL accordingly.

---

# 18. Postman Testing

Postman can be used to manually test the APIs.

Recommended sequence:

```text
Start Application
       ↓
Authentication
       ↓
Receive JWT
       ↓
Set JWT in Postman
       ↓
Call Protected APIs
       ↓
Test Transactions
       ↓
Test Validation
       ↓
Test Security
```

Refer to:

```text
POSTMAN_TESTING.md
```

for the API testing procedure.

---

# 19. Development Workflow

Before starting development:

```bash
git pull
```

Check the current state:

```bash
git status
```

Create a feature branch when appropriate:

```bash
git checkout -b feature/<feature-name>
```

Make the required changes.

Run tests:

```bash
mvn clean test
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
git commit -m "Describe the change"
```

Push:

```bash
git push
```

---

# 20. Sensitive Configuration

Never commit:

```text
JWT secrets
Database passwords
API keys
Access tokens
Private keys
Production credentials
Personal credentials
```

Before committing, check:

```bash
git status
```

and:

```bash
git diff --cached
```

Make sure no sensitive values are present.

---

# 21. Troubleshooting

## JWT_SECRET not found

Error:

```text
Could not resolve placeholder 'JWT_SECRET'
```

Solution:

Set the environment variable:

```bash
export JWT_SECRET="your-local-development-secret"
```

If running from IntelliJ, add the variable to the application's Run Configuration.

---

## MySQL connection failure

Check:

* MySQL is running.
* Database exists.
* Username is correct.
* Password is correct.
* JDBC URL is correct.
* MySQL port is correct.

---

## Java version error

Run:

```bash
java -version
```

The project requires Java 21.

---

## Maven build failure

Run:

```bash
mvn clean
mvn clean test
```

Review the first meaningful error in the Maven output.

---

## Port already in use

If the configured application port is already being used, stop the process using the port or configure a different local application port.

---

# 22. New Developer Checklist

* [ ] Install Java 21.
* [ ] Install Maven.
* [ ] Install Git.
* [ ] Install MySQL.
* [ ] Clone the repository.
* [ ] Open the project in IntelliJ IDEA.
* [ ] Configure Java 21.
* [ ] Create the local MySQL database.
* [ ] Configure database credentials.
* [ ] Configure `JWT_SECRET`.
* [ ] Verify environment variables.
* [ ] Run `mvn clean test`.
* [ ] Run the application.
* [ ] Verify application startup.
* [ ] Open Swagger UI.
* [ ] Configure Postman.
* [ ] Execute authentication testing.
* [ ] Execute transaction API testing.
* [ ] Verify Git status before committing.

---

# 23. Setup Complete

The local environment is considered ready when:

```text
Java 21
   +
Maven
   +
MySQL
   +
Environment Variables
   +
Project Dependencies
   +
Successful Maven Build
   +
Successful Application Startup
   +
Successful API Test
```

At this point, the developer can begin working on the Banking Transaction Service.
