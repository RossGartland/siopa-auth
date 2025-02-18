Siopa Auth Service 🔒

Authentication & Authorization Service for the Siopa Platform
🚀 Features

    ✅ User Registration & Login (JWT Authentication)
    ✅ Role-Based Access Control (Admin, Store Owner, User)
    ✅ Password Reset & Secure API Endpoints

🛠 Tech Stack

    Spring Boot 3.x, Spring Security 6.x, PostgreSQL, JWT

🔧 Setup
1️⃣ Clone & Configure

git clone https://github.com/your-repo/siopa-auth-service.git
cd siopa-auth-service

Set environment variables (Windows):

setx DB_USERNAME "your_db_username"
setx DB_PASSWORD "your_db_password"
setx JWT_SECRET "your_jwt_secret_key"
setx JWT_EXPIRATION_MS "86400000"

2️⃣ Run the Service

mvn spring-boot:run  # Using Maven
./gradlew bootRun     # Using Gradle

📌 API Endpoints
Method	Endpoint	Description	Auth
POST	/api/auth/signup	Register a new user	❌
POST	/api/auth/signin	Authenticate & get JWT	❌
GET	/api/auth/users/{id}	Get user details	✅
📜 User Signup Example (Postman)

{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123",
  "role": ["ROLE_USER"]
}

🔑 Security

    JWT-Based Authentication
    Role-Based Access Control (RBAC)
        ROLE_USER: General user
        ROLE_STORE_OWNER: Manage store
        ROLE_ADMIN: Full access
