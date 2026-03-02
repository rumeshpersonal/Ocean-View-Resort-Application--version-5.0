@echo off
REM Ocean View Resort - Backend Start Script
REM This script runs the Spring Boot backend server

cd /d "%~dp0ocean-view-resort\backend"
echo Starting Ocean View Resort Backend...
echo.

REM Try to use mvn from PATH first
set "MVN_CMD=mvn"
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    REM Fallback to common Maven location
    if exist "C:\Users\ASUS\.maven\maven-3.9.12\bin\mvn.cmd" (
        set "MVN_CMD=C:\Users\ASUS\.maven\maven-3.9.12\bin\mvn.cmd"
        echo Using Maven from: !MVN_CMD!
    ) else if exist "C:\Program Files\apache-maven*\bin\mvn.cmd" (
        for /d %%d in ("C:\Program Files\apache-maven*") do (
            set "MVN_CMD=%%d\bin\mvn.cmd"
        )
        echo Using Maven from: !MVN_CMD!
    ) else (
        echo.
        echo ERROR: Maven not found in PATH or common locations
        echo.
        echo Please do one of the following:
        echo 1. Run setup-environment.bat to auto-configure paths
        echo 2. Manually add Maven bin folder to your system PATH
        echo.
        echo Maven download: https://maven.apache.org/download.cgi
        echo Setup guide: Read QUICKSTART.md for detailed instructions
        echo.
        pause
        exit /b 1
    )
)

REM Check if Java is available
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo.
    echo ERROR: Java is not installed or not in PATH
    echo.
    echo Please do one of the following:
    echo 1. Run setup-environment.bat to auto-configure paths
    echo 2. Install Java 17 from: https://adoptium.net/
    echo 3. Or Oracle JDK: https://www.oracle.com/java/technologies/downloads/
    echo.
    echo Setup guide: Read QUICKSTART.md for detailed instructions
    echo.
    pause
    exit /b 1
)

echo Java found: & java -version
echo.
echo Starting backend on http://localhost:8080
echo.
echo Press Ctrl+C to stop the server
echo.

!MVN_CMD! spring-boot:run

pause
