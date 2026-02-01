@echo off
REM Compile script for Dormitory Management System (Windows)
REM Uses Maven for dependency management

echo ===================================
echo Dormitory Management System
echo ===================================

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Maven is not installed!
    echo.
    echo Please install Maven:
    echo   1. Download from https://maven.apache.org/download.cgi
    echo   2. Add Maven bin directory to PATH
    echo.
    exit /b 1
)

echo Compiling with Maven...
call mvn clean compile -q

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Compilation successful!
    echo.
    echo Before running, make sure MySQL is configured:
    echo   1. Start MySQL server
    echo   2. Run: mysql -u root -p ^< sql\schema.sql
    echo   3. Update database credentials in src\main\resources\dorm\db.properties
    echo.
    echo Run with: run.bat
) else (
    echo.
    echo Compilation failed!
    exit /b 1
)
