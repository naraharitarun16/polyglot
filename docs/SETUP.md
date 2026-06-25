# 🚀 Quick Setup Guide

## Prerequisites
- Java 17 or higher (JDK)
- Node.js 14+ and npm
- Maven 3.8+
- Git (optional)

---

## Step-by-Step Installation

### 1️⃣ Backend Setup

```bash
# Navigate to backend directory
cd chat

# Build the application (first time only, takes 2-3 minutes)
./mvnw clean package -DskipTests

# Start the backend server
java -jar target/chat-0.0.1-SNAPSHOT.jar --server.port=8082
```

**Expected Output:**
```
Started ChatApplication in 8.3 seconds (JVM running for 9.1)
Tomcat started on port 8082
```

**Access:**
- Backend API: `http://localhost:8082`
- H2 Console: `http://localhost:8082/h2-console`

---

### 2️⃣ Frontend Setup

```bash
# Navigate to frontend directory
cd chat-frontend

# Install dependencies
npm install

# Start development server
npm start
```

**Expected Output:**
```
Compiled successfully!
You can now view polyglot-chat-frontend in the browser.
Local: http://localhost:3000
```

**Access:**
- Frontend App: `http://localhost:3000`

---

## 3️⃣ Test the Application

### Create a User Account
1. Go to `http://localhost:3000`
2. Click "Sign Up"
3. Fill in:
   - Username: `alice`
   - Email: `alice@example.com`
   - Full Name: `Alice Johnson`
   - Password: `securePass123`
4. Click "Sign Up"

### Login
1. Use the credentials you just created
2. Start sending messages in real-time

### Database Verification
1. Go to `http://localhost:8082/h2-console`
2. Enter JDBC URL: `jdbc:h2:mem:chatdb`
3. Enter username: `sa`
4. Enter password: `chat123`
5. Click "Connect"
6. Query: `SELECT * FROM USERS`
7. You should see your newly created user

---

## 🛠️ Common Issues & Solutions

### Issue: Port 8082 already in use
```bash
# Kill existing Java process
taskkill /F /IM java.exe

# Or use different port
java -jar target/chat-0.0.1-SNAPSHOT.jar --server.port=8083
```

### Issue: npm modules not installing
```bash
# Clear npm cache
npm cache clean --force

# Remove node_modules and reinstall
rmdir /S /Q node_modules package-lock.json
npm install
```

### Issue: Maven build fails
```bash
# Clean Maven cache
./mvnw clean

# Try building again
./mvnw package -DskipTests
```

---

## 📝 Environment Variables (Optional)

Create a `.env` file in `chat` directory for custom settings:

```properties
# Server
SERVER_PORT=8082

# Database
DB_URL=jdbc:h2:mem:chatdb
DB_DRIVER=org.h2.Driver
DB_USERNAME=sa
DB_PASSWORD=chat123

# JWT
JWT_SECRET=your-secret-key-here
JWT_EXPIRATION=86400000
```

---

## ✅ Verification Checklist

- [ ] Java 17+ installed: `java -version`
- [ ] Maven installed: `./mvnw --version`
- [ ] Node.js installed: `npm --version`
- [ ] Backend builds successfully: `./mvnw clean package`
- [ ] Backend starts on port 8082
- [ ] H2 Console accessible at `http://localhost:8082/h2-console`
- [ ] Frontend installs successfully: `npm install`
- [ ] Frontend starts on port 3000
- [ ] Can create user account
- [ ] Can login successfully
- [ ] Can send messages
- [ ] Messages appear in database

---

## 🎯 Next Steps

1. **Test API Endpoints** → Open `test.http` file in VS Code
2. **Explore Database** → Connect to H2 Console
3. **Read Documentation** → See [API.md](API.md) for endpoints
4. **Customize** → Modify login page colors, chat interface, etc.

---

## 📞 Support

If you encounter issues:
1. Check the error message in terminal
2. Verify Java version: `java -version`
3. Check logs for detailed error information
4. Restart both backend and frontend
5. Clear cache: `npm cache clean --force`
