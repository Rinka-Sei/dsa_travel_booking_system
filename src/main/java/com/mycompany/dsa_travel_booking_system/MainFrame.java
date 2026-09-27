package com.mycompany.dsa_travel_booking_system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    // Global session state
    private String currentUser = null;
    private String currentRole = null;
    private UserHashTable userTable = new UserHashTable(20);

    // Panels
    private JPanel loginPanel;
    private JPanel dashboardPanel;

    // Login Components
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    // Dashboard Components
    private JLabel lblWelcome;
    private JTabbedPane tabbedPane;

    // Flight Management UI
    private JTextField txtFlightId, txtDep, txtArr, txtDepTime, txtFlightPrice;
    private JTextField txtSearchFlight; // Search Filter
    private JTable tblFlights;
    private DefaultTableModel flightModel;

    // Hotel Management UI
    private JTextField txtHotelId, txtHotelName, txtLocation, txtRating, txtHotelPrice;
    private JTextField txtSearchHotel; // Search Filter
    private JTable tblHotels;
    private DefaultTableModel hotelModel;

    // Package Deals UI
    private JTable tblPackages;
    private DefaultTableModel packageModel;

    // Booking UI
    private JTextField txtBookFlightId, txtBookHotelId;
    private JTable tblBookings;
    private DefaultTableModel bookingModel;

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

        tabbedPane.addTab("Flights", createFlightPanel());
        tabbedPane.addTab("Hotels", createHotelPanel());
        tabbedPane.addTab("Package Deals (QuickSort)", createPackagePanel());
        tabbedPane.addTab("Bookings & Itinerary", createBookingPanel());

        dashboardPanel.add(tabbedPane);
        add(dashboardPanel);
    }

    private JPanel createFlightPanel() {
        JPanel pnl = new JPanel();
        pnl.setLayout(null);

        JLabel lblId = new JLabel("Flight ID:");
        lblId.setBounds(10, 10, 80, 25);
        pnl.add(lblId);

        txtFlightId = new JTextField();
        txtFlightId.setBounds(90, 10, 100, 25);
        pnl.add(txtFlightId);

        JLabel lblDep = new JLabel("Departure:");
        lblDep.setBounds(200, 10, 80, 25);
        pnl.add(lblDep);

        txtDep = new JTextField();
        txtDep.setBounds(280, 10, 100, 25);
        pnl.add(txtDep);

        JLabel lblArr = new JLabel("Arrival:");
        lblArr.setBounds(390, 10, 80, 25);
        pnl.add(lblArr);

        txtArr = new JTextField();
        txtArr.setBounds(460, 10, 100, 25);
        pnl.add(txtArr);

        JLabel lblTime = new JLabel("Time (HHMM):");
        lblTime.setBounds(10, 45, 90, 25);
        pnl.add(lblTime);

        txtDepTime = new JTextField();
        txtDepTime.setBounds(100, 45, 90, 25);
        pnl.add(txtDepTime);

        JLabel lblPrice = new JLabel("Price:");
        lblPrice.setBounds(200, 45, 50, 25);
        pnl.add(lblPrice);

        txtFlightPrice = new JTextField();
        txtFlightPrice.setBounds(250, 45, 100, 25);
        pnl.add(txtFlightPrice);

        JButton btnAdd = new JButton("Add Flight");
        btnAdd.setBounds(370, 45, 110, 25);
        pnl.add(btnAdd);

        JButton btnSortBST = new JButton("Load (BST Sorted)");
        btnSortBST.setBounds(490, 45, 150, 25);
        pnl.add(btnSortBST);

        // Search Section
        JLabel lblSearch = new JLabel("Search Destination:");
        lblSearch.setBounds(10, 85, 130, 25);
        pnl.add(lblSearch);

        txtSearchFlight = new JTextField();
        txtSearchFlight.setBounds(140, 85, 150, 25);
        pnl.add(txtSearchFlight);

        txtSearchFlight.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterFlights(txtSearchFlight.getText().trim());
            }
        });

        flightModel = new DefaultTableModel(new String[]{"ID", "Departure", "Arrival", "Time", "Price"}, 0);
        tblFlights = new JTable(flightModel);
        JScrollPane sp = new JScrollPane(tblFlights);
        sp.setBounds(10, 120, 755, 390);
        pnl.add(sp);

        btnAdd.addActionListener(e -> {
            if (!"ADMIN".equals(currentRole)) {
                JOptionPane.showMessageDialog(this, "Only admins can add flights.");
                return;
            }
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement("INSERT INTO flights VALUES(?, ?, ?, ?, ?)")) {
                ps.setString(1, txtFlightId.getText());
                ps.setString(2, txtDep.getText());
                ps.setString(3, txtArr.getText());
                ps.setInt(4, Integer.parseInt(txtDepTime.getText()));
                ps.setDouble(5, Double.parseDouble(txtFlightPrice.getText()));
                ps.executeUpdate();
                loadFlightsBST();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        btnSortBST.addActionListener(e -> loadFlightsBST());

        return pnl;
    }

    private void loadFlightsBST() {
        flightModel.setRowCount(0);
        FlightBST bst = new FlightBST();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM flights")) {

            while (rs.next()) {
                Flight f = new Flight(
                        rs.getString("flight_id"),
                        rs.getString("departure"),
                        rs.getString("arrival"),
                        rs.getInt("departure_time"),
                        rs.getDouble("price")
                );
                bst.insert(f);
            }

            for (Flight f : bst.getSortedFlights()) {
                flightModel.addRow(new Object[]{f.getFlightId(), f.getDeparture(), f.getArrival(), f.getDepartureTime(), f.getPrice()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading flights: " + ex.getMessage());
        }
    }

    private void filterFlights(String query) {
        flightModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM flights WHERE LOWER(departure) LIKE ? OR LOWER(arrival) LIKE ?")) {
            String searchPattern = "%" + query.toLowerCase() + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                flightModel.addRow(new Object[]{
                        rs.getString("flight_id"),
                        rs.getString("departure"),
                        rs.getString("arrival"),
                        rs.getInt("departure_time"),
                        rs.getDouble("price")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage());
        }
    }

    private JPanel createHotelPanel() {
        JPanel pnl = new JPanel();
        pnl.setLayout(null);

        JLabel lblId = new JLabel("Hotel ID:");
        lblId.setBounds(10, 10, 70, 25);
        pnl.add(lblId);

        txtHotelId = new JTextField();
        txtHotelId.setBounds(80, 10, 80, 25);
        pnl.add(txtHotelId);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(170, 10, 50, 25);
        pnl.add(lblName);

        txtHotelName = new JTextField();
        txtHotelName.setBounds(220, 10, 120, 25);
        pnl.add(txtHotelName);

        JLabel lblLoc = new JLabel("Location:");
        lblLoc.setBounds(350, 10, 60, 25);
        pnl.add(lblLoc);

        txtLocation = new JTextField();
        txtLocation.setBounds(410, 10, 100, 25);
        pnl.add(txtLocation);

        JLabel lblRat = new JLabel("Rating:");
        lblRat.setBounds(10, 45, 50, 25);
        pnl.add(lblRat);

        txtRating = new JTextField();
        txtRating.setBounds(60, 45, 50, 25);
        pnl.add(txtRating);

        JLabel lblPrice = new JLabel("Price/Night:");
        lblPrice.setBounds(120, 45, 80, 25);
        pnl.add(lblPrice);

        txtHotelPrice = new JTextField();
        txtHotelPrice.setBounds(200, 45, 80, 25);
        pnl.add(txtHotelPrice);

        JButton btnAdd = new JButton("Add Hotel");
        btnAdd.setBounds(290, 45, 100, 25);
        pnl.add(btnAdd);

        // Search Section
        JLabel lblSearch = new JLabel("Search Location:");
        lblSearch.setBounds(10, 85, 120, 25);
        pnl.add(lblSearch);

        txtSearchHotel = new JTextField();
        txtSearchHotel.setBounds(130, 85, 150, 25);
        pnl.add(txtSearchHotel);

        txtSearchHotel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterHotels(txtSearchHotel.getText().trim());
            }
        });

        hotelModel = new DefaultTableModel(new String[]{"ID", "Name", "Location", "Rating", "Price/Night"}, 0);
        tblHotels = new JTable(hotelModel);
        JScrollPane sp = new JScrollPane(tblHotels);
        sp.setBounds(10, 120, 755, 390);
        pnl.add(sp);

        btnAdd.addActionListener(e -> {
            if (!"ADMIN".equals(currentRole)) {
                JOptionPane.showMessageDialog(this, "Only admins can add hotels.");
                return;
            }
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement("INSERT INTO hotels VALUES(?, ?, ?, ?, ?)")) {
                ps.setString(1, txtHotelId.getText());
                ps.setString(2, txtHotelName.getText());
                ps.setString(3, txtLocation.getText());
                ps.setDouble(4, Double.parseDouble(txtRating.getText()));
                ps.setDouble(5, Double.parseDouble(txtHotelPrice.getText()));
                ps.executeUpdate();
                loadHotels();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        return pnl;
    }

    private void loadHotels() {
        hotelModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM hotels")) {

            while (rs.next()) {
                hotelModel.addRow(new Object[]{
                        rs.getString("hotel_id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getDouble("rating"),
                        rs.getDouble("price_per_night")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading hotels: " + ex.getMessage());
        }
    }

    private void filterHotels(String query) {
        hotelModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM hotels WHERE LOWER(location) LIKE ? OR LOWER(name) LIKE ?")) {
            String searchPattern = "%" + query.toLowerCase() + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                hotelModel.addRow(new Object[]{
                        rs.getString("hotel_id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getDouble("rating"),
                        rs.getDouble("price_per_night")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage());
        }
    }

    private JPanel createPackagePanel() {
        JPanel pnl = new JPanel();
        pnl.setLayout(null);

        JButton btnSortPrice = new JButton("QuickSort by Price (Low to High)");
        btnSortPrice.setBounds(10, 10, 240, 25);
        pnl.add(btnSortPrice);

        JButton btnSortRating = new JButton("QuickSort by Feature (Rating)");
        btnSortRating.setBounds(260, 10, 230, 25);
        pnl.add(btnSortRating);

        packageModel = new DefaultTableModel(new String[]{"Flight ID", "Flight Price", "Hotel Name", "Hotel Rating", "Total Package Price"}, 0);
        tblPackages = new JTable(packageModel);
        JScrollPane sp = new JScrollPane(tblPackages);
        sp.setBounds(10, 45, 755, 460);
        pnl.add(sp);

        btnSortPrice.addActionListener(e -> loadPackagesQuickSort("PRICE"));
        btnSortRating.addActionListener(e -> loadPackagesQuickSort("RATING"));

        return pnl;
    }

    private List<PackageDeal> fetchAllPackages() {
        List<PackageDeal> packages = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt1 = conn.createStatement();
             ResultSet rsF = stmt1.executeQuery("SELECT * FROM flights")) {

            List<Flight> flights = new ArrayList<>();
            while (rsF.next()) {
                flights.add(new Flight(
                        rsF.getString("flight_id"),
                        rsF.getString("departure"),
                        rsF.getString("arrival"),
                        rsF.getInt("departure_time"),
                        rsF.getDouble("price")
                ));
            }

            Statement stmt2 = conn.createStatement();
            ResultSet rsH = stmt2.executeQuery("SELECT * FROM hotels");
            List<Hotel> hotels = new ArrayList<>();
            while (rsH.next()) {
                hotels.add(new Hotel(
                        rsH.getString("hotel_id"),
                        rsH.getString("name"),
                        rsH.getString("location"),
                        rsH.getDouble("rating"),
                        rsH.getDouble("price_per_night")
                ));
            }

            for (Flight f : flights) {
                for (Hotel h : hotels) {
                    packages.add(new PackageDeal(f, h));
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching packages: " + ex.getMessage());
        }
        return packages;
    }

    private void loadPackagesQuickSort(String mode) {
        packageModel.setRowCount(0);
        List<PackageDeal> packages = fetchAllPackages();

        if (!packages.isEmpty()) {
            if ("PRICE".equals(mode)) {
                QuickSort.sortByPrice(packages, 0, packages.size() - 1);
            } else if ("RATING".equals(mode)) {
                QuickSort.sortByRating(packages, 0, packages.size() - 1);
            }
        }

        for (PackageDeal pkg : packages) {
            packageModel.addRow(new Object[]{
                    pkg.getFlight().getFlightId(),
                    pkg.getFlight().getPrice(),
                    pkg.getHotel().getName(),
                    pkg.getHotelRating(),
                    pkg.getTotalPrice()
            });
        }
    }

    private JPanel createBookingPanel() {
        JPanel pnl = new JPanel();
        pnl.setLayout(null);

        JLabel lblFlightId = new JLabel("Flight ID:");
        lblFlightId.setBounds(10, 10, 70, 25);
        pnl.add(lblFlightId);

        txtBookFlightId = new JTextField();
        txtBookFlightId.setBounds(80, 10, 90, 25);
        pnl.add(txtBookFlightId);

        JLabel lblHotelId = new JLabel("Hotel ID:");
        lblHotelId.setBounds(180, 10, 70, 25);
        pnl.add(lblHotelId);

        txtBookHotelId = new JTextField();
        txtBookHotelId.setBounds(240, 10, 90, 25);
        pnl.add(txtBookHotelId);

        JButton btnBook = new JButton("Book Package");
        btnBook.setBounds(340, 10, 120, 25);
        pnl.add(btnBook);

        JButton btnItinerary = new JButton("View Itinerary");
        btnItinerary.setBounds(470, 10, 120, 25);
        pnl.add(btnItinerary);

        JButton btnInvoice = new JButton("Invoice");
        btnInvoice.setBounds(600, 10, 80, 25);
        pnl.add(btnInvoice);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(685, 10, 80, 25);
        pnl.add(btnCancel);

        bookingModel = new DefaultTableModel(new String[]{"Booking ID", "Customer", "Flight ID", "Hotel ID", "Total Price"}, 0);
        tblBookings = new JTable(bookingModel);
        JScrollPane sp = new JScrollPane(tblBookings);
        sp.setBounds(10, 45, 755, 460);
        pnl.add(sp);

        btnBook.addActionListener(e -> performBooking());
        btnItinerary.addActionListener(e -> generateItinerary());
        btnInvoice.addActionListener(e -> generateInvoice());
        btnCancel.addActionListener(e -> cancelBooking());

        return pnl;
    }

    private void performBooking() {
        String fId = txtBookFlightId.getText().trim();
        String hId = txtBookHotelId.getText().trim();

        if (fId.isEmpty() && hId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter at least a Flight ID or Hotel ID.");
            return;
        }

        double flightPrice = 0;
        double hotelPrice = 0;

        try (Connection conn = DBConnection.getConnection()) {
            if (!fId.isEmpty()) {
                PreparedStatement ps = conn.prepareStatement("SELECT price FROM flights WHERE flight_id = ?");
                ps.setString(1, fId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    flightPrice = rs.getDouble("price");
                }
            }

            if (!hId.isEmpty()) {
                PreparedStatement ps = conn.prepareStatement("SELECT price_per_night FROM hotels WHERE hotel_id = ?");
                ps.setString(1, hId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    hotelPrice = rs.getDouble("price_per_night");
                }
            }

            double total = flightPrice + hotelPrice;

            PreparedStatement psInsert = conn.prepareStatement("INSERT INTO bookings(username, flight_id, hotel_id, total_price) VALUES(?, ?, ?, ?)");
            psInsert.setString(1, currentUser);
            psInsert.setString(2, fId.isEmpty() ? null : fId);
            psInsert.setString(3, hId.isEmpty() ? null : hId);
            psInsert.setDouble(4, total);
            psInsert.executeUpdate();

            JOptionPane.showMessageDialog(this, "Booking Successful!");
            loadBookings();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Booking Failed: " + ex.getMessage());
        }
    }

    private void loadBookings() {
        bookingModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            String query = "ADMIN".equals(currentRole) ? "SELECT * FROM bookings" : "SELECT * FROM bookings WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            if (!"ADMIN".equals(currentRole)) {
                ps.setString(1, currentUser);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bookingModel.addRow(new Object[]{
                        rs.getInt("booking_id"),
                        rs.getString("username"),
                        rs.getString("flight_id"),
                        rs.getString("hotel_id"),
                        rs.getDouble("total_price")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading bookings: " + ex.getMessage());
        }
    }

    private void generateItinerary() {
        int selectedRow = tblBookings.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking from the table to view its itinerary.");
            return;
        }

        Object bId = bookingModel.getValueAt(selectedRow, 0);
        Object fId = bookingModel.getValueAt(selectedRow, 2);
        Object hId = bookingModel.getValueAt(selectedRow, 3);

        StringBuilder itinerary = new StringBuilder("=== TRAVEL ITINERARY ===\n");
        itinerary.append("Booking ID: ").append(bId).append("\n\n");

        try (Connection conn = DBConnection.getConnection()) {
            if (fId != null) {
                PreparedStatement psF = conn.prepareStatement("SELECT * FROM flights WHERE flight_id = ?");
                psF.setString(1, fId.toString());
                ResultSet rsF = psF.executeQuery();
                if (rsF.next()) {
                    itinerary.append("[FLIGHT DETAILS]\n")
                            .append("Flight ID: ").append(rsF.getString("flight_id")).append("\n")
                            .append("From: ").append(rsF.getString("departure")).append("\n")
                            .append("To: ").append(rsF.getString("arrival")).append("\n")
                            .append("Time: ").append(rsF.getInt("departure_time")).append(" HRS\n\n");
                }
            } else {
                itinerary.append("[FLIGHT DETAILS]: No flight booked.\n\n");
            }

            if (hId != null) {
                PreparedStatement psH = conn.prepareStatement("SELECT * FROM hotels WHERE hotel_id = ?");
                psH.setString(1, hId.toString());
                ResultSet rsH = psH.executeQuery();
                if (rsH.next()) {
                    itinerary.append("[HOTEL DETAILS]\n")
                            .append("Hotel: ").append(rsH.getString("name")).append("\n")
                            .append("Location: ").append(rsH.getString("location")).append("\n")
                            .append("Rating: ").append(rsH.getDouble("rating")).append(" Stars\n\n");
                }
            } else {
                itinerary.append("[HOTEL DETAILS]: No hotel booked.\n\n");
            }

            JOptionPane.showMessageDialog(this, itinerary.toString(), "Itinerary Summary", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error building itinerary: " + ex.getMessage());
        }
    }

    private void generateInvoice() {
        int selectedRow = tblBookings.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking from the table.");
            return;
        }

        Object bId = bookingModel.getValueAt(selectedRow, 0);
        Object user = bookingModel.getValueAt(selectedRow, 1);
        Object fId = bookingModel.getValueAt(selectedRow, 2);
        Object hId = bookingModel.getValueAt(selectedRow, 3);
        Object total = bookingModel.getValueAt(selectedRow, 4);

        String invoice = "====================================\n"
                + "          TRAVEL INVOICE            \n"
                + "====================================\n"
                + "Booking ID: " + bId + "\n"
                + "Customer: " + user + "\n"
                + "Flight Booked: " + (fId == null ? "None" : fId) + "\n"
                + "Hotel Booked: " + (hId == null ? "None" : hId) + "\n"
                + "------------------------------------\n"
                + "TOTAL AMOUNT: $" + total + "\n"
                + "====================================";

        JOptionPane.showMessageDialog(this, invoice, "Invoice Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cancelBooking() {
        int selectedRow = tblBookings.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel.");
            return;
        }

        int bookingId = (int) bookingModel.getValueAt(selectedRow, 0);

        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel booking #" + bookingId + "?", "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM bookings WHERE booking_id = ?")) {
                ps.setInt(1, bookingId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Booking cancelled successfully.");
                loadBookings();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error cancelling booking: " + ex.getMessage());
            }
        }
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
        loadFlightsBST();
        loadHotels();
        loadPackagesQuickSort("PRICE");
        loadBookings();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}