# Project Directory Structure

This document explains the organization of the Polyglot Chat Application for easy navigation and explanation during interviews.

```
📁 projectpayplot/
│
├── 📄 README.md                    ⭐ START HERE - Project overview & quick intro
│
├── 📁 docs/                        📚 Complete Documentation
│   ├── SETUP.md                    🚀 Quick start installation guide
│   ├── API.md                      🔌 REST API endpoints reference
│   ├── DATABASE.md                 🗄️ Database schema & queries
│   ├── ARCHITECTURE.md             🏛️ System design patterns
│   └── FOLDER_STRUCTURE.md         📂 This file
│
├── 📁 chat/                        🔧 Backend (Spring Boot)
│   ├── pom.xml                     📦 Maven dependencies & config
│   ├── mvnw / mvnw.cmd             🏃 Maven wrapper (build tool)
│   ├── test.http                   🧪 API test requests
│   ├── open-database.bat           🗄️ Quick H2 console launcher
│   │
│   ├── 📁 src/main/java/
│   │   └── com/polyglot/chat/
│   │       ├── ChatApplication.java        🎯 Main entry point
│   │       ├── 📁 config/
│   │       │   └── SecurityConfig.java     🔐 Spring Security setup
│   │       ├── 📁 controller/
│   │       │   ├── AuthController.java     🔑 Login/signup endpoints
│   │       │   └── ChatController.java     💬 Message endpoints
│   │       ├── 📁 model/
│   │       │   ├── User.java               👤 User entity
│   │       │   └── ChatMessage.java        📨 Message entity
│   │       ├── 📁 security/
│   │       │   └── JwtUtil.java            🔐 JWT token handling
│   │       ├── 📁 service/
│   │       │   ├── UserService.java        🛠️ User business logic
│   │       │   └── ChatMessageService.java 🛠️ Message business logic
│   │       └── 📁 dto/
│   │           ├── SignupRequest.java      📥 Signup request format
│   │           └── AuthResponse.java       📤 Auth response format
│   │
│   ├── 📁 src/main/resources/
│   │   ├── application.properties          ⚙️ Server config (port, DB, etc)
│   │   └── 📁 static/
│   │       └── index.html                  🌐 Static web files
│   │
│   ├── 📁 src/test/java/              🧪 Unit tests (optional)
│   │
│   └── 📁 target/                     🚫 BUILD OUTPUT (ignore, auto-generated)
│       └── chat-0.0.1-SNAPSHOT.jar    🎁 Compiled executable JAR
│
├── 📁 chat-frontend/               ⚛️ Frontend (React)
│   ├── package.json                 📦 NPM dependencies & config
│   ├── 📁 public/
│   │   └── index.html               🌐 HTML template
│   │
│   ├── 📁 src/
│   │   ├── index.js                 ▶️ React entry point
│   │   ├── App.js                   📱 Main React component
│   │   ├── App.css                  🎨 Styles
│   │   │
│   │   └── 📁 components/
│   │       ├── Login.js             🔑 Login/signup component
│   │       ├── Login.css            🎨 Login styles
│   │       ├── Chat.js              💬 Chat component
│   │       └── Chat.css             🎨 Chat styles
│   │
│   └── 📁 node_modules/             🚫 DEPENDENCIES (ignore, auto-generated)
│
├── 📁 polyglot-chat/               ⚠️ DEPRECATED (old version)
│   └── ... (previous project version)
│
├── 📄 CHANGES_SUMMARY.md           📝 History of recent changes
└── 📄 .gitignore                   🚫 Files to exclude from version control
```

---

## 🎯 What to Show an Interviewer

### Quick Demo Flow (5 minutes)

1. **README.md** (1 min)
   - Show project overview
   - Highlight tech stack
   - Show feature list

2. **Quick Start** (1 min)
   - Show how simple to run (3 commands)
   - Show backend starts on port 8082
   - Show frontend starts on port 3000

3. **Architecture Diagram** (1 min)
   - Show ARCHITECTURE.md
   - Explain MVC pattern
   - Explain Repository pattern

4. **Live Demo** (2 min)
   - Create new account
   - Login successfully
   - Send a message
   - Show message in database

---

## 📁 Folder Organization Strategy

### Backend Organization (chat/)
```
By Responsibility:
✅ config/        → Configuration beans
✅ controller/    → REST endpoints (Accept requests)
✅ service/       → Business logic (Process data)
✅ model/         → JPA entities (Store data)
✅ repository/    → Data access (Query DB)
✅ dto/           → Request/Response objects
✅ security/      → Authentication/JWT
```

**Interview Point:** "Follows clean architecture - easy to find and modify features"

### Frontend Organization (chat-frontend/src/)
```
By Feature:
✅ components/    → Reusable UI components
✅ App.js         → Main routing & state
✅ index.js       → Entry point
✅ index.css      → Global styles
```

**Interview Point:** "React best practices - modular and scalable"

---

## 🚫 Files to Ignore (in .gitignore)

```
# Build outputs
target/
node_modules/
dist/
build/

# IDE
.vscode/
.idea/
*.swp
*.swo

# OS
.DS_Store
Thumbs.db

# Environment
.env
.env.local

# Logs
*.log
logs/

# Temporary
*.tmp
temp/
```

---

## 📊 Key Files for Interview Questions

| Question | File to Show |
|----------|-------------|
| "What's your tech stack?" | README.md → Technology Stack table |
| "Show me the architecture" | docs/ARCHITECTURE.md → Diagrams |
| "How does authentication work?" | docs/API.md → Auth endpoints |
| "What's your database schema?" | docs/DATABASE.md → Tables |
| "How do you handle passwords?" | chat/src/main/java/.../UserService.java |
| "Show me the code" | chat/src/ for backend, chat-frontend/src/ for frontend |
| "Can you run it?" | docs/SETUP.md → Follow 3-step setup |
| "How do you test?" | chat/test.http → Show REST Client tests |

---

## ✅ Pre-Interview Checklist

- [ ] README.md - Clear project overview
- [ ] docs/ - All 4 documentation files present
- [ ] Backend builds: `./mvnw clean package`
- [ ] Frontend installs: `npm install`
- [ ] Backend starts on port 8082
- [ ] Frontend starts on port 3000
- [ ] Can create user account
- [ ] Can send/receive messages
- [ ] Database shows persisted data
- [ ] Clean git history (if using git)
- [ ] No build artifacts in repo (.gitignore works)
- [ ] All code properly formatted
- [ ] No sensitive data in files (passwords, API keys)

---

## 🎤 Interview Talking Points

**Using This Structure:**

1. **"Easy to Explain"**
   - Show folder structure
   - Point out separation of concerns
   - Highlight docs for quick reference

2. **"Production Ready"**
   - Show application.properties configuration
   - Show password hashing implementation
   - Show database schema documentation

3. **"Scalable Design"**
   - Show Repository pattern
   - Show Service layer abstraction
   - Show REST API design

4. **"Well Documented"**
   - Show README.md
   - Show API.md with examples
   - Show ARCHITECTURE.md diagrams

5. **"Easy to Set Up"**
   - Show SETUP.md - only 5 commands needed
   - Show it runs on local machine
   - Show test.http for testing

---

## 📖 Navigation Tips

**If asked about X:**
- **"How is it organized?"** → Show this file (FOLDER_STRUCTURE.md)
- **"What's the architecture?"** → Open docs/ARCHITECTURE.md
- **"Show me the API"** → Open docs/API.md
- **"How do you persist data?"** → Open docs/DATABASE.md
- **"How do I run it?"** → Open docs/SETUP.md
- **"Show me the code"** → Open chat/src/ or chat-frontend/src/

---

**File created:** February 13, 2026
