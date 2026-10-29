package ds;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link UserHashTable}.
 */
public class UserHashTableTest {

    private UserHashTable userHashTable;

    @BeforeEach
    public void setUp() {
        userHashTable = new UserHashTable(10);
    }

    @Test
    public void testPutAndGet() {
        User user1 = new User("alice", "pass123", "CUSTOMER");
        User user2 = new User("bob", "secret", "ADMIN");

        userHashTable.put("alice", user1);
        userHashTable.put("bob", user2);

        User retrievedAlice = userHashTable.get("alice");
        assertNotNull(retrievedAlice, "Retrieved user 'alice' should not be null.");
        assertEquals("alice", retrievedAlice.getUsername());
        assertEquals("pass123", retrievedAlice.getPassword());
        assertEquals("CUSTOMER", retrievedAlice.getRole());

        User retrievedBob = userHashTable.get("bob");
        assertNotNull(retrievedBob, "Retrieved user 'bob' should not be null.");
        assertEquals("bob", retrievedBob.getUsername());
        assertEquals("secret", retrievedBob.getPassword());
        assertEquals("ADMIN", retrievedBob.getRole());
    }

    @Test
    public void testSeparateChainingCollisionHandling() {
        // Use a hash table with capacity 1 to force all keys into the exact same bucket
        UserHashTable smallTable = new UserHashTable(1);

        User user1 = new User("user1", "p1", "CUSTOMER");
        User user2 = new User("user2", "p2", "CUSTOMER");
        User user3 = new User("user3", "p3", "ADMIN");

        smallTable.put("user1", user1);
        smallTable.put("user2", user2);
        smallTable.put("user3", user3);

        assertEquals(user1, smallTable.get("user1"), "User 1 should be retrievable from chained bucket.");
        assertEquals(user2, smallTable.get("user2"), "User 2 should be retrievable from chained bucket.");
        assertEquals(user3, smallTable.get("user3"), "User 3 should be retrievable from chained bucket.");
    }

    @Test
    public void testNonExistentKeyLookup() {
        User user = userHashTable.get("nonexistent");
        assertNull(user, "Lookup for non-existent key should return null.");
    }

    @Test
    public void testUpdateExistingKey() {
        User initialUser = new User("alice", "oldpass", "CUSTOMER");
        User updatedUser = new User("alice", "newpass", "ADMIN");

        userHashTable.put("alice", initialUser);
        assertEquals("oldpass", userHashTable.get("alice").getPassword());

        userHashTable.put("alice", updatedUser);
        assertEquals("newpass", userHashTable.get("alice").getPassword());
        assertEquals("ADMIN", userHashTable.get("alice").getRole());
    }

    @Test
    public void testInvalidCapacityThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new UserHashTable(0),
                "Constructor with capacity 0 should throw IllegalArgumentException.");
        assertThrows(IllegalArgumentException.class, () -> new UserHashTable(-5),
                "Constructor with negative capacity should throw IllegalArgumentException.");
    }
}
