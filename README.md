Siopa Auth Service 🔒

Authentication & Authorization for the Siopa Platform
Features

    User Registration & Login (JWT)
    Role-Based Access (Admin, Store Owner, User)
    Password Reset

Tech Stack

    Spring Boot, Spring Security, PostgreSQL, JWT

Setup

1️⃣ Clone & Configure

git clone https://github.com/your-repo/siopa-auth-service.git  
cd siopa-auth-service  

Set environment variables (Windows):

setx DB_USERNAME "your_db_username"  
setx DB_PASSWORD "your_db_password"  
setx JWT_SECRET "your_jwt_secret_key"  
setx JWT_EXPIRATION_MS "86400000"  

2️⃣ Run the Service

mvn spring-boot:run  # Maven  
./gradlew bootRun     # Gradle  

API Endpoints

🔹 POST /api/auth/signup → Register user
🔹 POST /api/auth/signin → Login & get JWT
🔹 GET /api/auth/users/{id} → Get user details (JWT required)
Signup Example (Postman)

{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123",
  "role": ["ROLE_USER"]
}

Security

    JWT-Based Authentication
    Role-Based Access: ROLE_USER, ROLE_STORE_OWNER, ROLE_ADMIN
