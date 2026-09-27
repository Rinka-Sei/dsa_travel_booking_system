package com.mycompany.dsa_travel_booking_system;

public class PackageDeal {
    private Flight flight;
    private Hotel hotel;
    private double totalPrice;

    public PackageDeal(Flight flight, Hotel hotel) {
        this.flight = flight;
        this.hotel = hotel;
        this.totalPrice = flight.getPrice() + hotel.getPricePerNight();
    }

    public Flight getFlight() { return flight; }
    public Hotel getHotel() { return hotel; }
    public double getTotalPrice() { return totalPrice; }
    public double getHotelRating() { return hotel.getRating(); } // Feature metric
}
