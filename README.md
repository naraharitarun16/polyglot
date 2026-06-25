# Polyglot Chat Application

## 📋 Project Overview

A **real-time chat application** with user authentication, message persistence, and multi-language support built with modern web technologies.

### 🎯 Key Features
- ✅ **User Authentication** - Signup/Login with password hashing (BCrypt)
- ✅ **Real-time Messaging** - WebSocket-based instant messaging
- ✅ **Database Persistence** - H2 in-memory database (upgradeable to PostgreSQL/MySQL)
- ✅ **Email Validation** - Unique username and email constraints
- ✅ **JWT Authorization** - Secure token-based authentication
- ✅ **Message History** - All messages stored and retrievable
- ✅ **User Management** - Track user activity and last login

---

## 🏗️ Project Architecture

```
polyglot-chat-app/
│
├── backend/                    # Spring Boot REST API & WebSocket Server
│   ├── src/main/java/
│   │   └── com/polyglot/chat/
│   │       ├── ChatApplication.java       (Main entry point)
│   │       ├── config/                    (Spring configuration)
│   │       ├── controller/                (REST endpoints)
│   │       ├── model/                     (JPA entities)
│   │       ├── security/                  (JWT & auth)
│   │       └── service/                   (Business logic)
│   ├── src/main/resources/
│   │   ├── application.properties         (Configuration)
│   │   └── static/                        (Static files)
│   └── pom.xml                            (Maven dependencies)
│
├── frontend/                   # React Web Application
│   ├── src/
│   │   ├── App.js              (Main React component)
│   │   ├── components/
│   │   │   ├── Login.js        (Login page)
│   │   │   └── Chat.js         (Chat interface)
│   │   └── index.js            (React entry point)
│   └── package.json            (NPM dependencies)
│
└── docs/                       # Documentation
    ├── API.md                  (REST API endpoints)
    ├── DATABASE.md             (Database schema)
    ├── SETUP.md                (Quick start guide)
    └── ARCHITECTURE.md         (System design)
```

---

## 🔧 Technology Stack

### Backend
| Technology | Version | Purpose |
|-----------|---------|---------|
| **Spring Boot** | 3.5.10 | REST API Framework |
| **Java** | 17 LTS | Programming Language |
| **Spring Data JPA** | Latest | ORM & Database Access |
| **H2 Database** | 2.3.232 | In-Memory Database |
| **Spring Security** | 6.2.15 | Authentication |
| **JWT** | Latest | Token Authorization |
| **WebSocket** | STOMP | Real-time Messaging |

### Frontend
| Technology | Version | Purpose |
|-----------|---------|---------|
| **React** | 18.2.0 | UI Framework |
| **STOMP** | 7.3.0 | WebSocket Client |
| **JavaScript (ES6)** | - | Programming Language |

---

## 🚀 Quick Start

### Prerequisites
- Java 17+ (JDK)
- Node.js 14+ & npm
- Maven 3.8+

### Backend Setup
```bash
cd chat
./mvnw clean package -DskipTests
java -jar target/chat-0.0.1-SNAPSHOT.jar --server.port=8082
```
**Backend runs on:** `http://localhost:8082`  
**H2 Console:** `http://localhost:8082/h2-console`

### Frontend Setup
```bash
cd chat-frontend
npm install
npm start
```
**Frontend runs on:** `http://localhost:3000`

---

## 🔐 Authentication Flow

1. **Signup** → User creates account → Password hashed with BCrypt → Stored in database
2. **Login** → User sends credentials → Validated with BCrypt → JWT token issued
3. **Chat** → Client sends JWT in headers → Server validates token → Message delivered

### Sample API Requests

**Signup:**
```json
POST /auth/signup
{
  "username": "alice",
  "password": "securePassword123",
  "email": "alice@example.com",
  "fullName": "Alice Johnson"
}
```

**Login:**
```json
POST /auth/login
{
  "username": "alice",
  "password": "securePassword123"
}
```

**Send Message:**
```json
WebSocket: ws://localhost:8082/ws
{
  "sender": "alice",
  "content": "Hello everyone!",
  "timestamp": "2026-02-13T10:30:00"
}
```

---

## 📊 Database Schema

### Users Table
| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| username | VARCHAR(50) | UNIQUE, NOT NULL |
| password | VARCHAR(255) | NOT NULL (Hashed) |
| email | VARCHAR(100) | UNIQUE, NOT NULL |
| fullName | VARCHAR(100) | NOT NULL |
| createdAt | TIMESTAMP | AUTO (NOW) |
| lastLogin | TIMESTAMP | Updated on login |
| isActive | BOOLEAN | Default: true |

### ChatMessages Table
| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| sender | VARCHAR(50) | FOREIGN KEY to Users |
| content | TEXT | NOT NULL |
| language | VARCHAR(20) | DEFAULT: 'en' |
| createdAt | TIMESTAMP | AUTO (NOW) |

---

## 📁 Key Files Explained

### Backend Components
- **ChatApplication.java** - Spring Boot main class that starts the server
- **AuthController.java** - Handles /auth/signup, /auth/login endpoints
- **ChatController.java** - Manages WebSocket /ws connections and messaging
- **User.java** - Database entity for user accounts with validation
- **ChatMessage.java** - Database entity for chat messages
- **UserService.java** - Business logic: registration, validation, password hashing
- **ChatMessageService.java** - Message persistence and retrieval
- **SecurityConfig.java** - Spring Security configuration with BCrypt encoder

### Frontend Components
- **App.js** - Main React component with routing
- **Login.js** - User login/signup page
- **Chat.js** - Real-time chat interface
- **index.js** - React DOM entry point

---

## 🧪 Testing

### Test API Endpoints
Open `chat/test.http` in VS Code with REST Client extension:
```
### Create new user
POST http://localhost:8082/auth/signup
Content-Type: application/json

{
  "username": "alice",
  "password": "password123",
  "email": "alice@example.com",
  "fullName": "Alice Johnson"
}

### Login
POST http://localhost:8082/auth/login
Content-Type: application/json

{
  "username": "alice",
  "password": "password123"
}

### Get user count
GET http://localhost:8082/auth/users/count
```

---

## 🛠️ Development Tools

### Database Manager
```bash
# Windows
cd chat && .\open-database.bat

# Or navigate to
http://localhost:8082/h2-console
```

### Build & Run
```bash
# Build only
cd chat && ./mvnw clean package

# Build and run
cd chat && ./mvnw spring-boot:run

# Run JAR directly
java -jar chat/target/chat-0.0.1-SNAPSHOT.jar --server.port=8082
```

---

## 📚 Documentation

- **[API.md](docs/API.md)** - Complete REST API reference
- **[DATABASE.md](docs/DATABASE.md)** - Database schema details
- **[SETUP.md](docs/SETUP.md)** - Installation instructions
- **[ARCHITECTURE.md](docs/ARCHITECTURE.md)** - System design patterns

---

## 📈 Project Structure Benefits

✅ **Clear Separation** - Backend API separate from frontend UI  
✅ **Scalability** - Each component can be scaled independently  
✅ **Maintainability** - Easy to locate and modify features  
✅ **Testability** - Backend APIs testable with REST Client  
✅ **Interview Ready** - Professional structure impresses interviewers  

---

## 🔄 Deployment Ready

### Backend Deployment
- ✅ Spring Boot JAR can run anywhere with Java 17+
- ✅ H2 upgradeable to PostgreSQL/MySQL with single config change
- ✅ Docker-ready (Dockerfile can be added)

### Frontend Deployment
- ✅ React build creates optimized static files
- ✅ Can be served by any web server
- ✅ Can be deployed to Netlify, Vercel, AWS S3 + CloudFront

---

## 👤 Author
Developed for polyglot chat application showcase

**Last Updated:** February 13, 2026
#   p o l y g l o t  
 #   p o l y g l o t  
 #   p o l y g l o t  
 