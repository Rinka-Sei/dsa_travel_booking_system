package service;

import database.DBConnection;
import ds.FlightBST;
import model.Flight;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service layer coordinating Flight operations between MySQL database and the {@link FlightBST} data structure.
 * Mirrors the {@code MovieCatalog} service architecture from the starter project blueprint.
 */
public class FlightCatalogService {

    private final FlightBST flightBST;

    /**
     * Constructs a FlightCatalogService and initializes its backing BST.
     */
    public FlightCatalogService() {
        this.flightBST = new FlightBST();
    }

    /**
     * Adds a new flight record to both the MySQL database and the internal BST cache.
     *
     * @param flight the flight to persist
     * @throws SQLException if a database access error occurs
     */
    public void addFlight(Flight flight) throws SQLException {
        if (flight == null) {
            return;
        }
        String sql = "INSERT INTO flights(flight_id, departure, departure_time, arrival, arrival_time, price) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, flight.getFlightId());
            ps.setString(2, flight.getDeparture());
            ps.setString(3, flight.getDepartureTime());
            ps.setString(4, flight.getArrival());
            ps.setString(5, flight.getArrivalTime());
            ps.setDouble(6, flight.getPrice());
            ps.executeUpdate();
        }
        flightBST.insert(flight);
    }

    /**
     * Updates an existing flight record in the MySQL database.
     *
     * @param flight the flight containing updated fields
     * @throws SQLException if a database access error occurs
     */
    public void updateFlight(Flight flight) throws SQLException {
        if (flight == null) {
            return;
        }
        String sql = "UPDATE flights SET departure = ?, departure_time = ?, arrival = ?, arrival_time = ?, price = ? WHERE flight_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, flight.getDeparture());
            ps.setString(2, flight.getDepartureTime());
            ps.setString(3, flight.getArrival());
            ps.setString(4, flight.getArrivalTime());
            ps.setDouble(5, flight.getPrice());
            ps.setString(6, flight.getFlightId());
            ps.executeUpdate();
        }
    }

    /**
     * Deletes a flight record from the MySQL database by flight ID.
     *
     * @param flightId the unique identifier of the flight to delete
     * @throws SQLException if a database access error occurs
     */
    public void deleteFlight(String flightId) throws SQLException {
        String sql = "DELETE FROM flights WHERE flight_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, flightId);
            ps.executeUpdate();
        }
    }

    /**
     * Loads all flight records from the database ordered chronologically via {@link FlightBST}.
     *
     * @return a list of flights sorted chronologically by military departure time
     * @throws SQLException if a database access error occurs
     */
    public List<Flight> getSortedFlightsBST() throws SQLException {
        FlightBST bst = new FlightBST();
        String sql = "SELECT * FROM flights";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Flight f = new Flight(
                        rs.getString("flight_id"),
                        rs.getString("departure"),
                        rs.getString("departure_time"),
                        rs.getString("arrival"),
                        rs.getString("arrival_time"),
                        rs.getDouble("price")
                );
                bst.insert(f);
            }
        }
        return bst.getSortedFlights();
    }

    /**
     * Filters flights matching a query string across ID, departure, or arrival.
     *
     * @param query the search substring
     * @return filtered list of flights
     * @throws SQLException if a database access error occurs
     */
    public List<Flight> searchFlights(String query) throws SQLException {
        List<Flight> list = new ArrayList<>();
        String sql = "SELECT * FROM flights WHERE flight_id LIKE ? OR departure LIKE ? OR arrival LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String pattern = "%" + query + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Flight(
                            rs.getString("flight_id"),
                            rs.getString("departure"),
                            rs.getString("departure_time"),
                            rs.getString("arrival"),
                            rs.getString("arrival_time"),
                            rs.getDouble("price")
                    ));
                }
            }
        }
        return list;
    }
}
