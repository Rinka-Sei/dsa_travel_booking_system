package gui;

import database.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class HotelPanel extends JPanel implements ActionListener {
    private JTextField txtHotelId, txtHotelName, txtLocation, txtRating, txtHotelPrice, txtSearchHotel;
    private JButton btnAdd, btnDelete;
    private JTable tblHotels;
    private DefaultTableModel hotelModel;
    private MainFrame parentFrame;

    public HotelPanel(MainFrame parent) {
        this.parentFrame = parent;
        setLayout(null);
        setBounds(0, 0, 790, 540);

        boolean isAdmin = "ADMIN".equals(parent.getCurrentRole());

        if (isAdmin) {
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

            btnAdd = new JButton("Add Hotel");
            btnAdd.setBounds(290, 45, 100, 25);
            btnAdd.addActionListener(this);
            add(btnAdd);

            btnDelete = new JButton("Remove Selected");
            btnDelete.setBounds(400, 45, 140, 25);
            btnDelete.addActionListener(this);
            add(btnDelete);
        }

        JLabel lblSearch = new JLabel("Search Location:");
        lblSearch.setBounds(10, isAdmin ? 85 : 10, 120, 25);
        add(lblSearch);

        txtSearchHotel = new JTextField();
        txtSearchHotel.setBounds(130, isAdmin ? 85 : 10, 150, 25);
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
        sp.setBounds(10, isAdmin ? 120 : 45, 755, isAdmin ? 370 : 450);
        add(sp);

        loadHotels();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement("INSERT INTO hotels VALUES(?, ?, ?, ?, ?)")) {
                ps.setString(1, txtHotelId.getText());
                ps.setString(2, txtHotelName.getText());
                ps.setString(3, txtLocation.getText());
                ps.setDouble(4, Double.parseDouble(txtRating.getText()));
                ps.setDouble(5, Double.parseDouble(txtHotelPrice.getText()));
                ps.executeUpdate();
                
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
        } else if (e.getSource() == btnDelete) {
            int selectedRow = tblHotels.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a hotel to remove.");
                return;
            }
            String hotelId = tblHotels.getValueAt(selectedRow, 0).toString();
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM hotels WHERE hotel_id = ?")) {
                ps.setString(1, hotelId);
                ps.executeUpdate();
                loadHotels();
                JOptionPane.showMessageDialog(this, "Hotel removed successfully.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error removing hotel: " + ex.getMessage());
            }
        }
    }

    public void loadHotels() {
        hotelModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM hotels")) {
            while (rs.next()) {
                hotelModel.addRow(new Object[]{rs.getString("hotel_id"), rs.getString("name"), rs.getString("location"), rs.getDouble("rating"), rs.getDouble("price_per_night")});
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
                hotelModel.addRow(new Object[]{rs.getString("hotel_id"), rs.getString("name"), rs.getString("location"), rs.getDouble("rating"), rs.getDouble("price_per_night")});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage());
        }
    }
}