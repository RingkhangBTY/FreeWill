# 🌐 FreeWill — Open Social Network

FreeWill is an open social networking platform where users can freely share their thoughts publicly and communicate privately through secure messaging.

The goal of this project is to build a scalable, secure, and production-ready backend system using **Spring Boot, PostgreSQL, and JPA**, designed to handle real-world traffic and future expansion.

---

## 🚀 Vision

To create an open digital space where:

- 📝 Users can share thoughts openly
- 💬 Users can chat privately
- 👥 Users can follow each other
- 🔐 Privacy and security are respected
- ⚡ System is scalable for millions of users

---

## 🏗️ Tech Stack

### Backend
- Java 21+
- Spring Boot
- Spring Data JPA
- Spring Security
- Hibernate
- Lombok

### Database
- PostgreSQL

### Build Tool
- Maven

---

## 📌 Core Features (Implemented)

### 👤 User Management
- User registration
- Login authentication
- BCrypt password hashing
- Delete account (password verification required)

### 📝 Posts
- Create post
- Delete post
- Ranked feed system
- Like & comment aggregation

### 💬 Comments
- Add comment to post
- Fetch comments per post

### ❤️ Likes
- Like posts
- Like count aggregation

### 👥 Follow System
- Follow user
- Prevent duplicate follow
- Prevent self-follow
- Check follow status
- Follower count in user search

### 🔎 User Search
- Search users by username
- Returns:
    - userId
    - username
    - bio
    - followers count
    - account creation date

---

## 🧠 Ranking Algorithm

Posts are ranked using a custom scoring system based on:

- Number of likes
- Number of comments
- Author follower count
- Time decay (newer posts prioritized)

This creates a dynamic and engaging feed.

---

## 🔐 Security

- Passwords stored using BCrypt hashing
- Spring Security authentication
- SQL Injection safe (JPA parameter binding)
- Prevent duplicate follow & invalid actions

Planned:
- JWT authentication
- Rate limiting
- End-to-End encryption for chat

---

## 🛠️ Future Roadmap

- 🔒 Post visibility (Public / Followers-only / Private)
- 💬 Real-time private chat (WebSocket)
- 🔐 End-to-End Encryption
- 📈 Personalized feed
- 🧠 AI-based ranking
- 🗄️ Redis caching
- ☁️ Cloud deployment
- 📦 Media upload (S3)
- 🔍 Full-text search (Elasticsearch)
- 📊 Monitoring & analytics

---

## 📈 Scalability Plan

Designed for future scaling with:

- PostgreSQL read replicas
- Table partitioning
- Redis caching
- Kafka for async processing
- Kubernetes deployment
- CDN for media delivery

---

## 🧪 Running the Project

1. Clone the repository
2. Configure PostgreSQL in `application.properties`
3. spring.datasource.url=jdbc:postgresql://localhost:5432/freewill
   spring.datasource.username=postgres
   spring.datasource.password=yourpassword
4. Run the application


---

## 🎯 Purpose

This project is built to practice and implement:

- Real-world database modeling
- Secure backend development
- Scalable architecture design
- Advanced JPA & SQL optimization

---

## 👨‍💻 Author

**Ringkhang Basumatary**  
B.Tech CSE | Backend & System Design Enthusiast  
Building scalable production-ready systems.



