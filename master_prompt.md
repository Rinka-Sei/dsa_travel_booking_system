# Master Framework & Domain Adaptation Blueprint (`master_prompt.md`)

This document serves as the absolute technical reference, architectural blueprint, and automated adaptation guide for the Java Swing + Custom DSA + JDBC reference architecture. It defines the core non-negotiable project rules, decoupling standards, 5-tier layer architecture, and detailed mapping specifications for transforming this repository baseline into **any of the 9 domain-specific student projects** (Items 1 through 8, and Item 10).

---

## 1. Absolute Project Rules & Constraints (Non-Negotiable)

Any system built upon or adapted from this blueprint must strictly follow these core rules. Any violation results in an automatic **0%**:

| Rule | Requirement | Rationale / Enforcement |
| :--- | :--- | :--- |
| **Zero External Libraries** | Only standard JDK standard APIs (Java 21+) and the MySQL JDBC connector (`mysql-connector-j`) are allowed. | Importing third-party UI libraries (e.g., FlatLaf, MigLayout) or external data collection utilities (e.g., Guava, Apache Commons) is strictly prohibited. |
| **Absolute GUI Layout** | GUI positioning must exclusively use `setLayout(null)` and explicit `setBounds(x, y, width, height)`. | No visual drag-and-drop form builders (NetBeans GUI Builder, IntelliJ GUI Designer, WindowBuilder) may be used. Windows must use generous sizing (e.g., `960x720`) to prevent squished controls. |
| **5-Tier Code Pattern** | Strict separation into `model` (POJOs), `ds` (Data Structures & Algorithms), `service` (Business Logic & Orchestration), `database` (JDBC Provider), and `gui` (Swing Views). | All UI frames and panels must explicitly `implements ActionListener` and override `actionPerformed(ActionEvent e)`. |
| **Database Seeding Rule** | Database setup scripts must reside in a root `database/` directory. `database/01_admin.sql` pre-seeds strictly the admin user (`admin` / `admin123`) and drops prior tables cleanly. `database/02_customers.sql` seeds sample test records. | Only the admin account can be pre-existing for presentations; all operational domain entities must be capable of live addition, update, and deletion via GUI. |
| **Full End-to-End CRUD** | Complete Create, Read, Update, and Delete operations for primary entities. | Adding an **"Update Selected"** feature with table row selection auto-fill is required for full CRUD credit. |

---

## 2. Refined Implementation Patterns & Architectural Standards

### A. Strict Input Formatting & Regex Validation
Form inputs must enforce domain formatting before executing database persistence:
- **Military Time or Formatted Codes:** Where schedules or identifiers are required, validate with strict regex (e.g., `^([01]\d|2[0-3]):([0-5]\d)$` for 24-hour military time, or `^[A-Z]{2,4}\d{3,4}$` for inventory/flight/course codes).
- Display descriptive `JOptionPane.showMessageDialog` error alerts on format failure.

### B. Smart Attribute Normalization & Composite Pairing
When composite entities are dynamically combined (e.g., Packages pairing Flights & Hotels, or Orders combining Restaurants & Customers, or Bundles combining Properties & Clients):
- Strip parenthetical codes, whitespace, and punctuation via normalization (`replaceAll("\\s*\\(.*?\\)", "")`).
- Implement bidirectional substring matching to match entities without requiring rigid string equality.

### C. Stateful UI Sorting Buttons with Directional Indicators
Table sorting buttons must toggle between Ascending and Descending order:
- Clicking a sort button toggles its direction (`"Sort (Ascending ↑)"` vs `"Sort (Descending ↓)"`).
- **Exclusive Active Indicator:** Activating one sort button automatically resets the label of other sort buttons.

### D. Dynamic Cross-Panel Event Notification
When CRUD modifications (Add, Update, Delete) occur in primary entity panels:
- The panel notifies the parent container (`parentFrame.refreshDependentPanel()`).
- The dashboard orchestrator cascades the update to live views, re-pairing composite entities and updating tables without requiring application restarts.

### E. Decoupled DSA via Java Reflection
GUI components interact with hand-crafted data structures (`ds.*`) via **Java Reflection** (`Class.forName(...)`, `getMethod(...)`, `invoke(...)`), allowing graceful SQL fallback if data structure classes are evaluated or swapped independently.

---

## 3. Project Adaptation Catalogs (Items 1–8 & 10)

Future developers and LLMs can directly adapt this baseline into any project item below by adopting the designated entities, custom data structures, and service methods.

```
===================================================================================
PROJECT ITEM INDEX (Refer to specific item number for generation):
  - Item 1 : Inventory Management System
  - Item 2 : Online Food Delivery System
  - Item 3 : School Management System
  - Item 4 : Hospital Management System
  - Item 5 : E-commerce Platform
  - Item 6 : Event Management System
  - Item 7 : Fitness Tracker Application
  - Item 8 : Online Voting System
  - Item 10: Real Estate Management System
===================================================================================
```

---

### Item 1: Inventory Management System
* **Domain Overview:** Tracks warehouse stock levels, products, suppliers, and purchase orders.
* **Entities (`model/`):**
  - `Product`: `id` (String), `name` (String), `category` (String), `quantity` (int), `supplierId` (String), `price` (double).
  - `Supplier`: `id` (String), `name` (String), `contactInfo` (String), `address` (String).
  - `StockOrder`: `orderId` (int), `productId` (String), `quantity` (int), `totalCost` (double), `orderDate` (String).
* **Data Structures (`ds/`):**
  - `InventoryLinkedList`: Singly/doubly linked list for dynamic inventory memory tracking.
  - `ProductBST`: Binary Search Tree ordering products by price or stock quantity for O(log n) search.
  - `SupplierHashTable`: Custom separate-chaining hash map for instant supplier lookups by ID/name.
* **Services (`service/`):**
  - `InventoryService`: Add/Update/Delete products, low-stock threshold alerts, and re-order triggers.
  - `SupplierRegistryService`: Supplier CRUD and hash map caching.
  - `StockReportService`: Generates low-stock reports and stock valuation summaries.
* **GUI Panels (`gui/`):**
  - `ProductPanel`: Table of products, Add/Update/Remove buttons, "Reset & Sort (BST)" button, category filter.
  - `SupplierPanel`: Supplier CRUD and contact directory.
  - `OrderReportPanel`: Stock re-order transactions and low-stock report viewer.

---

### Item 2: Online Food Delivery System
* **Domain Overview:** Coordinates restaurant profiles, customer food orders, delivery queues, and route dispatching.
* **Entities (`model/`):**
  - `Restaurant`: `id` (String), `name` (String), `cuisine` (String), `location` (String), `rating` (double).
  - `Customer`: `id` (String), `name` (String), `address` (String), `phone` (String).
  - `Order`: `orderId` (int), `customerId` (String), `restaurantId` (String), `totalAmount` (double), `status` (String), `deliveryTimeMinutes` (int).
* **Data Structures (`ds/`):**
  - `RestaurantHashTable`: Hash table indexing restaurants by location/cuisine for rapid search.
  - `OrderPriorityQueue`: Min-heap priority queue prioritizing orders by shortest delivery time and urgency.
  - `DeliveryRouteGraph`: Adjacency-list graph calculating minimum distance routes between delivery nodes.
* **Services (`service/`):**
  - `RestaurantCatalogService`: Restaurant CRUD and rating updates.
  - `OrderDispatchService`: Order placement, PriorityQueue management, and status updates (Pending -> Dispatched -> Delivered).
  - `DeliveryAnalyticsService`: Order volume, average delivery time, and customer history reports.
* **GUI Panels (`gui/`):**
  - `RestaurantPanel`: Restaurant management and cuisine search.
  - `OrderPanel`: Place orders, dispatch next urgent order from `OrderPriorityQueue`, track order statuses.
  - `CustomerPanel`: Customer directory and delivery history.

---

### Item 3: School Management System
* **Domain Overview:** Manages student enrollments, course offerings, grade assignments, and academic transcripts.
* **Entities (`model/`):**
  - `Student`: `id` (String), `name` (String), `email` (String), `yearLevel` (int).
  - `Course`: `courseId` (String), `courseName` (String), `credits` (int), `department` (String).
  - `Grade`: `gradeId` (int), `studentId` (String), `courseId` (String), `score` (double), `letterGrade` (String).
* **Data Structures (`ds/`):**
  - `StudentArrayList`: Custom resizable dynamic array for storing student records.
  - `CourseBST`: Binary Search Tree ordering courses by Course Code for binary search.
  - `GradeQuickSort`: Toggleable QuickSort ordering students by GPA / grade score (Ascending / Descending).
* **Services (`service/`):**
  - `StudentRegistryService`: Student CRUD and dynamic array resizing.
  - `CurriculumService`: Course catalog management and enrollment tracking.
  - `TranscriptService`: Calculates GPA, generates official transcripts, and performance distribution reports.
* **GUI Panels (`gui/`):**
  - `StudentPanel`: Student CRUD, name search, and enrolled courses viewer.
  - `CoursePanel`: Course management with BST code ordering.
  - `GradePanel`: Score entry, GPA transcript generator, and student rank sorting.

---

### Item 4: Hospital Management System
* **Domain Overview:** Manages hospital patients, doctor assignments, medical triage, and emergency appointments.
* **Entities (`model/`):**
  - `Patient`: `id` (String), `name` (String), `age` (int), `medicalHistory` (String), `triageLevel` (int).
  - `Doctor`: `id` (String), `name` (String), `specialty` (String), `department` (String).
  - `Appointment`: `appointmentId` (int), `patientId` (String), `doctorId` (String), `date` (String), `urgency` (int), `status` (String).
* **Data Structures (`ds/`):**
  - `PatientBST`: Binary Search Tree ordering patients by ID or admission date.
  - `AppointmentPriorityQueue`: Max-heap ordering appointments by medical triage urgency level (Emergency first).
  - `DoctorHashTable`: Separate-chaining hash table for instant doctor specialty lookups.
* **Services (`service/`):**
  - `PatientAdmissionService`: Patient records CRUD and triage assessment.
  - `AppointmentService`: Scheduling, priority queue triage dispatch, and doctor appointment calendars.
  - `MedicalReportService`: Generates patient treatment summaries and department admission analytics.
* **GUI Panels (`gui/`):**
  - `PatientPanel`: Patient registration, medical history updates, and BST search.
  - `DoctorPanel`: Doctor profiles and specialty directories.
  - `AppointmentPanel`: Appointment triage queue, "Admit Next Emergency Patient", and printable clinical summary.

---

### Item 5: E-commerce Platform
* **Domain Overview:** Online store catalog, shopping sessions, payment checkout, and product sorting.
* **Entities (`model/`):**
  - `Product`: `id` (String), `name` (String), `price` (double), `category` (String), `rating` (double).
  - `User`: `id` (String), `name` (String), `email` (String), `role` (String: ADMIN/CUSTOMER).
  - `Order`: `orderId` (int), `userId` (String), `productSummary` (String), `totalAmount` (double), `date` (String).
* **Data Structures (`ds/`):**
  - `UserSessionHashTable`: Hash map caching active user sessions and authentication tokens.
  - `ProductQuickSort`: QuickSort toggleable by price (low-to-high / high-to-low) and customer ratings.
  - `ProductBST`: Binary Search Tree for rapid product title and price range lookups.
* **Services (`service/`):**
  - `ProductCatalogService`: Catalog CRUD and QuickSort invocation.
  - `UserAuthService`: Authentication, registration, and hash table session management.
  - `OrderCheckoutService`: Cart processing, payment receipt generation, and sales revenue reports.
* **GUI Panels (`gui/`):**
  - `CatalogPanel`: Product gallery table with toggleable price/rating QuickSort buttons.
  - `CartCheckoutPanel`: Active order cart, checkout payment, and printable receipts.
  - `AdminProductPanel`: Admin controls to add, update, and remove products.

---

### Item 6: Event Management System
* **Domain Overview:** Organizes public conferences/events, attendee registrations, ticketing, and VIP waiting lists.
* **Entities (`model/`):**
  - `Event`: `eventId` (String), `name` (String), `date` (String), `location` (String), `capacity` (int).
  - `Attendee`: `id` (String), `name` (String), `email` (String), `vipStatus` (boolean).
  - `Registration`: `registrationId` (int), `eventId` (String), `attendeeId` (String), `ticketPrice` (double), `checkInStatus` (boolean).
* **Data Structures (`ds/`):**
  - `AttendeeLinkedList`: Dynamic linked list storing registered attendees for each event.
  - `EventBST`: Binary Search Tree organizing events chronologically by event date.
  - `WaitlistPriorityQueue`: Priority queue managing waitlisted attendees based on registration timestamp and VIP tier.
* **Services (`service/`):**
  - `EventHostingService`: Event CRUD and date BST sorting.
  - `RegistrationService`: Attendee ticketing, waitlist prioritization, and check-in confirmation.
  - `EventAnalyticsService`: Attendance percentage, feedback ratings, and ticket sales reports.
* **GUI Panels (`gui/`):**
  - `EventPanel`: Event schedule table, Add/Update/Delete event forms, "Reset & Sort (BST)".
  - `AttendeePanel`: Registration form, check-in toggle button, and attendee directory.
  - `WaitlistPanel`: VIP waitlist queue viewer and admission promotion.

---

### Item 7: Fitness Tracker Application
* **Domain Overview:** Tracks daily physical workouts, dietary nutrition intake, fitness goals, and calorie deficit analytics.
* **Entities (`model/`):**
  - `Workout`: `workoutId` (String), `exerciseName` (String), `durationMinutes` (int), `caloriesBurned` (double), `date` (String).
  - `Meal`: `mealId` (String), `foodName` (String), `calories` (double), `proteinGrams` (double), `mealType` (String).
  - `FitnessUser`: `id` (String), `name` (String), `targetCalories` (double), `weightKg` (double).
* **Data Structures (`ds/`):**
  - `WorkoutDynamicArray`: Resizable dynamic array for logging and accessing daily workout entries.
  - `WorkoutBST`: Binary Search Tree ordering workout logs chronologically by date/time.
  - `NutritionQuickSort`: QuickSort sorting meals by calorie density or protein content (Ascending/Descending).
* **Services (`service/`):**
  - `WorkoutTrackingService`: Workout logging, calorie calculations, and BST sorting.
  - `NutritionService`: Meal logging and QuickSort nutrition filtering.
  - `FitnessProgressService`: Daily net calorie report (`Burned - Consumed`) and goal notification engine.
* **GUI Panels (`gui/`):**
  - `WorkoutPanel`: Log workout form, workout history table, "Reset & Sort (BST)".
  - `MealPanel`: Log nutrition form, QuickSort buttons for calories and protein.
  - `ProgressReportPanel`: Visual summary of net calories, goal deficit progress, and printable fitness log.

---

### Item 8: Online Voting System
* **Domain Overview:** Secure election management, voter verification, candidate balloting, and audit reporting.
* **Entities (`model/`):**
  - `Candidate`: `candidateId` (String), `name` (String), `party` (String), `position` (String), `votesReceived` (int).
  - `Voter`: `voterId` (String), `name` (String), `hasVoted` (boolean), `district` (String).
  - `VoteAudit`: `auditId` (int), `voterId` (String), `candidateId` (String), `timestamp` (String).
* **Data Structures (`ds/`):**
  - `VoterHashTable`: Custom separate-chaining hash table for O(1) voter verification to strictly prevent double voting.
  - `CandidateQuickSort`: QuickSort ranking candidates dynamically by vote tally in descending order.
  - `DistrictGraph`: Graph modeling election districts and candidate ballot allocations.
* **Services (`service/`):**
  - `VoterRegistryService`: Voter registration and hash table authentication.
  - `BallotCastingService`: Secure ballot casting, atomic vote incrementation, and audit logging.
  - `ElectionAnalyticsService`: Turnout statistics, candidate leaderboard, and certified election results report.
* **GUI Panels (`gui/`):**
  - `BallotPanel`: Voter login, candidate selection, and one-click "Cast Ballot" with instant lock.
  - `CandidatePanel`: Candidate CRUD (Admin only) with dynamic position filtering.
  - `ElectionResultsPanel`: Dynamic leaderboard sorted by `CandidateQuickSort`, turnout percentage, and printable certificate.

---

### Item 10: Real Estate Management System
* **Domain Overview:** Property inventory, client profiles, real estate transactions, and property bundle investments.
* **Entities (`model/`):**
  - `Property`: `propertyId` (String), `title` (String), `location` (String), `type` (String: Condo/Villa/House), `price` (double), `status` (String).
  - `Client`: `clientId` (String), `name` (String), `contactInfo` (String), `budget` (double).
  - `PropertyTransaction`: `transactionId` (int), `clientId` (String), `propertyId` (String), `totalAmount` (double), `date` (String).
  - `InvestmentBundle`: Composite entity combining Properties with matching Client location preferences.
* **Data Structures (`ds/`):**
  - `PropertyBST`: Binary Search Tree ordering properties by listing price for chronological/budget search.
  - `ClientHashTable`: Hash table indexing clients by ID/phone with separate-chaining collision handling.
  - `PropertyQuickSort`: Toggleable QuickSort sorting property bundles by price (Ascending/Descending) or valuation rating.
* **Services (`service/`):**
  - `PropertyCatalogService`: Property CRUD, status updates (Available/Sold), and BST price sorting.
  - `ClientRegistryService`: Client directory CRUD and hash table caching.
  - `TransactionService`: Purchase transactions, deed generation, and executive sales volume reports.
* **GUI Panels (`gui/`):**
  - `PropertyPanel`: Property listings, Add/Update/Delete forms, "Reset & Sort (BST)" button, location search.
  - `ClientPanel`: Client profiles management and budget lookup.
  - `TransactionPanel`: Investment bundle matching, purchase checkout, printable deed invoice, and executive sales report.

---

## 4. Verification & Defense Checklist

Before submitting any project generated from this framework, ensure:
1. `mvn compile` executes with **zero compiler warnings or errors** under Java 21+.
2. `mvn test` executes with **100% passing tests** (no failures, no skipped tests).
3. `database/01_admin.sql` has `DROP TABLE IF EXISTS` in reverse foreign key order and strictly pre-seeds only `admin` / `admin123`.
4. All GUI frames and panels explicitly use `setLayout(null)` with `setBounds(...)` and contain zero drag-and-drop builder artifacts.
5. Every primary entity includes an **"Update Selected"** feature with table row selection auto-fill.
