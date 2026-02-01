@echo off
REM Simple compile script for Dormitory Management System (Windows)
REM Requires: JDK 21+ with JavaFX and MySQL Connector/J

echo Compiling Dormitory Management System...

REM Create output directory
if not exist out mkdir out
if not exist lib mkdir lib

REM Set JavaFX path (update this to your JavaFX installation path)
set JAVAFX_PATH=C:\javafx-sdk-21\lib

REM MySQL Connector path
set MYSQL_CONNECTOR=lib\mysql-connector-j.jar

REM Check if JavaFX path exists
if not exist "%JAVAFX_PATH%" (
    echo JavaFX not found at %JAVAFX_PATH%
    echo Please download JavaFX from https://openjfx.io/ and update JAVAFX_PATH in this script
    exit /b 1
)

REM Check if MySQL connector exists
if not exist "%MYSQL_CONNECTOR%" (
    echo MySQL Connector not found at %MYSQL_CONNECTOR%
    echo.
    echo Please download MySQL Connector/J:
    echo   1. Go to https://dev.mysql.com/downloads/connector/j/
    echo   2. Download the Platform Independent ZIP
    echo   3. Extract mysql-connector-j-X.X.X.jar to lib\mysql-connector-j.jar
    echo.
    exit /b 1
)

REM Compile all Java files
javac --module-path "%JAVAFX_PATH%" ^
      --add-modules javafx.controls,javafx.fxml ^
      -cp "%MYSQL_CONNECTOR%" ^
      -d out ^
      src\main\java\dorm\*.java ^
      src\main\java\dorm\dao\*.java ^
      src\main\java\dorm\model\*.java ^
      src\main\java\dorm\service\*.java ^
      src\main\java\dorm\ui\*.java ^
      src\main\java\dorm\ui\components\*.java ^
      src\main\java\dorm\util\*.java

if %ERRORLEVEL% EQU 0 (
    REM Copy resources to output directory
    echo Copying resources...
    if not exist out\dorm\ui mkdir out\dorm\ui
    xcopy /Y src\main\resources\dorm\ui\*.fxml out\dorm\ui\ >nul 2>&1
    xcopy /Y src\main\resources\dorm\*.properties out\dorm\ >nul 2>&1
    
    echo Compilation successful!
    echo.
    echo Before running, make sure MySQL is configured:
    echo   1. Start MySQL server
    echo   2. Run: mysql -u root -p ^< sql\schema.sql
    echo   3. Update database credentials in src\main\resources\dorm\db.properties
    echo.
    echo Run with: run.bat
) else (
    echo Compilation failed!
    exit /b 1
)
