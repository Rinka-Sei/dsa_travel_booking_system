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
 * Provides role-based administrative controls (Add/Remove Flight) with paired departure and arrival location/time inputs,
 * strict HH:mm 24-hour military time regex validation, and BST-sorted departure time rendering.
 */
public class FlightPanel extends JPanel implements ActionListener {

    private JTextField txtFlightId;
    private JTextField txtDep;
    private JTextField txtDepTime;
    private JTextField txtArr;
    private JTextField txtArrTime;
    private JTextField txtFlightPrice;
    private JTextField txtSearchFlight;
    private JButton btnAdd;
    private JButton btnUpdate;
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
        setBounds(0, 0, 900, 580);

        boolean isAdmin = "ADMIN".equals(parent.getCurrentRole());

        if (isAdmin) {
            JLabel lblId = new JLabel("ID:");
            lblId.setBounds(10, 10, 25, 25);
            add(lblId);

            txtFlightId = new JTextField();
            txtFlightId.setBounds(35, 10, 60, 25);
            add(txtFlightId);

            JLabel lblDep = new JLabel("Departure:");
            lblDep.setBounds(100, 10, 70, 25);
            add(lblDep);

            txtDep = new JTextField();
            txtDep.setBounds(170, 10, 85, 25);
            add(txtDep);

            JLabel lblDepTime = new JLabel("Dep Time:");
            lblDepTime.setBounds(260, 10, 65, 25);
            add(lblDepTime);

            txtDepTime = new JTextField();
            txtDepTime.setBounds(325, 10, 55, 25);
            add(txtDepTime);

            JLabel lblArr = new JLabel("Arrival:");
            lblArr.setBounds(385, 10, 50, 25);
            add(lblArr);

            txtArr = new JTextField();
            txtArr.setBounds(435, 10, 85, 25);
            add(txtArr);

            JLabel lblArrTime = new JLabel("Arr Time:");
            lblArrTime.setBounds(525, 10, 60, 25);
            add(lblArrTime);

            txtArrTime = new JTextField();
            txtArrTime.setBounds(585, 10, 55, 25);
            add(txtArrTime);

            JLabel lblPrice = new JLabel("Price:");
            lblPrice.setBounds(645, 10, 40, 25);
            add(lblPrice);

            txtFlightPrice = new JTextField();
            txtFlightPrice.setBounds(685, 10, 80, 25);
            add(txtFlightPrice);

            btnAdd = new JButton("Add Flight");
            btnAdd.setBounds(10, 45, 110, 25);
            btnAdd.addActionListener(this);
            add(btnAdd);

            btnUpdate = new JButton("Update Selected");
            btnUpdate.setBounds(125, 45, 135, 25);
            btnUpdate.addActionListener(this);
            add(btnUpdate);

            btnDelete = new JButton("Remove Selected");
            btnDelete.setBounds(265, 45, 140, 25);
            btnDelete.addActionListener(this);
            add(btnDelete);
        }

        btnSortBST = new JButton("Reset & Sort (BST)");
        btnSortBST.setBounds(isAdmin ? 720 : 310, isAdmin ? 45 : 10, 155, 25);
        btnSortBST.addActionListener(this);
        add(btnSortBST);

        JLabel lblSearch = new JLabel("Search Destination:");
        lblSearch.setBounds(10, isAdmin ? 80 : 10, 130, 25);
        add(lblSearch);

        txtSearchFlight = new JTextField();
        txtSearchFlight.setBounds(140, isAdmin ? 80 : 10, 150, 25);
        add(txtSearchFlight);

        txtSearchFlight.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterFlights(txtSearchFlight.getText().trim());
            }
        });

        flightModel = new DefaultTableModel(new String[]{"ID", "Departure", "Dep Time", "Arrival", "Arr Time", "Price"}, 0);
        tblFlights = new JTable(flightModel);
        if (isAdmin) {
            tblFlights.getSelectionModel().addListSelectionListener(event -> {
                if (!event.getValueIsAdjusting() && tblFlights.getSelectedRow() != -1) {
                    int r = tblFlights.getSelectedRow();
                    txtFlightId.setText(tblFlights.getValueAt(r, 0).toString());
                    txtDep.setText(tblFlights.getValueAt(r, 1).toString());
                    txtDepTime.setText(tblFlights.getValueAt(r, 2).toString());
                    txtArr.setText(tblFlights.getValueAt(r, 3).toString());
                    txtArrTime.setText(tblFlights.getValueAt(r, 4).toString());
                    txtFlightPrice.setText(tblFlights.getValueAt(r, 5).toString());
                }
            });
        }
        JScrollPane sp = new JScrollPane(tblFlights);
        sp.setBounds(10, isAdmin ? 115 : 45, 875, isAdmin ? 430 : 500);
        add(sp);

        loadFlightsBST();
    }

    /**
     * Handles action events for adding, deleting, and BST-sorting flight listings.
     * Validates strict HH:mm 24-hour military time input format for both departure and arrival times before saving.
     *
     * @param e the ActionEvent triggered
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            String depTimeInput = txtDepTime.getText().trim();
            String arrTimeInput = txtArrTime.getText().trim();
            String timeRegex = "^([01]\\d|2[0-3]):([0-5]\\d)$";

            if (!depTimeInput.matches(timeRegex) || !arrTimeInput.matches(timeRegex)) {
                JOptionPane.showMessageDialog(this,
                        "Invalid Time format! Both Departure and Arrival times must use 24-hour military time (HH:mm, e.g., 08:30 or 14:45).",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement("INSERT INTO flights(flight_id, departure, departure_time, arrival, arrival_time, price) VALUES(?, ?, ?, ?, ?, ?)")) {
                ps.setString(1, txtFlightId.getText().trim());
                ps.setString(2, txtDep.getText().trim());
                ps.setString(3, depTimeInput);
                ps.setString(4, txtArr.getText().trim());
                ps.setString(5, arrTimeInput);
                ps.setDouble(6, Double.parseDouble(txtFlightPrice.getText().trim()));
                ps.executeUpdate();
                
                txtFlightId.setText("");
                txtDep.setText("");
                txtDepTime.setText("");
                txtArr.setText("");
                txtArrTime.setText("");
                txtFlightPrice.setText("");

                loadFlightsBST();
                if (parentFrame != null) {
                    parentFrame.refreshPackagePanel();
                }
                JOptionPane.showMessageDialog(this, "Flight added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        } else if (e.getSource() == btnUpdate) {
            int selectedRow = tblFlights.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a flight from the table to update.");
                return;
            }
            String depTimeInput = txtDepTime.getText().trim();
            String arrTimeInput = txtArrTime.getText().trim();
            String timeRegex = "^([01]\\d|2[0-3]):([0-5]\\d)$";

            if (!depTimeInput.matches(timeRegex) || !arrTimeInput.matches(timeRegex)) {
                JOptionPane.showMessageDialog(this,
                        "Invalid Time format! Both Departure and Arrival times must use 24-hour military time (HH:mm, e.g., 08:30 or 14:45).",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement("UPDATE flights SET departure = ?, departure_time = ?, arrival = ?, arrival_time = ?, price = ? WHERE flight_id = ?")) {
                ps.setString(1, txtDep.getText().trim());
                ps.setString(2, depTimeInput);
                ps.setString(3, txtArr.getText().trim());
                ps.setString(4, arrTimeInput);
                ps.setDouble(5, Double.parseDouble(txtFlightPrice.getText().trim()));
                ps.setString(6, txtFlightId.getText().trim());
                ps.executeUpdate();

                loadFlightsBST();
                if (parentFrame != null) {
                    parentFrame.refreshPackagePanel();
                }
                JOptionPane.showMessageDialog(this, "Flight updated successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating flight: " + ex.getMessage());
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
                if (parentFrame != null) {
                    parentFrame.refreshPackagePanel();
                }
                JOptionPane.showMessageDialog(this, "Flight removed successfully.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error removing flight: " + ex.getMessage());
            }
        } else if (e.getSource() == btnSortBST) {
            txtSearchFlight.setText("");
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
                    Flight f = new Flight(
                            rs.getString("flight_id"),
                            rs.getString("departure"),
                            rs.getString("departure_time"),
                            rs.getString("arrival"),
                            rs.getString("arrival_time"),
                            rs.getDouble("price")
                    );
                    insertMethod.invoke(bstInstance, f);
                }
            }

            List<Flight> sortedFlights = (List<Flight>) getSortedMethod.invoke(bstInstance);
            for (Flight f : sortedFlights) {
                flightModel.addRow(new Object[]{
                        f.getFlightId(),
                        f.getDeparture(),
                        f.getDepartureTime(),
                        f.getArrival(),
                        f.getArrivalTime(),
                        f.getPrice()
                });
            }
            loadedViaBST = true;
        } catch (Exception ignored) {}

        if (!loadedViaBST) {
            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM flights")) {
                while (rs.next()) {
                    flightModel.addRow(new Object[]{
                            rs.getString("flight_id"),
                            rs.getString("departure"),
                            rs.getString("departure_time"),
                            rs.getString("arrival"),
                            rs.getString("arrival_time"),
                            rs.getDouble("price")
                    });
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
                flightModel.addRow(new Object[]{
                        rs.getString("flight_id"),
                        rs.getString("departure"),
                        rs.getString("departure_time"),
                        rs.getString("arrival"),
                        rs.getString("arrival_time"),
                        rs.getDouble("price")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage());
        }
    }
}
