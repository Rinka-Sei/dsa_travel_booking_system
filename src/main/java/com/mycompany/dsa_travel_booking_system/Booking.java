package com.mycompany.dsa_travel_booking_system;

public class Booking {

    private int bookingId;
    private String username;
    private String flightId;
    private String hotelId;
    private double totalPrice;

    public Booking(int bookingId, String username, String flightId, String hotelId, double totalPrice) {
        this.bookingId = bookingId;
        this.username = username;
        this.flightId = flightId;
        this.hotelId = hotelId;
        this.totalPrice = totalPrice;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getUsername() {
        return username;
    }

    public String getFlightId() {
        return flightId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
