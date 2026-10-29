package service;

import database.DBConnection;
import ds.UserHashTable;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service layer coordinating User and Customer registration, authentication, and caching via {@link UserHashTable}.
 * Mirrors the {@code CustomerRegistry} service architecture from the starter project blueprint.
 */
public class CustomerRegistryService {

    private final UserHashTable userHashTable;

    /**
     * Constructs CustomerRegistryService with an initial hash table cache capacity.
     */
    public CustomerRegistryService() {
        this.userHashTable = new UserHashTable(50);
    }

    /**
     * Seeds existing users from the MySQL database into the hash table cache.
     *
     * @throws SQLException if a database access error occurs
     */
    public void loadUsersIntoCache() throws SQLException {
        String sql = "SELECT * FROM users";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                User u = new User(rs.getString("username"), rs.getString("password"), rs.getString("role"));
                userHashTable.put(u.getUsername(), u);
            }
        }
    }

    /**
     * Authenticates a user against the in-memory cache with database fallback.
     *
     * @param username user identifier
     * @param password user password
     * @return the authenticated {@link User} object, or {@code null} if credentials are invalid
     * @throws SQLException if a database access error occurs
     */
    public User authenticate(String username, String password) throws SQLException {
        User u = userHashTable.get(username);
        if (u == null) {
            String sql = "SELECT * FROM users WHERE username = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        u = new User(rs.getString("username"), rs.getString("password"), rs.getString("role"));
                        userHashTable.put(u.getUsername(), u);
                    }
                }
            }
        }
        if (u != null && u.getPassword().equals(password)) {
            return u;
        }
        return null;
    }

    /**
     * Registers a new customer into both the database and the hash table cache.
     *
     * @param username customer username
     * @param password customer password
     * @throws SQLException if username already exists or insert fails
     */
    public void registerCustomer(String username, String password) throws SQLException {
        String sql = "INSERT INTO users(username, password, role) VALUES(?, ?, 'CUSTOMER')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
        }
        userHashTable.put(username, new User(username, password, "CUSTOMER"));
    }

    /**
     * Retrieves all customer users from the database.
     *
     * @return list of customer accounts
     * @throws SQLException if a database access error occurs
     */
    public List<User> getAllCustomers() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = 'CUSTOMER'";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new User(rs.getString("username"), rs.getString("password"), rs.getString("role")));
            }
        }
        return list;
    }

    /**
     * Gets the backing hash table cache.
     *
     * @return {@link UserHashTable} cache instance
     */
    public UserHashTable getUserHashTable() {
        return userHashTable;
    }
}
