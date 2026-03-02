# Ocean View Resort — Distributed Room Reservation System (Copilot Build Guide)

This single Markdown file is designed for **VS Code Copilot Agent** to read and automatically scaffold and implement the project from an **empty repository**.

---

## 0) Project Overview

**Client:** Ocean View Resort (Galle, Sri Lanka)  
**Problem:** Manual reservations cause booking conflicts and delays.  
**Solution:** A computerized system with:

- ✅ Desktop UI (**JavaFX**, menu-driven, user-friendly)
- ✅ Backend (**Spring Boot REST API**, web services → distributed app)
- ✅ Database (**MySQL**, persistent storage)
- ✅ Validation (restrict invalid entries)
- ✅ Billing (calculate total cost based on nights and room rate)
- ✅ Help (usage guide for new staff)
- ✅ Exit (safe close/logout)
- ✅ Seed data with Sri Lankan names

---

## 1) Tech Stack & Requirements

### Backend
- Java 17
- Spring Boot 3.x (Maven)
- Spring Web, Validation, Spring Data JPA
- MySQL Driver
- Spring Security (simple login endpoint; token-based)

### Desktop
- Java 17
- JavaFX 21 (FXML)
- Java HTTP Client (built-in)
- Jackson for JSON mapping

### Database
- MySQL 8+
- Seed data with Sri Lankan names

---

## 2) Repository Structure (create first)

In the empty repo root, create this structure:

```
ocean-view-resort/
  backend/
  desktop/
  database/
  docs/
  README.md
```

---

## 3) Database (MySQL) — Schema + Seed

### 3.1 Create `database/schema.sql`

```sql
CREATE DATABASE IF NOT EXISTS ocean_view_resort
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE ocean_view_resort;

-- USERS (for login)
CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL
);

-- ROOM TYPES
CREATE TABLE IF NOT EXISTS room_types (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  type_name ENUM('STANDARD','DELUXE','SUITE') NOT NULL UNIQUE,
  rate_per_night DECIMAL(10,2) NOT NULL
);

-- GUESTS
CREATE TABLE IF NOT EXISTS guests (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  full_name VARCHAR(100) NOT NULL,
  address VARCHAR(200) NOT NULL,
  contact_number VARCHAR(15) NOT NULL
);

-- RESERVATIONS
CREATE TABLE IF NOT EXISTS reservations (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  reservation_no VARCHAR(30) NOT NULL UNIQUE,
  guest_id BIGINT NOT NULL,
  room_type_id BIGINT NOT NULL,
  check_in DATE NOT NULL,
  check_out DATE NOT NULL,
  CONSTRAINT fk_res_guest FOREIGN KEY (guest_id) REFERENCES guests(id),
  CONSTRAINT fk_res_roomtype FOREIGN KEY (room_type_id) REFERENCES room_types(id)
);

-- BILLS
CREATE TABLE IF NOT EXISTS bills (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  reservation_id BIGINT NOT NULL UNIQUE,
  nights INT NOT NULL,
  total_amount DECIMAL(10,2) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_bill_res FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);
```

### 3.2 Create `database/seed.sql`

> Room types + sample Sri Lankan guests/reservations.  
> Users are seeded via backend using BCrypt (recommended).

```sql
USE ocean_view_resort;

INSERT INTO room_types (type_name, rate_per_night) VALUES
('STANDARD', 12000.00),
('DELUXE', 18000.00),
('SUITE', 25000.00)
ON DUPLICATE KEY UPDATE rate_per_night = VALUES(rate_per_night);

INSERT INTO guests (full_name, address, contact_number) VALUES
('Kasun Perera', 'No. 12, Wakwella Road, Galle', '0771234567'),
('Dinushi Fernando', 'No. 44, Marine Drive, Negombo', '0719876543'),
('Nimal Silva', 'No. 8, Temple Road, Matara', '0753332211');

INSERT INTO reservations (reservation_no, guest_id, room_type_id, check_in, check_out) VALUES
('RES-2026-0001', 1, 1, '2026-03-02', '2026-03-04'),
('RES-2026-0002', 2, 2, '2026-03-05', '2026-03-07');
```

### 3.3 Run SQL

In MySQL Workbench or CLI:

```sql
SOURCE /absolute/path/to/database/schema.sql;
SOURCE /absolute/path/to/database/seed.sql;
```

---

## 4) Backend (Spring Boot REST API) — Build Steps

### 4.1 Create Backend Skeleton

Inside `backend/` create a Spring Boot Maven project (Java 17).  
Ensure `groupId` = `com.oceanview` and `artifactId` = `backend`.

**Backend endpoints required:**
- `POST /api/auth/login` → returns `{ token, username }`
- `POST /api/reservations` → create reservation
- `GET /api/reservations/{reservationNo}` → view reservation details
- `POST /api/bills/{reservationNo}` → generate bill
- `GET /api/help` → help text

### 4.2 Backend `pom.xml` (FULL)

Create/replace `backend/pom.xml` with:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.oceanview</groupId>
  <artifactId>backend</artifactId>
  <version>1.0.0</version>
  <packaging>jar</packaging>

  <name>ocean-view-backend</name>

  <properties>
    <java.version>17</java.version>
    <spring-boot.version>3.3.2</spring-boot.version>
  </properties>

  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>${spring-boot.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>

  <dependencies>
    <!-- Web + JSON -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Validation -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- JPA -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- Security (token auth) -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!-- MySQL -->
    <dependency>
      <groupId>com.mysql</groupId>
      <artifactId>mysql-connector-j</artifactId>
      <scope>runtime</scope>
    </dependency>

    <!-- Lombok (optional) -->
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <optional>true</optional>
    </dependency>

    <!-- Tests -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>

      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <source>${java.version}</source>
          <target>${java.version}</target>
          <annotationProcessorPaths>
            <path>
              <groupId>org.projectlombok</groupId>
              <artifactId>lombok</artifactId>
              <version>1.18.34</version>
            </path>
          </annotationProcessorPaths>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```

### 4.3 Backend application config

Create `backend/src/main/resources/application.properties`:

```properties
server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/ocean_view_resort?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

app.auth.tokenSecret=CHANGE_ME_TO_A_LONG_RANDOM_SECRET
app.auth.tokenExpiryMinutes=720
```

### 4.4 Backend Package Layout

Create:

```
backend/src/main/java/com/oceanview/backend/
  BackendApplication.java
  controller/
  dto/
  model/
  repo/
  service/
  security/
```

### 4.5 Backend Main Class

`backend/src/main/java/com/oceanview/backend/BackendApplication.java`:

```java
package com.oceanview.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendApplication {
  public static void main(String[] args) {
    SpringApplication.run(BackendApplication.class, args);
  }
}
```

### 4.6 Entities (model)

Create these files:

- `model/User.java`
- `model/Guest.java`
- `model/RoomType.java`
- `model/Reservation.java`
- `model/Bill.java`

Use these exact implementations:

```java
// User.java
package com.oceanview.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name="users")
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false, unique=true, length=50)
  private String username;

  @Column(name="password_hash", nullable=false)
  private String passwordHash;
}
```

```java
// Guest.java
package com.oceanview.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name="guests")
public class Guest {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="full_name", nullable=false, length=100)
  private String fullName;

  @Column(nullable=false, length=200)
  private String address;

  @Column(name="contact_number", nullable=false, length=15)
  private String contactNumber;
}
```

```java
// RoomType.java
package com.oceanview.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name="room_types")
public class RoomType {

  public enum RoomTypeName { STANDARD, DELUXE, SUITE }

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name="type_name", nullable=false, unique=true)
  private RoomTypeName typeName;

  @Column(name="rate_per_night", nullable=false)
  private double ratePerNight;
}
```

```java
// Reservation.java
package com.oceanview.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name="reservations")
public class Reservation {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="reservation_no", nullable=false, unique=true, length=30)
  private String reservationNo;

  @ManyToOne(optional=false)
  @JoinColumn(name="guest_id")
  private Guest guest;

  @ManyToOne(optional=false)
  @JoinColumn(name="room_type_id")
  private RoomType roomType;

  @Column(name="check_in", nullable=false)
  private LocalDate checkIn;

  @Column(name="check_out", nullable=false)
  private LocalDate checkOut;
}
```

```java
// Bill.java
package com.oceanview.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name="bills")
public class Bill {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(optional=false)
  @JoinColumn(name="reservation_id", unique=true)
  private Reservation reservation;

  @Column(nullable=false)
  private int nights;

  @Column(name="total_amount", nullable=false)
  private double totalAmount;

  @Column(name="created_at", nullable=false)
  private LocalDateTime createdAt;
}
```

### 4.7 Repositories (repo)

Create these interfaces:

```java
// UserRepo.java
package com.oceanview.backend.repo;

import com.oceanview.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
}
```

```java
// GuestRepo.java
package com.oceanview.backend.repo;

import com.oceanview.backend.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepo extends JpaRepository<Guest, Long> {}
```

```java
// RoomTypeRepo.java
package com.oceanview.backend.repo;

import com.oceanview.backend.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoomTypeRepo extends JpaRepository<RoomType, Long> {
  Optional<RoomType> findByTypeName(RoomType.RoomTypeName typeName);
}
```

```java
// ReservationRepo.java
package com.oceanview.backend.repo;

import com.oceanview.backend.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReservationRepo extends JpaRepository<Reservation, Long> {
  Optional<Reservation> findByReservationNo(String reservationNo);
  boolean existsByReservationNo(String reservationNo);
}
```

```java
// BillRepo.java
package com.oceanview.backend.repo;

import com.oceanview.backend.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BillRepo extends JpaRepository<Bill, Long> {
  Optional<Bill> findByReservation_ReservationNo(String reservationNo);
}
```

### 4.8 DTOs (dto)

Create these:

```java
// LoginRequest.java
package com.oceanview.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
  @NotBlank String username,
  @NotBlank String password
) {}
```

```java
// LoginResponse.java
package com.oceanview.backend.dto;

public record LoginResponse(
  String token,
  String username
) {}
```

```java
// ReservationCreateRequest.java
package com.oceanview.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationCreateRequest(
  @NotBlank String guestName,
  @NotBlank String address,
  @NotBlank String contactNumber,
  @NotNull String roomType,
  @NotNull LocalDate checkIn,
  @NotNull LocalDate checkOut
) {}
```

```java
// ReservationResponse.java
package com.oceanview.backend.dto;

import java.time.LocalDate;

public record ReservationResponse(
  String reservationNo,
  String guestName,
  String address,
  String contactNumber,
  String roomType,
  LocalDate checkIn,
  LocalDate checkOut
) {}
```

```java
// BillResponse.java
package com.oceanview.backend.dto;

public record BillResponse(
  String reservationNo,
  int nights,
  double ratePerNight,
  double totalAmount
) {}
```

### 4.9 Security (token auth)

Create:

- `security/SecurityConfig.java`
- `security/TokenService.java`
- `security/TokenAuthFilter.java`

Use exactly the implementations from the prior version of this file (same as earlier sections in this document).

> If Copilot needs: those security classes are included earlier in this guide—create them exactly.

### 4.10 Services (service)

Create:

- `AuthService.java`
- `ReservationService.java`
- `BillingService.java`
- `DataSeeder.java`

Use exactly the implementations from earlier sections in this guide.

### 4.11 Controllers (controller)

Create:

- `ApiExceptionHandler.java`
- `AuthController.java`
- `ReservationController.java`
- `BillingController.java`
- `HelpController.java`

Use exactly the implementations from earlier sections in this guide.

### 4.12 Run Backend

```bash
cd backend
mvn spring-boot:run
```

---

## 5) Desktop (JavaFX) — Build Steps

### 5.1 Desktop `pom.xml`

Create `desktop/pom.xml` exactly as above.

### 5.2 Desktop Java Layout

Create the folder layout and files exactly as specified.

### 5.3 JavaFX FXML Screens

Create the FXML templates exactly as above.

### 5.4 JavaFX Controllers + API client

Create the controller classes and `ApiClient/Json` exactly as above.

### 5.5 Run Desktop

```bash
cd desktop
mvn javafx:run
```

---

## 6) Required Validations

Implement BOTH in backend and desktop:

- Contact: `0771234567` OR `+94771234567`
- Check-out must be after check-in
- RoomType must be STANDARD/DELUXE/SUITE
- User-friendly error messages from backend: `{"error":"..."}`

---

## 7) Run Order (Important)

1) Start MySQL server
2) Run schema and seed
3) Start backend (`mvn spring-boot:run`)
4) Start desktop (`mvn javafx:run`)

---

## 8) Sample API Payloads

### Login

POST `/api/auth/login`

```json
{"username":"reception","password":"Reception@123"}
```

### Create Reservation

POST `/api/reservations`

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

### Generate Bill

POST `/api/bills/RES-2026-XXXXXX`

---

## 9) Git Workflow (Daily commits)

- main (stable)
- dev (work)
- feature/* branches

Commit style:
- feat:, fix:, chore:, docs:, test:

---

## 10) Assumptions for Documentation

- Guests are not system users; staff registers guests.
- Room type rates stored in DB.
- Billing = nights × rate per night.
- Reservation number generated and unique.
- Distributed system: JavaFX → REST API → MySQL.
