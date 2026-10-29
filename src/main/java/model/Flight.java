package model;

/**
 * Represents a flight entity in the travel booking system.
 * Stores flight route details along with paired 24-hour military departure and arrival times ("HH:mm").
 */
public class Flight {

    private String flightId;
    private String departure;
    private String departureTime; // Format: "08:30" 24-hour military time
    private String arrival;
    private String arrivalTime;   // Format: "11:45" 24-hour military time
    private double price;

    /**
     * Constructs a new Flight with paired location and military time details.
     *
     * @param flightId      the unique identifier for the flight
     * @param departure     the departure location
     * @param departureTime the departure time in strict HH:mm 24-hour military time format (e.g., "08:30")
     * @param arrival       the arrival destination
     * @param arrivalTime   the arrival time in strict HH:mm 24-hour military time format (e.g., "11:45")
     * @param price         the price of the flight
     */
    public Flight(String flightId, String departure, String departureTime, String arrival, String arrivalTime, double price) {
        this.flightId = flightId;
        this.departure = departure;
        this.departureTime = departureTime;
        this.arrival = arrival;
        this.arrivalTime = arrivalTime;
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
     * Gets the departure time in 24-hour military time format ("HH:mm").
     *
     * @return the departure time string (e.g., "08:30")
     */
    public String getDepartureTime() {
        return departureTime;
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
     * Gets the arrival time in 24-hour military time format ("HH:mm").
     *
     * @return the arrival time string (e.g., "11:45")
     */
    public String getArrivalTime() {
        return arrivalTime;
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
