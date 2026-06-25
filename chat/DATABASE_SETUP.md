# Database Setup for Chat Application

## Overview
Your chat application now has a complete database setup to save user logins and messages. The application uses:
- **H2 Database** - In-memory relational database
- **Spring Data JPA** - Object-Relational Mapping (ORM)
- **Hibernate** - JPA implementation for database operations

## Database Configuration

### Tables Created

#### 1. **users** Table
Stores user login information:
- `id` (BIGINT, PRIMARY KEY, AUTO_INCREMENT)
- `username` (VARCHAR, UNIQUE, NOT NULL)
- `password` (VARCHAR, NOT NULL)
- `created_at` (TIMESTAMP, NOT NULL)
- `last_login` (TIMESTAMP)

#### 2. **chat_messages** Table
Stores all messages sent in the chat:
- `id` (BIGINT, PRIMARY KEY, AUTO_INCREMENT)
- `sender` (VARCHAR, NOT NULL)
- `content` (TEXT, NOT NULL)
- `language` (VARCHAR)
- `translated_content` (TEXT)
- `target_language` (VARCHAR)
- `created_at` (TIMESTAMP, NOT NULL)

## New Components Created

### Entity Classes
1. **User.java** (`src/main/java/com/polyglot/chat/model/User.java`)
   - JPA entity with @Entity and @Table annotations
   - Includes getters, setters, and constructors
   - Automatic timestamp tracking for account creation and last login

2. **ChatMessage.java** (`src/main/java/com/polyglot/chat/model/ChatMessage.java`)
   - JPA entity for persisting chat messages
   - Automatic timestamp creation on save
   - Supports translation tracking

### Repository Interfaces
1. **UserRepository.java** (`src/main/java/com/polyglot/chat/repository/UserRepository.java`)
   - Spring Data JPA repository for User entity
   - Methods: `findByUsername(String username)`

2. **ChatMessageRepository.java** (`src/main/java/com/polyglot/chat/repository/ChatMessageRepository.java`)
   - Spring Data JPA repository for ChatMessage entity
   - Methods:
     - `findBySenderOrderByCreatedAtDesc(String sender)` - Get all messages from a user
     - `findAllByOrderByCreatedAtDesc()` - Get all messages in reverse chronological order

### Service Classes
1. **UserService.java** (`src/main/java/com/polyglot/chat/service/UserService.java`)
   - Handles user-related operations
   - Methods:
     - `findByUsername(String username)` - Find user by username
     - `createUser(String username, String password)` - Create new user
     - `registerUser(String username, String password)` - Register with duplicate check
     - `updateLastLogin(String username)` - Update last login timestamp
     - `validateUser(String username, String password)` - Validate credentials

2. **ChatMessageService.java** (`src/main/java/com/polyglot/chat/service/ChatMessageService.java`)
   - Handles message-related operations
   - Methods:
     - `saveMessage(ChatMessage message)` - Save message to database
     - `getMessageById(Long id)` - Retrieve specific message
     - `getAllMessages()` - Get all messages
     - `getMessagesBySender(String sender)` - Get messages from specific user
     - `deleteMessage(Long id)` - Delete message
     - `getTotalMessageCount()` - Get message count

### Updated Controllers

1. **AuthController.java** (Updated)
   - `/auth/login` - Login endpoint (now validates against database)
   - `/auth/register` - New registration endpoint
   - Uses UserService for credential validation and user creation

2. **ChatController.java** (Updated)
   - Messages are now automatically saved to database
   - Added REST endpoints:
     - `GET /api/messages` - Retrieve all messages
     - `GET /api/messages/sender/{sender}` - Retrieve messages by sender

## Configuration

### application.properties
```properties
# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:chatdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=chat123

# JPA/Hibernate Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# H2 Console (Optional - for debugging)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

## Usage Examples

### Register a New User
```bash
POST http://localhost:8082/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "password": "securepassword123"
}
```

### Login
```bash
POST http://localhost:8082/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "securepassword123"
}
```

### Get All Messages
```bash
GET http://localhost:8082/api/messages
```

### Get Messages from Specific User
```bash
GET http://localhost:8082/api/messages/sender/john_doe
```

## WebSocket Support
Messages sent via WebSocket (`/app/send`) are automatically persisted to the database before being broadcast to other users.

## H2 Console
You can browse the database at: **http://localhost:8082/h2-console**
- JDBC URL: `jdbc:h2:mem:chatdb`
- Username: `sa`
- Password: `chat123`

## Benefits
✅ User login history is now tracked  
✅ All messages are persisted for later retrieval  
✅ Timestamps automatically recorded for all entries  
✅ Easy to add additional queries or analytics  
✅ Scalable - can be switched to PostgreSQL/MySQL by changing dependency and config  
✅ In-memory H2 for development, can use persistent database for production

## Next Steps
To enhance the database further, you could:
1. Switch to PostgreSQL or MySQL for persistence (change pom.xml and application.properties)
2. Add user-to-user private messaging
3. Add message search functionality
4. Add conversation/room tables for group chats
5. Add indexes on frequently queried columns for performance
