package reservations.entities;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.Objects;
import org.junit.jupiter.api.Test;

public class BookableTest {
    private String testName = "Test name";
    private int testMaxCapacity = 20;

    private Bookable bookable = new Bookable(testName, testMaxCapacity);

    @Test
    public void emptyConstructor_SetsCorrectProperties() {
        bookable = new Bookable();
        assertNotEquals("UUID of Bookable is not set", null, bookable.uuid);
        assertEquals("Name is set incorrectly", null, bookable.name);
        assertEquals("Max capacity is set incorrectly", 0, bookable.maxCapacity);
    }

    @Test
    public void constructorWithoutDescription_SetsCorrectProperties() {
        bookable = new Bookable(testName, testMaxCapacity);
        assertNotEquals("UUID of Bookable is not set", null, bookable.uuid);
        assertEquals("Name is set incorrectly", testName, bookable.name);
        assertEquals("Max capacity is set incorrectly", testMaxCapacity, bookable.maxCapacity);
    }

    @Test
    public void constructorWithoutDescription_ThrowsException_WhenCapacityNegative() {
        assertThrows(
                InvalidParameterException.class,
                () -> new Bookable(testName, -1)
        );
    }

    @Test
    public void fullConstructor_SetsCorrectProperties() {
        // Object is already initialized with the full constructor so we do not need to initialize
        // it again

        assertNotEquals("UUID of Bookable is not set", null, bookable.uuid);
        assertEquals("Name is set incorrectly", testName, bookable.name);
        assertEquals("Max capacity is set incorrectly", testMaxCapacity, bookable.maxCapacity);
    }

    @Test
    public void fullConstructor_ThrowsException_WhenCapacityNegative() {
        assertThrows(
                InvalidParameterException.class,
                () -> new Bookable(testName, -1)
        );
    }

    @Test
    public void getUUID_ReturnsCorrectResult() {
        assertEquals("Incorrect UUID returned", bookable.uuid, bookable.getUuid());
    }

    @Test
    public void getName_ReturnsCorrectResult() {
        assertEquals("Incorrect name returned", testName, bookable.getName());
    }

    @Test
    public void getMaxCapacity_ReturnsCorrectResult() {
        assertEquals(
                "Incorrect maximum capacity returned",
                testMaxCapacity,
                bookable.getMaxCapacity()
        );
    }

    @Test
    public void setName_CorrectlySetsName() {
        String newName = "New name";
        bookable.setName(newName);
        assertEquals("Name not set correctly", newName, bookable.name);
    }

    @Test
    public void setMaxCapacity_CorrectlySetsMaxCapacity_WhenOnBoundary() {
        int newMaxCapacity = 0;
        bookable.setMaxCapacity(newMaxCapacity);
        assertEquals("Maximum capacity not set correctly", newMaxCapacity, bookable.maxCapacity);
    }

    @Test
    public void setMaxCapacity_Fails_WhenNegative() {
        int newMaxCapacity = -1;
        assertThrows(InvalidParameterException.class,
                () -> bookable.setMaxCapacity(newMaxCapacity));
    }

    @Test
    public void checkMaxCapacityConstraints_Fails_WhenValueIsNegative() throws Exception {
        // Since the method is private but it is vital to test its correctness,
        // we are going to use reflection to test this method.
        Method method =
                bookable.getClass().getDeclaredMethod("checkMaxCapacityConstraints", int.class);
        method.setAccessible(true);
        try {
            method.invoke(bookable, -1);
            fail();
        } catch (InvocationTargetException e) {
            assertEquals("Exception should have been an InvalidParamterException",
                    e.getTargetException().getClass(),
                    InvalidParameterException.class);
        }
    }

    @Test
    public void equals_ReturnsTrue_WhenComparedWithSameObject() {
        boolean expected = true;
        assertEquals("Object not equal to itself", expected, bookable.equals(bookable));
    }

    @Test
    public void equals_ReturnsFalse_WhenComparedWithOtherClass() {
        boolean expected = false;
        assertEquals(
                "Object should not be equal to an object of another class",
                expected,
                bookable.equals(2)
        );
    }

    @Test
    public void equals_ReturnsTrue_WhenObjectsHaveTheSameUuid() {
        Bookable other = new Bookable();
        other.uuid = bookable.uuid;
        boolean expected = true;
        assertEquals(
                "Objects with the same UUID should be equal",
                expected,
                bookable.equals(other)
        );
    }

    @Test
    public void equals_ReturnsFalse_WhenObjectsHaveDifferentUuids() {
        Bookable other = new Bookable();
        boolean expected = false;
        assertEquals(
                "Object with different UUIDs should NOT be equal",
                expected,
                bookable.equals(other)
        );
    }

    @Test
    public void hashCode_ReturnsCorrectHash() {
        int expected = Objects.hash(
                bookable.uuid,
                bookable.name,
                bookable.maxCapacity
        );

        assertEquals("Hashes do not match", expected, bookable.hashCode());
    }

    @Test
    public void toString_ReturnsAnActualString() {
        String result = bookable.toString();
        assertNotEquals("String should not be null", null, result);
        assertNotEquals("String should not be empty", "", result);
    }
}
