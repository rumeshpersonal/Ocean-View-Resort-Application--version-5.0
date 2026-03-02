# Ocean View Resort - Frontend Start Script (PowerShell)
# This script runs the JavaFX desktop application

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Ocean View Resort - Frontend Startup" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Get script directory
$scriptDir = Split-Path -Parent -Path $MyInvocation.MyCommand.Definition

# Change to desktop directory using relative path
Push-Location "$scriptDir\ocean-view-resort\desktop"

# Check if Maven is available
Write-Host "Checking for Maven..." -ForegroundColor Yellow
$mvn = Get-Command mvn -ErrorAction SilentlyContinue
if (-not $mvn) {
    Write-Host "ERROR: Maven is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Maven and add it to your system PATH" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

# Check if Java is available
Write-Host "Checking for Java..." -ForegroundColor Yellow
$java = Get-Command java -ErrorAction SilentlyContinue
if (-not $java) {
    Write-Host "ERROR: Java is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Java 17 or above and add it to your system PATH" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "Java version:" -ForegroundColor Green
java -version
Write-Host ""
Write-Host "Maven version:" -ForegroundColor Green
mvn -v | Select-String "Apache Maven"
Write-Host ""
Write-Host "Starting frontend application..." -ForegroundColor Green
Write-Host ""

# Run Maven
mvn javafx:run

Pop-Location
Read-Host "Press Enter to exit"
