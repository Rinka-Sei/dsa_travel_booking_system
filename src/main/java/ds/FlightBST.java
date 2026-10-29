package ds;

import model.Flight;
import java.util.ArrayList;
import java.util.List;

/**
 * Binary Search Tree (BST) implementation for managing and sorting Flight objects by departure time.
 *
 * <p>Time Complexities:</p>
 * <ul>
 *   <li>Insertion ({@link #insert(Flight)}): O(log n) average case, O(n) worst case (skewed tree).</li>
 *   <li>In-order Traversal ({@link #getSortedFlights()}): O(n) time complexity.</li>
 * </ul>
 */
public class FlightBST {

    /**
     * Inner class representing a node in the Binary Search Tree.
     */
    private class Node {

        Flight flight;
        Node left;
        Node right;

        /**
         * Constructs a new BST node holding a flight.
         *
         * @param flight the flight data
         */
        Node(Flight flight) {
            this.flight = flight;
            left = right = null;
        }
    }

    private Node root;

    /**
     * Constructs an empty Binary Search Tree for flight storage.
     */
    public FlightBST() {
        this.root = null;
    }

    /**
     * Inserts a flight into the BST ordered by departure time.
     * <p>
     * Time Complexity: O(log n) average case, O(n) worst case.
     * </p>
     *
     * @param flight the flight to insert into the BST
     */
    public void insert(Flight flight) {
        root = insertRec(root, flight);
    }

    /**
     * Recursive helper method to insert a flight into the subtree rooted at {@code root}.
     *
     * @param root   the current root node of the subtree
     * @param flight the flight to insert
     * @return the updated root node of the subtree
     */
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
     * Performs an in-order traversal of the tree to retrieve all flights sorted in ascending order by departure time.
     * <p>
     * Time Complexity: O(n).
     * </p>

     * @return a {@link List} of flights sorted chronologically by departure time
     */
    public List<Flight> getSortedFlights() {
        List<Flight> list = new ArrayList<>();
        inOrderRec(root, list);
        return list;
    }

    /**
     * Recursive helper method for performing in-order traversal on the BST.
     *
     * @param root the current root node of the subtree
     * @param list the list collecting flights in sorted order
     */
    private void inOrderRec(Node root, List<Flight> list) {
        if (root != null) {
            inOrderRec(root.left, list);
            list.add(root.flight);
            inOrderRec(root.right, list);
        }
    }
}
