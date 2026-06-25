# 🗄️ Database Schema Documentation

## Database Type
- **Development:** H2 (In-Memory)
- **Production:** PostgreSQL / MySQL (Configurable)
- **ORM:** Spring Data JPA with Hibernate

## Connection String
```
jdbc:h2:mem:chatdb
```

---

## 📋 Tables

### 1. USERS Table

**Purpose:** Stores user account information with authentication credentials

| Column | Type | Constraints | Notes |
|--------|------|-------------|-------|
| ID | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier |
| USERNAME | VARCHAR(50) | UNIQUE, NOT NULL | Login identifier |
| PASSWORD | VARCHAR(255) | NOT NULL | BCrypt hashed password |
| EMAIL | VARCHAR(100) | UNIQUE, NOT NULL | Contact & recovery |
| FULL_NAME | VARCHAR(100) | NOT NULL | Display name |
| CREATED_AT | TIMESTAMP | NOT NULL, DEFAULT NOW() | Account creation time |
| LAST_LOGIN | TIMESTAMP | Nullable | Last login timestamp |
| IS_ACTIVE | BOOLEAN | DEFAULT TRUE | Account status |

**SQL Create Statement:**
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);
```

**Sample Data:**
```sql
INSERT INTO users (username, password, email, full_name) VALUES
('alice', '$2a$10$...hashedpassword...', 'alice@example.com', 'Alice Johnson'),
('bob', '$2a$10$...hashedpassword...', 'bob@example.com', 'Bob Smith');
```

---

### 2. CHAT_MESSAGES Table

**Purpose:** Stores all chat messages with sender information

| Column | Type | Constraints | Notes |
|--------|------|-------------|-------|
| ID | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique message ID |
| SENDER | VARCHAR(50) | NOT NULL, FOREIGN KEY | User who sent message |
| CONTENT | TEXT | NOT NULL | Message body |
| LANGUAGE | VARCHAR(20) | DEFAULT 'en' | Language code (i18n) |
| CREATED_AT | TIMESTAMP | NOT NULL, DEFAULT NOW() | Message timestamp |

**SQL Create Statement:**
```sql
CREATE TABLE chat_messages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sender VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    language VARCHAR(20) DEFAULT 'en',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender) REFERENCES users(username)
);
```

**Sample Data:**
```sql
INSERT INTO chat_messages (sender, content, language) VALUES
('alice', 'Hello everyone!', 'en'),
('bob', 'Hi Alice!', 'en'),
('alice', 'How are you?', 'en');
```

---

## 🔄 Relationships

```
USERS (1) ──── (M) CHAT_MESSAGES
```

**One-to-Many Relationship:**
- One user can send many messages
- Each message belongs to one user
- Foreign key: `CHAT_MESSAGES.sender` → `USERS.username`

---

## 🔐 Security Features

### Password Hashing
- **Algorithm:** BCrypt with cost factor 10
- **Example:** 
  - Plain: `password123`
  - Hashed: `$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86.lFN/3VCa`
- **Verification:** Uses `BCryptPasswordEncoder.matches()`

### Data Constraints
- **Unique Usernames:** Prevents duplicate accounts
- **Unique Emails:** Prevents email collision
- **NOT NULL:** Required fields enforced at database level
- **FOREIGN KEY:** Maintains referential integrity

---

## 📊 Query Examples

### Find User by Username
```sql
SELECT * FROM users WHERE username = 'alice';
```

### Find All Messages from User
```sql
SELECT * FROM chat_messages WHERE sender = 'alice' ORDER BY created_at DESC;
```

### Get All Users (Admin View)
```sql
SELECT id, username, email, full_name, created_at, last_login, is_active 
FROM users 
ORDER BY created_at DESC;
```

### Get Recent Messages (Last 50)
```sql
SELECT * FROM chat_messages 
ORDER BY created_at DESC 
LIMIT 50;
```

### Count Total Messages per User
```sql
SELECT sender, COUNT(*) as message_count 
FROM chat_messages 
GROUP BY sender 
ORDER BY message_count DESC;
```

### Find Inactive Users
```sql
SELECT username, email, created_at 
FROM users 
WHERE is_active = FALSE;
```

### Find User Last Activity
```sql
SELECT username, email, last_login 
FROM users 
WHERE last_login IS NOT NULL 
ORDER BY last_login DESC;
```

---

## 🔌 H2 Console Access

**URL:** `http://localhost:8082/h2-console`

**Connection Details:**
- Driver Class: `org.h2.Driver`
- JDBC URL: `jdbc:h2:mem:chatdb`
- User Name: `sa`
- Password: `chat123`

**Steps:**
1. Navigate to the URL above
2. Click "Connect"
3. Run SQL queries in the SQL editor
4. View table contents

---

## 🔄 Schema Evolution

### Current Version (v1.0)
```
USERS Table: 8 columns
CHAT_MESSAGES Table: 5 columns
```

### Potential Future Enhancements
- `USERS`: Add `profilePicture`, `phoneNumber`, `lastActiveTime`
- `USERS`: Add `role` for admin/moderator functionality
- `USERS`: Add `emailVerified` for email confirmation flow
- `CHAT_MESSAGES`: Add `editedAt` for message edit history
- `CHAT_MESSAGES`: Add `reactions` (emoji reactions)
- `CHAT_GROUPS` table for group chats
- `FRIEND_REQUESTS` table for friend system

---

## 🗂️ Data Export

### Export All Users to CSV
```sql
SELECT * FROM users;
```
Then use H2 Console export feature

### Export All Messages
```sql
SELECT * FROM chat_messages;
```

---

## ⚙️ Configuration

### Spring Boot Properties (application.properties)
```properties
# H2 Configuration
spring.datasource.url=jdbc:h2:mem:chatdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=chat123

# JPA/Hibernate Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### Switching to PostgreSQL (Production)
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/polyglot_chat
spring.datasource.driverClassName=org.postgresql.Driver
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL10Dialect
```

---

## 📈 Performance Considerations

### Indexes (Recommended for Production)
```sql
CREATE INDEX idx_username ON users(username);
CREATE INDEX idx_email ON users(email);
CREATE INDEX idx_sender ON chat_messages(sender);
CREATE INDEX idx_created_at ON chat_messages(created_at DESC);
```

### Query Optimization
- Add pagination for large message lists
- Use `SELECT * FROM chat_messages LIMIT 50` for recent messages
- Archive old messages periodically (>1 year)

---

## 🔍 Maintenance

### Backup Data
```sql
-- Export all data
SELECT * INTO OUTFILE 'backup.sql' 
FROM users;
```

### Clean Old Messages (Archive)
```sql
DELETE FROM chat_messages 
WHERE created_at < DATE_SUB(NOW(), INTERVAL 1 YEAR);
```

### Verify Data Integrity
```sql
SELECT COUNT(*) FROM users;
SELECT COUNT(*) FROM chat_messages;
```

---

**Last Updated:** February 13, 2026
