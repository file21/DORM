#!/bin/bash

# Simple compile script for Dormitory Management System
# Requires: JDK 21+ with JavaFX and MySQL Connector/J

echo "Compiling Dormitory Management System..."

# Create output directory
mkdir -p out

# Find JavaFX path (adjust this path based on your JavaFX installation)
# Common locations:
#   Linux: /usr/share/openjfx/lib
#   macOS: /opt/homebrew/opt/openjfx/libexec/lib
#   Windows: C:\javafx-sdk-21\lib

JAVAFX_PATH="/usr/share/openjfx/lib"

# MySQL Connector path (download from https://dev.mysql.com/downloads/connector/j/)
MYSQL_CONNECTOR="lib/mysql-connector-j.jar"

# Check if JavaFX path exists
if [ ! -d "$JAVAFX_PATH" ]; then
    echo "JavaFX not found at $JAVAFX_PATH"
    echo "Please install JavaFX or update JAVAFX_PATH in this script"
    echo ""
    echo "To install on Ubuntu/Debian:"
    echo "  sudo apt-get install openjfx"
    echo ""
    exit 1
fi

# Check if MySQL connector exists
if [ ! -f "$MYSQL_CONNECTOR" ]; then
    echo "MySQL Connector not found at $MYSQL_CONNECTOR"
    echo "Attempting to download MySQL Connector..."
    mkdir -p lib
    
    # Try to download MySQL connector
    if command -v wget &> /dev/null; then
        wget -O lib/mysql-connector-j.jar "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar" 2>/dev/null
    elif command -v curl &> /dev/null; then
        curl -L -o lib/mysql-connector-j.jar "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar" 2>/dev/null
    fi
    
    if [ ! -f "$MYSQL_CONNECTOR" ]; then
        echo ""
        echo "Could not download MySQL Connector. Please download manually:"
        echo "  1. Download from https://dev.mysql.com/downloads/connector/j/"
        echo "  2. Place the JAR file at: lib/mysql-connector-j.jar"
        echo ""
        exit 1
    fi
    echo "MySQL Connector downloaded successfully."
fi

# Compile all Java files
javac --module-path "$JAVAFX_PATH" \
      --add-modules javafx.controls,javafx.fxml \
      -cp "$MYSQL_CONNECTOR" \
      -d out \
      src/main/java/dorm/*.java \
      src/main/java/dorm/dao/*.java \
      src/main/java/dorm/model/*.java \
      src/main/java/dorm/service/*.java \
      src/main/java/dorm/ui/*.java \
      src/main/java/dorm/ui/components/*.java \
      src/main/java/dorm/util/*.java

if [ $? -eq 0 ]; then
    # Copy resources to output directory
    echo "Copying resources..."
    mkdir -p out/dorm/ui
    cp -r src/main/resources/dorm/ui/*.fxml out/dorm/ui/ 2>/dev/null || true
    cp -r src/main/resources/dorm/*.properties out/dorm/ 2>/dev/null || true
    
    echo "Compilation successful!"
    echo ""
    echo "Before running, make sure MySQL is configured:"
    echo "  1. Start MySQL server"
    echo "  2. Run: mysql -u root -p < sql/schema.sql"
    echo "  3. Update database credentials in src/main/resources/dorm/db.properties"
    echo ""
    echo "Run with: ./run.sh"
else
    echo "Compilation failed!"
    exit 1
fi
