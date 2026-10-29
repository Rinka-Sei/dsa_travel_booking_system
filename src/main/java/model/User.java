package model;

/**
 * Represents a user in the travel booking system.
 */
public class User {

    private String username;
    private String password;
    private String role;

    /**
     * Constructs a new User object.
     *
     * @param username the unique username of the user
     * @param password the password of the user
     * @param role     the role assigned to the user (e.g. "CUSTOMER", "ADMIN")
     */
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the user role.
     *
     * @return the role
     */
    public String getRole() {
        return role;
    }
}
