# 🏛️ System Architecture & Design Patterns

## 📐 High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    CLIENT LAYER (Web)                       │
│  ┌────────────────────────────────────────────────────────┐ │
│  │           React Frontend (Port 3000)                   │ │
│  │   - Login Component                                    │ │
│  │   - Chat Component                                     │ │
│  │   - Message Display & Input                           │ │
│  └────────────────────────────────────────────────────────┘ │
└──────────────────┬──────────────────────────────────────────┘
                   │ HTTP & WebSocket
                   ▼
┌─────────────────────────────────────────────────────────────┐
│                   SERVER LAYER (Spring Boot)                │
│  ┌────────────────────────────────────────────────────────┐ │
│  │           API Layer (Controllers)                      │ │
│  │   - AuthController  (/auth/signup, /auth/login)       │ │
│  │   - ChatController  (/api/messages, /ws)              │ │
│  └────────────────────────────────────────────────────────┘ │
│                        │                                    │
│  ┌────────────────────▼──────────────────────────────────┐ │
│  │        Business Logic Layer (Services)                │ │
│  │   - UserService (Auth, Validation, Password Hash)    │ │
│  │   - ChatMessageService (Message CRUD)                │ │
│  └────────────────────┬──────────────────────────────────┘ │
│                       │                                     │
│  ┌────────────────────▼──────────────────────────────────┐ │
│  │      Data Access Layer (Repository Pattern)           │ │
│  │   - UserRepository (Query users from DB)              │ │
│  │   - ChatMessageRepository (Query messages)            │ │
│  └────────────────────┬──────────────────────────────────┘ │
└──────────────────────┼──────────────────────────────────────┘
                       │ JPA/Hibernate
                       ▼
┌─────────────────────────────────────────────────────────────┐
│                   PERSISTENCE LAYER                         │
│  ┌────────────────────────────────────────────────────────┐ │
│  │    H2 Database (Development)                           │ │
│  │    - Users Table                                       │ │
│  │    - ChatMessages Table                                │ │
│  │                                                         │ │
│  │    PostgreSQL/MySQL (Production)                       │ │
│  └────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

---

## 🎯 Design Patterns Used

### 1. **MVC (Model-View-Controller)**
- **Model:** User, ChatMessage (JPA Entities)
- **View:** React Components (Login, Chat)
- **Controller:** AuthController, ChatController (REST Endpoints)

### 2. **Repository Pattern**
```java
// Data access abstraction
UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
```

**Benefits:**
- Decouples business logic from database
- Easy to mock for testing
- Database implementation can change independently

### 3. **Service Layer Pattern**
```java
// Business logic separation
@Service
UserService {
    public void registerUser(String username, String password, String email) {
        validateInput();
        hashPassword();
        saveToDatabase();
    }
}
```

**Benefits:**
- Centralized business logic
- Reusable across multiple controllers
- Testable in isolation

### 4. **DTO (Data Transfer Object) Pattern**
```java
// SignupRequest DTO
public class SignupRequest {
    private String username;
    private String password;
    private String email;
    private String fullName;
}

// AuthResponse DTO
public class AuthResponse {
    private boolean success;
    private String message;
    private String token;
    private UserInfo user;
}
```

**Benefits:**
- Validates input data
- Decouples API contracts from entities
- Easier to add/remove fields

### 5. **Singleton Pattern**
```java
@Service
@Bean
public class UserService {
    // Spring automatically manages single instance
}
```

### 6. **Strategy Pattern (Configuration)**
```java
@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### 7. **Observer Pattern (WebSocket)**
```java
// Subscribers listen to message events
@Controller
public class ChatController {
    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public Message handleMessage(Message message) {
        return message;
    }
}
```

---

## 🔐 Security Architecture

### Authentication Flow
```
User Input (Login)
    ↓
AuthController.login()
    ↓
UserService.validateUser()
    ↓
BCryptPasswordEncoder.matches(
    plainPassword, 
    hashedPassword
)
    ↓
JWT Token Generation
    ↓
Client Receives Token
    ↓
Client Includes Token in Headers
    ↓
Protected Endpoints Validate Token
```

### Password Security
```
Plain Password Input
    ↓
BCryptPasswordEncoder.encode()  (10 iterations)
    ↓
$2a$10$N9qo8uLOickgx2ZMRZoMye...  (Hashed)
    ↓
Stored in Database (Not Plain Text!)
    ↓
On Login: BCryptPasswordEncoder.matches()
    ↓
Compare with stored hash
```

**Why BCrypt?**
- Irreversible hashing (one-way)
- Slow by design (prevents brute force)
- Salted automatically (prevents rainbow tables)
- Industry standard

---

## 📡 Communication Patterns

### REST API (HTTP Request-Response)
```
Client (React)
    │
    ├─ HTTP POST → POST /auth/signup
    │              Headers: Content-Type: application/json
    │              Body: {username, password, email, fullName}
    │              
    └─ Receives JWT Token in Response
       Uses token for authenticated requests
```

### WebSocket (Bidirectional Communication)
```
Client (React)                          Server (Spring Boot)
    │                                        │
    │  WebSocket Connection (ws://)           │
    │◄──────────────────────────────────────►│
    │                                         │
    │  Subscribe: /topic/messages             │
    ├─────────────────────────────────────────│
    │                                         │
    │  Send Message JSON                      │
    ├────────────────────────────────────────►│ Receives on /app/send
    │                                         │ Broadcasts to all subscribers
    │  Receive Message JSON (Real-time)       │
    │◄────────────────────────────────────────┤
```

**Benefits:**
- Instant message delivery (no polling)
- Bidirectional communication
- Reduced server load
- Better user experience

---

## 📊 Data Flow Diagram

### User Signup Flow
```
1. User enters signup form (React)
2. Click Submit
    ↓
3. POST /auth/signup with SignupRequest DTO
    ↓
4. AuthController receives request
    ↓
5. UserService.registerUser() called
    ├─ validateRegistrationInput()
    │  ├─ Check username length (3-50)
    │  ├─ Check password length (6+)
    │  └─ Validate email format
    │
    ├─ Check duplicate username
    ├─ Check duplicate email
    │
    ├─ BCryptPasswordEncoder.encode()
    │
    └─ userRepository.save(newUser)
    ↓
6. User saved to H2 Database
    ↓
7. Return AuthResponse (success + token)
    ↓
8. Client stores JWT token
    ↓
9. User redirected to Chat page
```

### Message Send Flow
```
1. User types message (React Chat.js)
2. Click Send
    ↓
3. WebSocket Message Sent to /app/send
    │
    ├─ Message: {sender: "alice", content: "Hello"}
    ├─ Includes JWT token
    │
4. ChatController.sendMessage() receives
    ↓
5. ChatMessageService.saveMessage()
    │
    ├─ Create ChatMessage entity
    └─ Save to database
    ↓
6. Message Stored in H2 (CHAT_MESSAGES table)
    ↓
7. Message Broadcast to /topic/messages
    ↓
8. All Connected Clients Receive Message
    ↓
9. React Chat Component Re-renders
    │
    └─ Message appears for all users
```

---

## 🔄 Class Structure

### Entity Layer
```
User (JPA Entity)
├─ id: Long (PK)
├─ username: String (UNIQUE)
├─ password: String (hashed)
├─ email: String (UNIQUE)
├─ fullName: String
├─ createdAt: LocalDateTime
├─ lastLogin: LocalDateTime
└─ isActive: Boolean

ChatMessage (JPA Entity)
├─ id: Long (PK)
├─ sender: String (FK to User)
├─ content: String
├─ language: String
└─ createdAt: LocalDateTime
```

### DTO Layer
```
SignupRequest (Input DTO)
├─ username: String
├─ password: String
├─ email: String
└─ fullName: String

AuthResponse (Output DTO)
├─ success: Boolean
├─ message: String
├─ token: String
└─ user: UserInfo
```

### Service Layer
```
UserService
├─ registerUser(...)
├─ validateUser(...)
├─ validateRegistrationInput(...)
├─ findByUsername(...)
├─ findByEmail(...)
└─ updateLastLogin(...)

ChatMessageService
├─ saveMessage(...)
├─ getMessagesBySender(...)
└─ getAllMessages(...)
```

### Repository Layer
```
UserRepository extends JpaRepository<User, Long>
├─ findByUsername(): Optional<User>
├─ findByEmail(): Optional<User>
├─ existsByUsername(): Boolean
└─ existsByEmail(): Boolean

ChatMessageRepository extends JpaRepository<ChatMessage, Long>
├─ findBySenderOrderByCreatedAtDesc(): List
└─ findAllByOrderByCreatedAtDesc(): List
```

---

## ⚙️ Technology Integration

### Spring Boot Ecosystem
```
┌─ Spring Core (DI, Beans)
├─ Spring MVC (Controllers, REST)
├─ Spring Data JPA (Repositories, Queries)
├─ Spring Security (Authentication, Encryption)
├─ Spring WebSocket (Real-time messaging)
└─ Tomcat (Embedded Web Server)
```

### External Libraries
```
├─ Hibernate 6.6 (ORM Implementation)
├─ H2 Database (Development DB)
├─ JWT (Token generation)
├─ Jackson (JSON serialization)
├─ Lombok (Optional, getters/setters)
└─ Validation API (Jakarta)
```

### Frontend Libraries
```
├─ React 18.2 (UI Framework)
├─ STOMP 7.3 (WebSocket Protocol)
├─ SockJS (WebSocket Fallback)
└─ Fetch API (HTTP Requests)
```

---

## 🎯 Future Scalability

### Horizontal Scaling
```
Load Balancer (Nginx)
    │
    ├─ Spring Boot Instance 1 (Port 8082)
    ├─ Spring Boot Instance 2 (Port 8083)
    └─ Spring Boot Instance 3 (Port 8084)
    
Shared Database (PostgreSQL)
```

### Message Queue (Kafka/RabbitMQ)
```
Messages → Message Queue → Multiple Consumers
(Enables async processing)
```

### Caching Layer (Redis)
```
User Sessions → Redis Cache
Message History → Redis Cache
(Reduces database load)
```

---

## 📈 Performance Characteristics

| Operation | Current | Optimized |
|-----------|---------|-----------|
| User Signup | O(1) | O(1) with caching |
| Send Message | O(1) | O(1) with batch write |
| Get All Messages | O(n) | O(log n) with pagination |
| Find User | O(1) | O(1) with caching |
| Login | O(1)* | O(1) with Redis |

*BCrypt makes it slower by design for security

---

## 🔍 Monitoring & Logging

### Recommended Additions
```
├─ Spring Boot Actuator (metrics endpoint)
├─ SLF4J + Logback (structured logging)
├─ Micrometer (application metrics)
└─ ELK Stack (centralized logging)
```

---

**Architecture Version:** 1.0  
**Last Updated:** February 13, 2026
