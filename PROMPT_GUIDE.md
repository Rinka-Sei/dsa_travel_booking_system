# LLM Adaptation & Generation Guide (`PROMPT_GUIDE.md`)

This guide explains how to use `master_prompt.md` with **Google Gemini, ChatGPT, Claude, or any LLM** to automatically generate a complete, 100% compliant, professor-ready Java desktop project for any of the student project topics.

---

## 🎯 How It Works

`master_prompt.md` contains the complete architectural rules, coding constraints, and domain entity catalogs for all projects (Items 1–8 and Item 10). Because the architecture is already standardized in the blueprint, you only need to tell the LLM **which item number you want to generate**.

---

## 📋 The 1-Step Prompt Template

Copy and paste the prompt below directly into your LLM chat window. Simply change **`[INSERT ITEM NUMBER HERE]`** to the desired number (e.g., `Item 1`, `Item 4`, or `Item 10`):

```markdown
You are an expert Java Desktop & DSA Software Engineer.
I am providing you with the technical framework blueprint below ("master_prompt.md").

YOUR TASK:
Using the rules, 5-tier architecture, and standards specified in this blueprint, generate the complete codebase for:
👉 [INSERT ITEM NUMBER HERE, e.g., Item 4: Hospital Management System]

STRICT REQUIREMENTS (Any violation results in a 0%):
1. ZERO external libraries — Only standard JDK 21+ and MySQL Connector/J ('com.mysql:mysql-connector-j:8.3.0').
2. Pure absolute Swing layout — Every frame and panel MUST use 'setLayout(null)' and explicit 'setBounds(x, y, width, height)' with generous sizing (e.g. 960x720) so buttons are never squished. Absolutely no NetBeans drag-and-drop form builders.
3. 5-Tier Package Architecture:
   - model/ (POJOs)
   - ds/ (Hand-crafted DSA: BST, Hash Table, Priority Queue/QuickSort with O(1)/O(log n) methods)
   - service/ (Business logic & database coordination)
   - database/ (DBConnection.java with static getConnection)
   - gui/ (MainFrame, LoginFrame, and modular view panels implementing ActionListener)
4. Full CRUD with "Update": Primary entity panels MUST include an "Update Selected" button that auto-fills fields when a row in the JTable is clicked.
5. Self-Refreshing Database Setup:
   - Provide 'database/01_admin.sql' that drops existing tables safely (SET FOREIGN_KEY_CHECKS = 0; DROP TABLE IF EXISTS ...; SET FOREIGN_KEY_CHECKS = 1;) and inserts only the default admin ('admin' / 'admin123').
   - Provide 'database/02_customers.sql' with 10 sample test users/records and transactions.
6. JUnit 5 unit tests under 'src/test/java/ds/' covering the custom data structures.
7. Decoupled DSA: Reflective invocation or clean service wiring with graceful fallback.

Please provide all Java source files, SQL scripts, pom.xml, and a README.md ready to compile and run.

---
[ATTACH OR PASTE master_prompt.md CONTENT BELOW THIS LINE]
```

---

## 💡 Quick Copy Examples by Item

If you do not want to paste the prompt template manually, here are quick copy-paste lines for each item:

### To generate Item 1 (Inventory Management System):
> *"Using the master_prompt.md framework, generate **Item 1: Inventory Management System** with Product, Supplier, StockOrder entities, InventoryLinkedList, ProductBST, SupplierHashTable, full Add/Update/Delete GUI panels, self-refreshing SQL scripts, and JUnit 5 unit tests."*

### To generate Item 2 (Online Food Delivery System):
> *"Using the master_prompt.md framework, generate **Item 2: Online Food Delivery System** with Restaurant, Customer, Order entities, RestaurantHashTable, OrderPriorityQueue (urgent orders first), DeliveryRouteGraph, GUI panels, SQL scripts, and unit tests."*

### To generate Item 3 (School Management System):
> *"Using the master_prompt.md framework, generate **Item 3: School Management System** with Student, Course, Grade entities, StudentArrayList, CourseBST, GradeQuickSort, GPA transcript generator, full CRUD GUI, and unit tests."*

### To generate Item 4 (Hospital Management System):
> *"Using the master_prompt.md framework, generate **Item 4: Hospital Management System** with Patient, Doctor, Appointment entities, PatientBST, AppointmentPriorityQueue (triage emergency queue), DoctorHashTable, executive report, GUI, and unit tests."*

### To generate Item 5 (E-commerce Platform):
> *"Using the master_prompt.md framework, generate **Item 5: E-commerce Platform** with Product, User, Order entities, UserSessionHashTable, ProductQuickSort (toggleable price/rating), ProductBST, checkout receipt generator, and unit tests."*

### To generate Item 6 (Event Management System):
> *"Using the master_prompt.md framework, generate **Item 6: Event Management System** with Event, Attendee, Registration entities, AttendeeLinkedList, EventBST (chronological date sort), WaitlistPriorityQueue (VIP priority), check-in GUI, and unit tests."*

### To generate Item 7 (Fitness Tracker Application):
> *"Using the master_prompt.md framework, generate **Item 7: Fitness Tracker Application** with Workout, Meal, FitnessUser entities, WorkoutDynamicArray, WorkoutBST, NutritionQuickSort, net calorie deficit report, GUI, and unit tests."*

### To generate Item 8 (Online Voting System):
> *"Using the master_prompt.md framework, generate **Item 8: Online Voting System** with Candidate, Voter, VoteAudit entities, VoterHashTable (O(1) double-vote prevention), CandidateQuickSort (live leaderboard), DistrictGraph, certified results report, GUI, and unit tests."*

### To generate Item 10 (Real Estate Management System):
> *"Using the master_prompt.md framework, generate **Item 10: Real Estate Management System** with Property, Client, PropertyTransaction entities, PropertyBST (price sort), ClientHashTable, PropertyQuickSort, investment bundle pairing, deed generator, GUI, and unit tests."*

---

## 🛡️ Pre-Submission Verification (Checklist)

Whenever an LLM completes code generation for any item, test it locally:

```bash
# 1. Compile with JDK 21+
mvn compile

# 2. Run all JUnit 5 tests (Must be 100% passing)
mvn test

# 3. Launch application
mvn exec:java
```

Verify that:
1. `LoginFrame` loads cleanly with `admin` / `admin123`.
2. Selecting a row in the table populates input fields and clicking `"Update Selected"` saves changes to the database.
3. Clicking the sorting button (BST or QuickSort) orders rows accurately.
4. The window dimensions are comfortable (`960x720`) with no cropped labels.
