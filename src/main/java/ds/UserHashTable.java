package ds;

import model.User;

/**
 * Custom Hash Table implementation for storing and retrieving User objects using string keys.
 * Uses separate chaining (linked list) to handle hash collisions.
 *
 * <p>Time Complexities:</p>
 * <ul>
 *   <li>Insertion ({@link #put(String, User)}): O(1) average case, O(n) worst case (all keys in single bucket).</li>
 *   <li>Retrieval ({@link #get(String)}): O(1) average case, O(n) worst case.</li>
 * </ul>
 */
public class UserHashTable {

    /**
     * Inner class representing a node in the separate chaining linked list.
     */
    private class HashNode {

        String key;
        User value;
        HashNode next;

        /**
         * Constructs a new HashNode key-value pair.
         *
         * @param key   the string key (username)
         * @param value the associated user object
         */
        HashNode(String key, User value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private HashNode[] buckets;
    private int capacity;

    /**
     * Constructs a UserHashTable with the specified bucket capacity.
     *
     * @param capacity the number of initial buckets in the hash table
     * @throws IllegalArgumentException if capacity is less than or equal to 0
     */
    public UserHashTable(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.capacity = capacity;
        this.buckets = new HashNode[capacity];
    }

    /**
     * Computes the bucket index for a given string key using hash code mapping.
     *
     * @param key the string key
     * @return the valid array index for the bucket
     */
    private int getBucketIndex(String key) {
        int hashCode = Math.abs(key.hashCode());
        return hashCode % capacity;
    }

    /**
     * Inserts or updates a user key-value pair in the hash table.
     * Handles collisions by chaining new entries at the head of the bucket linked list.
     * <p>
     * Time Complexity: O(1) average case, O(n) worst case.
     * </p>
     *
     * @param key   the lookup key (username)
     * @param value the user object associated with the key
     */
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

    /**
     * Retrieves the user associated with the specified key.
     * <p>
     * Time Complexity: O(1) average case, O(n) worst case.
     * </p>
     *
     * @param key the lookup key (username)
     * @return the {@link User} object if found; {@code null} if the key is missing
     */
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
