# Dormitory Management System

A JavaFX desktop application for managing dormitory applications and assignments with CSV file-based data persistence.

## Features

### For Students
- Account registration with ID validation (format: UGR/XXXX/YY)
- Two-phase application system:
  - **Phase 1**: Sponsorship type, residency, address (Addis Ababa residents get dropdown selection)
  - **Phase 2**: Emergency contact, transaction ID (unlocked after Phase 1 approval)
- View announcements and messages from admin

### For Admins
- Filter applications by gender, residency, subcity, woreda, college, sponsorship, status
- Bulk approve/decline/request resubmit (with reason sent as message)
- Assign buildings to approved students
- Export selected students to CSV
- Post/edit/delete announcements
- Message students with read tracking

### For Owners
- All admin capabilities
- Manage admin staff accounts

## Requirements

- **Java JDK 21** or later
- **JavaFX 21** (included with most IDEs or via Maven/Gradle)
- **IntelliJ IDEA** (recommended)

## How to Run (IntelliJ IDEA)

1. Open project in IntelliJ IDEA
2. Ensure JavaFX is configured (File → Project Structure → Libraries)
3. Run `App.java` as main class

## Default Login Credentials

| Role    | Username | Password  |
|---------|----------|-----------|
| Admin   | admin    | admin123  |
| Owner   | owner    | owner123  |
| Student | student1 | pass1234  |

## Project Structure

```
src/main/java/dorm/
├── App.java                 # Main entry point
├── dao/                     # Data Access (CSV repositories)
├── model/                   # Data models (Student, User, etc.)
├── service/                 # Business logic
├── ui/                      # JavaFX UI components
└── util/                    # Utilities (CsvHelper)

data/                        # CSV data files (auto-created)
├── users.csv
├── students.csv
├── applications.csv
├── announcements.csv
└── messages.csv
```

## Application Flow

1. **Registration**: Student creates account (8+ char password, valid student ID)
2. **Phase 1**: Fill sponsorship, residency, address
3. **Admin Review**: Approve, decline, or request resubmit (with reason)
4. **Phase 2**: Fill emergency contact, transaction ID (if self-sponsored)
5. **Building Assignment**: Admin assigns building

## License

Educational project for AAU coursework.
