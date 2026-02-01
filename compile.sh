#!/bin/bash

# Compile script for Dormitory Management System
# Uses Maven for dependency management

echo "==================================="
echo "Dormitory Management System"
echo "==================================="

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Maven is not installed!"
    echo ""
    echo "Please install Maven:"
    echo "  Ubuntu/Debian: sudo apt-get install maven"
    echo "  macOS: brew install maven"
    echo ""
    exit 1
fi

echo "Compiling with Maven..."
mvn clean compile -q

if [ $? -eq 0 ]; then
    echo ""
    echo "Compilation successful!"
    echo ""
    echo "Before running, make sure MySQL is configured:"
    echo "  1. Start MySQL server"
    echo "  2. Run: mysql -u root -p < sql/schema.sql"
    echo "  3. Update database credentials in src/main/resources/dorm/db.properties"
    echo ""
    echo "Run with: ./run.sh"
else
    echo ""
    echo "Compilation failed!"
    exit 1
fi
