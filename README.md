
#  Money Manager API

A secure and scalable personal finance management backend built with **Spring Boot**. The application enables users to manage their income and expenses, visualize spending patterns, and securely access their financial data using JWT authentication.

---

##  Features

-  User Registration & Login
-  Email Verification with Activation Link
-  JWT Authentication & Authorization
-  Role-based Security using Spring Security
-  Add Income & Expense Transactions
-  Categorize Transactions
-  Filter Transactions by
    - Date
    - Category
    - Transaction Type
-  Dashboard Analytics
-  Monthly Report Generation
-  Export Reports as PDF & Excel
-  Exception Handling
- ️ MySQL Database Integration

---

##  Tech Stack

### Backend
- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate

### Database
- MySQL

### Authentication
- JWT (JSON Web Token)

### Build Tool
- Maven

### Additional Libraries
- Lombok
- Java Mail Sender
- Apache POI (Excel)
- PDF Generation Library

---

##  Project Structure

```
src
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── security
├── service
├── util
└── MoneyManagerApplication.java
```

---

##  Authentication Flow

1. User registers.
2. Verification email is sent.
3. User activates account.
4. User logs in.
5. JWT Token is generated.
6. Protected APIs require the JWT token.

---

##  Core Functionalities

### User Management
- Register User
- Verify Email
- Login
- Update Profile

### Transaction Management
- Add Transaction
- Update Transaction
- Delete Transaction
- View Transactions
- Filter Transactions

### Reports
- Monthly Expense Report
- Monthly Income Report
- Export to PDF
- Export to Excel

---

## ️Getting Started

###Clone Repository

```bash
git clone https://github.com/SIVATEJA2005/money-manager-api.git
```

### Navigate

```bash
cd money-manager-api
```

### Configure Database

Update:

```
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/money_manager
spring.datasource.username=root
spring.datasource.password=your_password
```

---

### Run the Application

Using Maven

```bash
mvn spring-boot:run
```

or

```bash
./mvnw spring-boot:run
```

---

##  API Modules

- Authentication
- User
- Transactions
- Categories
- Reports

---

##  Security

- Spring Security
- JWT Authentication
- Password Encryption using BCrypt
- Stateless Authentication
- Protected REST APIs

---

##  Future Improvements

- Budget Planning
- Savings Goals
- Recurring Transactions
- Email Notifications
- Mobile Application
- Charts & Insights
- Multi-Currency Support

---

##  Author

**Mandadi Sivateja**

- GitHub: https://github.com/SIVATEJA2005
- LinkedIn: *(Add your LinkedIn profile here)*
