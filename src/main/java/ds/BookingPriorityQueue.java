package ds;

import model.Booking;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Custom Priority Queue implementation for booking transactions ordered by criteria such as total price or booking ID.
 * Follows the starter project structure pattern (as in RentalPriorityQueue).
 *
 * <p>Time Complexities:</p>
 * <ul>
 *   <li>Enqueue ({@link #addBooking(Booking)}): O(log n)</li>
 *   <li>Dequeue highest priority ({@link #pollNextBooking()}): O(log n)</li>
 *   <li>Peek ({@link #peekNextBooking()}): O(1)</li>
 * </ul>
 */
public class BookingPriorityQueue {

    private final PriorityQueue<Booking> queue;

    /**
     * Constructs a BookingPriorityQueue defaulting to highest total price first (max-heap).
     */
    public BookingPriorityQueue() {
        this(Comparator.comparingDouble(Booking::getTotalPrice).reversed());
    }

    /**
     * Constructs a BookingPriorityQueue with a custom comparator.
     *
     * @param comparator the comparison logic for booking prioritization
     */
    public BookingPriorityQueue(Comparator<Booking> comparator) {
        this.queue = new PriorityQueue<>(comparator != null ? comparator : Comparator.comparingDouble(Booking::getTotalPrice).reversed());
    }

    /**
     * Inserts a booking transaction into the priority queue.
     *
     * @param booking the booking entity to enqueue
     */
    public void addBooking(Booking booking) {
        if (booking != null) {
            queue.offer(booking);
        }
    }

    /**
     * Retrieves and removes the highest priority booking transaction.
     *
     * @return the top priority {@link Booking}, or {@code null} if empty
     */
    public Booking pollNextBooking() {
        return queue.poll();
    }

    /**
     * Retrieves, but does not remove, the highest priority booking transaction.
     *
     * @return the top priority {@link Booking}, or {@code null} if empty
     */
    public Booking peekNextBooking() {
        return queue.peek();
    }

    /**
     * Checks whether the priority queue is empty.
     *
     * @return {@code true} if empty; {@code false} otherwise
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Returns the number of bookings currently in the priority queue.
     *
     * @return queue element count
     */
    public int size() {
        return queue.size();
    }
}
