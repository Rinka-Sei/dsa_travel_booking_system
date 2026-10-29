-- Customer Test Data Script (10 Customers & 20 Sample Bookings)
USE travel_booking_db;

-- 1. Insert exactly 10 customer accounts
INSERT IGNORE INTO users (username, password, role) VALUES
('customer1', 'pass123', 'CUSTOMER'),
('customer2', 'pass123', 'CUSTOMER'),
('customer3', 'pass123', 'CUSTOMER'),
('customer4', 'pass123', 'CUSTOMER'),
('customer5', 'pass123', 'CUSTOMER'),
('customer6', 'pass123', 'CUSTOMER'),
('customer7', 'pass123', 'CUSTOMER'),
('customer8', 'pass123', 'CUSTOMER'),
('customer9', 'pass123', 'CUSTOMER'),
('customer10', 'pass123', 'CUSTOMER');

-- Sample flights data (supporting bookings)
INSERT IGNORE INTO flights (flight_id, departure, departure_time, arrival, arrival_time, price) VALUES
('FL101', 'NYC', '08:30', 'Tokyo (NRT)', '16:45', 850.00),
('FL102', 'LAX', '10:15', 'Paris (CDG)', '18:30', 720.00),
('FL103', 'SFO', '14:00', 'London (LHR)', '22:15', 650.00),
('FL104', 'ORD', '09:45', 'Rome (FCO)', '17:30', 780.00),
('FL105', 'MIA', '21:00', 'Sydney (SYD)', '06:30', 1100.00);

-- Sample hotels data (supporting bookings)
INSERT IGNORE INTO hotels (hotel_id, name, location, rating, price_per_night) VALUES
('H201', 'Tokyo Grand Hotel', 'Tokyo', 4.8, 220.00),
('H202', 'Le Hotel Paris', 'Paris', 4.6, 190.00),
('H203', 'London Bridge Hotel', 'London', 4.5, 180.00),
('H204', 'Roma Suites', 'Rome', 4.7, 160.00),
('H205', 'Sydney Harbour Resort', 'Sydney', 4.9, 250.00);

-- 2. Insert exactly 20 sample bookings connecting customers with flights and hotels
INSERT INTO bookings (username, flight_id, hotel_id, total_price) VALUES
('customer1', 'FL101', 'H201', 1070.00),
('customer1', 'FL102', 'H202', 910.00),
('customer2', 'FL101', 'H201', 1070.00),
('customer2', 'FL103', 'H203', 830.00),
('customer3', 'FL102', 'H202', 910.00),
('customer3', 'FL104', 'H204', 940.00),
('customer4', 'FL103', 'H203', 830.00),
('customer4', 'FL105', 'H205', 1350.00),
('customer5', 'FL104', 'H204', 940.00),
('customer5', 'FL101', 'H201', 1070.00),
('customer6', 'FL105', 'H205', 1350.00),
('customer6', 'FL102', 'H202', 910.00),
('customer7', 'FL101', 'H201', 1070.00),
('customer7', 'FL103', 'H203', 830.00),
('customer8', 'FL102', 'H202', 910.00),
('customer8', 'FL104', 'H204', 940.00),
('customer9', 'FL103', 'H203', 830.00),
('customer9', 'FL105', 'H205', 1350.00),
('customer10', 'FL104', 'H204', 940.00),
('customer10', 'FL101', 'H201', 1070.00);
