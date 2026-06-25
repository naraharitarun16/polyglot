# 🚀 GET STARTED - Signup Feature

## ⚡ Quick Start (2 Minutes)

### Step 1: Test Signup
Open VS Code and click the "Send Request" button next to this request in `test.http`:

```
POST http://localhost:8082/auth/signup
Content-Type: application/json

{
  "username": "alice",
  "password": "password123",
  "email": "alice@example.com",
  "fullName": "Alice Johnson"
}
```

**Expected Response:**
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

### Step 2: Login
Test login with the same credentials:

```
POST http://localhost:8082/auth/login
Content-Type: application/json

{
  "username": "alice",
  "password": "password123"
}
```

### Step 3: Check Database
Open H2 Console to verify user was saved:
1. Go to: http://localhost:8082/h2-console
2. Click "Connect"
3. Run query: `SELECT * FROM users;`

## 🎯 Common Scenarios

### Scenario 1: Create Multiple Users
```
Signup User 1:
username: john_doe
password: secure123
email: john@example.com
fullName: John Doe

Signup User 2:
username: jane_smith
password: safe456
email: jane@example.com
fullName: Jane Smith
```

### Scenario 2: Test Validation
Try these and see the errors:

**Weak Password (< 6 chars)**
```json
{
  "username": "testuser",
  "password": "weak",
  "email": "test@example.com"
}
// Error: Password must be at least 6 characters
```

**Duplicate Username**
```json
{
  "username": "alice",
  "password": "newpass123",
  "email": "newemail@example.com"
}
// Error: Username 'alice' is already taken
```

**Invalid Email**
```json
{
  "username": "newuser",
  "password": "password123",
  "email": "invalid-email"
}
// Error: Invalid email format
```

## 📊 Verify It Works

### Check 1: Count Total Users
```
GET http://localhost:8082/auth/users/count

Response: {"totalUsers":2}
```

### Check 2: View All Users in H2
```sql
SELECT * FROM users;
```
Should show your registered users with encrypted passwords

### Check 3: Verify Password Hashing
```sql
SELECT username, password FROM users WHERE username='alice';
```
Password should look like: `$2a$10$abc...xyz` (BCrypt hash)

## 💡 Tips & Tricks

### Tip 1: Copy Connection Details
Open `database-manager.html` and click "Copy Connection Info"

### Tip 2: Use Postman
If you prefer Postman to VS Code:
1. Import the `test.http` file
2. Or manually create POST request to `/auth/signup`
3. Add JSON body with user data

### Tip 3: Test with cURL
```bash
# Quick signup test
curl -X POST http://localhost:8082/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username":"quicktest","password":"pass123","email":"quick@test.com"}'
```

### Tip 4: Generate Test Data
```bash
# Create 5 test users quickly
for i in {1..5}; do
  curl -X POST http://localhost:8082/auth/signup \
    -H "Content-Type: application/json" \
    -d "{\"username\":\"user$i\",\"password\":\"pass$i\",\"email\":\"user$i@test.com\"}"
done
```

## ✅ Checklist

After implementing, verify:

- [ ] Application running on port 8082
- [ ] Signup endpoint responds with user data
- [ ] Login works with created credentials
- [ ] Database shows encrypted passwords
- [ ] Duplicate usernames rejected
- [ ] Duplicate emails rejected
- [ ] Weak passwords rejected
- [ ] User count increments
- [ ] Email validation works
- [ ] H2 console accessible

## 🔧 Troubleshooting

### Issue: Application won't start
**Solution**: 
```bash
# Kill existing Java processes
Get-Process java | Stop-Process -Force

# Rebuild
cd d:\projectpayplot\chat
.\mvnw clean package -DskipTests

# Restart
java -jar target\chat-0.0.1-SNAPSHOT.jar --server.port=8082
```

### Issue: Can't find signup endpoint
**Solution**: Make sure the application compiled successfully
```bash
# Check for compile errors
.\mvnw clean compile
```

### Issue: Database queries not working
**Solution**: Open H2 console and verify connection
1. JDBC URL: `jdbc:h2:mem:chatdb`
2. Username: `sa`
3. Password: `chat123`

## 📚 Related Documentation

- **SIGNUP_FEATURE.md** - Complete feature guide
- **SIGNUP_QUICK_REFERENCE.md** - API quick reference
- **DATABASE_SETUP.md** - Database configuration
- **test.http** - All test requests

## 🎉 You're All Set!

Your signup feature is ready to use. Start testing now:

1. Open `test.http`
2. Find "Register/Signup New User - Alice"
3. Click "Send Request"
4. See the user created in response!

---

**Happy coding!** 🚀
