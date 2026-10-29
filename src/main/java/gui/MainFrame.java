package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main dashboard container frame for the Travel Booking System.
 * Hosts tabbed views for Flights, Hotels, Package Deals, and Bookings.
 */
public class MainFrame extends JFrame implements ActionListener {

    private String currentUser;
    private String currentRole;

    private JLabel lblWelcome;
    private JButton btnLogout;
    private JTabbedPane tabbedPane;

    private FlightPanel flightPanel;
    private HotelPanel hotelPanel;
    private PackagePanel packagePanel;
    private BookingPanel bookingPanel;

    /**
     * Constructs the MainFrame container for the authenticated user session.
     *
     * @param username the logged-in username
     * @param role     the role of the logged-in user ("ADMIN" or "CUSTOMER")
     */
    public MainFrame(String username, String role) {
        this.currentUser = username;
        this.currentRole = role;

        setTitle("Travel Booking System - Dashboard [" + role + "]");
        setSize(850, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        initDashboardComponents();
    }

    /**
     * Gets the username of the currently logged-in user.
     *
     * @return the logged-in username
     */
    public String getCurrentUser() {
        return currentUser;
    }

    /**
     * Gets the role of the currently logged-in user.
     *
     * @return the logged-in role
     */
    public String getCurrentRole() {
        return currentRole;
    }

    /**
     * Triggers a refresh of the bookings table inside the BookingPanel.
     */
    public void refreshBookingPanel() {
        if (bookingPanel != null) {
            bookingPanel.loadBookings();
        }
    }

    /**
     * Triggers a re-computation and re-render of dynamic package deals.
     */
    public void refreshPackagePanel() {
        if (packagePanel != null) {
            packagePanel.refreshPackages();
        }
    }

    /**
     * Initializes the absolute-positioned dashboard layout and tabbed view panels.
     */
    private void initDashboardComponents() {
        lblWelcome = new JLabel("Welcome, " + currentUser + " [" + currentRole + "]");
        lblWelcome.setBounds(20, 10, 300, 25);
        add(lblWelcome);

        btnLogout = new JButton("Logout");
        btnLogout.setBounds(700, 10, 100, 25);
        btnLogout.addActionListener(this);
        add(btnLogout);

        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(20, 40, 790, 580);

        flightPanel = new FlightPanel(this);
        hotelPanel = new HotelPanel(this);
        packagePanel = new PackagePanel(this);
        bookingPanel = new BookingPanel(this);

        tabbedPane.addTab("Flights", flightPanel);
        tabbedPane.addTab("Hotels", hotelPanel);
        tabbedPane.addTab("Package Deals", packagePanel);
        tabbedPane.addTab("Bookings & Itinerary", bookingPanel);

        add(tabbedPane);
    }

    /**
     * Handles action events such as user logout.
     *
     * @param e the ActionEvent triggered
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogout) {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            this.dispose();
        }
    }
}
