package reservations.entities;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotEquals;

import org.junit.jupiter.api.Test;

public class EquipmentTest {
    private String testRelatedSport = "test sport";

    private Equipment equipment =
            new Equipment("test name", 20, testRelatedSport);

    @Test
    public void equipmentIsABookable() {
        boolean expected = true;
        boolean actual = equipment instanceof Bookable;
        assertEquals("Equipment should be a bookable", expected, actual);
    }

    @Test
    public void emptyConstructor_SetsCorrectProperties() {
        equipment = new Equipment();
        assertEquals("Related sport is set incorrectly", null, equipment.getRelatedSport());
    }

    @Test
    public void constructorWithoutDescription_SetsCorrectProperties() {
        equipment = new Equipment("test name", 20, testRelatedSport);
        assertEquals(
                "Related sport is set incorrectly",
                testRelatedSport,
                equipment.getRelatedSport()
        );
    }

    @Test
    public void fullConstructor_SetsCorrectProperties() {
        // Object is already initialized with the full constructor so we do not need to initialize
        // it again

        assertEquals(
                "Related sport is set incorrectly",
                testRelatedSport,
                equipment.getRelatedSport()
        );
    }

    @Test
    public void getRelatedSports_ReturnsCorrectValue() {
        assertEquals(
                "Incorrect related sport returned",
                testRelatedSport,
                equipment.getRelatedSport()
        );
    }

    @Test
    public void setRelatedSport_SetsCorrectProperty() {
        String newSport = "New sport";
        equipment.setRelatedSport(newSport);
        assertEquals("Sport not set correctly", newSport, equipment.getRelatedSport());
    }

    @Test
    public void toString_ReturnsAnActualString() {
        String result = equipment.toString();
        assertNotEquals("String should not be null", null, result);
        assertNotEquals("String should not be empty", "", result);
    }
}
