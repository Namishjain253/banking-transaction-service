# Testing Guide

## 1. Overview

This document describes the testing approach for the **Banking Transaction Service**.

The application uses Spring Boot's testing framework and Maven for test execution.

The testing strategy covers:

* Unit testing
* Controller/API testing
* Service-layer testing
* Repository/database testing
* Security and JWT testing
* Validation testing
* Integration testing
* Build verification

---

## 2. Testing Technology

| Technology               | Purpose                            |
| ------------------------ | ---------------------------------- |
| Java 21                  | Application and test runtime       |
| Spring Boot 3.5.4        | Application framework              |
| Spring Boot Starter Test | Testing framework                  |
| JUnit                    | Unit and integration tests         |
| Spring Test              | Spring application context testing |
| Spring Data JPA          | Persistence testing                |
| MySQL                    | Database integration testing       |
| Spring Security          | Security testing                   |

---

## 3. Test Source Structure

Tests should be maintained under:

```text
src/
└── test/
    └── java/
```

A recommended package structure is:

```text
src/test/java/
└── org/hsbc/
    └── ...
```

Test packages should follow the structure of the production code where practical.

For example:

```text
src/main/java/org/hsbc/...
src/test/java/org/hsbc/...
```

---

# 4. Running Tests

## Run all tests

From the project root:

```bash
mvn test
```

This executes the test suite and reports the test results.

## Run the complete Maven verification

```bash
mvn verify
```

This is recommended before creating a pull request or release.

## Clean and test

```bash
mvn clean test
```

This removes previous build output and executes the tests from a clean state.

## Build and test

```bash
mvn clean install
```

This compiles the application, executes tests, and creates the application artifact.

---

# 5. Unit Testing

Unit tests should test individual classes in isolation.

The primary targets are:

* Service classes
* Business logic
* Utility classes
* Validation logic
* JWT-related components where appropriate

A unit test should avoid unnecessary dependencies on:

* MySQL
* External services
* The complete Spring application context

The goal is to make unit tests fast and deterministic.

### Example test structure

```java
class TransactionServiceTest {

    @Test
    void shouldCreateTransaction() {
        // Arrange

        // Act

        // Assert
    }
}
```

A good unit test should follow:

```text
Arrange
   ↓
Act
   ↓
Assert
```

---

# 6. Controller / API Testing

Controller tests should verify the REST API contract.

Important scenarios include:

### Successful request

Verify:

* HTTP status
* Response body
* Response structure
* Expected business result

### Invalid request

Verify:

* Validation is triggered
* Appropriate HTTP status is returned
* Error response is meaningful

### Missing required fields

Verify that requests containing missing mandatory fields are rejected.

### Invalid values

Verify that invalid transaction data is rejected correctly.

---

# 7. Authentication Testing

The application uses Spring Security and JWT authentication.

Security tests should verify both authenticated and unauthenticated access.

### Valid JWT

Expected behavior:

```text
Client
  |
  | Valid JWT
  v
Security Filter
  |
  | Token Valid
  v
Protected Endpoint
  |
  v
Successful Response
```

### Missing JWT

Expected behavior:

```text
Client
  |
  | No Authorization Header
  v
Protected Endpoint
  |
  v
Unauthorized Response
```

### Invalid JWT

The application should reject an invalid or malformed JWT.

### Expired JWT

An expired JWT should not provide access to protected endpoints.

### Authorization header

Protected API requests should use:

```text
Authorization: Bearer <JWT>
```

Never place a real JWT secret inside a test source file.

Use test-specific configuration or environment variables where required.

---

# 8. Validation Testing

The application uses Spring Boot Validation.

Validation tests should cover:

* Missing required fields
* Null values
* Empty values
* Invalid formats
* Invalid numeric values
* Boundary values
* Valid requests

Example:

```text
Valid Request
     |
     v
Validation
     |
     +---- Valid ------> Controller / Service
     |
     +---- Invalid ----> Error Response
```

---

# 9. Repository / Database Testing

Repository tests should verify persistence behavior.

Important scenarios include:

* Entity creation
* Entity retrieval
* Entity update
* Entity deletion
* Query behavior
* Invalid database operations
* Transaction behavior where applicable

Database integration tests should use an isolated test database or an appropriately configured test environment.

**Do not run destructive tests against a production database.**

---

# 10. Integration Testing

Integration tests verify that multiple application components work together.

Typical integration flow:

```text
HTTP Request
     |
     v
Controller
     |
     v
Security
     |
     v
Service
     |
     v
Repository
     |
     v
MySQL
```

Integration tests should verify the complete flow where appropriate.

Examples:

* Authentication → JWT generation
* JWT → Protected API
* API → Service
* Service → Repository
* Repository → Database

---

# 11. Test Data

Test data should be isolated from production data.

Recommended practices:

* Use dedicated test records.
* Avoid real customer information.
* Avoid production credentials.
* Avoid real JWT secrets.
* Clean up test data where necessary.
* Use deterministic test data.

Example:

```text
Test User
Test Account
Test Transaction
```

should be clearly identifiable as test data.

---

# 12. Negative Testing

Negative scenarios are especially important for a banking transaction service.

Tests should cover cases such as:

* Unauthorized request
* Invalid JWT
* Expired JWT
* Missing request fields
* Invalid transaction amount
* Invalid account information
* Duplicate transaction where applicable
* Transaction for a non-existent account
* Invalid transaction state
* Database failure
* Unexpected service errors

The exact scenarios should follow the business rules implemented by the application.

---

# 13. Security Testing

Security-related tests should verify:

* Protected endpoints cannot be accessed anonymously.
* Invalid JWT tokens are rejected.
* Expired JWT tokens are rejected.
* Authentication failures are handled correctly.
* Sensitive configuration is not exposed.
* Passwords or secrets are not returned in API responses.
* JWT secrets are not hard-coded in source code.

Sensitive values should be provided through environment variables.

Example:

```properties
jwt.secret=${JWT_SECRET}
```

---

# 14. Test Naming Convention

Use descriptive test names that explain the expected behavior.

Recommended:

```java
shouldCreateTransactionSuccessfully()
shouldRejectTransactionWhenAmountIsInvalid()
shouldRejectRequestWithoutAuthentication()
shouldRejectExpiredJwtToken()
shouldReturnTransactionWhenIdExists()
```

Avoid vague names such as:

```java
test1()
testTransaction()
checkData()
```

---

# 15. Test Execution Before Commit

Before committing code, run:

```bash
mvn clean test
```

Then verify Git changes:

```bash
git status
```

Review the changes:

```bash
git diff
```

If tests pass and the changes are correct:

```bash
git add .
git diff --cached
git commit -m "Add tests for transaction service"
```

---

# 16. Test Execution Before Push

Before pushing changes to GitHub:

```bash
mvn clean verify
```

Confirm the build succeeds.

Then:

```bash
git status
```

Finally:

```bash
git push
```

---

# 17. Test Checklist

Before considering a feature complete:

* [ ] Unit tests added
* [ ] Positive scenarios tested
* [ ] Negative scenarios tested
* [ ] Validation tested
* [ ] Authentication tested
* [ ] Authorization tested where applicable
* [ ] Repository behavior tested
* [ ] Integration flow tested where applicable
* [ ] No production credentials used
* [ ] No JWT secrets committed
* [ ] `mvn clean test` passes
* [ ] `mvn clean verify` passes
* [ ] Git working tree reviewed before push

---

# 18. Definition of Done

A feature can be considered test-complete when:

1. The expected business behavior is covered.
2. Important negative scenarios are covered.
3. Security requirements are tested.
4. Validation behavior is tested.
5. Relevant persistence behavior is tested.
6. Tests pass locally.
7. The Maven build succeeds.
8. No sensitive information is included in the repository.

---

## 19. Future Testing Improvements

As the project grows, the following can be considered:

* Increased unit-test coverage
* Automated integration tests
* Testcontainers for MySQL integration testing
* Automated API testing
* Security-focused test suites
* CI/CD test execution
* Code coverage reporting
* Static code analysis
* Performance testing for transaction APIs

These should be introduced based on the actual requirements and architecture of the service.
