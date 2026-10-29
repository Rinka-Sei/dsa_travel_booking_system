package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PackageDeal}.
 */
public class PackageDealTest {

    private Flight flight;
    private Hotel hotel;
    private PackageDeal packageDeal;

    @BeforeEach
    public void setUp() {
        flight = new Flight("FL101", "NYC", "PAR", 1800, 450.0);
        hotel = new Hotel("H202", "Le Hotel", "Paris", 4.7, 150.0);
        packageDeal = new PackageDeal(flight, hotel);
    }

    @Test
    public void testGetFlightAndHotel() {
        assertEquals(flight, packageDeal.getFlight(), "Flight reference should match constructor argument.");
        assertEquals(hotel, packageDeal.getHotel(), "Hotel reference should match constructor argument.");
    }

    @Test
    public void testTotalPriceCalculation() {
        double expectedPrice = 450.0 + 150.0;
        assertEquals(expectedPrice, packageDeal.getTotalPrice(), 0.001,
                "Total package price should equal flight price plus hotel price per night.");
    }

    @Test
    public void testHotelRatingAggregation() {
        assertEquals(4.7, packageDeal.getHotelRating(), 0.001,
                "Hotel rating should equal the aggregated hotel rating.");
    }
}
