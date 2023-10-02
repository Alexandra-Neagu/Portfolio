package users.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The type Reduced reservation test.
 */
public class ReducedReservationTest {

    private ReducedReservation reduced1;
    private ReducedReservation reduced2;
    private Timestamp timestamp;

    @BeforeEach
    private void setUp() {
        timestamp = new java.sql.Timestamp(
                Calendar.getInstance().getTimeInMillis()
        );
        reduced1 = new ReducedReservation(UUID.randomUUID(), timestamp);
        reduced2 = new ReducedReservation(UUID.randomUUID(), timestamp);
    }

    /**
     * Test getters of the UUID.
     */
    @Test
    public void testGetterUuid() {
        assertNotNull(reduced1.getUuid());
    }

    /**
     * Tests the getter and setter for the createdAt field.
     */
    @Test
    public void testGetterSetterCreatedAt() {
        assertEquals(timestamp, reduced1.getCreatedAt());
        reduced1.setCreatedAt(null);
        assertNull(reduced1.getCreatedAt());
    }

    /**
     * Test the toString method.
     */
    @Test
    public void testToString() {
        String expected = "ReducedReservation{"
                + "uuid=" + reduced1.getUuid()
                + ", createdAt=" + reduced1.getCreatedAt()
                + '}';
        assertEquals(expected, reduced1.toString());
    }

    /**
     * Test equals an object with itself.
     */
    @Test
    public void testEqualsSameObj() {
        assertTrue(reduced1.equals(reduced1));
    }

    /**
     * Test equals different objects from the same class.
     */
    @Test
    public void testEqualsDiffObjSameClass() {
        assertFalse(reduced1.equals(reduced2));
    }

    /**
     * Test equals different objects from different classes.
     */
    @Test
    public void testEqualsDiffObjDiffClass() {
        Team team = new Team();
        assertFalse(reduced1.equals(team));
    }

    /**
     * Test equals with null objects.
     */
    @Test
    public void testEqualsNull() {
        ReducedReservation nullUser = null;
        assertFalse(reduced1.equals(nullUser));
    }

    /**
     * Test the hash code of the object.
     */
    @Test
    public void testHashCode() {
        assertNotNull(reduced1.hashCode());
    }

    /**
     * Test default constructor.
     */
    @Test
    public void defaultConstructor() {
        ReducedReservation reducedReservation = new ReducedReservation();
        assertNotNull(reducedReservation);
    }

}
