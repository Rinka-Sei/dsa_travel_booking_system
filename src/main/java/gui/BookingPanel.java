package gui;

import database.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Panel view for managing customer bookings, itineraries, and printable invoices.
 * Supports booking creation, cancellation, itinerary summaries, and invoice generation.
 */
public class BookingPanel extends JPanel implements ActionListener {

    private JTextField txtBookFlightId;
    private JTextField txtBookHotelId;
    private JButton btnBook;
    private JButton btnItinerary;
    private JButton btnInvoice;
    private JButton btnCancel;
    private JTable tblBookings;
    private DefaultTableModel bookingModel;
    private MainFrame parentFrame;

    /**
     * Constructs the BookingPanel view and initializes its controls and table.
     *
     * @param parent the parent {@link MainFrame} dashboard container
     */
    public BookingPanel(MainFrame parent) {
        this.parentFrame = parent;
        setLayout(null);
        setBounds(0, 0, 790, 540);

        JLabel lblFlightId = new JLabel("Flight ID:");
        lblFlightId.setBounds(10, 10, 70, 25);
        add(lblFlightId);

        txtBookFlightId = new JTextField();
        txtBookFlightId.setBounds(80, 10, 90, 25);
        add(txtBookFlightId);

        JLabel lblHotelId = new JLabel("Hotel ID:");
        lblHotelId.setBounds(180, 10, 70, 25);
        add(lblHotelId);

        txtBookHotelId = new JTextField();
        txtBookHotelId.setBounds(240, 10, 90, 25);
        add(txtBookHotelId);

        btnBook = new JButton("Book Package");
        btnBook.setBounds(340, 10, 120, 25);
        btnBook.addActionListener(this);
        add(btnBook);

        btnItinerary = new JButton("View Itinerary");
        btnItinerary.setBounds(470, 10, 120, 25);
        btnItinerary.addActionListener(this);
        add(btnItinerary);

        btnInvoice = new JButton("Invoice");
        btnInvoice.setBounds(600, 10, 80, 25);
        btnInvoice.addActionListener(this);
        add(btnInvoice);

        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(685, 10, 80, 25);
        btnCancel.addActionListener(this);
        add(btnCancel);

        bookingModel = new DefaultTableModel(new String[]{"Booking ID", "Customer", "Flight ID", "Hotel ID", "Total Price"}, 0);
        tblBookings = new JTable(bookingModel);
        JScrollPane sp = new JScrollPane(tblBookings);
        sp.setBounds(10, 45, 755, 470);
        add(sp);

        loadBookings();
    }

    /**
     * Handles action events for booking creation, itinerary display, invoice generation, and cancellation.
     *
     * @param e the ActionEvent triggered
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBook) {
            performBooking();
        } else if (e.getSource() == btnItinerary) {
            generateItinerary();
        } else if (e.getSource() == btnInvoice) {
            generateInvoice();
        } else if (e.getSource() == btnCancel) {
            cancelBooking();
        }
    }

    /**
     * Creates a new booking in the database based on the provided flight and hotel IDs.
     */
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
            psInsert.setString(1, parentFrame.getCurrentUser());
            psInsert.setString(2, fId.isEmpty() ? null : fId);
            psInsert.setString(3, hId.isEmpty() ? null : hId);
            psInsert.setDouble(4, total);
            psInsert.executeUpdate();

            txtBookFlightId.setText("");
            txtBookHotelId.setText("");

            JOptionPane.showMessageDialog(this, "Booking Successful!");
            loadBookings();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Booking Failed: " + ex.getMessage());
        }
    }

    /**
     * Loads booking records from the database into the JTable.
     * Administrators see all user bookings, whereas customers see only their own bookings.
     */
    public void loadBookings() {
        bookingModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            String role = parentFrame.getCurrentRole();
            String user = parentFrame.getCurrentUser();
            String query = "ADMIN".equals(role) ? "SELECT * FROM bookings" : "SELECT * FROM bookings WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            if (!"ADMIN".equals(role)) {
                ps.setString(1, user);
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

    /**
     * Generates and displays a detailed travel itinerary for the selected booking entry.
     */
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
                            .append("From: ").append(rsF.getString("departure")).append(" (").append(rsF.getString("departure_time")).append(" HRS)\n")
                            .append("To: ").append(rsF.getString("arrival")).append(" (").append(rsF.getString("arrival_time")).append(" HRS)\n\n");
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

    /**
     * Generates and displays a text invoice dialog for the selected booking entry.
     */
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

    /**
     * Cancels the selected booking record from the database upon user confirmation.
     */
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
}
