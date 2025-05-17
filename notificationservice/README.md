```markdown
# 📢 Notification Service

A robust and extensible microservice built with Spring Boot that supports sending notifications via **Email**, **SMS**, and **In-App**. It uses RabbitMQ for queuing and supports both single and bulk notification delivery.

---

## 📁 Project Structure

```

notificationservice/
│
├── config/                # Configuration classes (Twilio, Email, Environment)
├── controller/            # REST API endpoints
├── modal/                 # Domain models
├── queue/                 # RabbitMQ producer
├── repo/                  # JPA repositories
├── service/               # Core service logic
└── NotificationServiceApplication.java  # Spring Boot main class

````

---

## 🚀 Features

- ✅ Send notifications via:
  - Email (SMTP)
  - SMS (Twilio)
  - In-app (stored in DB)
- ✅ Bulk notification support
- ✅ Phone number normalization and validation
- ✅ Exception handling and input validation
- ✅ RabbitMQ integration for asynchronous messaging
- ✅ Environment-based configuration loading

---

## ⚙️ Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/raja2576/notification-Service.git
cd notification-Service
````

### 2. Configure Environment

Set up your environment variables using `.env` or directly in your environment:

```
TWILIO_ACCOUNT_SID=your_sid
TWILIO_AUTH_TOKEN=your_auth_token
TWILIO_PHONE_NUMBER=your_twilio_number
EMAIL_HOST=smtp.yourprovider.com
EMAIL_PORT=587
EMAIL_USERNAME=your_email@example.com
EMAIL_PASSWORD=your_password
```

> Ensure RabbitMQ is running locally or available on your configured host.

### 3. Build & Run

```bash
./mvnw clean install
./mvnw spring-boot:run
```

The service will start on:
📍 `http://localhost:8080`

---

## 🔌 API Endpoints

### 1. Send Single Notification

`POST /notifications`

```json
{
  "userId": "123",
  "email": "test@example.com",
  "phone": "9876543210",
  "type": "email", // or "sms" or "inapp"
  "subject": "Hello!",
  "message": "Welcome to our service"
}
```

### 2. Send Bulk Notification

`POST /notifications/bulk`

```json
{
  "userIds": ["123", "124"],
  "type": "sms",
  "subject": "Bulk Alert",
  "message": "This is a bulk notification"
}
```

### 3. Get User Notifications

`GET /notifications/users/{userId}`

---

## 🧪 Screenshots

> *(Add screenshots of API testing using Postman or Swagger below)*

![Postman Screenshot](screenshots/postman-send.png)
![Swagger Screenshot](screenshots/swagger-ui.png)

---

## 🌐 Deployment (Optional)

🔗 [Live Demo / API Documentation](https://your-deployment-url.com)

---

## ✅ Assumptions Made

* Phone numbers are assumed to be Indian if not prefixed with `+` (can be adjusted).
* No user registration flow — user is created on-the-fly if `userId` doesn't exist.
* RabbitMQ is used primarily for future scalability, though synchronous sending is used in current version.

---

## 🛠️ Tech Stack

* Spring Boot
* Java 17+
* RabbitMQ
* Twilio API
* SMTP Email (via JavaMail)
* MySQL / H2 (configurable)
* Maven

---



```

