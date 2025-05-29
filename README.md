# Peer2Peer Loan Platform 💸

A full-stack Java Spring Boot application that simulates a peer-to-peer lending system. Borrowers can register and request loans, while lenders can fund them. The platform manages user authentication with JWT, role-based access, and secure payment logic.

## 🌟 Features

- 🔐 JWT-based user authentication (Login/Register)
- 👥 Role-based access for borrowers and lenders
- 💰 Loan requests and funding flow
- 📅 Loan due dates and repayment tracking
- 🛠 Built with Spring Boot, Spring Security, JPA, and PostgreSQL

## 🚀 Getting Started

1. Clone the repository  
   `git clone https://github.com/YOUR_USERNAME/peer2peer-loan.git`

2. Run the app  
   `./mvnw clean spring-boot:run`

3. Use Postman to interact with:
   - `/api/users/register`  
   - `/api/users/login`  
   - `/api/loans/request`  
   - `/api/loans/{id}/fund`  
   - `/api/loans/{id}/repay`  

## 📦 Tech Stack

- Java 21
- Spring Boot 3.5
- PostgreSQL
- JWT
- Maven

---
