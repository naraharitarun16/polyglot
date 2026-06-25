# 🌐 Polyglot Chat Application

A modern **real-time chat application** built using **Spring Boot**, **React**, **WebSocket**, and **JWT Authentication**. The application provides secure user authentication, persistent messaging, and scalable architecture for real-time communication.

---

## 📖 Overview

Polyglot Chat is a full-stack web application designed to demonstrate modern software engineering practices, including secure authentication, RESTful APIs, real-time communication, and database persistence.

The project follows a clean separation between the backend and frontend, making it easy to maintain, extend, and deploy.

---

# ✨ Features

* Secure user registration and login
* Password encryption using BCrypt
* JWT-based authentication and authorization
* Real-time messaging using WebSockets (STOMP)
* Persistent message storage
* User management with activity tracking
* Unique username and email validation
* RESTful API architecture
* Responsive React-based user interface

---

# 🏗 Project Architecture

```text
polyglot-chat/

├── chat/                     # Spring Boot Backend
│   ├── src/
│   ├── pom.xml
│   └── application.properties
│
├── chat-frontend/            # React Frontend
│   ├── src/
│   ├── public/
│   └── package.json
│
├── docs/                     # Project Documentation
│   ├── API.md
│   ├── DATABASE.md
│   ├── SETUP.md
│   └── ARCHITECTURE.md
│
└── README.md
```

---

# 🛠 Technology Stack

## Backend

| Technology        | Purpose                        |
| ----------------- | ------------------------------ |
| Java 17           | Programming Language           |
| Spring Boot       | Backend Framework              |
| Spring Security   | Authentication & Authorization |
| Spring Data JPA   | Database Access                |
| JWT               | Token-Based Authentication     |
| WebSocket (STOMP) | Real-Time Communication        |
| H2 Database       | Development Database           |
| Maven             | Dependency Management          |

---

## Frontend

| Technology       | Purpose                 |
| ---------------- | ----------------------- |
| React            | User Interface          |
| JavaScript (ES6) | Frontend Logic          |
| STOMP Client     | WebSocket Communication |
| HTML5 & CSS3     | UI Design               |

---

# 🚀 Getting Started

## Prerequisites

* Java 17 or later
* Maven 3.8+
* Node.js 14+
* npm

---

## Backend Setup

```bash
cd chat

./mvnw clean package -DskipTests

java -jar target/chat-0.0.1-SNAPSHOT.jar --server.port=8082
```

Backend URL

```
http://localhost:8082
```

H2 Console

```
http://localhost:8082/h2-console
```

---

## Frontend Setup

```bash
cd chat-frontend

npm install

npm start
```

Frontend URL

```
http://localhost:3000
```

---

# 🔐 Authentication Flow

### User Registration

* User submits registration details.
* Password is encrypted using BCrypt.
* User information is stored securely.

### User Login

* Credentials are validated.
* JWT access token is generated.
* Token is returned to the client.

### Messaging

* Client authenticates using JWT.
* WebSocket connection is established.
* Messages are broadcast in real time.
* Messages are stored in the database.

---

# 📡 REST API

## Register User

```http
POST /auth/signup
```

```json
{
  "username": "alice",
  "password": "securePassword123",
  "email": "alice@example.com",
  "fullName": "Alice Johnson"
}
```

---

## Login

```http
POST /auth/login
```

```json
{
  "username": "alice",
  "password": "securePassword123"
}
```

---

## WebSocket Endpoint

```
ws://localhost:8082/ws
```

Example Message

```json
{
  "sender": "alice",
  "content": "Hello everyone!",
  "timestamp": "2026-02-13T10:30:00"
}
```

---

# 🗄 Database Design

## Users

| Column    | Description               |
| --------- | ------------------------- |
| id        | Primary Key               |
| username  | Unique Username           |
| password  | BCrypt Encrypted Password |
| email     | Unique Email Address      |
| fullName  | User's Full Name          |
| createdAt | Account Creation Time     |
| lastLogin | Last Login Timestamp      |
| isActive  | User Status               |

---

## Chat Messages

| Column    | Description     |
| --------- | --------------- |
| id        | Primary Key     |
| sender    | Message Sender  |
| content   | Message Content |
| language  | Language Code   |
| createdAt | Timestamp       |

---

# 📁 Important Components

## Backend

* **ChatApplication.java** – Application entry point
* **AuthController.java** – Authentication APIs
* **ChatController.java** – WebSocket communication
* **User.java** – User entity
* **ChatMessage.java** – Message entity
* **UserService.java** – User business logic
* **ChatMessageService.java** – Message persistence
* **SecurityConfig.java** – Spring Security configuration

---

## Frontend

* **App.js** – Main application
* **Login.js** – Authentication page
* **Chat.js** – Chat interface
* **index.js** – React entry point

---

# 🧪 Testing

Example API tests can be executed using the REST Client extension in VS Code.

Example endpoints:

```http
POST /auth/signup

POST /auth/login

GET /auth/users/count
```

---

# 📚 Documentation

Additional documentation is available in the **docs/** directory.

* API Documentation
* Database Design
* Setup Guide
* Architecture Overview

---

# 🌟 Project Highlights

* Clean layered architecture
* Secure authentication using JWT
* Password encryption with BCrypt
* Real-time messaging with WebSockets
* Persistent database storage
* Modular and scalable design
* Professional project structure
* Easy deployment and maintenance

---

# 🚀 Deployment

### Backend

* Executable Spring Boot JAR
* Docker compatible
* Easy migration from H2 to PostgreSQL or MySQL

### Frontend

* Production-ready React build
* Deployable to Netlify
* Deployable to Vercel
* Deployable to AWS S3 or any static hosting platform

---

# 👨‍💻 Author

**Narahari Tarun**

Bachelor of Engineering – Computer Science

---

## 📄 License

This project is intended for educational and portfolio purposes.
