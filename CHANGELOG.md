# Project Changelog

All notable changes, milestones, refactoring phases, and documentation updates for the Travel Booking System repository are documented chronologically in this file.

---

## [2026-10-30] - Full CRUD Updates, Service Layer Architecture, Priority Queue & Executive Reports

### Added
- **Complete End-to-End Update Operations ("U" in CRUD):**
  - Added "Update Selected" buttons and table row selection listeners in `FlightPanel.java` and `HotelPanel.java`.
  - Updating a flight or hotel updates MySQL and automatically re-syncs dynamic package deals across panels.
- **Dedicated Service Layer Architecture (`service/`):**
  - Implemented `FlightCatalogService.java` for coordinating flight persistence and BST sorting.
  - Implemented `CustomerRegistryService.java` for user management and caching via `UserHashTable`.
  - Implemented `BookingManagementService.java` for hotel CRUD, booking workflows, and priority queue ordering.
- **`BookingPriorityQueue` (Priority Queue DSA) & Unit Tests:**
  - Implemented `ds.BookingPriorityQueue` (heap-backed transaction ordering) with comprehensive JUnit 5 tests (`BookingPriorityQueueTest.java`).
- **Executive System Summary Reporting:**
  - Added on-demand revenue and customer booking activity reporting accessible via a "Report" button on `BookingPanel.java`.
- **Database Self-Refreshing Drop Scripts:**
  - Updated `database/01_admin.sql` to safely drop existing tables in reverse foreign key order (`bookings -> hotels -> flights -> users`) before recreating them to cleanly prepare for `database/02_customers.sql`.

---

## [2026-10-30] - Quality Assurance, Dynamic Package Synchronization & Hash Table Enhancements

### Added
- **`UserHashTable.remove(String key)` Implementation & Unit Tests:**
  - Implemented node deletion with pointer re-linking for separate chaining in `ds.UserHashTable`.
  - Added unit test `testRemove()` in `ds.UserHashTableTest` covering single node deletion, preservation of neighboring chained nodes, and safe handling of non-existent keys.
- **Dynamic Cross-Panel Synchronization:**
  - Added `refreshPackages()` to `PackagePanel.java` and exposed `refreshPackagePanel()` in `MainFrame.java`.
  - Admin addition and removal operations in both `FlightPanel.java` and `HotelPanel.java` now automatically trigger package deal re-pairing and re-sorting without requiring application restarts.

### Changed
- **Maven Configuration (`pom.xml` & `nbactions.xml`):**
  - Updated `<exec.mainClass>` to point directly to `Main` instead of the outdated subpackage path, ensuring seamless launch via `mvn exec:java`.
- **Documentation Updates:**
  - Updated `README.md` and `master_prompt.md` to document dynamic panel event notification, hashtable deletion capabilities, and clean application launch specifications.

---

## [2026-10-30] - Paired Military Times, Root Database Scripts & Layout Refinements

### Added
- **Paired Military Departure & Arrival Times (`HH:mm`):**
  - Updated `flights` schema with paired `departure_time` (`VARCHAR(5)`) and `arrival_time` (`VARCHAR(5)`).
  - Updated `FlightPanel.java` input layout to pair Departure Time right next to Departure Location and Arrival Time right next to Arrival Location.
  - Added strict regex validation (`^([01]\d|2[0-3]):([0-5]\d)$`) for both time fields.
  - Updated `tblFlights` table headers to `["ID", "Departure", "Dep Time", "Arrival", "Arr Time", "Price"]`.
- **Root Database Seed Scripts (`database/`):**
  - Created `database/01_admin.sql` initializing database schema and pre-seeding default admin account (`admin` / `admin123`).
  - Created `database/02_customers.sql` inserting exactly 10 customer accounts and 20 sample flight-hotel bookings.
- **Smart Destination-Matching Package Deals:**
  - Implemented `PackageDeal.normalizeDestination()` and `PackageDeal.isDestinationMatch()` to pair flights and hotels strictly when destinations match, ignoring airport codes or parenthetical indicators (e.g. Flight arrival `"Tokyo (NRT)"` matches Hotel location `"Tokyo"`).
- **Toggleable QuickSort with Dynamic Directional Indicators:**
  - Refactored `QuickSort.java` to support both Ascending and Descending sorting modes.
  - Updated `PackagePanel.java` buttons (`btnSortPrice` and `btnSortRating`) with dynamic directional indicators (`"Sort Price (Ascending ↑)"` vs `"Sort Price (Descending ↓)"`), enforcing exclusive active indicator state clearing.

### Changed
- **Unit Test Suite Updates:**
  - Updated `ds/FlightBSTTest.java` to cover paired departure and arrival military times and chronological departure sorting.
  - Updated `ds/QuickSortTest.java` and `model/PackageDealTest.java` to verify toggleable sorting modes and smart destination normalization.
- **Documentation Updates:**
  - Updated `README.md` and `master_prompt.md` to reflect paired military time validation, relational string pairing, stateful UI sorting buttons, and root `database/` seed script layout.

---

## [2026-10-30] - Framework Baseline, Blueprint & Quality Assurance Phase

### Added
- **JUnit 5 Unit Test Suite:**
  - Added `ds/UserHashTableTest.java` covering `put()`, `get()`, separate chaining collision handling with low bucket capacities, non-existent key lookups, key value updating, and constructor validation.
  - Added `ds/FlightBSTTest.java` covering flight insertion, in-order traversal (`getSortedFlights()`), chronological departure time ordering, empty tree handling, and duplicate departure times.
  - Added `ds/QuickSortTest.java` covering `sortByPrice()` and `sortByRating()` across empty lists, single-element lists, unsorted multi-element lists, duplicate metrics, and null safety.
  - Added `model/PackageDealTest.java` covering package price summation (`flight.price + hotel.pricePerNight`), hotel rating aggregation, and entity reference validation.
- **Framework & Adaptation Guide (`master_prompt.md`):**
  - Published comprehensive blueprint detailing non-negotiable project rules, decoupled reflection architecture, and domain adaptation mappings (e.g., adapting Travel Booking baseline to Real Estate Management Systems).
- **Open-Source Licensing:**
  - Standardized repository license to the open-source MIT License (`LICENSE`).
- **Production-Grade JavaDoc Documentation:**
  - Added JavaDoc summaries, `@param`, `@return`, `@throws`, and algorithm time complexity annotations (e.g., O(1), O(log n), O(n log n)) across all model, database, data structure, and GUI classes.

### Changed
- **README Documentation Update:**
  - Documented modular package architecture, absolute layout positioning rules (`setLayout(null)`), JDBC connector requirements, database setup schema, and reflection decoupling details.

---

## [2026-10-29] - Core Architecture, Modular Panel Splits & Decoupled DSA Integration

### Added
- **MVC-Inspired Modular Architecture:**
  - Created entity models: `User`, `Flight`, `Hotel`, `PackageDeal`, and `Booking` under `model/`.
  - Created JDBC connection layer `database/DBConnection.java`.
- **Custom Data Structures & Algorithms:**
  - Implemented `ds.UserHashTable` using separate chaining linked lists for user lookup caching.
  - Implemented `ds.FlightBST` binary search tree for in-order flight departure time sorting.
  - Implemented `ds.QuickSort` utility for dual sorting (ascending price / descending hotel rating).
- **Modular Swing Panel Split:**
  - Refactored monolithic GUI interface into distinct modular view panels: `FlightPanel`, `HotelPanel`, `PackagePanel`, and `BookingPanel` hosted inside `MainFrame` tabbed dashboard.
- **Role-Based Access Control (RBAC):**
  - Restricted flight/hotel management controls (Add/Remove forms and actions) exclusively to `ADMIN` accounts.
  - Configured `CUSTOMER` role views for browsing, searching, booking package deals, viewing itineraries, and generating invoice summaries.
- **Decoupled DSA via Java Reflection:**
  - Integrated dynamic class loading (`Class.forName(...)`) in `LoginFrame`, `FlightPanel`, and `PackagePanel` to ensure UI components operate independently of concrete DSA implementations with graceful SQL fallback.
- **GUI Live-Seeding Compliance:**
  - Configured database setup rule retaining only the default pre-seeded `admin` account (`admin` / `admin123`) in database tables.
