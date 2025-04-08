# Financial Tracker

A project designed to help users track profits and expenses. **Spring Boot** is used for the backend and **React** (and Vite) for the frontend.

## Features

- Tracking system for finances.
- **Spring Boot** backend with REST APIs.
- **Springdoc-OpenAPI** supports **Swagger-UI** to automate REST API documentation generation.
- **React** frontend built using **Vite** for fast development and optimized builds.
- **MSW** allows for API mocking in the frontend for testing.
- **Email/password registration and login** functionality.
- **Spring Security** with **JWT-based authentication** with stateless session management.
- **PostgreSQL** for data persistence with **Spring Data JPA**.

## Tech Stack

- **Backend**: Spring Boot, Spring Security, Spring Data JPA, JWT, Gradle
- **Frontend**: React, Vite, Tanstack (Table, Query, Router)
- **Database**: PostgreSQL
- **Other**: Docker

## Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/BeaverByte/FinancialTracker.git
   ```

2. Navigate to the root directory:

   ```bash
   cd FinancialTracker
   ```

3. Backend Setup (Spring Boot):

   - Create a PostgreSQL database:

   ```sql
   CREATE DATABASE financial_transactions;
   ```

   - Create `.env` in src/main/resources with your database credentials and JWT properties.

   ```
   DATABASE_URL=jdbc:postgresql://localhost:5432/financial_transactions
   DATABASE_USERNAME= your_database_username
   DATABASE_PASSWORD= your_database_password
   ```

   - The JWT Cookie Name should be different from the JWT Refresh Cookie Name.

   ```
   JWT_COOKIE_NAME=
   JWT_REFRESH_COOKIE_NAME=
   JWT_SECRET=
   JWT_EXPIRATION_MS=
   JWT_REFRESH_EXPIRATION_MS=
   ```

   - Run command to start the backend:

   ```bash
   npm run backend
   ```

4. Frontend Setup (React and Vite):

   - Navigate to the `frontend` folder:

   ```bash
   cd frontend
   ```

   - Install dependencies and start the frontend:

   ```bash
   npm install
   npm run dev
   ```

5. The application will be accessible at `http://localhost:5173`.

## Security

- **JWT Authentication**: JSON Web Tokens (JWT) are used to secure API requests and enforce stateless session management.
- **Email/Password Authentication**: Users can register and log in using their email and password. Upon successful authentication, JWT tokens are issued.
