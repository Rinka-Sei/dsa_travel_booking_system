package model;

/**
 * Represents a flight entity in the travel booking system.
 */
public class Flight {

    private String flightId;
    private String departure;
    private String arrival;
    private String departureTime; // Format: "14:30" 24-hour military time
    private double price;

    /**
     * Constructs a new Flight with the specified details.
     *
     * @param flightId      the unique identifier for the flight
     * @param departure     the departure location
     * @param arrival       the arrival destination
     * @param departureTime the departure time in strict HH:mm 24-hour military time format (e.g., "14:30")
     * @param price         the price of the flight
     */
    public Flight(String flightId, String departure, String arrival, String departureTime, double price) {
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
     * Gets the departure time in 24-hour military time format ("HH:mm").
     *
     * @return the departure time string (e.g., "14:30")
     */
    public String getDepartureTime() {
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
