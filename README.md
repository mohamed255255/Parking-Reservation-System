# Parking Reservation System  :

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Security-Spring%20Security%20%2B%20JWT-red?logo=springsecurity)](https://spring.io/projects/spring-security)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14-blue?logo=postgresql)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Container-Docker-2496ED?logo=docker)](https://www.docker.com/)
[![PayMob](https://img.shields.io/badge/Payments-PayMob-FF6F61)](https://www.paymob.com/)
[![OpenAPI](https://img.shields.io/badge/API-OpenAPI%20%2F%20Swagger-85EA2D?logo=swagger)](https://swagger.io/tools/open-source/openapi/)
[![Tests](https://img.shields.io/badge/Tests-JUnit5%20%2B%20Mockito-yellow?logo=jest)](https://junit.org/junit5/)
[![Monitoring](https://img.shields.io/badge/Monitoring-Spring%20Boot%20Actuator-lightgrey?logo=grafana)](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
[![GitHub Actions](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-2088FF?logo=githubactions)](https://github.com/features/actions)
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
  - Implemented using **schedulers** to send **emails** when parking slots become available
  - future enhancements : i will use firebase and in case of microservice approach i will use message queues

- 💳 **Payment & Billing**  
  - Parking fees calculated based on stay duration  
  - Secure PayMob integration for digital payments , handled idempotency,security and webhook   
  - Automated email receipts sent after payment
  - store payment transactions in the DB

- 📦 **Deployment & Reliability**  
  - Dockerized for deployment readiness
  - used github actions for CI / CD
  - Unit tested with JUnit5 & Mockito
  
---

## 🛠️ Tech Stack  

- **Backend:** Spring Boot, Spring Security, JWT, REST API  
- **Database:** PostgreSQL  
- **Payment:** PayMob API  
- **Containerization:** Docker  
- **Testing:** JUnit5, Mockito  
- **CI/CD:** Github actions
---

## 🔹 Architecutre / Design Patterns Used  

- **Clean architecture (Controller → Service → Repository → Database)**
- Used **DTOs** for requests and responses to decouple persistence models from exposed APIs, improving security, readability, and maintainability of the codebase.
- **Global exception handling** 







