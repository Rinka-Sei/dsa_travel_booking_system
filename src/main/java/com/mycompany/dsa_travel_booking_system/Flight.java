package com.mycompany.dsa_travel_booking_system;

public class Flight {

    private String flightId;
    private String departure;
    private String arrival;
    private int departureTime; // Format: 1430 for 2:30 PM
    private double price;

    public Flight(String flightId, String departure, String arrival, int departureTime, double price) {
        this.flightId = flightId;
        this.departure = departure;
        this.arrival = arrival;
        this.departureTime = departureTime;
        this.price = price;
    }

    public String getFlightId() {
        return flightId;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public double getPrice() {
        return price;
    }
}
