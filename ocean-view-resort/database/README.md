# Database Setup Guide - Ocean View Resort

This guide explains how to set up the MySQL database for the Ocean View Resort reservation system.

---

## Prerequisites

- **MySQL 8.0+** installed
- **MySQL command-line client** or **MySQL Workbench**
- Database script files: `schema.sql` and `seed.sql`

---

## Step 1: Start MySQL Server

### Windows
```bash
net start MySQL80
```

### macOS (Homebrew)
```bash
brew services start mysql
```

### Linux (systemd)
```bash
sudo systemctl start mysql
```

---

## Step 2: Create Database & Tables

### Option A: Using Command Line

Navigate to the `database/` directory and run:

```bash
cd database
mysql -u root -p < schema.sql
```

When prompted, enter your MySQL root password.

### Option B: Using MySQL Workbench

1. Open **MySQL Workbench**
2. Go to **File** → **Open SQL Script**
3. Select `database/schema.sql`
4. Click **Execute** (or press Ctrl+Shift+Enter)

---

## Step 3: Seed Initial Data

This step inserts room types and sample guests/reservations.

### Option A: Using Command Line

```bash
mysql -u root -p < seed.sql
```

### Option B: Using MySQL Workbench

1. Open **File** → **Open SQL Script**
2. Select `database/seed.sql`
3. Click **Execute**

---

## Step 4: Verify Database Setup

### Check Database Created

```bash
mysql -u root -p

mysql> SHOW DATABASES;
```

You should see `ocean_view_resort` in the list.

### Check Tables Created

```bash
mysql> USE ocean_view_resort;
mysql> SHOW TABLES;
```

Expected output:
```
+-------------------------------+
| Tables_in_ocean_view_resort   |
+-------------------------------+
| bills                         |
| guests                        |
| reservations                  |
| room_types                    |
| users                         |
+-------------------------------+
```

### Check Data Inserted

```bash
mysql> SELECT * FROM room_types;
mysql> SELECT * FROM guests;
```

Expected output:
```
Room Types:
+----+----------+---------------+
| id | type_name | rate_per_night |
+----+----------+---------------+
| 1  | STANDARD | 12000.00      |
| 2  | DELUXE   | 18000.00      |
| 3  | SUITE    | 25000.00      |
+----+----------+---------------+

Guests (sample):
+----+------------------+--------------------------------+----------------+
| id | full_name        | address                        | contact_number |
+----+------------------+--------------------------------+----------------+
| 1  | Kasun Perera     | No. 12, Wakwella Road, Galle   | 0771234567     |
| 2  | Dinushi Fernando | No. 44, Marine Drive, Negombo  | 0719876543     |
| 3  | Nimal Silva      | No. 8, Temple Road, Matara     | 0753332211     |
+----+------------------+--------------------------------+----------------+
```

---

## Database Schema Overview

### users
Staff login credentials (encrypted passwords)
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL
);
```

### room_types
Available room options and pricing
```sql
CREATE TABLE room_types (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  type_name ENUM('STANDARD','DELUXE','SUITE') NOT NULL UNIQUE,
  rate_per_night DECIMAL(10,2) NOT NULL
);
```

### guests
Guest contact information
```sql
CREATE TABLE guests (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  full_name VARCHAR(100) NOT NULL,
  address VARCHAR(200) NOT NULL,
  contact_number VARCHAR(15) NOT NULL
);
```

### reservations
Booking records linking guests to room types
```sql
CREATE TABLE reservations (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  reservation_no VARCHAR(30) NOT NULL UNIQUE,
  guest_id BIGINT NOT NULL,
  room_type_id BIGINT NOT NULL,
  check_in DATE NOT NULL,
  check_out DATE NOT NULL,
  FOREIGN KEY (guest_id) REFERENCES guests(id),
  FOREIGN KEY (room_type_id) REFERENCES room_types(id)
);
```

### bills
Billing records for completed reservations
```sql
CREATE TABLE bills (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  reservation_id BIGINT NOT NULL UNIQUE,
  nights INT NOT NULL,
  total_amount DECIMAL(10,2) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);
```

---

## Sample Data Inserted

### Room Types
- **STANDARD**: LKR 12,000 per night
- **DELUXE**: LKR 18,000 per night
- **SUITE**: LKR 25,000 per night

### Sample Guests
1. **Kasun Perera** - No. 12, Wakwella Road, Galle - 0771234567
2. **Dinushi Fernando** - No. 44, Marine Drive, Negombo - 0719876543
3. **Nimal Silva** - No. 8, Temple Road, Matara - 0753332211
4. **Sahan Jayasinghe** - No. 21, Lighthouse Street, Galle - 0765552211
5. **Anjali Kumar** - No. 15, Beach Road, Colombo - 0772223333

### Sample Reservations
1. **RES-2026-0001** - Kasun Perera, STANDARD room, Mar 2-4, 2026
2. **RES-2026-0002** - Dinushi Fernando, DELUXE room, Mar 5-7, 2026

---

## Important Notes

### Character Set
Database uses UTF-8 (utf8mb4) for proper support of special characters and emoji.

### Password Security
User passwords are hashed using BCrypt by the backend application. Never store plain-text passwords.

### Auto-Increment IDs
All tables use auto-incrementing primary keys for unique identification.

### Foreign Keys
- Reservations reference guests and room_types
- Bills reference reservations
- This ensures data integrity and prevents orphaned records

### Timestamps
The `bills` table automatically records creation time using `CURRENT_TIMESTAMP`.

---

## Resetting the Database

If you need to start fresh, you can drop and recreate the database:

```bash
mysql -u root -p

mysql> DROP DATABASE ocean_view_resort;
mysql> SOURCE /path/to/schema.sql;
mysql> SOURCE /path/to/seed.sql;
```

Or in one command:
```bash
mysql -u root -p < schema.sql && mysql -u root -p < seed.sql
```

---

## Connecting from Backend

The Spring Boot backend connects to this database with:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ocean_view_resort?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

Make sure to update the password in `backend/src/main/resources/application.properties`.

---

## Connecting from MySQL Workbench

1. Open MySQL Workbench
2. Click **"+"** next to "MySQL Connections"
3. Set:
   - **Connection Name:** Ocean View Resort
   - **Hostname:** localhost
   - **Port:** 3306
   - **Username:** root
   - **Password:** [your password]
4. Test connection and save

---

## Troubleshooting

### "Access denied for user 'root'@'localhost'"
- Check your MySQL password
- Update password in connection string

### "Can't connect to MySQL server on 'localhost' (10061)"
- MySQL server is not running
- Start it using commands in Step 1

### "Table already exists"
- Database has already been initialized
- Use DDL to drop/recreate if needed (see "Resetting the Database")

### "Character set mismatch"
- Use `utf8mb4` charset (already set in schema.sql)
- Ensure your MySQL client also uses UTF-8

---

## Next Steps

1. ✅ Database is initialized and seeded
2. Update backend `application.properties` with correct password
3. Start the **Backend API** server
4. Start the **Desktop JavaFX** application

---

**Last Updated:** March 1, 2026  
**Status:** ✅ Database Ready
