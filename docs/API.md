# 🔌 REST API Reference

## Base URL
```
http://localhost:8082
```

---

## Authentication Endpoints

### 1. Sign Up (Create Account)
```http
POST /auth/signup
Content-Type: application/json

{
  "username": "alice",
  "password": "securePassword123",
  "email": "alice@example.com",
  "fullName": "Alice Johnson"
}
```

**Response (Success):**
```json
{
  "success": true,
  "message": "User registered successfully! Please log in.",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "username": "alice",
    "email": "alice@example.com",
    "fullName": "Alice Johnson"
  }
}
```

**Response (Error):**
```json
{
  "success": false,
  "message": "Username already exists",
  "token": null,
  "user": null
}
```

**Validation Rules:**
- Username: 3-50 characters
- Password: Minimum 6 characters
- Email: Valid email format, must be unique
- Full Name: Required, non-empty

---

### 2. Login
```http
POST /auth/login
Content-Type: application/json

{
  "username": "alice",
  "password": "securePassword123"
}
```

**Response (Success):**
```json
{
  "success": true,
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "username": "alice",
    "email": "alice@example.com",
    "fullName": "Alice Johnson"
  }
}
```

**Response (Error):**
```json
{
  "success": false,
  "message": "Invalid username or password",
  "token": null,
  "user": null
}
```

---

### 3. Register (Alias for Sign Up)
```http
POST /auth/register
Content-Type: application/json

{
  "username": "bob",
  "password": "securePassword456",
  "email": "bob@example.com",
  "fullName": "Bob Smith"
}
```

Same response format as `/auth/signup`

---

### 4. Get User Count
```http
GET /auth/users/count
```

**Response:**
```json
{
  "totalUsers": 5,
  "activeUsers": 4
}
```

---

## Message Endpoints

### 5. Get All Messages
```http
GET /api/messages
```

**Response:**
```json
[
  {
    "id": 1,
    "sender": "alice",
    "content": "Hello everyone!",
    "language": "en",
    "createdAt": "2026-02-13T10:30:00"
  },
  {
    "id": 2,
    "sender": "bob",
    "content": "Hi Alice!",
    "language": "en",
    "createdAt": "2026-02-13T10:31:00"
  }
]
```

---

### 6. Get Messages by Sender
```http
GET /api/messages/sender/alice
```

**Response:**
```json
[
  {
    "id": 1,
    "sender": "alice",
    "content": "Hello everyone!",
    "language": "en",
    "createdAt": "2026-02-13T10:30:00"
  }
]
```

---

## WebSocket Connection

### Real-time Messaging via WebSocket

**Connect:**
```javascript
const stompClient = new StompClient();
stompClient.connect(
  {},
  () => {
    console.log('Connected');
    stompClient.subscribe('/topic/messages', (message) => {
      console.log('Received:', message.body);
    });
  }
);
```

**Send Message:**
```javascript
stompClient.send('/app/send', {}, JSON.stringify({
  sender: 'alice',
  content: 'Hello everyone!'
}));
```

**Message Format:**
```json
{
  "sender": "alice",
  "content": "Hello everyone!",
  "timestamp": "2026-02-13T10:30:00"
}
```

---

## Error Codes

| Code | Meaning | Solution |
|------|---------|----------|
| 400 | Bad Request | Check request format and required fields |
| 401 | Unauthorized | Login required / Invalid token |
| 404 | Not Found | Resource doesn't exist |
| 409 | Conflict | Username/Email already exists |
| 500 | Server Error | Server issue, try again later |

---

## Authentication Headers

For protected endpoints (future use):

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## Testing with REST Client

**VS Code Setup:**
1. Install "REST Client" extension
2. Open `chat/test.http`
3. Click "Send Request" above each endpoint

**Postman Setup:**
1. Create new collection
2. Import requests from examples above
3. Set environment variable: `BASE_URL = http://localhost:8082`
4. Use `{{BASE_URL}}/auth/login` in requests

---

## Rate Limits
Currently: **No rate limiting** (can be added in production)

## CORS
Currently: **Enabled for localhost:3000** (frontend)

## API Version
**Current Version:** 1.0.0  
**Last Updated:** February 13, 2026
