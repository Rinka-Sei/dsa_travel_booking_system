package model;

/**
 * Represents a combined package deal containing a flight and a hotel.
 * Includes smart destination-matching logic to pair flights and hotels cleanly.
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
    public Flight getFlight() {
        return flight;
    }

    /**
     * Gets the hotel included in this package deal.
     *
     * @return the hotel object
     */
    public Hotel getHotel() {
        return hotel;
    }

    /**
     * Gets the combined total price of the package deal.
     *
     * @return the total price
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Gets the rating of the hotel in this package deal.
     *
     * @return the hotel rating
     */
    public double getHotelRating() {
        return hotel.getRating();
    }

    /**
     * Normalizes a location or destination string by converting to lowercase, stripping parenthetical airport
     * codes/indicators, removing punctuation, and collapsing whitespace.
     *
     * @param destination raw location string (e.g. "Tokyo (NRT)")
     * @return normalized destination string (e.g. "tokyo")
     */
    public static String normalizeDestination(String destination) {
        if (destination == null) {
            return "";
        }
        String normalized = destination.toLowerCase().trim();
        normalized = normalized.replaceAll("\\s*\\(.*?\\)", "");
        normalized = normalized.replaceAll("[^a-z0-9\\s]", " ");
        return normalized.replaceAll("\\s+", " ").trim();
    }

    /**
     * Evaluates whether a flight's arrival destination matches a hotel's location.
     * Handles string normalization and bidirectional matching to ignore airport codes or minor suffix variations.
     *
     * @param flightArrival the flight arrival destination (e.g., "Tokyo (NRT)")
     * @param hotelLocation the hotel location city/region (e.g., "Tokyo")
     * @return {@code true} if destinations match; {@code false} otherwise
     */
    public static boolean isDestinationMatch(String flightArrival, String hotelLocation) {
        String normFlight = normalizeDestination(flightArrival);
        String normHotel = normalizeDestination(hotelLocation);

        if (normFlight.isEmpty() || normHotel.isEmpty()) {
            return false;
        }

        if (normFlight.equals(normHotel)) {
            return true;
        }

        if (normFlight.contains(normHotel) || normHotel.contains(normFlight)) {
            return true;
        }

        String[] flightTokens = normFlight.split(" ");
        String[] hotelTokens = normHotel.split(" ");

        for (String ft : flightTokens) {
            if (ft.length() >= 3) {
                for (String ht : hotelTokens) {
                    if (ht.length() >= 3 && (ft.equals(ht) || ft.contains(ht) || ht.contains(ft))) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
