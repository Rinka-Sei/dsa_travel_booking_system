-- Database Initialization & Admin Seeding Script
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
    departure_time VARCHAR(5) NOT NULL,
    arrival VARCHAR(100) NOT NULL,
    arrival_time VARCHAR(5) NOT NULL,
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
