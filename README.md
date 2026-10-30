# Travel Booking System (Java) — Group 9 Framework & Blueprint

An educational, open-source Java Swing CRUD application system designed as a reference framework and blueprint for Java desktop applications implementing custom Data Structures & Algorithms (DSA), JDBC database connectivity, and clean MVC-inspired architecture.

---

## 🚀 Key Functional Features & Updates

- **Paired Military Time Formatting & Validation (`HH:mm`):** Flight departure and arrival times are stored in strict 24-hour military time format (`"HH:mm"`, e.g., `"08:30"`, `"14:45"`, `"22:15"`). Form inputs pair Departure Time next to Departure Location and Arrival Time next to Arrival Location, validated via strict regex (`^([01]\d|2[0-3]):([0-5]\d)$`) with visual error popups.
- **Smart Destination-Matching Package Deals:** Dynamic flight and hotel pairing evaluates destinations to ensure only matching locations are combined (e.g., Flight arrival `"Tokyo (NRT)"` matches Hotel location `"Tokyo"` via string normalization and bidirectional substring matching). Dynamic re-pairing is triggered automatically whenever flights or hotels are added or deleted by an administrator.
- **Interactive Toggleable QuickSort Buttons:** Sorting controls (`btnSortPrice` and `btnSortRating`) toggle between Ascending and Descending order. Dynamic directional indicators (`"Sort Price (Ascending ↑)"` vs `"Sort Price (Descending ↓)"`) update active buttons while resetting inactive indicators.
- **Custom User Hash Table with Full CRUD:** Custom hash table supporting insertion, retrieval, and collision-safe separate chaining node deletion (`remove()`).
- **Priority Queue & Executive Reporting:** Custom `BookingPriorityQueue` supporting transaction prioritization by gross amount, complemented by on-demand executive revenue and booking activity reports.
- **Complete End-to-End CRUD:** Both Flights and Hotels support full Add, Update, Remove, and Search capabilities directly via GUI with automatic synchronization across panels.
- **Dedicated Service Layer:** Integrated `service/` package (`FlightCatalogService`, `CustomerRegistryService`, `BookingManagementService`) isolating database orchestration and business logic from the GUI.
- **Role-Based Access Control:** `ADMIN` users can add, update, and remove flights and hotels, while `CUSTOMER` users browse, search, book packages, view itineraries, and generate invoices.

---

## 📐 Modular Package Structure

```text
com.mycompany.dsa_travel_booking_system (src/main/java)
├── database
│   └── DBConnection.java            # JDBC database connection provider
├── ds
│   ├── BookingPriorityQueue.java    # Priority Queue for booking transaction ordering
│   ├── FlightBST.java               # Binary Search Tree for flight military time sorting
│   ├── QuickSort.java               # Custom QuickSort algorithm (ascending/descending toggle)
│   └── UserHashTable.java           # Custom Hash Table with separate chaining for user caching
├── gui
│   ├── BookingPanel.java            # Bookings, itinerary summaries, executive report, and invoice view
│   ├── FlightPanel.java             # Flight CRUD (Add/Update/Delete), regex time validation, and BST
│   ├── HotelPanel.java              # Hotel CRUD (Add/Update/Delete) and location search
│   ├── LoginFrame.java              # Role-based login and registration window
│   ├── MainFrame.java               # Main dashboard container frame and tabbed view
│   └── PackagePanel.java            # Dynamic matched package deals and QuickSort views
├── model
│   ├── Booking.java                 # POJO for Booking entity
│   ├── Flight.java                  # POJO for Flight entity (departureTime and arrivalTime "HH:mm")
│   ├── Hotel.java                   # POJO for Hotel entity
│   ├── PackageDeal.java             # POJO for combined Package Deal + Destination Matching Logic
│   └── User.java                    # POJO for User entity
├── service
│   ├── BookingManagementService.java# Business logic for Hotels, Bookings, Reports & Priority Queue
│   ├── CustomerRegistryService.java # Business logic for Users and CustomerHashTable caching
│   └── FlightCatalogService.java    # Business logic for Flight CRUD and FlightBST sorting
└── Main.java                        # Standalone application entry point
```

---

## 📜 Strict Professor Rule Compliance

1. **Absolute Layout Positioning (`setLayout(null)`):** Every frame and panel uses explicit coordinate layout (`setLayout(null)`) and manual component placement via `setBounds(x, y, width, height)`. No visual NetBeans GUI builders or drag-and-drop tools are used.
2. **Zero External Libraries (JDBC Connector Only):** Relies solely on standard JDK 21+ APIs and `mysql-connector-j` as the sole external dependency.
3. **GUI Live-Seeding Rule:** Database setup scripts are located in the root `database/` directory (`database/01_admin.sql` pre-seeding default admin `admin` / `admin123`, and `database/02_customers.sql` containing test data with 10 customers and 20 sample bookings).

---

## 🔌 Decoupled / Plug-and-Play DSA via Reflection

GUI components interact with custom data structures (`UserHashTable`, `FlightBST`, `QuickSort`) via **Java Reflection** (`Class.forName(...)`, `getMethod(...)`, `invoke(...)`), allowing graceful SQL fallback if data structure classes are removed or modified.

---

## 🛠️ Database Setup & Execution

### Database Setup Scripts
Execute `database/01_admin.sql` to initialize schema and seed the admin user, and `database/02_customers.sql` for test customer accounts and bookings:

```bash
mysql -u root < database/01_admin.sql
mysql -u root < database/02_customers.sql
```

### Build & Run Commands
```bash
# Compile project
mvn compile

# Launch application
mvn exec:java

# Run JUnit 5 unit test suite
mvn test

# Generate JavaDocs
mvn javadoc:javadoc
```

---

## 📄 License

This repository is licensed under the open-source [MIT License](LICENSE).
