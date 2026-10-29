# Master Framework & Domain Adaptation Guide (`master_prompt.md`)

This document serves as the absolute technical reference, architectural blueprint, and adaptation guide for the Java Swing + DSA + JDBC reference framework. It details the core rules, decoupled design patterns, refined functional validation standards, and exact step-by-step instructions for adapting this repository baseline into other domain-specific CRUD systems (e.g., Real Estate Management System, Movie Rental System, Hospital System, etc.).

---

## 1. Absolute Project Rules & Constraints (Non-Negotiable)

Any system built upon or derived from this framework must strictly follow these core rules:

| Rule | Requirement | Rationale / Enforcement |
| :--- | :--- | :--- |
| **Zero External Libraries** | Only standard JDK standard APIs (Java 21+) and the MySQL JDBC connector (`mysql-connector-j`) are allowed. | Importing third-party GUI libraries (e.g., FlatLaf, MigLayout) or data collection libraries (e.g., Guava, Commons) violates project constraints and results in a 0%. |
| **Absolute GUI Layout** | GUI positioning must exclusively use `setLayout(null)` and explicit `setBounds(x, y, width, height)`. | No visual drag-and-drop form builders (e.g., NetBeans GUI Builder, IntelliJ GUI Designer, Eclipse WindowBuilder) may be used. |
| **MVC Code Pattern** | Strict separation into `model` (POJOs), `ds` (Data Structures & Algorithms), `database` (JDBC Connection), and `gui` (Swing Views). | All UI frames and panels must explicitly `implements ActionListener` and override `actionPerformed(ActionEvent e)` for event handling. |
| **Database Seeding Rule** | The database must contain **no pre-existing project data** except for the initial administrator user (`admin` / `admin123`). | All domain entities (flights, hotels, properties, movies, patients, etc.) must be inserted live via the GUI during presentation testing using scripts `01_admin.sql` and `02_customers.sql`. |

---

## 2. Refined Implementation Patterns & Validation Standards

### A. Strict Input Validation & Standardized Formatting
Form inputs must enforce strict domain formatting before saving to database records:
- **Military Time Validation (`HH:mm`):** Departure times or time slots must be formatted as 24-hour military time (`"HH:mm"`). Validate inputs using regex `^([01]\d|2[0-3]):([0-5]\d)$` and display a `JOptionPane` warning if invalid.

```java
String timeInput = txtDepTime.getText().trim();
if (!timeInput.matches("^([01]\\d|2[0-3]):([0-5]\\d)$")) {
    JOptionPane.showMessageDialog(this,
        "Invalid Time format! Please use 24-hour military time (HH:mm, e.g., 08:30 or 14:45).",
        "Validation Error", JOptionPane.WARNING_MESSAGE);
    return;
}
```

### B. Smart Attribute Normalization & Relational Pairing
When combining composite domain entities (e.g., pairing Flights with Hotels into Package Deals, or Properties with Clients into Investment Bundles):
- Strip parenthetical indicators, airport codes, and punctuation using string normalization (`replaceAll("\\s*\\(.*?\\)", "")`).
- Perform bidirectional substring and keyword matching to pair entities accurately without requiring exact string equality.

```java
public static String normalize(String str) {
    if (str == null) return "";
    String norm = str.toLowerCase().trim();
    norm = norm.replaceAll("\\s*\\(.*?\\)", ""); // Remove parenthetical codes
    norm = norm.replaceAll("[^a-z0-9\\s]", " "); // Strip punctuation
    return norm.replaceAll("\\s+", " ").trim();
}

public static boolean isMatch(String dest1, String dest2) {
    String n1 = normalize(dest1);
    String n2 = normalize(dest2);
    return !n1.isEmpty() && !n2.isEmpty() && (n1.equals(n2) || n1.contains(n2) || n2.contains(n1));
}
```

### C. Stateful UI Sorting Buttons with Directional Arrows
Table sorting controls should support toggleable Ascending and Descending order with dynamic button label indicators:
- Clicking a sort button toggles its direction (`"Sort Price (Ascending ↑)"` vs `"Sort Price (Descending ↓)"`).
- **Exclusive Active Indicator:** Activating one sort button resets the label of other sort buttons (e.g., clearing the arrow from `"Sort Rating"`).

---

## 3. Modular Architecture & Decoupled DSA Pattern

### Package Architecture
The codebase follows a decoupled 4-tier package organization:

1. **`model`**: Contains lightweight Plain Old Java Objects (POJOs) representing domain entities with private fields, getters, setters, and constructors.
2. **`ds`**: Contains hand-crafted Data Structures and Algorithms implemented from scratch (e.g., Binary Search Trees, Custom Hash Tables, QuickSort implementations).
3. **`database`**: Provides static JDBC connection management via `DBConnection.java`.
4. **`gui`**: Contains Swing containers (`MainFrame`, `LoginFrame`) and modular view panels (`FlightPanel`, `HotelPanel`, `PackagePanel`, `BookingPanel`).

### Decoupled DSA via Reflection (Plug-and-Play Setup)
To decouple the user interface from concrete data structure implementations, GUI panels call data structure methods reflectively using `Class.forName(...)`:

```java
try {
    Class<?> quickSortClass = Class.forName("ds.QuickSort");
    Method sortPriceMethod = quickSortClass.getMethod("sortByPrice", List.class, int.class, int.class, boolean.class);
    sortPriceMethod.invoke(null, packages, 0, packages.size() - 1, ascending);
} catch (Exception e) {
    // Graceful Fallback: Proceed with unsorted list or SQL ORDER BY
}
```

---

## 4. Domain Adaptation Guide (Group 9 Baseline Adaptation)

This section illustrates how future developers can transform this Travel Booking baseline system into other application domains, such as **Real Estate Management System (Item 10)** or other project themes.

### Domain Entity Mapping Matrix

| Component Layer | Travel Booking Baseline (Group 9) | Real Estate Management System | Movie Rental System | Hospital Management System |
| :--- | :--- | :--- | :--- | :--- |
| **Primary Entity 1** | `Flight` | `Property` | `Movie` | `Doctor` |
| **Primary Entity 2** | `Hotel` | `Client` | `Customer` | `Patient` |
| **Combined Entity** | `PackageDeal` | `PropertyBundle` / `InvestmentPackage` | `RentalPackage` | `Appointment` |
| **Transaction Entity** | `Booking` | `Transaction` | `RentalRecord` | `MedicalRecord` |
| **User & Role** | `User` (`ADMIN` / `CUSTOMER`) | `User` (`ADMIN` / `CLIENT`) | `User` (`ADMIN` / `MEMBER`) | `User` (`ADMIN` / `STAFF`) |

---

### Step-by-Step Blueprint: Transforming Baseline to Real Estate System

#### Step 1: Model Package (`model/`)
Replace or extend model classes:
1. **`Property.java`**:
   - Fields: `propertyId` (String), `location` (String), `type` (String e.g., "Condo", "Villa"), `price` (double), `status` (String e.g., "Available", "Sold").
2. **`Client.java`**:
   - Fields: `clientId` (String), `name` (String), `contactInfo` (String), `budget` (double).
3. **`Transaction.java`**:
   - Fields: `transactionId` (int), `username` (String), `propertyId` (String), `clientId` (String), `totalAmount` (double), `date` (String).

#### Step 2: Data Structures Package (`ds/`)
Adapt custom data structures to property metrics:
1. **`PropertyBST.java` (Binary Search Tree)**:
   - Order nodes based on `property.getPrice()` or listing date for O(log n) insertion and O(n) in-order traversal price sorting.
2. **`ClientHashTable.java` (Hash Table with Chaining)**:
   - Store and lookup client details using `clientId` or `username` as lookup keys with separate chaining.
3. **`QuickSort.java`**:
   - Adapt `sortByPrice(packages, low, high, ascending)` to sort property investment bundles by price (ascending/descending) or property valuation rating.

#### Step 3: Database Layer (`database/DBConnection.java` & Schema Scripts)
Create MySQL schema scripts `01_admin.sql` and `02_customers.sql`:
```sql
CREATE DATABASE IF NOT EXISTS real_estate_db;
USE real_estate_db;

CREATE TABLE users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- Pre-seed REQUIRED admin account
INSERT IGNORE INTO users (username, password, role) VALUES ('admin', 'admin123', 'ADMIN');

CREATE TABLE properties (
    property_id VARCHAR(20) PRIMARY KEY,
    location VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    price DOUBLE NOT NULL,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE clients (
    client_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    contact_info VARCHAR(100) NOT NULL,
    budget DOUBLE NOT NULL
);

CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    property_id VARCHAR(20),
    client_id VARCHAR(20),
    total_amount DOUBLE NOT NULL
);
```

#### Step 4: GUI Layer (`gui/`)
Maintain the frame architecture (`MainFrame`, `LoginFrame`) and adapt view panels:
1. **`PropertyPanel.java`** (replaces `FlightPanel`):
   - Admin Controls: Add/Delete Property fields (`setLayout(null)` absolute coordinates).
   - Feature: Search by location/type, load properties sorted by price using `PropertyBST` via reflection.
2. **`ClientPanel.java`** (replaces `HotelPanel`):
   - Admin Controls: Add/Remove Client profiles.
   - Client Lookup cached in `ClientHashTable`.
3. **`TransactionPanel.java`** (replaces `BookingPanel`):
   - Form for processing property purchases/rentals.
   - Action buttons: "Generate Deed/Receipt" (Invoice summary), "View Property Details" (Itinerary summary), and "Cancel Transaction".

---

## 5. Verification & Submission Checklist

Before submitting any derived project, run the following verification steps:
1. `mvn compile` — Ensures 100% clean compilation with Java 21+.
2. `mvn test` — Executes JUnit 5 unit tests for all custom data structures and models.
3. `mvn javadoc:javadoc` — Confirms all classes, methods, and time complexities are documented without JavaDoc errors.
4. **Database Verification:** Confirm database schema contains no project data except the single initial `admin` account (`admin` / `admin123`).
