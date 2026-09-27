package com.mycompany.dsa_travel_booking_system;

import java.util.ArrayList;
import java.util.List;

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
