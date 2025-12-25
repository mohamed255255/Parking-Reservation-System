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

## Features  

- 🔐 **Authentication & Authorization**  
  - Role-based access (Admin/User) using Spring Security + JWT  
  - Password reset & Two-Factor Authentication (2FA)  

- 🏢 **Garage & Slot Management**  
  - Admin can create garages , define slot dimensions.
  - Admin generates QR codes for every created slot to comfirm arrival
  - Users only see slots that fit their vehicle size

- ⏳ **Real-Time Notifications**  
  - Users can request "remind me later" when slots are unavailable  
  - Implemented using **schedulers** to send **emails** when parking slots become available

- 🚗 **Reservation**
  - User creates a reservation if the slot is empty. The reservation is created with status **PENDING** ⏳
  - The reservation is confirmed only when the user scans the QR code at the physical location 📍  
    Once confirmed, the parking timer starts and payment begins 💳⏱️
  - **QR code rejection cases **
    - The QR code represents a specific **slot ID + garage ID**.  
      QR codes from other locations are rejected 🚫
    - If the QR code is not linked to an active **PENDING** reservation, it is rejected 🛑
    - If the slot is already occupied whether by another user on the way or already parked car , the scan is rejected 🚧
  - User can view all of their reservations 📋
  
- 💳 **Payment & Billing**
  - Parking fees calculated based on stay duration  
  - Secure PayMob integration for digital payments , handled idempotency , HMAC verification   
  - Automated email receipts sent after payment 

- 📦 **Deployment & Reliability**  
  - Dockerized for deployment readiness
  - used github actions for CI / CD
  - Unit tested with JUnit5 & Mockito
    
 -  **Database**  
    -  designed DB schema that follows normalization for optimized queries and indexes
    -  used @Transactional for dependent steps and data integrety
    -  used validations for columns in the entity layer

 - **Future features**
     - scale throgh : break the services into microservicse , rabbit mq for notifications , add K8s
     - create Refund for the canceled payment
     - add Angular later for admin dashboard and user UI
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

- **Clean architecture**
- **DTOs** for requests and responses to decouple persistence models from exposed APIs, improving security, readability, and maintainability of the codebase.
- **Global exception handling** 
- **Strategy Design Pattern** : allowing seamless integration of any kind of paymet gateways






