
````markdown
# Notification Service

A robust microservice to handle sending notifications via Email, SMS, and In-App messages.  
Built with Spring Boot, RabbitMQ, Twilio, and SMTP integration.

---

## Table of Contents

- [Project Structure](#project-structure)  
- [Features](#features)  
- [Setup Instructions](#setup-instructions)  
- [API Documentation](#api-documentation)  
- [Assumptions](#assumptions)  
- [Screenshots](#screenshots)  
- [Future Enhancements](#future-enhancements)  
- [Contact](#contact)  

---

## Project Structure

```plaintext
notificationservice/
├── config/                 # Twilio, SMTP, environment configs
├── controller/             # REST API endpoints
├── modal/                  # Java models/entities (User, Notification, etc.)
├── queue/                  # RabbitMQ message producers
├── repo/                   # Spring Data JPA Repositories
├── service/                # Notification service logic
└── NotificationServiceApplication.java  # Main Spring Boot application
````

---

## Features

* Send notifications to users via:

  * **Email** (SMTP)
  * **SMS** (Twilio)
  * **In-App** (stored in database)
* Bulk notification support
* Phone number normalization and validation
* Persist notifications for user retrieval
* RESTful API endpoints for interaction
* RabbitMQ integration for scalable message queuing (setup assumed)

---

## Setup Instructions

1. **Clone the repository**

   ```bash
   git clone https://github.com/raja2576/notification-Service.git
   cd notification-Service
   ```

2. **Configure Environment Variables**

   Set the following environment variables or update `EnvConfig` to suit your environment:

   | Variable              | Description                                          |
   | --------------------- | ---------------------------------------------------- |
   | `TWILIO_ACCOUNT_SID`  | Your Twilio Account SID                              |
   | `TWILIO_AUTH_TOKEN`   | Your Twilio Auth Token                               |
   | `TWILIO_PHONE_NUMBER` | Your Twilio phone number                             |
   | SMTP Configs          | Your SMTP host, username, password for email sending |

3. **Database**

   Configure your database connection in `application.properties` or `application.yml`. By default, H2 or any Spring-supported datasource can be used.

4. **Run RabbitMQ**

   Make sure RabbitMQ is running on your machine or server, accessible by the service.

5. **Build and Run**

   Using Maven:

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

6. **Test the APIs**

   Use tools like Postman or curl (see API documentation below).

---

## API Documentation

### 1. Send Single Notification

**POST** `/notifications`

**Request Body** (JSON):

```json
{
  "userId": "user123",
  "email": "user@example.com",
  "phone": "+919876543210",
  "type": "email", 
  "subject": "Welcome!",
  "message": "Hello, welcome to our service."
}
```

* `type` can be `email`, `sms`, or `inapp`
* `email` or `phone` must be provided

**Success Response:**

* Status: `200 OK`
* Body: `"Notification queued"`

**Error Response:**

* Status: `400 Bad Request`
* Body: Error message explaining missing fields or invalid data

---

### 2. Get Notifications for a User

**GET** `/notifications/users/{userId}`

**Example:**

```
GET /notifications/users/user123
```

**Success Response:**

* Status: `200 OK`
* Body: JSON array of notification messages

```json
[
  "Hello, welcome to our service.",
  "Your order has been shipped."
]
```

**Error Response:**

* Status: `404 Not Found`
* Body: `"User not found with id: user123"`

---

### 3. Send Bulk Notifications

**POST** `/notifications/bulk`

**Request Body** (JSON):

```json
{
  "userIds": ["user123", "user456"],
  "type": "sms",
  "subject": "Update",
  "message": "This is a bulk notification."
}
```

* Sends the same notification to multiple users
* Users without required contact info will be skipped with a console log

**Success Response:**

* Status: `200 OK`
* Body: `"Bulk notification sent successfully."`

**Error Response:**

* Status: `400 Bad Request`
* Body: Error message on invalid input or missing user IDs

---

## Assumptions

* Users are identified uniquely by `userId`.
* Phone numbers are normalized to E.164 format, with default country code `+91` if missing.
* Twilio and SMTP services are correctly configured with valid credentials.
* RabbitMQ is used for queuing but setup details are abstracted here.
* Email and SMS sending services (`EmailSender`, `SmsSender`) are implemented and injected correctly.

---

## Screenshots

*Add screenshots of your Postman tests, application logs, or UI here*

---

## Future Enhancements

* Add authentication and authorization for API endpoints
* Support for more notification channels (push notifications, WhatsApp)
* Dashboard for notification history and stats
* Retry mechanism and dead letter queues for failed messages

---

## Contact

For questions or suggestions, please contact:
**Raja** - [raja2576@gmail.com](mailto:raja2576@gmail.com)
GitHub: [https://github.com/raja2576](https://github.com/raja2576)

---

*Thank you for using Notification Service!*

```

---

If you want, I can also help you generate sample Postman collections or curl commands for quick testing! Would that be helpful?
```
