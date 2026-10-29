package model;

/**
 * Represents a combined package deal containing a flight and a hotel.
 */
public class PackageDeal {
    private Flight flight;
    private Hotel hotel;
    private double totalPrice;

    /**
     * Constructs a new PackageDeal combining a flight and a hotel.
     *
     * @param flight the flight component of the package deal
     * @param hotel  the hotel component of the package deal
     */
    public PackageDeal(Flight flight, Hotel hotel) {
        this.flight = flight;
        this.hotel = hotel;
        this.totalPrice = flight.getPrice() + hotel.getPricePerNight();
    }

    /**
     * Gets the flight included in this package deal.
     *
     * @return the flight object
     */
    public Flight getFlight() { return flight; }

    /**
     * Gets the hotel included in this package deal.
     *
     * @return the hotel object
     */
    public Hotel getHotel() { return hotel; }

    /**
     * Gets the combined total price of the package deal.
     *
     * @return the total price
     */
    public double getTotalPrice() { return totalPrice; }

    /**
     * Gets the rating of the hotel in this package deal.
     *
     * @return the hotel rating
     */
    public double getHotelRating() { return hotel.getRating(); } // Feature metric
}
