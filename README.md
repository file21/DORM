# Dormitory Management System

A JavaFX desktop application for managing dormitory applications and assignments with MySQL database persistence.

## Features

### For Students
- Account registration with ID validation (format: UGR/XXXX/YY)
- Student ID serves as username for simpler login
- Two-phase application system:
  - **Phase 1**: Sponsorship type, residency, address (Addis Ababa residents get dropdown selection)
  - **Phase 2**: Emergency contact, transaction ID (unlocked after Phase 1 approval)
- View announcements and messages from admin
- Send messages to admin (max 80 characters)

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

---

## Requirements

| Component | Version | Notes |
|-----------|---------|-------|
| **Java JDK** | 17, 21, or later | OpenJDK or Oracle JDK |
| **JavaFX SDK** | 17+ | Must match JDK version |
| **MySQL Server** | 8.0+ | Required for data storage |
| **MySQL Connector/J** | 8.3.0+ | JDBC driver (auto-downloaded) |
| **IDE** | IntelliJ IDEA (recommended) | Eclipse/NetBeans also work |

---

## Database Setup

Before running the application, you must set up the MySQL database:

### 1. Install MySQL Server
If you don't have MySQL installed:
- **Ubuntu/Debian**: `sudo apt-get install mysql-server`
- **macOS**: `brew install mysql`
- **Windows**: Download from https://dev.mysql.com/downloads/mysql/

### 2. Initialize the Database

```bash
# Log into MySQL
mysql -u root -p

# Run the schema script
source sql/schema.sql
```

Or from command line:
```bash
mysql -u root -p < sql/schema.sql
```

### 3. Configure Database Connection

Edit `src/main/resources/dorm/db.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/dormitory_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.username=root
db.password=your_password_here
```

---

## Quick Start

### Option 1: IntelliJ IDEA (Recommended)

1. **Open Project**
   - File → Open → Select this project folder

2. **Configure JavaFX** (if not using module system)
   - File → Project Structure → Libraries → Add JavaFX SDK
   - Or add VM options: `--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml`

3. **Set Working Directory**
   - Run → Edit Configurations → Working Directory: `$ProjectFileDir$`
   - **Important**: The `data/` folder must be accessible from the working directory

4. **Run Application**
   - Right-click `App.java` → Run 'App.main()'

### Option 2: Command Line (Linux/macOS)

```bash
# Navigate to project root
cd Dormitory-Management-System

# Compile (downloads MySQL connector automatically if missing)
./compile.sh

# Run
./run.sh
```

### Option 3: Command Line (Windows)

```batch
# Compile
compile.bat

# Run
run.bat
```

Note: You may need to download MySQL Connector/J manually on Windows - see compile.bat for instructions.

### Option 4: Eclipse

1. Import as Existing Project
2. Add JavaFX library and MySQL Connector/J to Build Path
3. Run Configurations → Arguments → VM Arguments:
   ```
   --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls
   ```
4. Set Working Directory to project root

---

## Troubleshooting

### "Error: JavaFX runtime components are missing"
- Ensure JavaFX SDK is downloaded and VM options are set correctly
- Download from: https://openjfx.io/

### "Data not saving / Connection refused"
- Ensure MySQL server is running
- Check database credentials in `src/main/resources/dorm/db.properties`
- Verify the `dormitory_db` database exists (run `sql/schema.sql`)

### "MySQL driver not found"
- Ensure `lib/mysql-connector-j.jar` exists
- Run `./compile.sh` to auto-download the connector

### "Class not found" errors
- Check that all `.java` files are compiled
- Verify package structure matches folder structure

---

## Important: Database Configuration

**The application requires a running MySQL server.**

The database connection is configured in `src/main/resources/dorm/db.properties`:
```
project-root/
├── lib/                      ← MySQL Connector JAR
├── sql/                      ← Database schema
│   └── schema.sql
├── src/
│   └── main/
│       ├── java/dorm/
│       └── resources/dorm/
│           └── db.properties ← Database configuration
└── README.md
```

If you get connection errors, check:
1. MySQL server is running
2. Database credentials are correct in `db.properties`
3. The `dormitory_db` database exists

## Default Login Credentials

| Role    | Username | Password  |
|---------|----------|-----------|
| Admin   | admin    | admin123  |
| Owner   | owner    | owner123  |
| Student | student1 | pass1234  |

## Project Structure

```
src/main/java/dorm/
├── App.java                    # Main entry point (JavaFX Application)
├── dao/                        # Data Access Layer (Repository Pattern)
│   ├── *Repository.java        # Repository interfaces (abstraction)
│   ├── MySql*Repository.java   # MySQL implementations (active)
│   ├── Csv*Repository.java     # CSV implementations (legacy/backup)
│   ├── DatabaseConnection.java # MySQL connection manager
│   ├── DaoFactory.java         # Factory for creating repositories
│   └── DataAccessException.java # Custom exception (proper error handling)
├── model/                      # Domain Models (Encapsulation)
│   ├── Student.java            # Extends User (Inheritance)
│   ├── User.java               # Base user class
│   ├── DormApplication.java    # Application entity
│   ├── Announcement.java       # Announcement entity
│   ├── Message.java            # Message entity
│   └── *.java                  # Enums (Gender, Role, College, etc.)
├── service/                    # Business Logic Layer
│   └── DatabaseDormService.java # Service facade (DIP - depends on interfaces)
├── ui/                         # Presentation Layer (JavaFX)
│   ├── LoginViewDb.java        # Login/Registration screen
│   ├── StudentDashboardDb.java # Student dashboard
│   ├── AdminDashboardDb.java   # Admin dashboard
│   ├── OwnerDashboardDb.java   # Owner dashboard
│   └── components/             # Reusable UI components (SRP)
│       ├── ApplicationTableBuilder.java
│       ├── ApplicationFilterPane.java
│       ├── ApplicationActionsPane.java
│       ├── AnnouncementPane.java
│       ├── MessagePane.java
│       ├── StudentSearchPane.java
│       └── ExportUtil.java
└── util/                       # Utilities
    └── CsvHelper.java          # CSV file operations

sql/                            # Database schema
└── schema.sql                  # MySQL initialization script

lib/                            # External libraries
└── mysql-connector-j.jar       # MySQL JDBC driver (auto-downloaded)
```

## Architecture & Design Patterns

### OOP Principles Applied

| Principle | Implementation |
|-----------|---------------|
| **Encapsulation** | Private fields with getters/setters in all model classes |
| **Abstraction** | Repository interfaces hide implementation details |
| **Inheritance** | `Student extends User` with meaningful specialization |
| **Polymorphism** | Repository interfaces with CSV implementations |

### SOLID Principles Applied

| Principle | Implementation |
|-----------|---------------|
| **SRP** | Repositories handle data, Service handles logic, UI handles display |
| **OCP** | MySQL implementations added without modifying service layer; CSV can be swapped back easily |
| **LSP** | Student can substitute for User where applicable |
| **ISP** | Small, focused repository interfaces |
| **DIP** | Service depends on repository interfaces, not concrete implementations |

### Design Patterns

- **Repository Pattern**: Abstract data access behind interfaces
- **Factory Pattern**: `DaoFactory` creates repository instances
- **MVC-like**: Model (entities), View (JavaFX UI), Controller (Service)

## Application Flow

1. **Registration**: Student creates account (8+ char password, valid student ID)
2. **Phase 1**: Fill sponsorship, residency, address
3. **Admin Review**: Approve, decline, or request resubmit (with reason)
4. **Phase 2**: Fill emergency contact, transaction ID (if self-sponsored)
5. **Building Assignment**: Admin assigns building

## License

Educational project for AAU coursework.
