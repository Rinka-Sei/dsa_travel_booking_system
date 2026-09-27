package com.mycompany.dsa_travel_booking_system.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provides database connectivity management for the travel booking system.
 */
public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/travel_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Default XAMPP password is empty

    /**
     * Default constructor for DBConnection.
     */
    public DBConnection() {
    }

    /**
     * Establishes and returns a connection to the MySQL database.
     *
     * @return active {@link Connection} to the database
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
