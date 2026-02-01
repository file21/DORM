@echo off
REM Run script for Dormitory Management System (Windows)
REM Uses Maven with JavaFX plugin

echo Starting Dormitory Management System...

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Maven is not installed!
    echo Please install Maven first.
    exit /b 1
)

REM Run the application
call mvn javafx:run -q

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Failed to start. Please check:
    echo   1. MySQL server is running
    echo   2. Database credentials in src\main\resources\dorm\db.properties are correct
    echo   3. Run: mysql -u root -p ^< sql\schema.sql to create the database
)
