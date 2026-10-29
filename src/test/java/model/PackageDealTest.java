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
        flight = new Flight("FL101", "NYC", "Paris (CDG)", "18:00", 450.0);
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

    @Test
    public void testNormalizeDestination() {
        assertEquals("tokyo", PackageDeal.normalizeDestination("Tokyo (NRT)"));
        assertEquals("tokyo", PackageDeal.normalizeDestination("Tokyo (HND)"));
        assertEquals("paris", PackageDeal.normalizeDestination("  Paris (CDG)  "));
        assertEquals("new york", PackageDeal.normalizeDestination("New York (JFK)"));
        assertEquals("", PackageDeal.normalizeDestination(null));
    }

    @Test
    public void testIsDestinationMatch() {
        assertTrue(PackageDeal.isDestinationMatch("Tokyo (NRT)", "Tokyo"),
                "Flight to 'Tokyo (NRT)' should match Hotel in 'Tokyo'.");
        assertTrue(PackageDeal.isDestinationMatch("Tokyo (HND)", "Tokyo"),
                "Flight to 'Tokyo (HND)' should match Hotel in 'Tokyo'.");
        assertTrue(PackageDeal.isDestinationMatch("Paris (CDG)", "Paris"),
                "Flight to 'Paris (CDG)' should match Hotel in 'Paris'.");
        assertTrue(PackageDeal.isDestinationMatch("New York (JFK)", "New York"),
                "Flight to 'New York (JFK)' should match Hotel in 'New York'.");

        assertFalse(PackageDeal.isDestinationMatch("Tokyo (NRT)", "Paris"),
                "Flight to 'Tokyo (NRT)' should NOT match Hotel in 'Paris'.");
        assertFalse(PackageDeal.isDestinationMatch("London (LHR)", "Rome"),
                "Flight to 'London (LHR)' should NOT match Hotel in 'Rome'.");
    }
}
