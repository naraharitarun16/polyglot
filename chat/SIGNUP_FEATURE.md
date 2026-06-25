# Chat Application - Signup Feature Documentation

## Overview
Your chat application now includes a complete user signup/registration system with password hashing, email validation, and database persistence.

## New Features

### 1. **User Registration with Email**
- Users can now sign up with username, password, email, and optional full name
- All user data is saved to the database
- Email and username uniqueness is enforced

### 2. **Password Security**
- Passwords are encrypted using BCrypt algorithm
- Stored passwords cannot be reversed
- Login validates against encrypted passwords

### 3. **Input Validation**
- **Username**: 3-50 characters, unique
- **Password**: Minimum 6 characters for security
- **Email**: Valid email format required, unique per user
- **Full Name**: Optional field

### 4. **User Profile Information**
- Account creation timestamp automatically recorded
- Last login timestamp tracked
- Active/inactive status for user management
- User ID for authentication

## API Endpoints

### Signup Endpoint
```
POST /auth/signup
Content-Type: application/json

{
  "username": "john_doe",
  "password": "securepassword123",
  "email": "john@example.com",
  "fullName": "John Doe"
}
```

**Response (Success - 201 Created):**
```json
{
  "success": true,
  "message": "User registered successfully! Please log in.",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "fullName": "John Doe"
  }
}
```

**Response (Duplicate Username - 409 Conflict):**
```json
{
  "success": false,
  "message": "Username 'john_doe' is already taken"
}
```

**Response (Duplicate Email - 409 Conflict):**
```json
{
  "success": false,
  "message": "Email 'john@example.com' is already registered"
}
```

**Response (Weak Password - 400 Bad Request):**
```json
{
  "success": false,
  "message": "Password must be at least 6 characters"
}
```

### Login Endpoint (Updated)
```
POST /auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "securepassword123"
}
```

**Response (Success - 200 OK):**
```json
{
  "success": true,
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "fullName": "John Doe"
  }
}
```

**Response (Failure - 401 Unauthorized):**
```json
{
  "success": false,
  "message": "Invalid username or password"
}
```

### Get User Count
```
GET /auth/users/count

Response:
{
  "totalUsers": 5
}
```

## Database Schema

### Users Table
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  full_name VARCHAR(100),
  created_at TIMESTAMP NOT NULL,
  last_login TIMESTAMP,
  is_active BOOLEAN DEFAULT TRUE
);
```

## Usage Examples

### Example 1: Create New Account
```bash
curl -X POST http://localhost:8082/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "password": "mypassword123",
    "email": "alice@email.com",
    "fullName": "Alice Smith"
  }'
```

### Example 2: Login with Credentials
```bash
curl -X POST http://localhost:8082/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "password": "mypassword123"
  }'
```

### Example 3: Try Duplicate Username
```bash
curl -X POST http://localhost:8082/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "alice",
    "password": "different123",
    "email": "alice2@email.com",
    "fullName": "Alice Johnson"
  }'
# Response: Username 'alice' is already taken
```

## Validation Rules

| Field | Rules | Example |
|-------|-------|---------|
| Username | 3-50 chars, unique | `john_doe`, `user123` |
| Password | Minimum 6 chars | `securepass123`, `MyP@ss2024` |
| Email | Valid format, unique | `user@example.com` |
| Full Name | Optional, any length | `John Doe`, optional |

## Security Features

✅ **Password Hashing**: BCrypt encryption  
✅ **Input Validation**: Server-side validation for all fields  
✅ **Email Uniqueness**: Prevents duplicate email registrations  
✅ **Username Uniqueness**: Prevents duplicate usernames  
✅ **JWT Tokens**: Secure token-based authentication  
✅ **User Status Tracking**: Active/inactive user management  

## Testing the Feature

### Using REST Client (VS Code)
1. Open `test.http` file in VS Code
2. Click "Send Request" on the signup request
3. Replace the username/email with a unique value
4. View the response with user data
5. Copy the token for authenticated requests

### Using Postman
1. Create POST request to `http://localhost:8082/auth/signup`
2. Set Headers: `Content-Type: application/json`
3. Add JSON body with signup details
4. Send and view response

### Using cURL
```bash
curl -X POST http://localhost:8082/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com",
    "fullName": "Test User"
  }'
```

## Database Verification

### View All Users in H2 Console
1. Open: `http://localhost:8082/h2-console`
2. Run query:
```sql
SELECT * FROM users;
```

### Check User Registration Count
```
GET http://localhost:8082/auth/users/count
```

## Features Summary

| Feature | Status | Details |
|---------|--------|---------|
| User Registration | ✅ Complete | Full signup with email & validation |
| Password Hashing | ✅ Complete | BCrypt encryption |
| Email Validation | ✅ Complete | Format and uniqueness checks |
| Login | ✅ Updated | Enhanced with user profile info |
| Database Persistence | ✅ Complete | All data saved in H2 database |
| JWT Tokens | ✅ Complete | Token generation on signup |
| Input Validation | ✅ Complete | Server-side validation |
| Error Handling | ✅ Complete | Detailed error messages |

## Next Steps

To further enhance your application:
1. Add password reset functionality
2. Implement email verification
3. Add social login (Google, GitHub)
4. Create user profile endpoints
5. Add role-based access control
6. Implement password strength meter
7. Add two-factor authentication
