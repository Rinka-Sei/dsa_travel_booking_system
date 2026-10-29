package com.mycompany.dsa_travel_booking_system.gui;

import com.mycompany.dsa_travel_booking_system.db.DBConnection;
import com.mycompany.dsa_travel_booking_system.ds.FlightBST;
import com.mycompany.dsa_travel_booking_system.model.Flight;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class FlightPanel extends JPanel {
    private JTextField txtFlightId, txtDep, txtArr, txtDepTime, txtFlightPrice, txtSearchFlight;
    private JTable tblFlights;
    private DefaultTableModel flightModel;
    private MainFrame parentFrame;

    public FlightPanel(MainFrame parent) {
        this.parentFrame = parent;
        setLayout(null);
        setBounds(0, 0, 790, 520);

        JLabel lblId = new JLabel("Flight ID:");
        lblId.setBounds(10, 10, 80, 25);
        add(lblId);

        txtFlightId = new JTextField();
        txtFlightId.setBounds(90, 10, 100, 25);
        add(txtFlightId);

        JLabel lblDep = new JLabel("Departure:");
        lblDep.setBounds(200, 10, 80, 25);
        add(lblDep);

        txtDep = new JTextField();
        txtDep.setBounds(280, 10, 100, 25);
        add(txtDep);

        JLabel lblArr = new JLabel("Arrival:");
        lblArr.setBounds(390, 10, 80, 25);
        add(lblArr);

        txtArr = new JTextField();
        txtArr.setBounds(460, 10, 100, 25);
        add(txtArr);

        JLabel lblTime = new JLabel("Time (HHMM):");
        lblTime.setBounds(10, 45, 90, 25);
        add(lblTime);

        txtDepTime = new JTextField();
        txtDepTime.setBounds(100, 45, 90, 25);
        add(txtDepTime);

        JLabel lblPrice = new JLabel("Price:");
        lblPrice.setBounds(200, 45, 50, 25);
        add(lblPrice);

        txtFlightPrice = new JTextField();
        txtFlightPrice.setBounds(250, 45, 100, 25);
        add(txtFlightPrice);

        JButton btnAdd = new JButton("Add Flight");
        btnAdd.setBounds(370, 45, 110, 25);
        add(btnAdd);

        JButton btnSortBST = new JButton("Load (BST Sorted)");
        btnSortBST.setBounds(490, 45, 150, 25);
        add(btnSortBST);

        JLabel lblSearch = new JLabel("Search Destination:");
        lblSearch.setBounds(10, 85, 130, 25);
        add(lblSearch);

        txtSearchFlight = new JTextField();
        txtSearchFlight.setBounds(140, 85, 150, 25);
        add(txtSearchFlight);

        txtSearchFlight.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterFlights(txtSearchFlight.getText().trim());
            }
        });

        flightModel = new DefaultTableModel(new String[]{"ID", "Departure", "Arrival", "Time", "Price"}, 0);
        tblFlights = new JTable(flightModel);
        JScrollPane sp = new JScrollPane(tblFlights);
        sp.setBounds(10, 120, 755, 360);
        add(sp);

        btnAdd.addActionListener(e -> {
            if (!"ADMIN".equals(parentFrame.getCurrentRole())) {
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
                
                // Clear text fields after successful insertion
                txtFlightId.setText("");
                txtDep.setText("");
                txtArr.setText("");
                txtDepTime.setText("");
                txtFlightPrice.setText("");

                loadFlightsBST();
                JOptionPane.showMessageDialog(this, "Flight added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        btnSortBST.addActionListener(e -> loadFlightsBST());
        loadFlightsBST();
    }

    public void loadFlightsBST() {
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
}