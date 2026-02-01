#!/bin/bash

# Simple run script for Dormitory Management System
# Requires: JDK 21+ with JavaFX and MySQL Connector/J

echo "Starting Dormitory Management System..."

# Find JavaFX path (adjust this path based on your JavaFX installation)
JAVAFX_PATH="/usr/share/openjfx/lib"

# MySQL Connector path
MYSQL_CONNECTOR="lib/mysql-connector-j.jar"

# Check if JavaFX path exists
if [ ! -d "$JAVAFX_PATH" ]; then
    echo "JavaFX not found at $JAVAFX_PATH"
    echo "Please install JavaFX or update JAVAFX_PATH in this script"
    exit 1
fi

# Check if compiled classes exist
if [ ! -d "out" ]; then
    echo "No compiled classes found. Run ./compile.sh first"
    exit 1
fi

# Check if MySQL connector exists
if [ ! -f "$MYSQL_CONNECTOR" ]; then
    echo "MySQL Connector not found at $MYSQL_CONNECTOR"
    echo "Please run ./compile.sh first to download it"
    exit 1
fi

# Run the application
java --module-path "$JAVAFX_PATH" \
     --add-modules javafx.controls,javafx.fxml \
     -cp "out:$MYSQL_CONNECTOR" \
     dorm.App
