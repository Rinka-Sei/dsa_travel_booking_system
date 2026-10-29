package com.mycompany.dsa_travel_booking_system.gui;

import com.mycompany.dsa_travel_booking_system.db.DBConnection;
import com.mycompany.dsa_travel_booking_system.ds.UserHashTable;
import com.mycompany.dsa_travel_booking_system.model.User;

import javax.swing.*;
import java.sql.*;

/**
 * Main graphical user interface frame for the Travel Booking System.
 * Modularized to manage view navigation and session states.
 */
public class MainFrame extends JFrame {

    private String currentUser = null;
    private String currentRole = null;
    private UserHashTable userTable = new UserHashTable(20);

    private JPanel loginPanel;
    private JPanel dashboardPanel;

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    private JLabel lblWelcome;
    private JTabbedPane tabbedPane;

    private FlightPanel flightPanel;
    private HotelPanel hotelPanel;
    private PackagePanel packagePanel;
    private BookingPanel bookingPanel;

    public MainFrame() {
        setTitle("Travel Booking System");
        setSize(850, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        loadUsersIntoHashTable();
        initLoginPanel();
        initDashboardPanel();

        showLogin();
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public String getCurrentRole() {
        return currentRole;
    }

    private void loadUsersIntoHashTable() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {
            while (rs.next()) {
                User u = new User(rs.getString("username"), rs.getString("password"), rs.getString("role"));
                userTable.put(u.getUsername(), u);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Connection Error: " + ex.getMessage());
        }
    }

    private void initLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBounds(0, 0, 850, 650);

        JLabel lblTitle = new JLabel("TRAVEL BOOKING SYSTEM LOGIN");
        lblTitle.setBounds(300, 80, 300, 30);
        loginPanel.add(lblTitle);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(250, 150, 100, 25);
        loginPanel.add(lblUser);

        txtUsername = new JTextField();
        txtUsername.setBounds(350, 150, 200, 25);
        loginPanel.add(txtUsername);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(250, 200, 100, 25);
        loginPanel.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(350, 200, 200, 25);
        loginPanel.add(txtPassword);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(270, 260, 100, 30);
        loginPanel.add(btnLogin);

        JButton btnRegister = new JButton("Register");
        btnRegister.setBounds(410, 260, 100, 30);
        loginPanel.add(btnRegister);

        btnLogin.addActionListener(e -> performLogin());
        btnRegister.addActionListener(e -> performRegister());

        add(loginPanel);
    }

    private void performLogin() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        User u = userTable.get(user);
        if (u != null && u.getPassword().equals(pass)) {
            currentUser = u.getUsername();
            currentRole = u.getRole();
            lblWelcome.setText("Welcome, " + currentUser + " [" + currentRole + "]");
            
            // Clear login text fields after login
            txtUsername.setText("");
            txtPassword.setText("");

            showDashboard();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!");
        }
    }

    private void performRegister() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fields cannot be empty.");
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO users(username, password, role) VALUES(?, ?, 'CUSTOMER')")) {
            ps.setString(1, user);
            ps.setString(2, pass);
            ps.executeUpdate();

            User newUser = new User(user, pass, "CUSTOMER");
            userTable.put(user, newUser);
            
            txtUsername.setText("");
            txtPassword.setText("");

            JOptionPane.showMessageDialog(this, "Customer Registered Successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Registration failed or user exists: " + ex.getMessage());
        }
    }

    private void initDashboardPanel() {
        dashboardPanel = new JPanel();
        dashboardPanel.setLayout(null);
        dashboardPanel.setBounds(0, 0, 850, 650);

        lblWelcome = new JLabel("Welcome");
        lblWelcome.setBounds(20, 10, 300, 25);
        dashboardPanel.add(lblWelcome);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(700, 10, 100, 25);
        btnLogout.addActionListener(e -> showLogin());
        dashboardPanel.add(btnLogout);

        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(20, 40, 790, 560);

        flightPanel = new FlightPanel(this);
        hotelPanel = new HotelPanel(this);
        packagePanel = new PackagePanel();
        bookingPanel = new BookingPanel(this);

        tabbedPane.addTab("Flights", flightPanel);
        tabbedPane.addTab("Hotels", hotelPanel);
        tabbedPane.addTab("Package Deals", packagePanel);
        tabbedPane.addTab("Bookings & Itinerary", bookingPanel);

        dashboardPanel.add(tabbedPane);
        add(dashboardPanel);
    }

    private void showLogin() {
        loginPanel.setVisible(true);
        dashboardPanel.setVisible(false);
        currentUser = null;
        currentRole = null;
    }

    private void showDashboard() {
        loginPanel.setVisible(false);
        dashboardPanel.setVisible(true);
        flightPanel.loadFlightsBST();
        hotelPanel.loadHotels();
        packagePanel.loadPackagesQuickSort("PRICE");
        bookingPanel.loadBookings();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}