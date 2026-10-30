package service;

import database.DBConnection;
import ds.BookingPriorityQueue;
import model.Booking;
import model.Hotel;
import model.PackageDeal;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service layer coordinating Hotel CRUD, Booking transactions, report generation, and {@link BookingPriorityQueue}.
 * Mirrors the {@code RentalService} service architecture from the starter project blueprint.
 */
public class BookingManagementService {

    /**
     * Constructs a BookingManagementService instance.
     */
    public BookingManagementService() {
    }

    // ==================== HOTEL CRUD ====================

    /**
     * Adds a new hotel listing to the MySQL database.
     *
     * @param hotel hotel entity
     * @throws SQLException on database error
     */
    public void addHotel(Hotel hotel) throws SQLException {
        if (hotel == null) {
            return;
        }
        String sql = "INSERT INTO hotels(hotel_id, name, location, rating, price_per_night) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hotel.getHotelId());
            ps.setString(2, hotel.getName());
            ps.setString(3, hotel.getLocation());
            ps.setDouble(4, hotel.getRating());
            ps.setDouble(5, hotel.getPricePerNight());
            ps.executeUpdate();
        }
    }

    /**
     * Updates an existing hotel listing in the database.
     *
     * @param hotel updated hotel entity
     * @throws SQLException on database error
     */
    public void updateHotel(Hotel hotel) throws SQLException {
        if (hotel == null) {
            return;
        }
        String sql = "UPDATE hotels SET name = ?, location = ?, rating = ?, price_per_night = ? WHERE hotel_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hotel.getName());
            ps.setString(2, hotel.getLocation());
            ps.setDouble(3, hotel.getRating());
            ps.setDouble(4, hotel.getPricePerNight());
            ps.setString(5, hotel.getHotelId());
            ps.executeUpdate();
        }
    }

    /**
     * Deletes a hotel record from the database by ID.
     *
     * @param hotelId hotel identifier
     * @throws SQLException on database error
     */
    public void deleteHotel(String hotelId) throws SQLException {
        String sql = "DELETE FROM hotels WHERE hotel_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hotelId);
            ps.executeUpdate();
        }
    }

    /**
     * Retrieves all hotels from the database.
     *
     * @return list of hotels
     * @throws SQLException on database error
     */
    public List<Hotel> getAllHotels() throws SQLException {
        List<Hotel> list = new ArrayList<>();
        String sql = "SELECT * FROM hotels";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Hotel(
                        rs.getString("hotel_id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getDouble("rating"),
                        rs.getDouble("price_per_night")
                ));
            }
        }
        return list;
    }

    /**
     * Filters hotels matching a location query.
     *
     * @param query search location
     * @return list of matching hotels
     * @throws SQLException on database error
     */
    public List<Hotel> searchHotels(String query) throws SQLException {
        List<Hotel> list = new ArrayList<>();
        String sql = "SELECT * FROM hotels WHERE location LIKE ? OR name LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String pattern = "%" + query + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Hotel(
                            rs.getString("hotel_id"),
                            rs.getString("name"),
                            rs.getString("location"),
                            rs.getDouble("rating"),
                            rs.getDouble("price_per_night")
                    ));
                }
            }
        }
        return list;
    }

    // ==================== BOOKINGS & PRIORITY QUEUE ====================

    /**
     * Creates a new booking in the database.
     *
     * @param username   customer username
     * @param flightId   flight ID
     * @param hotelId    hotel ID
     * @param totalPrice booking total
     * @throws SQLException on database error
     */
    public void createBooking(String username, String flightId, String hotelId, double totalPrice) throws SQLException {
        String sql = "INSERT INTO bookings(username, flight_id, hotel_id, total_price) VALUES(?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, flightId != null && !flightId.isEmpty() ? flightId : null);
            ps.setString(3, hotelId != null && !hotelId.isEmpty() ? hotelId : null);
            ps.setDouble(4, totalPrice);
            ps.executeUpdate();
        }
    }

    /**
     * Cancels / deletes a booking by ID.
     *
     * @param bookingId booking identifier
     * @throws SQLException on database error
     */
    public void cancelBooking(int bookingId) throws SQLException {
        String sql = "DELETE FROM bookings WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.executeUpdate();
        }
    }

    /**
     * Retrieves all bookings from the database.
     *
     * @param currentUser logged-in user
     * @param isAdmin     whether user is admin
     * @return list of bookings
     * @throws SQLException on database error
     */
    public List<Booking> getBookings(String currentUser, boolean isAdmin) throws SQLException {
        List<Booking> list = new ArrayList<>();
        String sql = isAdmin ? "SELECT * FROM bookings" : "SELECT * FROM bookings WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (!isAdmin) {
                ps.setString(1, currentUser);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Booking(
                            rs.getInt("booking_id"),
                            rs.getString("username"),
                            rs.getString("flight_id"),
                            rs.getString("hotel_id"),
                            rs.getDouble("total_price")
                    ));
                }
            }
        }
        return list;
    }

    /**
     * Loads bookings into a {@link BookingPriorityQueue} ordered by highest total price.
     *
     * @param currentUser logged-in username
     * @param isAdmin     whether the caller has admin rights
     * @return priority queue containing the loaded bookings
     * @throws SQLException on database error
     */
    public BookingPriorityQueue getBookingsPriorityQueue(String currentUser, boolean isAdmin) throws SQLException {
        BookingPriorityQueue queue = new BookingPriorityQueue();
        List<Booking> bookings = getBookings(currentUser, isAdmin);
        for (Booking b : bookings) {
            queue.addBooking(b);
        }
        return queue;
    }

    // ==================== ADMINISTRATIVE REPORT GENERATION ====================

    /**
     * Generates a comprehensive summary report of booking transactions, total revenue, and activity.
     *
     * @return formatted report string
     * @throws SQLException on database error
     */
    public String generateSystemSummaryReport() throws SQLException {
        int totalBookings = 0;
        double totalRevenue = 0;
        Map<String, Integer> customerActivity = new HashMap<>();

        String sql = "SELECT username, total_price FROM bookings";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                totalBookings++;
                totalRevenue += rs.getDouble("total_price");
                String user = rs.getString("username");
                customerActivity.put(user, customerActivity.getOrDefault(user, 0) + 1);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=====================================================\n");
        sb.append("         TRAVEL BOOKING SYSTEM — EXECUTIVE REPORT   \n");
        sb.append("=====================================================\n\n");
        sb.append(String.format("Total Completed Bookings : %d\n", totalBookings));
        sb.append(String.format("Gross System Revenue     : $%,.2f\n", totalRevenue));
        sb.append(String.format("Average Booking Value    : $%,.2f\n\n", totalBookings > 0 ? (totalRevenue / totalBookings) : 0.0));
        sb.append("Customer Transaction Activity Breakdown:\n");
        sb.append("-----------------------------------------------------\n");
        if (customerActivity.isEmpty()) {
            sb.append("No booking activity found.\n");
        } else {
            for (Map.Entry<String, Integer> entry : customerActivity.entrySet()) {
                sb.append(String.format(" - %-20s : %d bookings\n", entry.getKey(), entry.getValue()));
            }
        }
        sb.append("=====================================================\n");
        return sb.toString();
    }
}
