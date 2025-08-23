# Garage System

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14-blue)](https://www.postgresql.org/)
[![Security](https://img.shields.io/badge/Security-Spring%20Security%20%2B%20JWT-red)](https://spring.io/projects/spring-security)
[![Tests](https://img.shields.io/badge/Tests-JUnit5%20%2B%20Mockito-yellow)](https://junit.org/junit5/)

An application that simulates a **vehicle parking system** by managing a garage with available slots.  
Vehicles are assigned to slots using one of two strategies:  

- **First-Come-First-Served (FCFS)**
- **Best-Fit Allocation**

It also calculates **parking fees** based on the duration of stay.

---

## ✨ Features
-  Manage garage slots  
-  Register vehicle entry and exit  
-  Assign vehicles with **FCFS** or **Best-Fit** strategy  
-  Calculate parking fees  
-  Secure authentication & authorization with **Spring Security + JWT**  
-  Persist data with **PostgreSQL**  
-  Tested with **JUnit5 & Mockito**

---

## 🛠️ Tech Stack
- **Backend:** Spring Boot  
- **Database:** PostgreSQL  
- **Security:** Spring Security + JWT  
- **Testing:** JUnit5, Mockito

---
### 🔹 Design Patterns Used

- **Strategy Pattern**  
  - For parking slot assignment (`FirstComeFirstServed` vs. `BestFit`).  

- **Observer Pattern** *(planned for notifications)*  
  - For events like **slot availability** or **expiring vehicle time**.  

- **Factory Pattern**  
  - For creating vehicle objects dynamically (`Car`, `Truck`, `Motorcycle`).  

- **Singleton Pattern**  
  - For components like `FeeCalculatorService` where a single shared instance is required.
  - 
- **Layered Architecture (MVC + Service + Repository)**  
  - Clear separation of concerns:  
    - **Controller Layer** → REST endpoints  
    - **Service Layer** → Business logic (parking, fee calculation)  
    - **Repository Layer** → Database operations with PostgreSQL  

## 📌 Future Improvements

The current version is an MVP that focuses on the **core garage system**. later points will cover:

- **Containerization & Deployment**  
- **Payment gateway**  


