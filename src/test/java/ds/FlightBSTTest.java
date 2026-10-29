package ds;

import model.Flight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link FlightBST}.
 */
public class FlightBSTTest {

    private FlightBST flightBST;

    @BeforeEach
    public void setUp() {
        flightBST = new FlightBST();
    }

    @Test
    public void testEmptyBST() {
        List<Flight> flights = flightBST.getSortedFlights();
        assertNotNull(flights, "Sorted flights list should not be null.");
        assertTrue(flights.isEmpty(), "Sorted flights list for empty BST should be empty.");
    }

    @Test
    public void testSingleFlightInsertion() {
        Flight f1 = new Flight("FL001", "NYC", "LAX", 1430, 299.99);
        flightBST.insert(f1);

        List<Flight> flights = flightBST.getSortedFlights();
        assertEquals(1, flights.size());
        assertEquals("FL001", flights.get(0).getFlightId());
    }

    @Test
    public void testChronologicalDepartureTimeOrdering() {
        Flight f1 = new Flight("FL101", "JFK", "LHR", 1430, 500.0);
        Flight f2 = new Flight("FL102", "JFK", "CDG", 800, 450.0);
        Flight f3 = new Flight("FL103", "JFK", "DXB", 2015, 800.0);
        Flight f4 = new Flight("FL104", "JFK", "HND", 1200, 650.0);

        // Insert out of order
        flightBST.insert(f1); // 1430
        flightBST.insert(f2); // 0800
        flightBST.insert(f3); // 2015
        flightBST.insert(f4); // 1200

        List<Flight> sorted = flightBST.getSortedFlights();
        assertEquals(4, sorted.size());

        assertEquals(800, sorted.get(0).getDepartureTime());
        assertEquals("FL102", sorted.get(0).getFlightId());

        assertEquals(1200, sorted.get(1).getDepartureTime());
        assertEquals("FL104", sorted.get(1).getFlightId());

        assertEquals(1430, sorted.get(2).getDepartureTime());
        assertEquals("FL101", sorted.get(2).getFlightId());

        assertEquals(2015, sorted.get(3).getDepartureTime());
        assertEquals("FL103", sorted.get(3).getFlightId());
    }

    @Test
    public void testDuplicateDepartureTimeInsertion() {
        Flight f1 = new Flight("FL201", "SFO", "ORD", 1000, 200.0);
        Flight f2 = new Flight("FL202", "SFO", "SEA", 1000, 150.0);

        flightBST.insert(f1);
        flightBST.insert(f2);

        List<Flight> sorted = flightBST.getSortedFlights();
        assertEquals(2, sorted.size());
        assertEquals(1000, sorted.get(0).getDepartureTime());
        assertEquals(1000, sorted.get(1).getDepartureTime());
    }
}
