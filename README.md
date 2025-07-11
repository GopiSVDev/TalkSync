# 📡 TalkSync

**TalkSync** is the backend for a real-time chat application, built with modern Java technologies and robust
architectural patterns to provide secure, scalable, and real-time communication. It provides:

- **RESTful APIs** for user management and authentication
- **WebSocket (STOMP)** endpoints for live messaging

---

## ✨ Features

- 🔒 **User Registration & Authentication**: Secure sign-up, login, and one-click guest login with Faker-generated names
- ⚙️ **User Management**: Update profile, delete account, search users by keyword
- 🌐 **Online Presence**: Real-time online status tracking and last-seen updates via WebSocket
- 💬 **Chat Management**: Create, retrieve, delete, and search chats
- ⌨️ **Realtime Typing Indicator**: Live typing status broadcast with STOMP
- 📩 **Messaging**: Send and receive chat messages in real-time
- 🗑️ **Guest Cleanup**: Scheduled task removes guest accounts after 24 hours

---

## 🛠️ Tech Stack

| Component      | Technology                     |
|----------------|--------------------------------|
| **Language**   | Java 21                        |
| **Framework**  | Spring Boot 3.5                |
| **Build Tool** | Maven                          |
| **WebSocket**  | Spring WebSocket (STOMP)       |
| **Security**   | Spring Security + JWT (`jjwt`) |
| **Database**   | PostgreSQL                     |
| **ORM**        | Spring Data JPA (Hibernate)    |
| **Validation** | Spring Boot Starter Validation |
| **Utilities**  | Lombok, Java Faker             |

---

## ⚙️ API Endpoints

### 🔐 Authentication `/api/auth`

| Method | Endpoint    | Description                  |
|--------|-------------|------------------------------|
| POST   | `/register` | Register a new user          |
| POST   | `/login`    | Authenticate and receive JWT |
| POST   | `/guest`    | One-click guest login        |

### 🧑‍💼 User Management `/api/user`

| Method | Endpoint            | Description                 |
|--------|---------------------|-----------------------------|
| POST   | `/update`           | Update current user profile |
| DELETE | `/delete/{id}`      | Delete user account         |
| GET    | `/search/{keyword}` | Search users by keyword     |

### 💬 Chat Management `/api/chat`

| Method | Endpoint            | Description             |
|--------|---------------------|-------------------------|
| POST   | `/create`           | Create a new chat       |
| GET    | `/{chatId}`         | Retrieve chat by ID     |
| DELETE | `/delete/{chatId}`  | Delete a chat           |
| GET    | `/search/{keyword}` | Search chats by keyword |

> **Note**: All REST endpoints require `Authorization: Bearer <token>`

---

## 🔌 WebSocket Endpoints

- **Endpoint URL**: `/ws` (WebSocket handshake)

- **Message Mapping**:

    - `/typing` → Broadcast typing status
    - `/chat.send` → Send chat message

- **Destination Prefix**: `/app`

- **Broadcast Topics**:

    - `/topic/typing` (subscriptions for typing indicators)
    - `/topic/messages` (subscriptions for chat messages)
    - `/topic/status` (online status updates)

## 🏗️ Architecture Overview

```
Client (Web/Mobile)
        |
   WebSocket / HTTP
        |
 Spring Boot Application
 ├─ Auth & Security (JWT)
 ├─ REST Controllers (Auth, Users)
 ├─ WebSocket Controller (Chat)
 └─ Data Layer (JPA Repositories)
        |
 PostgreSQL Database
```

## 🖥️ Frontend

The companion **React** application is inspired by **Telegram**’s sleek interface, delivering a familiar and intuitive
user experience:

- 📱 **Modern Chat List**: Sidebar displaying recent chats with avatars and timestamp previews
- 💬 **Telegram‑Style Bubbles**: Clean message bubbles with date separators and user avatars for clarity
- 🔍 **Search in Chats**: Quickly find messages and conversations via a persistent search bar
- ⌨️ **Typing & Presence Indicators**: Real-time “user is typing…” cues and online/offline status markers
- 🎨 **Theming & Layout**: Light/dark mode toggle, responsive design with Tailwind CSS for mobile and desktop

## 🔗 Links

- **Backend Repository**: [Current Repo](https://github.com/GopiSVDev/TalkSync)
- **Frontend Repository**: [Click Here](https://github.com/GopiSVDev/TalkSync_ui)
- **Live Demo**: Pending.....