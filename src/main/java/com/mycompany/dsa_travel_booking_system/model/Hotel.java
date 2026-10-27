package com.mycompany.dsa_travel_booking_system.model;

/**
 * Represents a hotel entity in the travel booking system.
 */
public class Hotel {

    private String hotelId;
    private String name;
    private String location;
    private double rating;
    private double pricePerNight;

    /**
     * Constructs a new Hotel with the specified details.
     *
     * @param hotelId       the unique identifier for the hotel
     * @param name          the name of the hotel
     * @param location      the city or region where the hotel is located
     * @param rating        the customer rating of the hotel (e.g. 4.5)
     * @param pricePerNight the room rate per night
     */
    public Hotel(String hotelId, String name, String location, double rating, double pricePerNight) {
        this.hotelId = hotelId;
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.pricePerNight = pricePerNight;
    }

    /**
     * Gets the hotel ID.
     *
     * @return the hotel ID
     */
    public String getHotelId() {
        return hotelId;
    }

    /**
     * Gets the hotel name.
     *
     * @return the hotel name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the hotel location.
     *
     * @return the hotel location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the hotel rating.
     *
     * @return the hotel rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * Gets the price per night for the hotel.
     *
     * @return the price per night
     */
    public double getPricePerNight() {
        return pricePerNight;
    }
}
