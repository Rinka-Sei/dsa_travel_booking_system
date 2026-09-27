# Travel Booking System (Java)

> **CONFIDENTIAL & EXPERIMENTAL**
> This repository contains an experimental prototype for a Travel Booking System built with Java Swing, JDBC, and custom Data Structures & Algorithms. It is strictly confidential and restricted for private use only.

---

## Overview

The Travel Booking System is a desktop application designed to manage flights, hotels, package deals, and customer bookings. It demonstrates fundamental Data Structures & Algorithms (DSA) principles and clean architecture (MVC-inspired package structure).

Key features include:
- **User Authentication:** Login and registration system supporting Customer and Admin roles using a custom Hash Table with separate chaining.
- **Flight Management & Search:** Flight listings ordered by departure time using a Binary Search Tree (BST) for fast in-order retrieval, alongside destination search.
- **Hotel Management & Search:** Hotel listings and location/name search functionality.
- **Package Deals:** Dynamic package creation combining flights and hotels, sorted using QuickSort by price (low to high) or hotel rating (high to low).
- **Bookings, Itinerary & Invoice Generation:** Customers can book packages, view detailed itineraries, and generate printable invoice summaries.

---

## Repository Package Structure

```text
com.mycompany.dsa_travel_booking_system
├── db
│   └── DBConnection.java        # JDBC database connection management
├── ds
│   ├── FlightBST.java           # Binary Search Tree for flight departure sorting
│   ├── UserHashTable.java       # Custom Hash Table with separate chaining for user lookups
│   └── QuickSort.java           # QuickSort algorithm implementation for package deals
├── gui
│   └── MainFrame.java           # Swing UI container and event handlers
└── model
    ├── Booking.java             # Booking entity model
    ├── Flight.java              # Flight entity model
    ├── Hotel.java               # Hotel entity model
    ├── PackageDeal.java         # Package Deal entity model
    └── User.java                # User entity model
```

---

## Requirements

- **Java Development Kit (JDK):** Version 21 or higher
- **Apache Maven:** Version 3.8+
- **MySQL Database Server:** (e.g. XAMPP / MySQL Community Server)

---

## Database Setup

1. Start your MySQL database server.
2. Create a database named `travel_db`.
3. Configure the database connection settings in `src/main/java/com/mycompany/dsa_travel_booking_system/db/DBConnection.java` if necessary (default: `localhost:3306`, user: `root`, empty password).

---

## Build & Run

### Compiling the Application
```bash
mvn compile
```

### Running the Application
```bash
mvn exec:java
```

### Generating JavaDocs
```bash
mvn javadoc:javadoc
```

---

## Running Unit Tests

The project includes JUnit 5 unit tests for all custom data structures (`UserHashTable`, `FlightBST`, and `QuickSort`).

To run all unit tests:
```bash
mvn test
```

---

## License

This software is licensed under a **Proprietary & Confidential License**. See the [LICENSE](LICENSE) file for full details.
