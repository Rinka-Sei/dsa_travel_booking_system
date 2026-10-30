package gui;

import database.DBConnection;
import model.Flight;
import model.Hotel;
import model.PackageDeal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel view for browsing and booking dynamic flight-hotel package deals.
 * Features smart destination-matching filter and toggleable QuickSort buttons with directional arrow indicators.
 */
public class PackagePanel extends JPanel implements ActionListener {

    private JButton btnSortPrice;
    private JButton btnSortRating;
    private JButton btnBookPackage;
    private JTable tblPackages;
    private DefaultTableModel packageModel;
    private MainFrame parentFrame;

    private boolean priceAscending = true;
    private boolean ratingAscending = false;
    private String activeSortMode = "PRICE";

    /**
     * Constructs the PackagePanel view and initializes its controls and table layout.
     *
     * @param parent the parent {@link MainFrame} dashboard container
     */
    public PackagePanel(MainFrame parent) {
        this.parentFrame = parent;
        setLayout(null);
        setBounds(0, 0, 900, 580);

        btnSortPrice = new JButton("Sort Price (Ascending ↑)");
        btnSortPrice.setBounds(10, 10, 200, 25);
        btnSortPrice.addActionListener(this);
        add(btnSortPrice);

        btnSortRating = new JButton("Sort Rating");
        btnSortRating.setBounds(220, 10, 200, 25);
        btnSortRating.addActionListener(this);
        add(btnSortRating);

        btnBookPackage = new JButton("Book Selected Package");
        btnBookPackage.setBounds(430, 10, 200, 25);
        btnBookPackage.addActionListener(this);
        add(btnBookPackage);

        packageModel = new DefaultTableModel(new String[]{"Flight ID", "Flight Price", "Hotel ID", "Hotel Name", "Rating", "Total Price"}, 0);
        tblPackages = new JTable(packageModel);
        JScrollPane sp = new JScrollPane(tblPackages);
        sp.setBounds(10, 45, 875, 520);
        add(sp);

        loadPackagesQuickSort("PRICE", priceAscending);
    }

    /**
     * Handles action events for price sorting, rating sorting, and package booking.
     * Toggles directional arrow indicators exclusively on the active sort button.
     *
     * @param e the ActionEvent triggered
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSortPrice) {
            if (!"PRICE".equals(activeSortMode)) {
                activeSortMode = "PRICE";
                priceAscending = true;
            } else {
                priceAscending = !priceAscending;
            }
            btnSortPrice.setText("Sort Price (" + (priceAscending ? "Ascending ↑" : "Descending ↓") + ")");
            btnSortRating.setText("Sort Rating");
            loadPackagesQuickSort("PRICE", priceAscending);

        } else if (e.getSource() == btnSortRating) {
            if (!"RATING".equals(activeSortMode)) {
                activeSortMode = "RATING";
                ratingAscending = false; // Default descending (higher rating first)
            } else {
                ratingAscending = !ratingAscending;
            }
            btnSortRating.setText("Sort Rating (" + (ratingAscending ? "Ascending ↑" : "Descending ↓") + ")");
            btnSortPrice.setText("Sort Price");
            loadPackagesQuickSort("RATING", ratingAscending);

        } else if (e.getSource() == btnBookPackage) {
            bookSelectedPackage();
        }
    }

    /**
     * Persists a booking for the currently selected package deal row in the table.
     */
    private void bookSelectedPackage() {
        int selectedRow = tblPackages.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a package deal from the table.");
            return;
        }

        String fId = tblPackages.getValueAt(selectedRow, 0).toString();
        String hId = tblPackages.getValueAt(selectedRow, 2).toString();
        double totalPrice = Double.parseDouble(tblPackages.getValueAt(selectedRow, 5).toString());

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO bookings(username, flight_id, hotel_id, total_price) VALUES(?, ?, ?, ?)")) {
            ps.setString(1, parentFrame.getCurrentUser());
            ps.setString(2, fId);
            ps.setString(3, hId);
            ps.setDouble(4, totalPrice);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Package Booked Successfully!");
            parentFrame.refreshBookingPanel();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Booking Failed: " + ex.getMessage());
        }
    }

    /**
     * Fetches available flight and hotel records and constructs PackageDeal pairs strictly when
     * flight arrival destination matches hotel location using smart string normalization.
     *
     * @return a {@link List} of destination-matched {@link PackageDeal} objects
     */
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
                        rsF.getString("departure_time"),
                        rsF.getString("arrival"),
                        rsF.getString("arrival_time"),
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
                    if (PackageDeal.isDestinationMatch(f.getArrival(), h.getLocation())) {
                        packages.add(new PackageDeal(f, h));
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching packages: " + ex.getMessage());
        }
        return packages;
    }

    /**
     * Re-fetches all matching packages and re-renders them according to the currently active sort mode.
     */
    public void refreshPackages() {
        if ("RATING".equals(activeSortMode)) {
            loadPackagesQuickSort("RATING", ratingAscending);
        } else {
            loadPackagesQuickSort("PRICE", priceAscending);
        }
    }

    /**
     * Loads package deals and sorts them using {@code ds.QuickSort} via reflection.
     *
     * @param mode      the sorting criterion ("PRICE" or "RATING")
     * @param ascending {@code true} for ascending order, {@code false} for descending order
     */
    public void loadPackagesQuickSort(String mode, boolean ascending) {
        packageModel.setRowCount(0);
        List<PackageDeal> packages = fetchAllPackages();

        try {
            Class<?> quickSortClass = Class.forName("ds.QuickSort");
            if (!packages.isEmpty()) {
                if ("PRICE".equals(mode)) {
                    try {
                        Method sortPriceMethod = quickSortClass.getMethod("sortByPrice", List.class, int.class, int.class, boolean.class);
                        sortPriceMethod.invoke(null, packages, 0, packages.size() - 1, ascending);
                    } catch (NoSuchMethodException e) {
                        Method sortPriceMethod = quickSortClass.getMethod("sortByPrice", List.class, int.class, int.class);
                        sortPriceMethod.invoke(null, packages, 0, packages.size() - 1);
                    }
                } else if ("RATING".equals(mode)) {
                    try {
                        Method sortRatingMethod = quickSortClass.getMethod("sortByRating", List.class, int.class, int.class, boolean.class);
                        sortRatingMethod.invoke(null, packages, 0, packages.size() - 1, ascending);
                    } catch (NoSuchMethodException e) {
                        Method sortRatingMethod = quickSortClass.getMethod("sortByRating", List.class, int.class, int.class);
                        sortRatingMethod.invoke(null, packages, 0, packages.size() - 1);
                    }
                }
            }
        } catch (Exception ignored) {}

        for (PackageDeal pkg : packages) {
            packageModel.addRow(new Object[]{
                    pkg.getFlight().getFlightId(),
                    pkg.getFlight().getPrice(),
                    pkg.getHotel().getHotelId(),
                    pkg.getHotel().getName(),
                    pkg.getHotelRating(),
                    pkg.getTotalPrice()
            });
        }
    }
}
