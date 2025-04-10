# Scheve Schilder Portaal – Backend

This is the **backend** API for the **Scheve Schilder Portaal**, a Spring Boot application designed to manage weekly painting lessons for users and admins. It supports user registration, lesson scheduling, student assignment, authentication with JWT, and role-based access control.

---

## 🧱 Tech Stack

- Java 21 (longTermSupport)
- Spring Boot 3.4
- Spring Security (JWT)
- JPA & Hibernate
- H2 / PostgreSQL (dev/prod ready)
- Maven
- JUnit & Mockito

---

## 📁 Project Structure



---

## 🚀 Features

- ✅ User registration with role `ROLE_USER`
- ✅ Admin dashboard with `ROLE_ADMIN` to:
    - Create/edit/delete lessons and weeks
    - Manage users/students
    - Change passwords
- ✅ Weekly lesson planning with max 10 students per slot
- ✅ Student can register/unregister for lessons
- ✅ JWT-based login/logout system
- ✅ Email-password login with BCrypt
- ✅ Spring Security filtering & authorization
- ✅ Custom DTO structure for JSON responses
- ✅ RESTful endpoints (stateless)

---

## 🔐 Authentication

This API uses **JWT** for stateless authentication.

- `POST /auth/login` returns a token
- Pass it in headers like:

```http
Authorization: Bearer <your_token>
