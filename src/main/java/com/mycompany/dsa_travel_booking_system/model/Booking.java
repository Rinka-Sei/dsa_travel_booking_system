package com.mycompany.dsa_travel_booking_system.model;

/**
 * Represents a booking entry made by a user for a flight and/or hotel.
 */
public class Booking {

    private int bookingId;
    private String username;
    private String flightId;
    private String hotelId;
    private double totalPrice;

    /**
     * Constructs a new Booking record.
     *
     * @param bookingId  the unique identifier of the booking
     * @param username   the username of the customer who created the booking
     * @param flightId   the ID of the booked flight (may be null if only hotel booked)
     * @param hotelId    the ID of the booked hotel (may be null if only flight booked)
     * @param totalPrice the total cost of the booking
     */
    public Booking(int bookingId, String username, String flightId, String hotelId, double totalPrice) {
        this.bookingId = bookingId;
        this.username = username;
        this.flightId = flightId;
        this.hotelId = hotelId;
        this.totalPrice = totalPrice;
    }

    /**
     * Gets the booking ID.
     *
     * @return the booking ID
     */
    public int getBookingId() {
        return bookingId;
    }

    /**
     * Gets the username associated with the booking.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the flight ID of the booking.
     *
     * @return the flight ID
     */
    public String getFlightId() {
        return flightId;
    }

    /**
     * Gets the hotel ID of the booking.
     *
     * @return the hotel ID
     */
    public String getHotelId() {
        return hotelId;
    }

    /**
     * Gets the total price of the booking.
     *
     * @return the total price
     */
    public double getTotalPrice() {
        return totalPrice;
    }
}
