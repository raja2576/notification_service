```markdown
# 📢 Notification Service

A scalable and easy-to-use **Notification Microservice** built with **Spring Boot** that delivers messages to users via **Email**, **SMS**, and **In-App notifications**. Designed to be simple, fast, and extensible for use in any microservices architecture or monolithic application.

---

## 🧠 What Does This Service Do?

Imagine you're building an app like Amazon or Swiggy. You want to notify users when:

- Their order is confirmed ✅
- Their delivery is on the way 📦
- Their password is changed 🔐

This Notification Service allows you to send these alerts **via Email, SMS**, or as **in-app messages**, automatically.

---

## 🌟 Key Features

- 🔔 Send **Email**, **SMS**, and **In-App** notifications
- 👥 Send to **single** or **multiple users** (bulk)
- ✅ Validates and saves user contact info automatically
- 🗃️ Uses **RabbitMQ** for message queuing
- 📩 Simple REST API interface
- 🔐 Phone numbers are validated and standardized

---

## 📂 Project Structure (Overview for Developers)

```

notificationservice/
├── config/                 # Twilio, SMTP, environment configs
├── controller/             # REST API endpoints
├── modal/                  # Java models/entities (User, Notification, etc.)
├── queue/                  # RabbitMQ message producers
├── repo/                   # Spring Data JPA Repositories
├── service/                # Notification service logic
└── NotificationServiceApplication.java

````

---

## 🛠️ Tech Stack

| Component     | Technology             |
|---------------|------------------------|
| Backend       | Java 17, Spring Boot   |
| Messaging     | RabbitMQ               |
| Email         | JavaMail + SMTP        |
| SMS           | Twilio API             |
| Database      | NoSql | Neon DB
| Build Tool    | Maven                  |

---

## 🚀 How to Set Up (Step-by-Step)

### 1. Clone the Repository

```bash
git clone https://github.com/raja2576/notification-Service.git
cd notification-Service
````

### 2. Configure Your Environment

Create a `.env` file or set these as system environment variables:

```properties
# Twilio SMS
TWILIO_ACCOUNT_SID=your_account_sid
TWILIO_AUTH_TOKEN=your_auth_token
TWILIO_PHONE_NUMBER=your_twilio_number

# Email (SMTP)
EMAIL_HOST=smtp.yourprovider.com
EMAIL_PORT=587
EMAIL_USERNAME=your_email@example.com
EMAIL_PASSWORD=your_password
```

> 📝 **Note:** Twilio and Email credentials are required only for their respective notification types.

### 3. Run the Application

```bash
./mvnw spring-boot:run
```

App will be available at:
👉 `http://localhost:8080`

---

## 📘 API Documentation

### 1. ✅ Send a Single Notification

`POST /notifications`

#### ➡️ Request Body (Example)

```json
{
  "userId": "u001",
  "email": "test@example.com",
  "phone": "9876543210",
  "type": "email",  // Options: email, sms, inapp
  "subject": "Welcome!",
  "message": "Thanks for joining our service."
}
```

#### ✅ Response

```json
"Notification queued"
```

---

### 2. 📨 Send Bulk Notification

`POST /notifications/bulk`

#### ➡️ Request Body (Example)

```json
{
  "userIds": ["u001", "u002"],
  "type": "email",
  "subject": "Holiday Notice",
  "message": "Our offices will remain closed tomorrow."
}
```

#### ✅ Response

```json
"Bulk notification sent successfully."
```

> ❗ Will skip users without a phone/email depending on type.

---

### 3. 🔍 Get Notifications for a User

`GET /notifications/users/{userId}`

#### Example

```
GET /notifications/users/u001
```

#### ✅ Response

```json
[
  "Thanks for joining our service.",
  "Your profile has been updated.",
  "Holiday notice: Office will be closed."
]
```

---

## 🧪 Testing the APIs

You can test the above endpoints using tools like:

* [✅ Postman](https://www.postman.com/)
* ✅ Swagger (if integrated)
* ✅ Curl or browser

> Include screenshots here:

```
📸 [Add screenshots of Postman testing or Swagger UI]
```

---

## 🌐 Optional: Live Demo

> 🔗 [Your Deployed URL or Swagger Documentation](https://your-api.com/docs)

---

## ✅ Assumptions

* If a phone number doesn't start with `+`, it's treated as an **Indian number (+91)**
* If a user doesn't exist, they're **automatically created** when sending the notification
* In-app notifications are stored in the database and not pushed externally

---

## 📦 Example Users

You can insert sample users via your database or allow the API to create them during notification.

---

## 🔐 Security Notes

* Always keep your `.env` file or credentials out of version control
* Use HTTPS in production to secure SMS and email APIs



---

```
