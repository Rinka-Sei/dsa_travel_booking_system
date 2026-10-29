package com.mycompany.dsa_travel_booking_system.gui;

import com.mycompany.dsa_travel_booking_system.db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class HotelPanel extends JPanel {
    private JTextField txtHotelId, txtHotelName, txtLocation, txtRating, txtHotelPrice, txtSearchHotel;
    private JTable tblHotels;
    private DefaultTableModel hotelModel;
    private MainFrame parentFrame;

    public HotelPanel(MainFrame parent) {
        this.parentFrame = parent;
        setLayout(null);
        setBounds(0, 0, 790, 520);

        JLabel lblId = new JLabel("Hotel ID:");
        lblId.setBounds(10, 10, 70, 25);
        add(lblId);

        txtHotelId = new JTextField();
        txtHotelId.setBounds(80, 10, 80, 25);
        add(txtHotelId);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(170, 10, 50, 25);
        add(lblName);

        txtHotelName = new JTextField();
        txtHotelName.setBounds(220, 10, 120, 25);
        add(txtHotelName);

        JLabel lblLoc = new JLabel("Location:");
        lblLoc.setBounds(350, 10, 60, 25);
        add(lblLoc);

        txtLocation = new JTextField();
        txtLocation.setBounds(410, 10, 100, 25);
        add(txtLocation);

        JLabel lblRat = new JLabel("Rating:");
        lblRat.setBounds(10, 45, 50, 25);
        add(lblRat);

        txtRating = new JTextField();
        txtRating.setBounds(60, 45, 50, 25);
        add(txtRating);

        JLabel lblPrice = new JLabel("Price/Night:");
        lblPrice.setBounds(120, 45, 80, 25);
        add(lblPrice);

        txtHotelPrice = new JTextField();
        txtHotelPrice.setBounds(200, 45, 80, 25);
        add(txtHotelPrice);

        JButton btnAdd = new JButton("Add Hotel");
        btnAdd.setBounds(290, 45, 100, 25);
        add(btnAdd);

        JLabel lblSearch = new JLabel("Search Location:");
        lblSearch.setBounds(10, 85, 120, 25);
        add(lblSearch);

        txtSearchHotel = new JTextField();
        txtSearchHotel.setBounds(130, 85, 150, 25);
        add(txtSearchHotel);

        txtSearchHotel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterHotels(txtSearchHotel.getText().trim());
            }
        });

        hotelModel = new DefaultTableModel(new String[]{"ID", "Name", "Location", "Rating", "Price/Night"}, 0);
        tblHotels = new JTable(hotelModel);
        JScrollPane sp = new JScrollPane(tblHotels);
        sp.setBounds(10, 120, 755, 360);
        add(sp);

        btnAdd.addActionListener(e -> {
            if (!"ADMIN".equals(parentFrame.getCurrentRole())) {
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
                
                // Clear text fields after successful insertion
                txtHotelId.setText("");
                txtHotelName.setText("");
                txtLocation.setText("");
                txtRating.setText("");
                txtHotelPrice.setText("");

                loadHotels();
                JOptionPane.showMessageDialog(this, "Hotel added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        loadHotels();
    }

    public void loadHotels() {
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
}