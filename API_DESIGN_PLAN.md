# FreeWill API Priority Plan (Spring Boot + Android)
Repository: `RingkhangBTY/FreeWill`  
Date: 2026-04-01

---

## Priority Legend

- **P0 (Must Have / MVP)**
- **P1 (Important / V1+)**
- **P2 (Enhancement / Scale)**

---

## P0 — Must Have (Build First)

### 1) Auth + Core Security
These endpoints unblock all user flows.

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `POST /api/v1/auth/refresh`
- `POST /api/v1/auth/logout`
- `GET /api/v1/me`
- `PATCH /api/v1/me/password`

### 2) Profile + Social Graph (Core)
- `GET /api/v1/users/{username}`
- `PATCH /api/v1/users/me`
- `POST /api/v1/follows/{targetUserId}`
- `DELETE /api/v1/follows/{targetUserId}`

### 3) Posts + Home Feed
- `POST /api/v1/posts`
- `GET /api/v1/posts/{postId}`
- `PATCH /api/v1/posts/{postId}`
- `DELETE /api/v1/posts/{postId}`
- `GET /api/v1/users/{id}/posts`
- `GET /api/v1/feed/home?cursor=...&limit=...`

### 4) Engagement (Core)
- `POST /api/v1/posts/{postId}/like`
- `DELETE /api/v1/posts/{postId}/like`
- `GET /api/v1/posts/{postId}/comments?cursor=...`
- `POST /api/v1/posts/{postId}/comments`

### 5) Realtime Chat (Core Flow)
#### REST
- `POST /api/v1/chats`
- `GET /api/v1/chats`
- `GET /api/v1/chats/{chatId}/messages?cursor=...`
- `POST /api/v1/chats/{chatId}/messages`

#### WebSocket
- `WS /ws` (JWT handshake)
- Subscribe: `/topic/chats/{chatId}`
- Publish: `/app/chats/{chatId}/send`

### 6) Notifications (Core)
- `GET /api/v1/notifications?cursor=...`
- `PATCH /api/v1/notifications/{id}/read`

---

## P1 — Important (After MVP Stabilizes)

### 1) Auth + Account Hardening
- `POST /api/v1/auth/verify-email`
- `POST /api/v1/auth/forgot-password`
- `POST /api/v1/auth/reset-password`
- `PATCH /api/v1/me/privacy`
- `GET /api/v1/sessions`
- `DELETE /api/v1/sessions/{sessionId}`

### 2) Profile & Discovery
- `POST /api/v1/users/me/avatar`
- `GET /api/v1/users/{id}/followers`
- `GET /api/v1/users/{id}/following`
- `GET /api/v1/users/search?q=...`

### 3) Feed Expansion
- `GET /api/v1/feed/explore?cursor=...&limit=...`
- `POST /api/v1/posts/{postId}/bookmark`
- `DELETE /api/v1/posts/{postId}/bookmark`

### 4) Comments Advanced
- `PATCH /api/v1/comments/{commentId}`
- `DELETE /api/v1/comments/{commentId}`
- `POST /api/v1/comments/{commentId}/like`
- `DELETE /api/v1/comments/{commentId}/like`

### 5) Chat Enhancements
- `POST /api/v1/chats/{chatId}/members`
- `DELETE /api/v1/chats/{chatId}/members/{userId}`
- Presence: `/topic/presence/{chatId}`
- Typing: `/topic/chats/{chatId}/typing`

### 6) Notifications + Android Push
- `POST /api/v1/notifications/read-all`
- `POST /api/v1/devices` (register Android FCM token)
- `DELETE /api/v1/devices/{deviceId}`

### 7) Recommendation APIs (Initial)
- `GET /api/v1/suggestions/users`
- `GET /api/v1/suggestions/posts`
- `POST /api/v1/interactions` (click/like/dwell-time events)

### 8) Moderation (Basic)
- `POST /api/v1/reports`

---

## P2 — Enhancement / Scale

### 1) Recommendation & Trends
- `GET /api/v1/trending/topics`

### 2) Moderation + Admin Console
- `GET /api/v1/admin/reports`
- `PATCH /api/v1/admin/reports/{id}`
- `POST /api/v1/admin/users/{id}/suspend`
- `DELETE /api/v1/admin/users/{id}/suspend`

---

## Recommended Build Order (Execution Plan)

1. Auth/Security  
2. User profile + follows  
3. Posts + home feed  
4. Comments + likes  
5. Realtime chat  
6. Notifications  
7. Suggestions/ranking improvements  
8. Moderation/admin  

---

## Suggested Spring Boot Architecture

### Modules
- `auth`
- `user`
- `social-graph`
- `post`
- `feed`
- `chat`
- `notification`
- `recommendation`

### Tech Choices
- **Security**: Spring Security + JWT + refresh tokens + role/permission checks  
- **Realtime**: STOMP over WebSocket (or RSocket for advanced streaming)  
- **Feed strategy**: Start with fan-out-on-read; optimize with Redis cache later  
- **Database**: PostgreSQL + Redis  
- **Search (later)**: Elasticsearch / OpenSearch  
- **Async events**: Kafka or RabbitMQ for notification/feed propagation  

---

## Optional Next Deliverables

If needed next, create:
1. **Database schema** (tables + relationships), and  
2. **Minimal v1 API contracts** (request/response JSON DTOs) for direct implementation in Spring Boot + Android.