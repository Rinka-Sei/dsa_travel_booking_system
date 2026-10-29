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
        Flight f1 = new Flight("FL001", "NYC", "14:30", "LAX", "18:00", 299.99);
        flightBST.insert(f1);

        List<Flight> flights = flightBST.getSortedFlights();
        assertEquals(1, flights.size());
        assertEquals("FL001", flights.get(0).getFlightId());
        assertEquals("14:30", flights.get(0).getDepartureTime());
        assertEquals("18:00", flights.get(0).getArrivalTime());
    }

    @Test
    public void testChronologicalMilitaryDepartureTimeOrdering() {
        Flight f1 = new Flight("FL101", "JFK", "14:45", "LHR", "22:15", 500.0);
        Flight f2 = new Flight("FL102", "JFK", "08:30", "CDG", "16:00", 450.0);
        Flight f3 = new Flight("FL103", "JFK", "22:15", "DXB", "06:30", 800.0);
        Flight f4 = new Flight("FL104", "JFK", "12:00", "HND", "20:00", 650.0);

        // Insert out of order
        flightBST.insert(f1); // "14:45"
        flightBST.insert(f2); // "08:30"
        flightBST.insert(f3); // "22:15"
        flightBST.insert(f4); // "12:00"

        List<Flight> sorted = flightBST.getSortedFlights();
        assertEquals(4, sorted.size());

        assertEquals("08:30", sorted.get(0).getDepartureTime());
        assertEquals("FL102", sorted.get(0).getFlightId());

        assertEquals("12:00", sorted.get(1).getDepartureTime());
        assertEquals("FL104", sorted.get(1).getFlightId());

        assertEquals("14:45", sorted.get(2).getDepartureTime());
        assertEquals("FL101", sorted.get(2).getFlightId());

        assertEquals("22:15", sorted.get(3).getDepartureTime());
        assertEquals("FL103", sorted.get(3).getFlightId());
    }

    @Test
    public void testDuplicateDepartureTimeInsertion() {
        Flight f1 = new Flight("FL201", "SFO", "10:00", "ORD", "14:00", 200.0);
        Flight f2 = new Flight("FL202", "SFO", "10:00", "SEA", "12:30", 150.0);

        flightBST.insert(f1);
        flightBST.insert(f2);

        List<Flight> sorted = flightBST.getSortedFlights();
        assertEquals(2, sorted.size());
        assertEquals("10:00", sorted.get(0).getDepartureTime());
        assertEquals("10:00", sorted.get(1).getDepartureTime());
    }
}
