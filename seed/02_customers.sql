-- =========================================================================
-- PART 2: SEED CUSTOMERS, FLIGHTS, HOTELS, AND BOOKINGS
-- =========================================================================

START TRANSACTION;

-- Temporarily bypass checks to ensure smooth multi-table seeding
SET FOREIGN_KEY_CHECKS = 0;

-- 1. Seed Customer Users
INSERT INTO travel_booking_db.users (username, password, role) VALUES
('johndoe', 'pass123', 'CUSTOMER'),
('mariasantos', 'secure2026', 'CUSTOMER'),
('alex_traveler', 'alexpass', 'CUSTOMER');

-- 2. Seed Flights
INSERT INTO travel_booking_db.flights (flight_id, departure, arrival, departure_time, price) VALUES
('FL-101', 'Manila (MNL)', 'Tokyo (NRT)', 630, 280.00),
('FL-102', 'Manila (MNL)', 'Singapore (SIN)', 815, 150.00),
('FL-103', 'Cebu (CEB)', 'Seoul (ICN)', 1100, 210.00),
('FL-104', 'Manila (MNL)', 'Bangkok (BKK)', 1430, 175.50),
('FL-105', 'Davao (DVO)', 'Manila (MNL)', 1845, 85.00),
('FL-106', 'Manila (MNL)', 'Sydney (SYD)', 2215, 520.00);

-- 3. Seed Hotels
INSERT INTO travel_booking_db.hotels (hotel_id, name, location, rating, price_per_night) VALUES
('HT-201', 'Grand Hyatt', 'Tokyo', 4.8, 250.00),
('HT-202', 'Marina Bay Sands', 'Singapore', 4.9, 450.00),
('HT-203', 'Backpackers Hostel', 'Bangkok', 3.5, 30.00),
('HT-204', 'Lotte Hotel', 'Seoul', 4.6, 180.00),
('HT-205', 'Ocean View Resort', 'Cebu', 4.2, 110.00),
('HT-206', 'City Central Hotel', 'Sydney', 4.0, 140.00);

-- 4. Seed Bookings
INSERT INTO travel_booking_db.bookings (username, flight_id, hotel_id, total_price) VALUES
('johndoe', 'FL-101', 'HT-201', 530.00),
('mariasantos', 'FL-102', 'HT-202', 600.00),
('alex_traveler', 'FL-104', 'HT-203', 205.50),
('johndoe', 'FL-103', 'HT-204', 390.00);

SET FOREIGN_KEY_CHECKS = 1;
COMMIT;