-- =========================================================================
-- PART 1: DATABASE, TABLES, AND ADMIN SETUP
-- =========================================================================

CREATE DATABASE IF NOT EXISTS travel_booking_db;

-- 1. Safely drop tables in reverse order of foreign key dependencies
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS travel_booking_db.bookings;
DROP TABLE IF EXISTS travel_booking_db.hotels;
DROP TABLE IF EXISTS travel_booking_db.flights;
DROP TABLE IF EXISTS travel_booking_db.users;
SET FOREIGN_KEY_CHECKS = 1;

-- ==========================================
-- 2. CREATE TABLES
-- ==========================================

CREATE TABLE travel_booking_db.users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER'
);

CREATE TABLE travel_booking_db.flights (
    flight_id VARCHAR(20) PRIMARY KEY,
    departure VARCHAR(50) NOT NULL,
    arrival VARCHAR(50) NOT NULL,
    departure_time INT NOT NULL,
    price DOUBLE NOT NULL
);

CREATE TABLE travel_booking_db.hotels (
    hotel_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL,
    rating DOUBLE NOT NULL,
    price_per_night DOUBLE NOT NULL
);

CREATE TABLE travel_booking_db.bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    flight_id VARCHAR(20),
    hotel_id VARCHAR(20),
    total_price DOUBLE NOT NULL,
    CONSTRAINT fk_bookings_username FOREIGN KEY (username) REFERENCES travel_booking_db.users(username) ON DELETE CASCADE,
    CONSTRAINT fk_bookings_flight FOREIGN KEY (flight_id) REFERENCES travel_booking_db.flights(flight_id) ON DELETE SET NULL,
    CONSTRAINT fk_bookings_hotel FOREIGN KEY (hotel_id) REFERENCES travel_booking_db.hotels(hotel_id) ON DELETE SET NULL
);

-- ==========================================
-- 3. SEED ADMIN USER ONLY
-- ==========================================

INSERT INTO travel_booking_db.users (username, password, role) 
VALUES ('admin', 'admin123', 'ADMIN');