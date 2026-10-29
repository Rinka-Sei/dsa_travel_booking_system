package gui;

import database.DBConnection;
import model.Flight;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.List;

/**
 * Panel view for managing and searching flights.
 * Provides role-based administrative controls (Add/Remove Flight) and BST-sorted departure time rendering.
 */
public class FlightPanel extends JPanel implements ActionListener {

    private JTextField txtFlightId;
    private JTextField txtDep;
    private JTextField txtArr;
    private JTextField txtDepTime;
    private JTextField txtFlightPrice;
    private JTextField txtSearchFlight;
    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnSortBST;
    private JTable tblFlights;
    private DefaultTableModel flightModel;
    private MainFrame parentFrame;

    /**
     * Constructs the FlightPanel view and initializes its layout and components.
     *
     * @param parent the parent {@link MainFrame} dashboard container
     */
    public FlightPanel(MainFrame parent) {
        this.parentFrame = parent;
        setLayout(null);
        setBounds(0, 0, 790, 540);

        boolean isAdmin = "ADMIN".equals(parent.getCurrentRole());

        if (isAdmin) {
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

            btnAdd = new JButton("Add Flight");
            btnAdd.setBounds(370, 45, 110, 25);
            btnAdd.addActionListener(this);
            add(btnAdd);

            btnDelete = new JButton("Remove Selected");
            btnDelete.setBounds(490, 45, 150, 25);
            btnDelete.addActionListener(this);
            add(btnDelete);
        }

        btnSortBST = new JButton("Load (BST Sorted)");
        btnSortBST.setBounds(isAdmin ? 650 : 310, isAdmin ? 45 : 10, 150, 25);
        btnSortBST.addActionListener(this);
        add(btnSortBST);

        JLabel lblSearch = new JLabel("Search Destination:");
        lblSearch.setBounds(10, isAdmin ? 85 : 10, 130, 25);
        add(lblSearch);

        txtSearchFlight = new JTextField();
        txtSearchFlight.setBounds(140, isAdmin ? 85 : 10, 150, 25);
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
        sp.setBounds(10, isAdmin ? 120 : 45, 755, isAdmin ? 370 : 450);
        add(sp);

        loadFlightsBST();
    }

    /**
     * Handles action events for adding, deleting, and BST-sorting flight listings.
     *
     * @param e the ActionEvent triggered
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement("INSERT INTO flights VALUES(?, ?, ?, ?, ?)")) {
                ps.setString(1, txtFlightId.getText());
                ps.setString(2, txtDep.getText());
                ps.setString(3, txtArr.getText());
                ps.setInt(4, Integer.parseInt(txtDepTime.getText()));
                ps.setDouble(5, Double.parseDouble(txtFlightPrice.getText()));
                ps.executeUpdate();
                
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
        } else if (e.getSource() == btnDelete) {
            int selectedRow = tblFlights.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a flight to remove.");
                return;
            }
            String flightId = tblFlights.getValueAt(selectedRow, 0).toString();
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM flights WHERE flight_id = ?")) {
                ps.setString(1, flightId);
                ps.executeUpdate();
                loadFlightsBST();
                JOptionPane.showMessageDialog(this, "Flight removed successfully.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error removing flight: " + ex.getMessage());
            }
        } else if (e.getSource() == btnSortBST) {
            loadFlightsBST();
        }
    }

    /**
     * Loads flight records from the database and orders them chronologically using {@code ds.FlightBST} via reflection.
     * Falls back to direct database retrieval if {@code ds.FlightBST} is not available.
     */
    public void loadFlightsBST() {
        flightModel.setRowCount(0);
        boolean loadedViaBST = false;

        try {
            Class<?> bstClass = Class.forName("ds.FlightBST");
            Object bstInstance = bstClass.getDeclaredConstructor().newInstance();
            Method insertMethod = bstClass.getMethod("insert", Flight.class);
            Method getSortedMethod = bstClass.getMethod("getSortedFlights");

            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM flights")) {
                while (rs.next()) {
                    Flight f = new Flight(rs.getString("flight_id"), rs.getString("departure"), rs.getString("arrival"), rs.getInt("departure_time"), rs.getDouble("price"));
                    insertMethod.invoke(bstInstance, f);
                }
            }

            List<Flight> sortedFlights = (List<Flight>) getSortedMethod.invoke(bstInstance);
            for (Flight f : sortedFlights) {
                flightModel.addRow(new Object[]{f.getFlightId(), f.getDeparture(), f.getArrival(), f.getDepartureTime(), f.getPrice()});
            }
            loadedViaBST = true;
        } catch (Exception ignored) {}

        if (!loadedViaBST) {
            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM flights")) {
                while (rs.next()) {
                    flightModel.addRow(new Object[]{rs.getString("flight_id"), rs.getString("departure"), rs.getString("arrival"), rs.getInt("departure_time"), rs.getDouble("price")});
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error loading flights: " + ex.getMessage());
            }
        }
    }

    /**
     * Filters flights by departure or arrival location query.
     *
     * @param query the search substring
     */
    private void filterFlights(String query) {
        flightModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM flights WHERE LOWER(departure) LIKE ? OR LOWER(arrival) LIKE ?")) {
            String searchPattern = "%" + query.toLowerCase() + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                flightModel.addRow(new Object[]{rs.getString("flight_id"), rs.getString("departure"), rs.getString("arrival"), rs.getInt("departure_time"), rs.getDouble("price")});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage());
        }
    }
}
