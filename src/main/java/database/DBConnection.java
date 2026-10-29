package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Handles JDBC connection management for the MySQL database.
 * Provides static utility methods to obtain database connection instances.
 */
public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/travel_booking_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Default private constructor to prevent instantiation of utility class.
     */
    private DBConnection() {
    }

    /**
     * Establishes and returns a new {@link Connection} to the configured MySQL database.
     *
     * @return an active JDBC {@link Connection} instance
     * @throws SQLException if a database access error occurs or the connection URL is invalid
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
