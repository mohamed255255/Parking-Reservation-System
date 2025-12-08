# Parking Reservation System  :

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Security-Spring%20Security%20+%20JWT-red?logo=springsecurity)](https://spring.io/projects/spring-security)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14-blue?logo=postgresql)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Container-Docker-2496ED?logo=docker)](https://www.docker.com/)
[![PayPal](https://img.shields.io/badge/Payments-PayPal-00457C?logo=paypal)](https://developer.paypal.com/)
[![OpenAPI](https://img.shields.io/badge/API-OpenAPI%20%2F%20Swagger-85EA2D?logo=swagger)](https://swagger.io/tools/open-source/openapi/)
[![Tests](https://img.shields.io/badge/Tests-JUnit5%20+%20Mockito-yellow?logo=jest)](https://junit.org/junit5/)
[![Monitoring](https://img.shields.io/badge/Monitoring-Spring%20Boot%20Actuator-lightgrey?logo=grafana)](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)

---


The application allows users to easily book parking spaces, join a waiting queue when all spots are full, and receive real-time notifications when a suitable slot becomes available. It also manages reservations, tracks booking history, and enables users to pay parking fees securely through digital payments. 
Admin have full control over garage and slot creation , user details, reservations, and billing.

## ✨ Features  

- 🔐 **Authentication & Authorization**  
  - Role-based access (Admin/User) using Spring Security + JWT  
  - Password reset & Two-Factor Authentication (2FA)  

- 🏢 **Garage & Slot Management**  
  - Admin can create garages and define slot dimensions  
  - Users only see slots that fit their vehicle  

- ⏳ **Real-Time Notifications**  
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
- **Payment:** PayPal SDK  
- **Containerization:** Docker  
- **Testing:** JUnit5, Mockito  
- **CI/CD:** Github actions
---

## 🔹 Design Patterns Used  

- **Clean architecture (Controller → Service → Repository → Database)**
-  Implemented a **unified response structure** by wrapping all API outputs in a custom `ApiResponse` object, ensuring consistency across success and error responses.
- Used **DTOs** to decouple persistence models from exposed APIs, improving security, readability, and maintainability of the codebase.  
- **Observer Pattern** (for notifications & event-driven updates)
---

## 📚 What I Learned in this project

- **Spring Boot & Core Concepts**  
  - **DTOs, validators, and validation annotations** for clean data handling and security
  - **global exception handling** for consistent error responses  
  - wrapping all the returned objects in unified API Response 
  - Learned and applied **Aspect-Oriented Programming** concepts for cross-cutting concerns(e.g., logging, validation, transaction handling)

- **Spring Security & Authentication**  
  - **JWT authentication & authorization** in spring
  - Learned how  **filters** , **AuthenticationManager**, and **CustomUserDetailsService** work internally
  - Applied `@PreAuthorize` for fine-grained method-level security  
  - **password reset** and **2FA (Two-Factor Authentication)**  

- **Database & Persistence**  
  - How **Hibernate/JPA** persist Java objects in EntityManager behind the scenes   
  - Practiced `@Transactional` and **transaction propagation**   
  - **sorting & pagination**
  - How to avoid N+1 problems 

- **System Reliability & Monitoring**  
  - Integrated **Spring Boot Actuator** for monitoring  

- **Testing & Quality Assurance**  
  - Wrote **unit tests with JUnit5** and **mocking with Mockito** to validate business logic  

- **Extra features**  
  - Explored `@Qualifier` vs `@Primary` 
  - Integrated **OpenAPI/Swagger** for API documentation and testing
  - Explored **Lombok** and **Guava** packages in the app
  - database schema migration using **Flyway**
  - how Maven works : POM file , scopes , build lifecycle, profiles

- **Event-Driven & Messaging**  
  - **RabbitMQ**  
  - learned **event-driven architecture** to notify users when slots are freed  

- **Integration & Deployment**  
  - Integrating a **third-party payment API (PayPal SDK)** with spring  
  - basic knowledge in docker 
  - basic CI / CD concepts








