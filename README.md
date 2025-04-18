# Scheve Schilder Portaal – Back-end 💻

This is the **backend** API for the **Scheve Schilder Portaal**, a Spring Boot application designed to manage weekly painting lessons for users and admins. It supports user registration, lesson scheduling, student assignment, personal student artwork gallery, authentication with JWT, and role-based access control.

---

## 🧱 Tech Stack

- Java 21 JDK (LongTermSupport)
- Spring Boot 3.4
- Spring Security (JWT)
- JPA & Hibernate
- PostgreSQL
- Maven
- JUnit & Mockito

---

## 📁 Project Structure
- config || Configuration for Cors
- controllers || REST API controllers
- dtos || Data Transfer Objects
- exceptions || Custom exceptions
- models || Domain model entities
- repositories || JPA repositories
- security || Security configuration
- service || Business logic services
---

## 🚀 Features

- ✅ User registration with role `ROLE_USER`
- ✅ Admin dashboard with `ROLE_ADMIN` to:
    - Create/edit/delete lessons and weeks
    - Manage users/students
    - Change passwords
- ✅ Weekly lesson planning with max 10 students per slot
- ✅ Student can register/unregister for lessons
- ✅ Student has own gallery to upload artwork
- ✅ JWT-based login/logout system
- ✅ Email-password login with BCrypt
- ✅ Spring Security filtering & authorization
- ✅ Custom DTO structure for JSON responses
- ✅ RESTful endpoints

---


## 🛠️Getting Started

1. Make sure you have these Installed before running.
- IDE Recommended IntelliJ https://www.jetbrains.com/idea/download
- JDK 21 https://www.oracle.com/nl/java/technologies/downloads/#java21
- PostgresSQL 17+ https://www.postgresql.org/download/
- PgAdmin 4 v8 https://www.pgadmin.org/download/
- Postman https://www.postman.com/downloads/

2. DataBase Configuration
- Startup pgAdmin 4 and Login.
- Now create a database in the servers called `scheveschilderportaal`.

3. Aplication.Properties Configuration
- Go to the `application.properties`file.
- Make sure you fill in / change to the right username & password that you run locally on your postgres. 

![image info](./uploads/Password-Instruction.png)

4. Run the Application
- Now run the `BackendPortaalScheveschilderApplication` and test the endpoints with Po
- Start testing the endpoints with `Postman` with the `ScheveSchilderPortaal.postman_collection`file.
- Make sure to start with `POST /auth/login` to get a valid bearer token.
--- 
## 🖱️Test Roles
USER
##### email : `john@example.com` || Password : `password123`

ADMIN
#### Email : `admin@test.nl` || Password : `password123`

---

## 🔐 Authentication

This API uses **JWT** for stateless authentication.

- `POST /auth/login` returns a token
- Pass it in headers like:

```http
Authorization: Bearer <your_token>
````
--- 
## 💻API Endpoints

- `POST` - http://localhost:8080/auth/login
- `GET` - http://localhost:8080/weeks
- `POST` - http://localhost:8080/weeks
- `DELETE`  - http://localhost:8080/weeks/{id}
- `POST` - http://localhost:8080/weeks/{id}/lessons/{id}/students/{email}
- `DELETE`  - http://localhost:8080/weeks/{id}/lessons/{id}/students/{email}
- `POST` - http://localhost:8080/register
- `GET` - http://localhost:8080/users
- `GET` - http://localhost:8080/users/{email}
- `PUT` - http://localhost:8080/users/{email}
- `DELETE` - http://localhost:8080/users/{email}
- `GET` - http://localhost:8080/students
- `GET` - http://localhost:8080/students?slot={Les_moment}
- `GET` - http://localhost:8080/galleries/{email}
- `GET` - http://localhost:8080/galleries/{email}/artworks
- `POST` - http://localhost:8080/galleries/{email}/artworks
- `POST` - http://localhost:8080/galleries/{email}/artworks/{id}/photo
- `GET` - http://localhost:8080/artworks/{id}/photo
- `DELETE` - http://localhost:8080/galleries/{email}/artworks/{id}

