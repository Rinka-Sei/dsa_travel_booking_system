package com.mycompany.dsa_travel_booking_system.gui;

import com.mycompany.dsa_travel_booking_system.db.DBConnection;
import com.mycompany.dsa_travel_booking_system.ds.QuickSort;
import com.mycompany.dsa_travel_booking_system.model.Flight;
import com.mycompany.dsa_travel_booking_system.model.Hotel;
import com.mycompany.dsa_travel_booking_system.model.PackageDeal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PackagePanel extends JPanel {
    private JTable tblPackages;
    private DefaultTableModel packageModel;

    public PackagePanel() {
        setLayout(null);
        setBounds(0, 0, 790, 520);

        JButton btnSortPrice = new JButton("QuickSort by Price (Low to High)");
        btnSortPrice.setBounds(10, 10, 240, 25);
        add(btnSortPrice);

        JButton btnSortRating = new JButton("QuickSort by Feature (Rating)");
        btnSortRating.setBounds(260, 10, 230, 25);
        add(btnSortRating);

        packageModel = new DefaultTableModel(new String[]{"Flight ID", "Flight Price", "Hotel Name", "Hotel Rating", "Total Package Price"}, 0);
        tblPackages = new JTable(packageModel);
        JScrollPane sp = new JScrollPane(tblPackages);
        sp.setBounds(10, 45, 755, 460);
        add(sp);

        btnSortPrice.addActionListener(e -> loadPackagesQuickSort("PRICE"));
        btnSortRating.addActionListener(e -> loadPackagesQuickSort("RATING"));

        loadPackagesQuickSort("PRICE");
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

    public void loadPackagesQuickSort(String mode) {
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
}