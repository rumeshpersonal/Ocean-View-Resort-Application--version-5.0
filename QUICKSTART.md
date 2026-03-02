# Ocean View Resort - Quick Start Guide

## Prerequisites (One-time Setup)

Before running the application, you need to install:

### 1. Java Development Kit (JDK 17+)
- Download from: https://www.oracle.com/java/technologies/downloads/
- Or use OpenJDK from: https://adoptium.net/
- After installation, verify: Open PowerShell and run `java -version`

### 2. Apache Maven
- Download from: https://maven.apache.org/download.cgi
- Extract to a location (e.g., `C:\Program Files\apache-maven`)
- Add Maven `bin` folder to your system PATH
- Verify: Open PowerShell and run `mvn -version`

### 3. MySQL Database
- Download XAMPP from: https://www.apachefriends.org/
- Or install MySQL directly from: https://dev.mysql.com/downloads/mysql/
- Start MySQL service before running the application

## How to Add Java and Maven to System PATH (Windows)

### Easiest Method: Run the Setup Script
1. Open the project folder
2. Right-click `setup-environment.bat`
3. Select "Run as administrator"
4. Follow the prompts
5. **Close all PowerShell/Command Prompt windows**
6. **Open a new PowerShell/Command Prompt window**
7. Now run the backend/frontend scripts

### Manual Method:
#### For Java:
1. Right-click "This PC" → Properties
2. Click "Advanced system settings"
3. Click "Environment Variables"
4. Under "User variables" or "System variables", click "New"
5. Variable name: `JAVA_HOME`
6. Variable value: `C:\path\to\your\java\installation` (e.g., `C:\Program Files\Java\jdk-17`)
7. Click OK
8. Now find the `Path` variable, click Edit
9. Click "New" and add: `C:\path\to\your\java\installation\bin`
10. Click OK

#### For Maven:
1. Follow same steps as Java
2. Variable name: `MAVEN_HOME`
3. Variable value: `C:\path\to\apache-maven-3.9.x`
4. Add to Path: `C:\path\to\apache-maven-3.9.x\bin`

## Running the Application

### Option 1: Setup First (Recommended for First-Time Users)
1. Copy the project folder to any location on your computer
2. Open the project folder in File Explorer
3. **Right-click `setup-environment.bat`** and select "Run as administrator"
4. Follow the prompts to auto-configure Java and Maven paths
5. **Close all PowerShell/Command Prompt windows** (important!)
6. **Open a new PowerShell/Command Prompt window**
7. Now run the application scripts:
   - **Backend**: Double-click `run-backend.bat`
   - **Frontend**: Double-click `run-frontend.bat` (in a separate window)

### Option 2: Simple Click-and-Run (If Paths Already Configured)
1. **To run Backend**: Double-click `run-backend.bat`
2. **To run Frontend**: Double-click `run-frontend.bat` (in a separate window)

### Option 2: PowerShell Commands
1. Open PowerShell in the project root folder
2. **To run Backend**: `.\run-backend.bat`
3. **To run Frontend**: `.\run-frontend.bat`

### Option 3: Manual Commands (Advanced)
```powershell
REM Backend
cd .\ocean-view-resort\backend
mvn spring-boot:run

REM Frontend (in another PowerShell window)
cd .\ocean-view-resort\desktop
mvn javafx:run
```

## Application Access

- **Backend API**: http://localhost:8080
- **Frontend GUI**: Launches automatically
- **Database**: localhost:3306 (MySQL)

## Troubleshooting

### "Maven is not installed or not in PATH"
**Solution:**
1. Run `setup-environment.bat` as administrator (right-click → Run as administrator)
2. Close all command/PowerShell windows
3. Open a new command/PowerShell window
4. Try running the project scripts again

If issue persists:
- Download Maven from: https://maven.apache.org/download.cgi
- Extract to a folder (e.g., `C:\Program Files\apache-maven`)
- Run `setup-environment.bat` again

### "mvn: The term 'mvn' is not recognized"
- Maven is not in your system PATH
- Run `setup-environment.bat` and follow the prompts
- Make sure to close and reopen your command/PowerShell window after setup

### "java: The term 'java' is not recognized"
- Java is not in your system PATH
- Run `setup-environment.bat` and follow the prompts
- Or manually install Java 17 from: https://adoptium.net/
- Make sure to close and reopen your command/PowerShell window after setup

### "Cannot connect to database"
- Make sure MySQL is running (check XAMPP or MySQL service)
- Database will auto-create on first run

### Port 8080 already in use
- Another application is using port 8080
- Check via: `netstat -ano | findstr ":8080"`
- Kill the process or change the port in `backend/src/main/resources/application.properties`

## Project Structure

```
Ocean-View-Resort-Application--version-5.0/
├── setup-environment.bat     <- Run this first (right-click → Run as admin)
├── run-backend.bat           <- Click to start backend
├── run-frontend.bat          <- Click to start frontend
├── run-backend.ps1           <- PowerShell alternative for backend
├── run-frontend.ps1          <- PowerShell alternative for frontend
├── QUICKSTART.md             <- This file
└── ocean-view-resort/
    ├── backend/              <- Spring Boot API
    ├── desktop/              <- JavaFX Desktop App
    └── database/             <- SQL scripts
```

## Notes

- Keep the relative folder structure intact for the scripts to work
- The batch files assume Java and Maven are in your system PATH
- On first run, the backend will create database tables automatically
- Default login: username=`reception` (password will be set during first run)

## For macOS/Linux Users

Use the PowerShell commands manually:
```bash
# Backend
cd ocean-view-resort/backend
mvn spring-boot:run

# Frontend
cd ocean-view-resort/desktop
mvn javafx:run
```
