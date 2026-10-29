package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public String getCurrentUser() { return currentUser; }
    public String getCurrentRole() { return currentRole; }

    public void refreshBookingPanel() {
        if (bookingPanel != null) bookingPanel.loadBookings();
    }

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogout) {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            this.dispose();
        }
    }
}