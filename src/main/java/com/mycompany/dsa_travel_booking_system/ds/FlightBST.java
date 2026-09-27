package com.mycompany.dsa_travel_booking_system.ds;

import com.mycompany.dsa_travel_booking_system.model.Flight;
import java.util.ArrayList;
import java.util.List;

/**
 * Binary Search Tree implementation for managing and sorting Flight objects by departure time.
 *
 * Time Complexities:
 * <ul>
 *   <li>Insertion ({@link #insert(Flight)}): O(log n) average case, O(n) worst case (unbalanced).</li>
 *   <li>In-order traversal ({@link #getSortedFlights()}): O(n) time complexity.</li>
 * </ul>
 */
public class FlightBST {

    private class Node {

        Flight flight;
        Node left, right;

        Node(Flight flight) {
            this.flight = flight;
            left = right = null;
        }
    }

    private Node root;

    /**
     * Constructs an empty Binary Search Tree for flights.
     */
    public FlightBST() {
        this.root = null;
    }

    /**
     * Inserts a flight into the BST ordered by departure time.
     * <p>
     * Time Complexity: O(log n) average, O(n) worst case.
     * </p>
     *
     * @param flight the flight to insert
     */
    public void insert(Flight flight) {
        root = insertRec(root, flight);
    }

    private Node insertRec(Node root, Flight flight) {
        if (root == null) {
            return new Node(flight);
        }
        if (flight.getDepartureTime() < root.flight.getDepartureTime()) {
            root.left = insertRec(root.left, flight);
        } else {
            root.right = insertRec(root.right, flight);
        }
        return root;
    }

    /**
     * Returns a list of all flights sorted in ascending order by departure time via in-order traversal.
     * <p>
     * Time Complexity: O(n).
     * </p>
     *
     * @return a list of flights sorted by departure time
     */
    public List<Flight> getSortedFlights() {
        List<Flight> list = new ArrayList<>();
        inOrderRec(root, list);
        return list;
    }

    private void inOrderRec(Node root, List<Flight> list) {
        if (root != null) {
            inOrderRec(root.left, list);
            list.add(root.flight);
            inOrderRec(root.right, list);
        }
    }
}
