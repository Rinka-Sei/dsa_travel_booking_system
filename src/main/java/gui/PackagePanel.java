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
 * Utilizes {@code ds.QuickSort} via Java Reflection for sorting packages by price or hotel rating.
 */
public class PackagePanel extends JPanel implements ActionListener {

    private JButton btnSortPrice;
    private JButton btnSortRating;
    private JButton btnBookPackage;
    private JTable tblPackages;
    private DefaultTableModel packageModel;
    private MainFrame parentFrame;

    /**
     * Constructs the PackagePanel view and initializes its controls and table layout.
     *
     * @param parent the parent {@link MainFrame} dashboard container
     */
    public PackagePanel(MainFrame parent) {
        this.parentFrame = parent;
        setLayout(null);
        setBounds(0, 0, 790, 540);

        btnSortPrice = new JButton("Sort Price");
        btnSortPrice.setBounds(10, 10, 120, 25);
        btnSortPrice.addActionListener(this);
        add(btnSortPrice);

        btnSortRating = new JButton("Sort Rating");
        btnSortRating.setBounds(140, 10, 120, 25);
        btnSortRating.addActionListener(this);
        add(btnSortRating);

        btnBookPackage = new JButton("Book Selected Package");
        btnBookPackage.setBounds(270, 10, 180, 25);
        btnBookPackage.addActionListener(this);
        add(btnBookPackage);

        packageModel = new DefaultTableModel(new String[]{"Flight ID", "Flight Price", "Hotel ID", "Hotel Name", "Rating", "Total Price"}, 0);
        tblPackages = new JTable(packageModel);
        JScrollPane sp = new JScrollPane(tblPackages);
        sp.setBounds(10, 45, 755, 470);
        add(sp);

        loadPackagesQuickSort("PRICE");
    }

    /**
     * Handles action events for price sorting, rating sorting, and package booking.
     *
     * @param e the ActionEvent triggered
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSortPrice) {
            loadPackagesQuickSort("PRICE");
        } else if (e.getSource() == btnSortRating) {
            loadPackagesQuickSort("RATING");
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
     * Fetches all available flight and hotel records and computes Cartesian product package combinations.
     *
     * @return a {@link List} of combined {@link PackageDeal} objects
     */
    private List<PackageDeal> fetchAllPackages() {
        List<PackageDeal> packages = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt1 = conn.createStatement();
             ResultSet rsF = stmt1.executeQuery("SELECT * FROM flights")) {

            List<Flight> flights = new ArrayList<>();
            while (rsF.next()) {
                flights.add(new Flight(rsF.getString("flight_id"), rsF.getString("departure"), rsF.getString("arrival"), rsF.getInt("departure_time"), rsF.getDouble("price")));
            }

            Statement stmt2 = conn.createStatement();
            ResultSet rsH = stmt2.executeQuery("SELECT * FROM hotels");
            List<Hotel> hotels = new ArrayList<>();
            while (rsH.next()) {
                hotels.add(new Hotel(rsH.getString("hotel_id"), rsH.getString("name"), rsH.getString("location"), rsH.getDouble("rating"), rsH.getDouble("price_per_night")));
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

    /**
     * Loads package deals and sorts them using {@code ds.QuickSort} via reflection.
     *
     * @param mode the sorting criterion ("PRICE" for ascending price, "RATING" for descending rating)
     */
    public void loadPackagesQuickSort(String mode) {
        packageModel.setRowCount(0);
        List<PackageDeal> packages = fetchAllPackages();

        try {
            Class<?> quickSortClass = Class.forName("ds.QuickSort");
            if (!packages.isEmpty()) {
                if ("PRICE".equals(mode)) {
                    Method sortPriceMethod = quickSortClass.getMethod("sortByPrice", List.class, int.class, int.class);
                    sortPriceMethod.invoke(null, packages, 0, packages.size() - 1);
                } else if ("RATING".equals(mode)) {
                    Method sortRatingMethod = quickSortClass.getMethod("sortByRating", List.class, int.class, int.class);
                    sortRatingMethod.invoke(null, packages, 0, packages.size() - 1);
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
