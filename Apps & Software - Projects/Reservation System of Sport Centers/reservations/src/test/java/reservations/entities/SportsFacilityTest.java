package reservations.entities;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SportsFacilityTest {
    private int testMinCapacity = 10;
    private String testName = "name";
    private int testMaxCapacity = 20;

    private SportsFacility facility =
            new SportsFacility("name", testMinCapacity, testMaxCapacity);

    Method method;

    /**
     * Sets up some common parameters.
     */
    @BeforeEach
    public void setup() throws NoSuchMethodException {
        method = facility.getClass().getDeclaredMethod(
                "checkCapacityConstraints",
                int.class,
                int.class
        );
    }

    @Test
    public void facility_IsABookable() {
        boolean expected = true;
        boolean actual = facility instanceof Bookable;
        assertEquals("Facility should be a bookable", expected, actual);
    }

    @Test
    public void emptyConstructor_SetsCorrectProperties() {
        facility = new SportsFacility();
        assertEquals(
                "Minimum capacity should be left to 0",
                0,
                facility.getMinCapacity()
        );
    }

    @Test
    public void constructorWithNoDescriptionAndMinCapacity_SetsCorrectProperties() {
        facility = new SportsFacility(testName, testMaxCapacity);
        assertEquals(
                "Minimum capacity should be left to its default value",
                1,
                facility.getMinCapacity()
        );
    }

    @Test
    public void
        constructorWithNoDescriptionAndMinCapacity_ThrowsExceptionWhenMaxCapacityInvalid() {
        assertThrows(
                InvalidParameterException.class,
                () -> new SportsFacility(testName, 0)
        );
    }

    @Test
    public void constructorWithNoMinCapacity_SetsCorrectProperties() {
        facility = new SportsFacility(testName, testMaxCapacity);
        assertEquals(
                "Minimum capacity should be left to its default value",
                1,
                facility.getMinCapacity()
        );
    }

    @Test
    public void constructorWithNoMinCapacity_ThrowsExceptionWhenMaxCapacityInvalid() {
        assertThrows(
                InvalidParameterException.class,
                () -> new SportsFacility(testName, 0)
        );
    }

    @Test
    public void constructorWithNoDescription_SetsCorrectProperties() {
        facility = new SportsFacility(testName, testMinCapacity, testMaxCapacity);
        assertEquals(
                "Minimum capacity not set correctly",
                testMinCapacity,
                facility.getMinCapacity()
        );
    }

    @Test
    public void constructorWithNoDescription_ThrowsExceptionWhenCapacitiesInvalid() {
        assertThrows(
                InvalidParameterException.class,
                () -> new SportsFacility(testName, 0, 1)
        );
    }

    @Test
    public void fullConstructor_SetsCorrectProperties() {
        facility = new SportsFacility(testName, testMinCapacity, testMaxCapacity);
        assertEquals(
                "Minimum capacity not set correctly",
                testMinCapacity,
                facility.getMinCapacity()
        );
    }

    @Test
    public void fullConstructor_ThrowsExceptionWhenCapacitiesInvalid() {
        assertThrows(
                InvalidParameterException.class,
                () -> new SportsFacility(testName, 1, 0)
        );
    }

    @Test
    public void getMinCapacity_ReturnsCorrectValue() {
        int result = facility.getMinCapacity();

        assertEquals(
                "Incorrect minimum capacity returned",
                testMinCapacity,
                result
        );
    }

    @Test
    public void setMinCapacity_SetsCorrectProperty() {
        int newCapacity = 5;
        int previousMaxCapacity = facility.getMaxCapacity();

        facility.setMinCapacity(newCapacity);
        int result = facility.getMinCapacity();
        assertEquals("Minimum capacity not set correctly", newCapacity, result);
        assertEquals(
                "Max capacity should remain the same",
                previousMaxCapacity,
                facility.getMaxCapacity()
        );
    }

    @Test
    public void setMinCapacity_ThrowsException_WhenCapacityInvalid() {
        assertThrows(
                InvalidParameterException.class,
                () -> facility.setMinCapacity(0)
        );
    }

    @Test
    public void checkCapacityConstraints_Fails_WhenMinCapacityBelowBoundary()
            throws Exception {
        method.setAccessible(true);
        try {
            method.invoke(facility, 0, 1);
            fail();
        } catch (InvocationTargetException e) {
            assertEquals("Exception should have been an InvalidParamterException",
                    e.getTargetException().getClass(),
                    InvalidParameterException.class);
        }
    }

    @Test
    public void checkCapacityConstraints_Fails_WhenMaxCapacityBelowBoundary()
            throws Exception {
        method.setAccessible(true);
        try {
            method.invoke(facility, 1, 0);
            fail();
        } catch (InvocationTargetException e) {
            assertEquals(
                    "Exception should have been an InvalidParameterException",
                    e.getTargetException().getClass(),
                    InvalidParameterException.class
            );
        }
    }

    @Test
    public void checkCapacityConstraints_Fails_WhenMaxCapacitySmallerThanMinCapacity()
            throws Exception {
        method.setAccessible(true);
        try {
            method.invoke(facility, 5, 3);
            fail();
        } catch (InvocationTargetException e) {
            assertEquals("Exception should have been an InvalidParamterException",
                    e.getTargetException().getClass(),
                    InvalidParameterException.class);
        }
    }

    @Test
    public void checkCapacityConstraints_Succeeds_WhenMaxCapacityEqualToMinCapacity()
            throws Exception {
        method.setAccessible(true);

        assertDoesNotThrow(() -> method.invoke(facility, 1, 1));
    }

    @Test
    public void setMaxCapacity_ThrowsException_WhenCapacityInvalid() {
        assertThrows(
                InvalidParameterException.class,
                () -> facility.setMaxCapacity(0)
        );
    }

    @Test
    public void setMaxCapacity_SetsCorrectField_WhenCapacityValid() {
        int expected = facility.getMinCapacity() + 1;
        facility.setMaxCapacity(expected);
        assertEquals("Different capacities returned", expected, facility.getMaxCapacity());
    }

    @Test
    public void toString_ReturnsAnActualString() {
        String result = facility.toString();
        assertNotEquals("Result should not be null", null, result);
        assertNotEquals("Result should not be empty", "", result);
    }

}
