package com.mycompany.dsa_travel_booking_system;

public class UserHashTable {

    private class HashNode {

        String key;
        User value;
        HashNode next;

        HashNode(String key, User value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private HashNode[] buckets;
    private int capacity;

    public UserHashTable(int capacity) {
        this.capacity = capacity;
        this.buckets = new HashNode[capacity];
    }

    private int getBucketIndex(String key) {
        int hashCode = Math.abs(key.hashCode());
        return hashCode % capacity;
    }

    public void put(String key, User value) {
        int index = getBucketIndex(key);
        HashNode head = buckets[index];

        while (head != null) {
            if (head.key.equals(key)) {
                head.value = value;
                return;
            }
            head = head.next;
        }

        HashNode newNode = new HashNode(key, value);
        newNode.next = buckets[index];
        buckets[index] = newNode;
    }

    public User get(String key) {
        int index = getBucketIndex(key);
        HashNode head = buckets[index];

        while (head != null) {
            if (head.key.equals(key)) {
                return head.value;
            }
            head = head.next;
        }
        return null;
    }
}
