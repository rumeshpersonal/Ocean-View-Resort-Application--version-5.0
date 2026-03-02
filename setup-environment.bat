@echo off
REM Ocean View Resort - Environment Setup Script
REM This script helps configure Java and Maven paths for the project

setlocal enabledelayedexpansion

title Ocean View Resort - Environment Setup

echo.
echo ========================================
echo Ocean View Resort - Setup Wizard
echo ========================================
echo.
echo This script will help you configure Java and Maven for the project.
echo.

REM Check if running as administrator
openfiles >nul 2>&1
if errorlevel 1 (
    echo WARNING: This script should be run as Administrator to modify system PATH
    echo.
    pause
    exit /b 1
)

REM Try to find Java
echo Searching for Java installation...
where java >nul 2>nul
if %errorlevel% equ 0 (
    echo [OK] Java found in PATH
    java -version 2>&1 | findstr /i "version" >nul
    if !errorlevel! equ 0 (
        for /f "tokens=*" %%a in ('java -version 2^>^&1') do (
            echo Found: %%a
            goto :java_found
        )
    )
)

echo [NOT FOUND] Java not in system PATH
echo.
echo Would you like to search for Java on your drives? (y/n)
set /p search_java=
if /i "%search_java%"=="y" (
    echo Searching for Java (this may take a moment)...
    for /d %%d in (C:\Program\ D:\Program\) do (
        if exist "%%d\Java\jdk*\bin\java.exe" (
            set "JAVA_PATH=%%d\Java\jdk*\bin"
            echo Found Java at: !JAVA_PATH!
            goto :add_java
        )
    )
    if exist "C:\Users\ASUS\.jdk\jdk-17.0.16\bin\java.exe" (
        set "JAVA_PATH=C:\Users\ASUS\.jdk\jdk-17.0.16\bin"
        echo Found Java at: !JAVA_PATH!
        goto :add_java
    )
)

:java_found
echo.

REM Try to find Maven
echo Searching for Maven installation...
where mvn >nul 2>nul
if %errorlevel% equ 0 (
    echo [OK] Maven found in PATH
    mvn -version 2>&1 | findstr /i "Apache Maven" >nul
    if !errorlevel! equ 0 (
        echo Maven is already available
        goto :maven_found
    )
)

echo [NOT FOUND] Maven not in system PATH
echo.
echo Checking common Maven locations...

if exist "C:\Users\ASUS\.maven\maven-3.9.12\bin\mvn.cmd" (
    set "MAVEN_PATH=C:\Users\ASUS\.maven\maven-3.9.12\bin"
    echo Found Maven at: !MAVEN_PATH!
    goto :add_maven
)

if exist "C:\Program Files\apache-maven*\bin\mvn.cmd" (
    for /d %%d in (C:\Program Files\apache-maven*) do (
        set "MAVEN_PATH=%%d\bin"
        echo Found Maven at: !MAVEN_PATH!
        goto :add_maven
    )
)

echo Maven not found in common locations
echo.

:add_maven
if defined MAVEN_PATH (
    echo Adding Maven to system PATH: !MAVEN_PATH!
    setx PATH "!MAVEN_PATH!;!PATH!"
    echo Maven path added successfully
)

:add_java
if defined JAVA_PATH (
    echo Adding Java to system PATH: !JAVA_PATH!
    setx PATH "!JAVA_PATH!;!PATH!"
    echo Java path added successfully
)

:maven_found
echo.
echo ========================================
echo Setup Complete!
echo ========================================
echo.
echo To apply the changes, please:
echo 1. Close this window
echo 2. Close all other command/PowerShell windows
echo 3. Open a new command prompt or PowerShell window
echo 4. Run the project scripts again
echo.

pause
exit /b 0
