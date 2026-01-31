# Dormitory Management System

A JavaFX desktop application for managing dormitory applications and assignments with CSV file-based data persistence.

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
| **IDE** | IntelliJ IDEA (recommended) | Eclipse/NetBeans also work |

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

### Option 2: Command Line

```bash
# Navigate to project root
cd Dormitory-Management-V5

# Compile (adjust JavaFX path as needed)
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls \
      -d out src/main/java/dorm/**/*.java

# Run (must run from project root so data/ folder is accessible)
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls \
     -cp out dorm.App
```

### Option 3: Eclipse

1. Import as Existing Project
2. Add JavaFX library to Build Path
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

### "Data not saving / File not found"
- Ensure working directory is set to project root
- The `data/` folder must exist in the current working directory
- Application will auto-create `data/` folder if missing

### "Class not found" errors
- Check that all `.java` files are compiled
- Verify package structure matches folder structure

---

## Important: Working Directory

**The application must run from the project root directory.**

The CSV files are stored in `data/` relative to the working directory:
```
project-root/          ← Run from here
├── data/              ← Auto-created, stores CSV files
├── src/
│   └── main/java/dorm/
└── README.md
```

If you get "file not found" errors, check your IDE's Run Configuration to ensure the working directory is set correctly.

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
│   ├── Csv*Repository.java     # CSV implementations
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
│   ├── AdminDashboardDb.java   # Admin dashboard (~45 LOC - uses BaseDashboard)
│   ├── OwnerDashboardDb.java   # Owner dashboard (~69 LOC - adds StaffTab)
│   └── components/             # Reusable UI components (SRP)
│       ├── BaseDashboard.java      # Shared dashboard base (composition)
│       ├── ApplicationsTab.java    # Applications management tab
│       ├── AnnouncementsTab.java   # Announcements tab wrapper
│       ├── MessagesTab.java        # Messages tab wrapper
│       ├── SearchTab.java          # Student search tab wrapper
│       ├── StaffTab.java           # Staff management (Owner only)
│       ├── ApplicationTableBuilder.java
│       ├── ApplicationFilterPane.java
│       ├── ApplicationActionsPane.java
│       ├── AnnouncementPane.java
│       ├── MessagePane.java
│       ├── StudentSearchPane.java
│       ├── ExportUtil.java
│       └── AlertHelper.java        # Consistent error/success alerts
└── util/                       # Utilities
    └── CsvHelper.java          # CSV file operations

data/                           # CSV data files (auto-created)
├── users.csv
├── students.csv
├── applications.csv
├── announcements.csv
└── messages.csv
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
| **SRP** | Each component has single responsibility: Tabs, Panes, Builders, Helpers |
| **OCP** | BaseDashboard can be extended without modification (Owner adds StaffTab) |
| **LSP** | Student can substitute for User where applicable |
| **ISP** | Small, focused repository interfaces |
| **DIP** | Service depends on repository interfaces, not concrete implementations |

### Design Patterns

- **Repository Pattern**: Abstract data access behind interfaces
- **Factory Pattern**: `DaoFactory` creates repository instances  
- **Composition**: BaseDashboard used by Admin/Owner via composition (not inheritance)
- **Builder Pattern**: `ApplicationTableBuilder` builds complex table configurations
- **Observer Pattern**: Callbacks for refresh/alert notifications between components
- **MVC-like**: Model (entities), View (JavaFX UI), Controller (Service)

### Code Quality Metrics

| Component | V5 LOC | V7 LOC | Reduction |
|-----------|--------|--------|-----------|
| AdminDashboardDb | ~977 | ~45 | 95% |
| OwnerDashboardDb | ~1034 | ~69 | 93% |
| **Total Dashboards** | **~2011** | **~114** | **94%** |

*Logic moved to reusable components in `ui/components/`*

## Application Flow

1. **Registration**: Student creates account (8+ char password, valid student ID)
2. **Phase 1**: Fill sponsorship, residency, address
3. **Admin Review**: Approve, decline, or request resubmit (with reason)
4. **Phase 2**: Fill emergency contact, transaction ID (if self-sponsored)
5. **Building Assignment**: Admin assigns building

## License

Educational project for AAU coursework.
