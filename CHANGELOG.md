# Project Changelog

All notable changes, milestones, refactoring phases, and documentation updates for the Travel Booking System repository are documented chronologically in this file.

---

## [2026-10-30] - Functional Enhancements, Smart Pairing & Toggleable QuickSort

### Added
- **24-Hour Military Time Validation (`HH:mm`):**
  - Updated `flights` schema to store departure times as `VARCHAR(5)` strings formatted as `"HH:mm"`.
  - Added strict regex validation (`^([01]\d|2[0-3]):([0-5]\d)$`) in `FlightPanel.java` with warning dialogs for invalid time entries.
  - Created SQL database scripts `01_admin.sql` (schema setup & admin seeding) and `02_customers.sql` (empty template enforcing live GUI seeding).
- **Smart Destination-Matching Package Deals:**
  - Implemented `PackageDeal.normalizeDestination()` and `PackageDeal.isDestinationMatch()` to pair flights and hotels strictly when destinations match.
  - Ignores airport code suffixes and parenthetical indicators (e.g., Flight arrival `"Tokyo (NRT)"` matches Hotel location `"Tokyo"`).
- **Toggleable QuickSort with Dynamic Directional Arrows:**
  - Refactored `QuickSort.java` to support both Ascending and Descending sorting modes for price and hotel rating.
  - Updated `PackagePanel.java` sort buttons (`btnSortPrice` and `btnSortRating`) with dynamic directional indicators (`"Sort Price (Ascending ↑)"` vs `"Sort Price (Descending ↓)"`).
  - Enforced exclusive active indicator state clearing on inactive sort buttons.

### Changed
- **Unit Test Suite Expansion:**
  - Updated `ds/FlightBSTTest.java` to test chronological sorting with string military time (`"08:30"`, `"12:00"`, `"14:45"`, `"22:15"`).
  - Updated `ds/QuickSortTest.java` to verify both ascending and descending sorting modes across price and rating edge cases.
  - Updated `model/PackageDealTest.java` to test destination normalization and smart matching rules.
- **Documentation Updates:**
  - Updated `README.md` and `master_prompt.md` framework guide with new patterns for military time validation, relational string pairing, and stateful UI sorting buttons.

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
