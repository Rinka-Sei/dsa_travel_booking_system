package ds;

import model.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link BookingPriorityQueue}.
 */
public class BookingPriorityQueueTest {

    private BookingPriorityQueue priorityQueue;

    @BeforeEach
    public void setUp() {
        priorityQueue = new BookingPriorityQueue();
    }

    @Test
    public void testDefaultPricePriorityOrder() {
        Booking b1 = new Booking(1, "alice", "FL101", "H201", 500.0);
        Booking b2 = new Booking(2, "bob", "FL102", "H202", 1200.0);
        Booking b3 = new Booking(3, "charlie", "FL103", "H203", 800.0);

        priorityQueue.addBooking(b1);
        priorityQueue.addBooking(b2);
        priorityQueue.addBooking(b3);

        assertEquals(3, priorityQueue.size());
        assertEquals(b2, priorityQueue.peekNextBooking(), "Top booking should be the highest price (1200.0)");
        assertEquals(b2, priorityQueue.pollNextBooking());
        assertEquals(b3, priorityQueue.pollNextBooking(), "Next should be 800.0");
        assertEquals(b1, priorityQueue.pollNextBooking(), "Last should be 500.0");
        assertTrue(priorityQueue.isEmpty());
        assertNull(priorityQueue.pollNextBooking());
    }

    @Test
    public void testCustomComparatorOrder() {
        // Ascending price order (min-heap)
        BookingPriorityQueue minQueue = new BookingPriorityQueue(Comparator.comparingDouble(Booking::getTotalPrice));

        Booking b1 = new Booking(1, "alice", "FL101", "H201", 900.0);
        Booking b2 = new Booking(2, "bob", "FL102", "H202", 300.0);

        minQueue.addBooking(b1);
        minQueue.addBooking(b2);

        assertEquals(b2, minQueue.pollNextBooking(), "Min queue should yield lowest price first");
        assertEquals(b1, minQueue.pollNextBooking());
    }

    @Test
    public void testNullSafety() {
        priorityQueue.addBooking(null);
        assertEquals(0, priorityQueue.size());
        assertNull(priorityQueue.peekNextBooking());
        assertNull(priorityQueue.pollNextBooking());
    }
}
