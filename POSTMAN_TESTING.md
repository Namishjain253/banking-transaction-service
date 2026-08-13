# Postman API Testing Guide

## 1. Overview

This document describes how to test the **Banking Transaction Service** APIs using Postman.

Postman can be used to manually verify:

* Authentication
* JWT generation
* Protected APIs
* Request validation
* Transaction operations
* HTTP status codes
* Response payloads
* Error handling

---

# 2. Prerequisites

Before starting Postman testing, make sure:

* Java 21 is installed.
* MySQL is running.
* Required environment variables are configured.
* The Banking Transaction Service is running.
* Postman is installed.

Start the application using:

```bash
mvn spring-boot:run
```

Confirm that the application starts successfully before testing APIs.

---

# 3. Base URL

For local development, the base URL is normally:

```text
http://localhost:8080
```

If the application uses a different port, use the configured application port.

Create a Postman environment variable:

```text
BASE_URL
```

Example:

```text
BASE_URL=http://localhost:8080
```

Then use:

```text
{{BASE_URL}}
```

in API requests.

---

# 4. Recommended Postman Environment

Create a Postman environment named:

```text
Banking Transaction - Local
```

Recommended variables:

| Variable         | Example                 | Purpose         |
| ---------------- | ----------------------- | --------------- |
| `BASE_URL`       | `http://localhost:8080` | Application URL |
| `TOKEN`          | Generated JWT           | Authentication  |
| `USER_ID`        | Test user ID            | Test data       |
| `ACCOUNT_ID`     | Test account ID         | Test data       |
| `TRANSACTION_ID` | Test transaction ID     | Test data       |

Do not store real production credentials in Postman environments.

For shared Postman collections, use placeholders or secret variables.

---

# 5. API Authentication Flow

The recommended testing flow is:

```text
Login API
    |
    v
Receive JWT
    |
    v
Save JWT in Postman
    |
    v
Call Protected API
    |
    v
Authorization: Bearer <JWT>
```

---

# 6. Authentication API

Use the actual authentication endpoint implemented by the application.

Example structure:

```text
POST {{BASE_URL}}/<authentication-endpoint>
```

### Headers

```text
Content-Type: application/json
```

### Request Body

Use the request structure defined by the application.

Example:

```json
{
  "username": "testuser",
  "password": "testpassword"
}
```

### Expected Response

A successful authentication request should return an authentication response containing a JWT or authentication information according to the implementation.

Example:

```json
{
  "token": "<JWT>"
}
```

**Do not use a real production password or token in documentation.**

---

# 7. Save JWT Automatically in Postman

If the authentication response contains the JWT in a field named `token`, a Postman test script can store it automatically.

Example:

```javascript
const response = pm.response.json();

if (response.token) {
    pm.environment.set("TOKEN", response.token);
}
```

If the application uses a different response field, update the script accordingly.

---

# 8. Calling Protected APIs

For APIs requiring authentication, configure:

```text
Authorization
Type: Bearer Token
Token: {{TOKEN}}
```

Postman will send:

```text
Authorization: Bearer <JWT>
```

Do not manually copy the JWT into every request if the Postman environment variable can be used.

---

# 9. Transaction API Testing

Use the actual transaction endpoints implemented by the application.

A recommended Postman collection structure is:

```text
Banking Transaction Service
│
├── Authentication
│   └── Login
│
├── Transactions
│   ├── Create Transaction
│   ├── Get Transaction
│   ├── Get Transactions
│   ├── Update Transaction
│   └── Delete Transaction
│
└── Negative Tests
    ├── Unauthorized Request
    ├── Invalid JWT
    ├── Invalid Request
    └── Resource Not Found
```

Only include operations that actually exist in the application.

---

# 10. Create Transaction

Use the actual create-transaction endpoint.

Example:

```text
POST {{BASE_URL}}/<transaction-endpoint>
```

### Authorization

```text
Bearer Token
{{TOKEN}}
```

### Headers

```text
Content-Type: application/json
```

### Request Body

Use the exact DTO/request structure defined by the application.

Example placeholder:

```json
{
  "accountId": "{{ACCOUNT_ID}}",
  "amount": 1000.00,
  "type": "CREDIT"
}
```

### Verify

Check:

* HTTP status
* Response body
* Transaction ID
* Transaction amount
* Transaction type
* Account information
* Database persistence

---

# 11. Get Transaction

Example:

```text
GET {{BASE_URL}}/<transaction-endpoint>/{{TRANSACTION_ID}}
```

### Authorization

```text
Bearer Token
{{TOKEN}}
```

### Verify

* Correct HTTP status
* Correct transaction ID
* Correct transaction details
* No sensitive information exposed

---

# 12. Get Transactions

Example:

```text
GET {{BASE_URL}}/<transaction-endpoint>
```

### Authorization

```text
Bearer Token
{{TOKEN}}
```

Verify:

* HTTP status
* Response format
* Returned transaction records
* Pagination/filtering if implemented

---

# 13. Update Transaction

If transaction updates are supported:

```text
PUT {{BASE_URL}}/<transaction-endpoint>/{{TRANSACTION_ID}}
```

or the HTTP method implemented by the application.

Verify:

* Authentication
* Request validation
* Updated response
* Database state

---

# 14. Delete Transaction

If transaction deletion is supported:

```text
DELETE {{BASE_URL}}/<transaction-endpoint>/{{TRANSACTION_ID}}
```

Verify:

* Authentication
* Authorization
* HTTP status
* Resource deletion
* Subsequent GET behavior

---

# 15. Validation Testing

Test invalid requests intentionally.

Examples:

### Missing required field

```json
{
  "amount": 1000.00
}
```

### Invalid amount

```json
{
  "amount": -100
}
```

### Empty value

```json
{
  "accountId": ""
}
```

### Invalid data type

```json
{
  "amount": "invalid"
}
```

The expected HTTP status and error response should match the application's exception-handling implementation.

---

# 16. Authentication Negative Tests

## No JWT

Send a protected request without an Authorization header.

Expected result:

```text
Unauthorized / Forbidden
```

according to the application's security configuration.

---

## Invalid JWT

Set:

```text
TOKEN=invalid-token
```

Call a protected API.

Expected result:

```text
Authentication failure
```

---

## Expired JWT

Use an expired token and call a protected API.

Expected result:

```text
Authentication failure
```

---

# 17. Resource Not Found

Use a transaction ID that does not exist.

Example:

```text
GET {{BASE_URL}}/<transaction-endpoint>/999999999
```

Verify that the application returns the expected not-found response.

---

# 18. Postman Tests

Postman tests can automatically validate responses.

Example:

```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});
```

Check response time:

```javascript
pm.test("Response time is acceptable", function () {
    pm.expect(pm.response.responseTime).to.be.below(2000);
});
```

Check JSON response:

```javascript
pm.test("Response is JSON", function () {
    pm.response.to.be.json;
});
```

Check a response field:

```javascript
pm.test("Transaction ID is present", function () {
    const response = pm.response.json();
    pm.expect(response).to.have.property("id");
});
```

The exact assertions should match the application's real response structure.

---

# 19. Recommended Test Sequence

Execute the Postman collection in this order:

```text
1. Start Application
        ↓
2. Verify Application Health
        ↓
3. Authenticate
        ↓
4. Save JWT
        ↓
5. Create Test Transaction
        ↓
6. Capture Transaction ID
        ↓
7. Get Transaction
        ↓
8. Get Transactions
        ↓
9. Update Transaction (if supported)
        ↓
10. Delete Transaction (if supported)
        ↓
11. Test Validation Errors
        ↓
12. Test Invalid JWT
        ↓
13. Test Missing JWT
        ↓
14. Test Not Found
```

---

# 20. Postman Collection Naming

Recommended collection name:

```text
Banking Transaction Service API
```

Recommended folders:

```text
Banking Transaction Service API
│
├── 01 - Authentication
├── 02 - Transactions
├── 03 - Validation Tests
├── 04 - Security Tests
└── 05 - Negative Tests
```

---

# 21. Test Evidence

For important testing, capture:

* Request URL
* HTTP method
* Request headers
* Request body
* Response status
* Response body
* Test result

Do not capture or commit real:

* Passwords
* JWT secrets
* Access tokens
* API keys
* Database credentials

---

# 22. Definition of Done

Postman testing is considered complete when:

* [ ] Application starts successfully.
* [ ] Authentication API works.
* [ ] JWT is generated successfully.
* [ ] JWT can access protected APIs.
* [ ] Transaction creation works.
* [ ] Transaction retrieval works.
* [ ] Transaction listing works.
* [ ] Update works, if implemented.
* [ ] Delete works, if implemented.
* [ ] Validation errors are handled correctly.
* [ ] Missing JWT is rejected.
* [ ] Invalid JWT is rejected.
* [ ] Not-found scenarios are handled.
* [ ] Response status codes are verified.
* [ ] Response bodies are verified.
* [ ] No sensitive information is stored in the collection.

---

# 23. Final Verification

Before committing Postman documentation or collections to Git:

```bash
git status
```

Review:

```bash
git diff
```

If a Postman collection is added, inspect it carefully for:

* Passwords
* JWT tokens
* API keys
* Database credentials
* Personal credentials

Then:

```bash
git add POSTMAN_TESTING.md
git diff --cached
git commit -m "Add Postman API testing documentation"
git push
```

---

## Important

The endpoint names and JSON payloads in this document are intentionally shown as placeholders until they are verified against the application's actual controllers and DTOs.

Once the source code is reviewed, replace:

```text
<authentication-endpoint>
<transaction-endpoint>
```

and the example JSON structures with the application's **actual API contracts**.
