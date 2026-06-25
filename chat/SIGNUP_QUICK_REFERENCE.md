# Quick Reference - Signup API

## 🚀 Quick Start

### Signup Request
```
POST http://localhost:8082/auth/signup
{
  "username": "your_username",
  "password": "your_password",
  "email": "your@email.com",
  "fullName": "Your Name"
}
```

### Login Request
```
POST http://localhost:8082/auth/login
{
  "username": "your_username",
  "password": "your_password"
}
```

## ✅ Validation Rules

- **Username**: 3-50 characters, must be unique
- **Password**: Minimum 6 characters
- **Email**: Must be valid and unique
- **Full Name**: Optional

## 📊 Database Fields

User accounts include:
- id (auto-generated)
- username (unique)
- password (encrypted with BCrypt)
- email (unique)
- fullName (optional)
- createdAt (automatic)
- lastLogin (automatic)
- isActive (default: true)

## 🔒 Security

✓ Passwords are hashed with BCrypt
✓ Email format validated
✓ Duplicate prevention for username & email
✓ JWT token generation on successful registration
✓ User activity tracking (last login)

## 📍 Key Endpoints

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/auth/signup` | Create new account |
| POST | `/auth/register` | Alias for signup |
| POST | `/auth/login` | Login to account |
| GET | `/auth/users/count` | Get total users |
| GET | `/api/messages` | Get all messages |
| GET | `/h2-console` | Database console |

## 💡 Error Codes

| Code | Message | Solution |
|------|---------|----------|
| 400 | Invalid input | Check validation rules |
| 409 | Username/Email taken | Use different username/email |
| 401 | Invalid credentials | Check username and password |
| 500 | Server error | Check server logs |

## 🧪 Test It Now

```bash
# Signup
curl -X POST http://localhost:8082/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com",
    "fullName": "Test User"
  }'

# Login
curl -X POST http://localhost:8082/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

## 📁 Related Files

- `SIGNUP_FEATURE.md` - Complete documentation
- `test.http` - HTTP test requests
- `database-manager.html` - Database UI
- `open-database.bat` - Launch script
