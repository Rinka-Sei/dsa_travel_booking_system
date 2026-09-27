package com.mycompany.dsa_travel_booking_system.ds;

import com.mycompany.dsa_travel_booking_system.model.Flight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link FlightBST}.
 */
class FlightBSTTest {

    private FlightBST flightBST;

    @BeforeEach
    void setUp() {
        flightBST = new FlightBST();
    }

    @Test
    void testGetSortedFlightsEmptyBST() {
        List<Flight> sortedFlights = flightBST.getSortedFlights();
        assertNotNull(sortedFlights);
        assertTrue(sortedFlights.isEmpty(), "Sorted flights list should be empty for an empty BST");
    }

    @Test
    void testInsertAndInOrderTraversal() {
        Flight f1 = new Flight("F101", "NYC", "LAX", 1430, 250.0); // 14:30
        Flight f2 = new Flight("F102", "NYC", "ORD", 800, 150.0);   // 08:00
        Flight f3 = new Flight("F103", "NYC", "MIA", 1800, 300.0);  // 18:00
        Flight f4 = new Flight("F104", "NYC", "SFO", 1100, 280.0);  // 11:00

        flightBST.insert(f1);
        flightBST.insert(f2);
        flightBST.insert(f3);
        flightBST.insert(f4);

        List<Flight> sorted = flightBST.getSortedFlights();
        assertEquals(4, sorted.size());
        assertEquals("F102", sorted.get(0).getFlightId()); // 800
        assertEquals("F104", sorted.get(1).getFlightId()); // 1100
        assertEquals("F101", sorted.get(2).getFlightId()); // 1430
        assertEquals("F103", sorted.get(3).getFlightId()); // 1800
    }

    @Test
    void testTimeOrderingWithDuplicateTimes() {
        Flight f1 = new Flight("F201", "JFK", "LHR", 1200, 500.0);
        Flight f2 = new Flight("F202", "EWR", "LHR", 1200, 480.0);

        flightBST.insert(f1);
        flightBST.insert(f2);

        List<Flight> sorted = flightBST.getSortedFlights();
        assertEquals(2, sorted.size());
        assertEquals(1200, sorted.get(0).getDepartureTime());
        assertEquals(1200, sorted.get(1).getDepartureTime());
    }
}
