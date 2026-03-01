# Desktop Application - Quick Start Guide

## Project Structure

```
desktop/
├── pom.xml                                          # Maven configuration
└── src/
    ├── main/
    │   ├── java/com/oceanview/desktop/
    │   │   ├── DesktopApplication.java              # Main entry point
    │   │   ├── api/
    │   │   │   └── ApiClient.java                   # REST API client
    │   │   ├── controller/
    │   │   │   ├── LoginController.java             # Login screen
    │   │   │   ├── MainMenuController.java          # Main menu
    │   │   │   ├── CreateReservationController.java # Create reservation
    │   │   │   ├── ViewReservationController.java   # View reservation
    │   │   │   ├── GenerateBillController.java      # Generate bill
    │   │   │   └── HelpController.java              # Help/usage
    │   │   └── util/
    │   │       └── Validator.java                   # Input validation
    │   └── resources/
    │       └── fxml/
    │           ├── login.fxml                       # Login UI
    │           ├── mainmenu.fxml                    # Main menu UI
    │           ├── createreservation.fxml           # Create reservation UI
    │           ├── viewreservation.fxml             # View reservation UI
    │           ├── generatebill.fxml                # Generate bill UI
    │           └── help.fxml                        # Help UI
```

## Prerequisites

1. **Java JDK 17+** installed and set in PATH
2. **Maven 3.8+** installed and set in PATH
3. **Spring Boot Backend** running on `localhost:8080`
4. **MySQL Database** initialized with schema and seed data

## Setup & Run Instructions

### Step 1: Build the Desktop Application

```bash
cd desktop
mvn clean install
```

### Step 2: Run the Desktop Application

```bash
mvn javafx:run
```

Or using the IDE:
- Open the project in VS Code with Java Extension Pack
- Right-click on `DesktopApplication.java`
- Select "Run" or "Debug"

## Default Login Credentials

After you seed the backend database, use:

- **Username:** `reception`
- **Password:** `Reception@123`

> These credentials are created in the backend's `DataSeeder` class.

## Features

### 1. Login Screen
- Username/password authentication
- Validates against backend REST API
- Generates JWT token for authenticated requests

### 2. Main Menu
- 4 main operations available:
  - Create New Reservation
  - View Reservation Details
  - Generate Bill
  - View Help & Usage Guide
- Logout and Exit options

### 3. Create Reservation
**Input Fields:**
- Guest Name (required)
- Address (required)
- Contact Number (validates 0771234567 or +94771234567)
- Room Type (STANDARD, DELUXE, SUITE)
- Check-in Date (YYYY-MM-DD format)
- Check-out Date (must be after check-in)

**Validation:**
- All fields required
- Contact format: `0771234567` OR `+94771234567`
- Check-out must be after check-in
- Room type must be STANDARD/DELUXE/SUITE

**Output:** Reservation number (e.g., RES-2026-0001)

### 4. View Reservation
**Input:**
- Reservation number (e.g., RES-2026-0001)

**Output:**
- Guest name, address, contact
- Room type, check-in, check-out dates

### 5. Generate Bill
**Input:**
- Reservation number

**Output:**
- Number of nights
- Rate per night
- Total amount (nights × rate)

### 6. Help & Usage Guide
- Displays help text fetched from backend
- Usage instructions for all features
- Contact and support information

## API Integration

The desktop app communicates with the backend via HTTP:

**Base URL:** `http://localhost:8080/api`

**Endpoints Used:**
- `POST /api/auth/login` - Authenticate
- `POST /api/reservations` - Create reservation
- `GET /api/reservations/{reservationNo}` - View reservation
- `POST /api/bills/{reservationNo}` - Generate bill
- `GET /api/help` - Get help text

## Error Handling

All API errors are displayed to the user:
- Invalid credentials → "Login failed: Invalid credentials"
- Invalid date range → "Check-out must be after check-in"
- Network errors → Appropriate error message

## Technologies Used

- **JavaFX 21** - UI framework
- **FXML** - XML-based UI markup
- **Jackson** - JSON serialization
- **Java HTTP Client** - REST API communication
- **Maven** - Build tool

## Development Notes

### Adding New Features

1. Create controller in `controller/` package
2. Create FXML in `resources/fxml/`
3. Add method in `MainMenuController` to load new scene
4. Update `ApiClient` if new endpoints needed

### Validation

All validation is done in `util/Validator.java`:
- `isValidContact(String)` - Contact number format
- `isValidDateRange(String, String)` - Date range validation
- `isValidRoomType(String)` - Room type validation
- `isNotEmpty(String)` - Required field check

### API Client

`ApiClient.java` handles all REST communication:
- Automatic Bearer token injection for authenticated requests
- JSON serialization/deserialization with Jackson
- Error message extraction from backend responses

## Troubleshooting

**Problem:** "Connection refused" on login
- **Solution:** Ensure backend is running on port 8080
  ```bash
  cd ../backend
  mvn spring-boot:run
  ```

**Problem:** "User not found" error
- **Solution:** Seed backend database with user data
  ```bash
  mysql -u root -p ocean_view_resort < ../database/seed.sql
  ```

**Problem:** "Invalid date format"
- **Solution:** Use YYYY-MM-DD format (e.g., 2026-03-10)

**Problem:** JavaFX modules not found
- **Solution:** Maven will auto-download JavaFX 21. If issues persist:
  ```bash
  mvn clean install -U
  ```

## Next Steps

1. ✅ Create backend Spring Boot REST API (see backend/README.md)
2. ✅ Initialize MySQL database (see database/schema.sql)
3. ✅ Seed test data (see database/seed.sql)
4. ✅ Run desktop app (this guide)
5. Test all features end-to-end

---

**Built with ❤️ for Ocean View Resort**
