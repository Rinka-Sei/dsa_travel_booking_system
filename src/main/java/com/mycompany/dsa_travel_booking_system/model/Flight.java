package com.mycompany.dsa_travel_booking_system.model;

/**
 * Represents a flight entity in the travel booking system.
 */
public class Flight {

    private String flightId;
    private String departure;
    private String arrival;
    private int departureTime; // Format: 1430 for 2:30 PM
    private double price;

    /**
     * Constructs a new Flight with the specified details.
     *
     * @param flightId      the unique identifier for the flight
     * @param departure     the departure location
     * @param arrival       the arrival destination
     * @param departureTime the departure time in HHMM 24-hour format (e.g. 1430)
     * @param price         the price of the flight
     */
    public Flight(String flightId, String departure, String arrival, int departureTime, double price) {
        this.flightId = flightId;
        this.departure = departure;
        this.arrival = arrival;
        this.departureTime = departureTime;
        this.price = price;
    }

    /**
     * Gets the flight ID.
     *
     * @return the flight ID
     */
    public String getFlightId() {
        return flightId;
    }

    /**
     * Gets the departure location.
     *
     * @return the departure location
     */
    public String getDeparture() {
        return departure;
    }

    /**
     * Gets the arrival location.
     *
     * @return the arrival location
     */
    public String getArrival() {
        return arrival;
    }

    /**
     * Gets the departure time.
     *
     * @return the departure time in HHMM format
     */
    public int getDepartureTime() {
        return departureTime;
    }

    /**
     * Gets the price of the flight.
     *
     * @return the flight price
     */
    public double getPrice() {
        return price;
    }
}
