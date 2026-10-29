# Travel Booking System (Java) — Group 9 Framework & Blueprint

An educational, open-source Java Swing CRUD application system designed as a reference framework and blueprint for Java desktop applications implementing custom Data Structures & Algorithms (DSA), JDBC database connectivity, and clean MVC-inspired architecture.

---

## 📐 Modular Package Structure

The project is structured cleanly into decoupled packages to enforce separation of concerns:

```text
com.mycompany.dsa_travel_booking_system (src/main/java)
├── database
│   └── DBConnection.java        # JDBC database connection provider
├── ds
│   ├── FlightBST.java           # Binary Search Tree for flight departure time sorting
│   ├── QuickSort.java           # Custom QuickSort algorithm for package pricing/ratings
│   └── UserHashTable.java       # Custom Hash Table with separate chaining for user caching
├── gui
│   ├── BookingPanel.java        # Bookings, itinerary summaries, and invoice view
│   ├── FlightPanel.java         # Flight CRUD management, search, and BST sorting
│   ├── HotelPanel.java          # Hotel CRUD management and location search
│   ├── LoginFrame.java          # Role-based login and registration window
│   ├── MainFrame.java           # Main dashboard container frame and tabbed view
│   └── PackagePanel.java        # Dynamic package deal combinations and QuickSort views
├── model
│   ├── Booking.java             # Plain Old Java Object (POJO) for Booking entity
│   ├── Flight.java              # POJO for Flight entity
│   ├── Hotel.java               # POJO for Hotel entity
│   ├── PackageDeal.java         # POJO for combined Flight + Hotel Package Deal
│   └── User.java                # POJO for User entity
└── Main.java                    # Standalone application entry point
```

---

## 📜 Strict Professor Rule Compliance

This repository strictly adheres to academic constraints for Java Swing and Data Structure projects:

1. **Absolute Layout Positioning (`setLayout(null)`):**
   - Every frame (`LoginFrame`, `MainFrame`) and panel (`FlightPanel`, `HotelPanel`, `PackagePanel`, `BookingPanel`) uses explicit coordinate layout (`setLayout(null)`) and manual component placement via `setBounds(x, y, width, height)`.
   - **Zero Visual GUI Builders:** No NetBeans Form Builder, Matisse, or visual drag-and-drop tools are permitted or used.

2. **Zero External Libraries (JDBC Connector Only):**
   - The application relies solely on standard Java Development Kit (JDK 21) APIs (`javax.swing.*`, `java.awt.*`, `java.sql.*`).
   - The **only** external JAR dependency allowed and imported is `mysql-connector-j` for JDBC database communication.

3. **GUI Live-Seeding Rule:**
   - The initial database database state contains **no pre-seeded project data** except for the single default administrator user (`admin` / `admin123`).
   - All domain entries (flights, hotels, package deals, and customer bookings) are created live through the GUI during presentations and runtime testing.

---

## 🔌 Decoupled / Plug-and-Play DSA via Java Reflection

To ensure total decoupling between user interface components and custom data structure implementations:
- GUI components (`LoginFrame`, `FlightPanel`, `PackagePanel`) interact with custom data structures (`UserHashTable`, `FlightBST`, `QuickSort`) via **Java Reflection** (`Class.forName(...)`, `getMethod(...)`, `invoke(...)`).
- **Graceful Fallback:** If a custom data structure class is removed or modified, the application will not fail to compile or crash at runtime; it gracefully falls back to default SQL queries and standard database sorting.

---

## 🚀 Requirements & Database Setup

### Prerequisites
- **Java Development Kit (JDK):** Version 21 or higher
- **Apache Maven:** Version 3.8+
- **MySQL Database Server:** (e.g., MySQL Community Server or XAMPP)

### Database Setup
1. Start your local MySQL server.
2. Execute the following SQL statements to create the database schema and initialize the required pre-seeded admin user:

```sql
CREATE DATABASE IF NOT EXISTS travel_booking_db;
USE travel_booking_db;

-- User authentication table
CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- Pre-seeded default admin account (REQUIRED)
INSERT IGNORE INTO users (username, password, role)
VALUES ('admin', 'admin123', 'ADMIN');

-- Flight listings table
CREATE TABLE IF NOT EXISTS flights (
    flight_id VARCHAR(20) PRIMARY KEY,
    departure VARCHAR(100) NOT NULL,
    arrival VARCHAR(100) NOT NULL,
    departure_time INT NOT NULL,
    price DOUBLE NOT NULL
);

-- Hotel listings table
CREATE TABLE IF NOT EXISTS hotels (
    hotel_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL,
    rating DOUBLE NOT NULL,
    price_per_night DOUBLE NOT NULL
);

-- Bookings table
CREATE TABLE IF NOT EXISTS bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    flight_id VARCHAR(20),
    hotel_id VARCHAR(20),
    total_price DOUBLE NOT NULL,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);
```

3. Update database credentials in `src/main/java/database/DBConnection.java` if your local MySQL configuration differs from defaults (`localhost:3306`, user: `root`, empty password).

---

## 🛠️ Build, Run & Test

### Compiling the Project
```bash
mvn compile
```

### Running the Application
Launch the standalone entry point (`Main.java` -> `LoginFrame`):
```bash
mvn exec:java
```

### Running Unit Tests
Execute JUnit 5 unit tests for core data structures (`UserHashTable`, `FlightBST`, `QuickSort`) and domain models (`PackageDeal`):
```bash
mvn test
```

### Generating Production JavaDocs
Generate complete API documentation under `target/site/apidocs/`:
```bash
mvn javadoc:javadoc
```

---

## 📄 License

This repository is licensed under the open-source [MIT License](LICENSE).
