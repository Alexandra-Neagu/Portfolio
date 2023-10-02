package users.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The type User test class.
 */
public class UserTest {

    private User user;
    private User user1;
    private User user2;

    private UUID userUuid1 = UUID.fromString("12345678-1234-1234-1234-123456789012");
    private UUID userUuid2 = UUID.fromString("abcdefab-abcd-abcd-abcd-abcdefabcdef");
    private UUID userUuid3 = UUID.fromString("1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d");

    private final String nameLiteral = "Jake";

    @BeforeEach
    private void setUp() {
        user = new User.UserBuilder()
                        .withName("John")
                        .withUuid(userUuid1)
                        .withHasPremium(true)
                        .build();
        user1 = new User.UserBuilder()
                        .withName("John")
                        .withUuid(userUuid2)
                        .withHasPremium(true)
                        .build();
        user2 = new User.UserBuilder()
                        .withName(nameLiteral)
                        .withUuid(userUuid3)
                        .withHasPremium(true)
                        .build();
    }

    /**
     * Test getter UUID.
     */
    @Test
    public void testGetterUuid() {
        User randUser = new User.UserBuilder().withUuid(userUuid1).withName("Gigi").build();
        assertNotNull(randUser.getUuid());
    }

    /**
     * Test getter reservations.
     */
    @Test
    public void testGetterReservations() {
        assertNotNull(user.getReservations());
    }

    /**
     * Test getter setter name.
     */
    @Test
    public void testGetterSetterName() {
        assertEquals("John", user.getName());
        user.setName(nameLiteral);
        assertEquals(nameLiteral, user.getName());
    }

    /**
     * Test getter setter hasPassword.
     */
    @Test
    public void testGetterSetterHasPremium() {
        assertTrue(user.getHasPremium());
        user.setHasPremium(false);
        assertFalse(user.getHasPremium());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        String expected = "User{uuid="
                + user.getUuid()
                + ", name='John', hasPremium=true"
                + ", reservations=" + user.getReservations().toString()
                + "}";
        assertEquals(expected, user.toString());
    }

    // there will (statistically) never be 2 equal Users,
    // because they get assigned random UUIDs when instantiated

    /**
     * Test equals an object with itself.
     */
    @Test
    public void testEqualsSameObj() {
        assertTrue(user.equals(user));
    }

    /**
     * Test equals different objects from the same class.
     */
    @Test
    public void testEqualsDiffObjSameClass() {
        assertFalse(user1.equals(user2));
    }

    /**
     * Test equals different objects from different classes.
     */
    @Test
    public void testEqualsDiffObjDiffClass() {
        Team team = new Team();
        assertFalse(user.equals(team));
    }

    /**
     * Test equals with null objects.
     */
    @Test
    public void testEqualsNull() {
        User nullUser = null;
        assertFalse(user1.equals(nullUser));
    }

    /**
     * Test the hash code of the object.
     */
    @Test
    public void testHashCode() {
        assertNotNull(user.hashCode());
    }

    /**
     * Test that an IllegalArgumentException is thrown
     * when trying to create an invalid user with a null name.
     */
    @Test
    public void invalidUserNullName() {
        assertThrows(IllegalArgumentException.class, () -> new User.UserBuilder()
                                                                    .withName(null)
                                                                    .withUuid(userUuid1)
                                                                    .withHasPremium(true)
                                                                    .build());
    }

    /**
     * Test that an IllegalArgumentException is thrown
     * when trying to create an invalid user with a null UUID.
     */
    @Test
    public void invalidUserNullUUID() {
        assertThrows(IllegalArgumentException.class, () -> new User.UserBuilder()
                                                                    .withName(nameLiteral)
                                                                    .withUuid(null)
                                                                    .withHasPremium(true)
                                                                    .build());
    }

    /**
     * Test that an IllegalArgumentException is thrown
     * when trying to set the name of a user to null.
     */
    @Test
    public void setNameException() {
        assertThrows(IllegalArgumentException.class, () -> user.setName(null));
    }

    /**
     * Test the default constructor.
     */
    @Test
    public void defaultConstructor() {
        User user3 = new User();
        assertNotNull(user3);
    }

}
