# Parking Reservation System  :

[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14-blue)](https://www.postgresql.org/)
[![Security](https://img.shields.io/badge/Security-Spring%20Security%20%2B%20JWT-red)](https://spring.io/projects/spring-security)
[![Tests](https://img.shields.io/badge/Tests-Spring%20Boot%20Test%20%2B%20Security%20Test-yellow)](https://junit.org/junit5/)

The application allows users to easily book parking spaces, join a waiting queue when all spots are full, and receive real-time notifications when a suitable slot becomes available. It also manages reservations, tracks booking history, and enables users to pay parking fees securely through digital payments. Admins have full control over garage and slot creation , user details, reservations, and billing.
---

## ✨ Features  

- 🔐 **Authentication & Authorization**  
  - Role-based access (Admin/User) using Spring Security + JWT  
  - Password reset & Two-Factor Authentication (2FA)  

- 🏢 **Garage & Slot Management**  
  - Admin can create garages and define slot dimensions  
  - Users only see slots that fit their vehicle  

- ⏳ **Queue & Real-Time Notifications**  
  - Users can request "remind me later" when slots are unavailable  
  - Notification system alerts users instantly when a slot is freed  

- 💳 **Payment & Billing**  
  - Parking fees calculated based on stay duration  
  - Secure PayPal integration for digital payments  
  - Automated email receipts sent after payment  

- 📦 **Deployment & Reliability**  
  - Dockerized for deployment readiness  
  - Unit tested with JUnit5 & Mockito  

---

## 🛠️ Tech Stack  

- **Backend:** Spring Boot, Spring Security, JWT, REST API  
- **Database:** PostgreSQL  
- **Messaging/Queue:** RabbitMQ  
- **Payment:** PayPal SDK  
- **Containerization:** Docker  
- **Testing:** JUnit5, Mockito  

---

## 🔹 Design Patterns Used  

- **Layered Architecture (Controller → Service → Repository)**
-  Implemented a **unified response structure** by wrapping all API outputs in a custom `ApiResponse` object, ensuring consistency across success and error responses.
- Used **DTOs** to decouple persistence models from exposed APIs, improving security, readability, and maintainability of the codebase.  
- **Observer Pattern** (for notifications & event-driven updates)

---

## 📚 What I Learned in this project

- **Spring Boot & Core Concepts**  
  - Built REST APIs with layered architecture   
  - Applied **DTOs, validators, and validation annotations** for clean data handling  
  - Implemented **global exception handling** for consistent error responses  
  - Designed a **unified API response structure** for all returned objects  

- **Spring Security & Authentication**  
  - Implemented **JWT authentication & authorization** with role-based access (Admin/User)  
  - Learned how **filters**, **AuthenticationManager**, and **CustomUserDetailsService** work internally  
  - Applied `@PreAuthorize` for fine-grained method-level security  
  - Added features like **password reset** and **2FA (Two-Factor Authentication)**  

- **Database & Persistence**  
  - Used **Hibernate/JPA** to persist Java objects behind the scenes   
  - Practiced `@Transactional` and **transaction propagation** scenarios  
  - Implemented **sorting & pagination**
  - How to avoid N+1 problems 

- **System Reliability & Monitoring**  
  - Integrated **Spring Boot Actuator** for health checks and monitoring  
  - Structured **logging** to track key application events  

- **Testing & Quality Assurance**  
  - Wrote **unit tests with JUnit5** and **mocking with Mockito** to validate business logic  

- **Extra features**  
  - Explored `@Qualifier` vs `@Primary` for dependency injection management 
  - Integrated **OpenAPI/Swagger** for API documentation and testing

- **Event-Driven & Messaging**  
  - Designed a **queue & notification system** with **RabbitMQ**  
  - Applied **event-driven programming concepts** to notify users when slots are freed  

- **Integration & Deployment**  
  - Integrated a **third-party payment API (PayPal SDK)** for secure payments  
  - Sent **automated email receipts** post-transaction  
  - **Dockerized** the application for deployment readiness








