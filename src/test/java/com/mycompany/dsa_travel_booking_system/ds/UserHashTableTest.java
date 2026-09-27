package com.mycompany.dsa_travel_booking_system.ds;

import com.mycompany.dsa_travel_booking_system.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link UserHashTable}.
 */
class UserHashTableTest {

    private UserHashTable userHashTable;

    @BeforeEach
    void setUp() {
        // Small capacity to intentionally force hash collisions
        userHashTable = new UserHashTable(3);
    }

    @Test
    void testPutAndGetSingleUser() {
        User user = new User("john_doe", "pass123", "CUSTOMER");
        userHashTable.put("john_doe", user);

        User retrieved = userHashTable.get("john_doe");
        assertNotNull(retrieved, "Retrieved user should not be null");
        assertEquals("john_doe", retrieved.getUsername());
        assertEquals("pass123", retrieved.getPassword());
        assertEquals("CUSTOMER", retrieved.getRole());
    }

    @Test
    void testUpdateExistingKey() {
        User initialUser = new User("john_doe", "old_pass", "CUSTOMER");
        userHashTable.put("john_doe", initialUser);

        User updatedUser = new User("john_doe", "new_pass", "ADMIN");
        userHashTable.put("john_doe", updatedUser);

        User retrieved = userHashTable.get("john_doe");
        assertNotNull(retrieved);
        assertEquals("new_pass", retrieved.getPassword());
        assertEquals("ADMIN", retrieved.getRole());
    }

    @Test
    void testGetMissingKeyReturnsNull() {
        User retrieved = userHashTable.get("non_existent_user");
        assertNull(retrieved, "Getting a non-existent key should return null");
    }

    @Test
    void testCollisionHandling() {
        // Keys designed or inserted in small capacity (3) to test chaining
        User u1 = new User("userA", "passA", "CUSTOMER");
        User u2 = new User("userB", "passB", "ADMIN");
        User u3 = new User("userC", "passC", "CUSTOMER");
        User u4 = new User("userD", "passD", "CUSTOMER");

        userHashTable.put("userA", u1);
        userHashTable.put("userB", u2);
        userHashTable.put("userC", u3);
        userHashTable.put("userD", u4);

        assertEquals(u1, userHashTable.get("userA"));
        assertEquals(u2, userHashTable.get("userB"));
        assertEquals(u3, userHashTable.get("userC"));
        assertEquals(u4, userHashTable.get("userD"));
    }

    @Test
    void testInvalidCapacityConstructorThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new UserHashTable(0));
        assertThrows(IllegalArgumentException.class, () -> new UserHashTable(-5));
    }
}
