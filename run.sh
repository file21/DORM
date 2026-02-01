#!/bin/bash

# Run script for Dormitory Management System
# Uses Maven with JavaFX plugin

echo "Starting Dormitory Management System..."

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed!"
    echo "Please install Maven first."
    exit 1
fi

# Run the application
mvn javafx:run -q

if [ $? -ne 0 ]; then
    echo ""
    echo "Failed to start. Please check:"
    echo "  1. MySQL server is running"
    echo "  2. Database credentials in src/main/resources/dorm/db.properties are correct"
    echo "  3. Run: mysql -u root -p < sql/schema.sql to create the database"
fi
