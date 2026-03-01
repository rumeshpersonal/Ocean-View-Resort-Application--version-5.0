# Ocean View Resort Backend - Setup & API Documentation

## Overview

The Ocean View Resort Backend is a **Spring Boot 3.3** REST API that provides room reservation, billing, and authentication services for the hotel management system. It connects to a **MySQL 8+** database and uses **JWT token-based authentication**.

---

## Tech Stack

- **Java**: 17 (LTS)
- **Framework**: Spring Boot 3.3.2
- **Web**: Spring Web (REST API)
- **Database**: JPA/Hibernate + MySQL 8+
- **Security**: Spring Security + JWT tokens
- **Validation**: Jakarta Validation
- **Build**: Maven 3.9+

---

## Prerequisites

Before running the backend, ensure you have:

1. **Java 17 JDK** installed
   ```bash
   java -version  # Should show Java 17.x.x
   ```

2. **MySQL 8+** installed and running
   ```bash
   mysql --version  # Should show MySQL 8.0+
   ```

3. **Maven 3.9+** installed
   ```bash
   mvn --version
   ```

4. **Database initialized** with schema and seed data
   - See [Database Setup Guide](#database-setup-guide) below

---

## Project Structure

```
backend/
├── pom.xml                          # Maven configuration
├── src/main/
│   ├── java/com/oceanview/backend/
│   │   ├── BackendApplication.java  # Main Spring Boot entry point
│   │   ├── controller/              # REST API endpoints
│   │   │   ├── AuthController.java
│   │   │   ├── ReservationController.java
│   │   │   ├── BillingController.java
│   │   │   ├── HelpController.java
│   │   │   └── ApiExceptionHandler.java
│   │   ├── service/                 # Business logic
│   │   │   ├── AuthService.java
│   │   │   ├── ReservationService.java
│   │   │   ├── BillingService.java
│   │   │   └── DataSeeder.java
│   │   ├── model/                   # JPA entities
│   │   │   ├── User.java
│   │   │   ├── Guest.java
│   │   │   ├── RoomType.java
│   │   │   ├── Reservation.java
│   │   │   └── Bill.java
│   │   ├── repo/                    # Data access layer
│   │   │   ├── UserRepo.java
│   │   │   ├── GuestRepo.java
│   │   │   ├── RoomTypeRepo.java
│   │   │   ├── ReservationRepo.java
│   │   │   └── BillRepo.java
│   │   ├── dto/                     # Data transfer objects
│   │   │   ├── LoginRequest.java
│   │   │   ├── LoginResponse.java
│   │   │   ├── ReservationCreateRequest.java
│   │   │   ├── ReservationResponse.java
│   │   │   ├── BillResponse.java
│   │   │   └── ErrorResponse.java
│   │   └── security/                # Security & JWT
│   │       ├── SecurityConfig.java
│   │       ├── TokenService.java
│   │       └── TokenAuthFilter.java
│   └── resources/
│       └── application.properties    # Configuration
```

---

## Configuration

### 1. Database Credentials

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ocean_view_resort?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

Replace `YOUR_MYSQL_PASSWORD` with your MySQL root password.

### 2. JWT Secret Key

Update the JWT secret (must be long and random):

```properties
app.auth.tokenSecret=CHANGE_ME_TO_A_LONG_RANDOM_SECRET_KEY_12345678901234567890
app.auth.tokenExpiryMinutes=720
```

### 3. Default Server Port

```properties
server.port=8080
```

---

## Build & Run

### 1. Build the Project

```bash
cd backend
mvn clean install
```

This will:
- Compile all Java files
- Package dependencies
- Create executable JAR

### 2. Run the Backend Server

```bash
mvn spring-boot:run
```

Or:

```bash
java -jar target/backend-1.0.0.jar
```

**Expected Output:**
```
Started BackendApplication in X.XXX seconds
Server is running on http://localhost:8080
✓ Admin user 'reception' created
```

---

## REST API Endpoints

### 1. Authentication (Public)

#### POST `/api/auth/login`

Login with credentials and receive JWT token.

**Request:**
```json
{
  "username": "reception",
  "password": "Reception@123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "username": "reception"
}
```

**Error (401 Unauthorized):**
```json
{
  "error": "Invalid credentials"
}
```

---

### 2. Reservation Management (Protected)

All endpoints require `Authorization: Bearer <token>` header.

#### POST `/api/reservations`

Create a new reservation.

**Request:**
```json
{
  "guestName": "Sahan Jayasinghe",
  "address": "No. 21, Lighthouse Street, Galle",
  "contactNumber": "0765552211",
  "roomType": "DELUXE",
  "checkIn": "2026-03-10",
  "checkOut": "2026-03-12"
}
```

**Response (201 Created):**
```json
{
  "reservationNo": "RES-2026-0001",
  "guestName": "Sahan Jayasinghe",
  "address": "No. 21, Lighthouse Street, Galle",
  "contactNumber": "0765552211",
  "roomType": "DELUXE",
  "checkIn": "2026-03-10",
  "checkOut": "2026-03-12"
}
```

**Validation Errors (400 Bad Request):**
```json
{
  "error": "Check-out date must be after check-in date"
}
```

---

#### GET `/api/reservations/{reservationNo}`

View reservation details.

**URL:** `/api/reservations/RES-2026-0001`

**Response (200 OK):**
```json
{
  "reservationNo": "RES-2026-0001",
  "guestName": "Sahan Jayasinghe",
  "address": "No. 21, Lighthouse Street, Galle",
  "contactNumber": "0765552211",
  "roomType": "DELUXE",
  "checkIn": "2026-03-10",
  "checkOut": "2026-03-12"
}
```

**Not Found (404):**
```json
{
  "error": "Reservation not found"
}
```

---

### 3. Billing (Protected)

#### POST `/api/bills/{reservationNo}`

Generate bill for a reservation.

**URL:** `POST /api/bills/RES-2026-0001`

**Response (200 OK):**
```json
{
  "reservationNo": "RES-2026-0001",
  "nights": 2,
  "ratePerNight": 18000.0,
  "totalAmount": 36000.0
}
```

**Calculation:**
- nights = checkout_date - checkin_date
- totalAmount = nights × ratePerNight

---

### 4. Help (Public)

#### GET `/api/help`

Get help/usage information.

**Response (200 OK):**
```json
{
  "message": "===============================\nOCEAN VIEW RESORT - HELP GUIDE\n...[detailed help text]..."
}
```

---

## Room Types & Rates

```
STANDARD: LKR 12,000 per night
DELUXE:   LKR 18,000 per night
SUITE:    LKR 25,000 per night
```

---

## Validation Rules

### Contact Number
- Valid formats: `0771234567` or `+94771234567`
- Required for all reservations

### Dates
- Check-out must be **after** check-in
- Minimum 1 night stay required

### Room Type
- Must be: `STANDARD`, `DELUXE`, or `SUITE`
- Case-sensitive

### Reservation Number
- Auto-generated: `RES-2026-XXXX`
- Unique across system

---

## Database Schema

### Tables

**users** - Staff login credentials
- id (PK)
- username (UNIQUE)
- password_hash (BCrypt)

**guests** - Guest information
- id (PK)
- full_name
- address
- contact_number

**room_types** - Available room options
- id (PK)
- type_name (ENUM: STANDARD, DELUXE, SUITE)
- rate_per_night (DECIMAL)

**reservations** - Booking records
- id (PK)
- reservation_no (UNIQUE)
- guest_id (FK → guests)
- room_type_id (FK → room_types)
- check_in (DATE)
- check_out (DATE)

**bills** - Billing records
- id (PK)
- reservation_id (FK → reservations, UNIQUE)
- nights (INT)
- total_amount (DECIMAL)
- created_at (TIMESTAMP)

---

## Security

### Authentication Flow

1. **Desktop app** sends credentials to `/api/auth/login`
2. **Backend** validates user and returns JWT token
3. **Desktop app** stores token in memory
4. **Desktop app** includes token in all subsequent requests:
   ```
   Authorization: Bearer <token>
   ```
5. **Backend** validates token and processes request
6. Token **expires** after 720 minutes (12 hours)

### Password Hashing

- User passwords stored as **BCrypt hashes**
- Passwords never stored in plain text
- Default user created on startup:
  - Username: `reception`
  - Password: `Reception@123` (hashed)

---

## Error Handling

All errors return JSON with status codes:

```json
{
  "error": "Error description here"
}
```

| Status | Meaning |
|--------|---------|
| 200    | Success |
| 201    | Created |
| 400    | Bad Request / Validation Error |
| 401    | Unauthorized / Invalid Token |
| 404    | Not Found |
| 500    | Server Error |

---

## Testing

### Test with cURL

```bash
# 1. Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"reception","password":"Reception@123"}'

# Response contains token, copy it

# 2. Create Reservation
curl -X POST http://localhost:8080/api/reservations \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "guestName": "Kasun Perera",
    "address": "No. 12, Wakwella Road, Galle",
    "contactNumber": "0771234567",
    "roomType": "STANDARD",
    "checkIn": "2026-03-15",
    "checkOut": "2026-03-17"
  }'

# 3. View Reservation
curl -X GET http://localhost:8080/api/reservations/RES-2026-0001 \
  -H "Authorization: Bearer YOUR_TOKEN"

# 4. Generate Bill
curl -X POST http://localhost:8080/api/bills/RES-2026-0001 \
  -H "Authorization: Bearer YOUR_TOKEN"

# 5. Get Help
curl -X GET http://localhost:8080/api/help
```

---

## Common Issues

### Issue: "Connection refused" at localhost:3306

**Solution:** Ensure MySQL server is running
```bash
# Windows
net start MySQL80

# macOS
brew services start mysql
```

### Issue: "Access denied for user 'root'@'localhost'"

**Solution:** Update password in `application.properties`
```properties
spring.datasource.password=YOUR_CORRECT_PASSWORD
```

### Issue: "Table doesn't exist"

**Solution:** Run database schema and seed scripts
```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < database/seed.sql
```

---

## Next Steps

1. ✅ Backend is running on `http://localhost:8080`
2. Start the **Desktop JavaFX application**:
   ```bash
   cd ../desktop
   mvn javafx:run
   ```
3. Login with: `reception` / `Reception@123`
4. Test all reservation features

---

## Support

For issues or questions, refer to:
- [ocean-view-resort-copilot.md](../ocean-view-resort-copilot.md) - Complete build guide
- [Database Setup Guide](../database/README.md) - Database initialization
- Main [README.md](../README.md) - Project overview

---

**Last Updated:** March 1, 2026  
**Status:** ✅ Ready for Production
