package reservations.entities;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotEquals;

import java.sql.Timestamp;
import java.util.Calendar;
import org.junit.jupiter.api.Test;

public class LessonTest {
    private int testMinCapacity = 10;
    private String testName = "name";
    private String testDescription = "description";
    private int testMaxCapacity = 20;
    private SportsFacility testSportsFacility = new SportsFacility();
    private Timestamp testStartTime = new java.sql.Timestamp(
            Calendar.getInstance().getTimeInMillis()
    );
    private Timestamp testEndtime = new java.sql.Timestamp(
            Calendar.getInstance().getTimeInMillis()
    );
    private Lesson lesson = new Lesson(
            testName,
            testMaxCapacity,
            testSportsFacility,
            testStartTime,
            testEndtime
    );

    @Test
    public void emptyConstructor_SetsCorrectProperties() {
        lesson = new Lesson();
        assertEquals(
                "Sports facility is set incorrectly",
                null,
                lesson.getSportsFacility()
        );
        assertEquals(
                "Start time not set correctly",
                null,
                lesson.getStartTime()
        );
        assertEquals(
                "End time not set correctly",
                null,
                lesson.getEndTime()
        );
    }

    @Test
    public void constructorWithoutDescription_SetsCorrectProperties() {
        lesson = new Lesson(
                testName,
                testMaxCapacity,
                testSportsFacility,
                testStartTime,
                testEndtime
        );

        assertEquals(
                "Sports facility not set correctly",
                testSportsFacility,
                lesson.getSportsFacility()
        );
        assertEquals(
                "Start time not set correctly",
                testStartTime,
                lesson.getStartTime()
        );
        assertEquals(
                "End time not set correctly",
                testEndtime,
                lesson.getEndTime()
        );
    }

    @Test
    public void fullConstructor_SetsProperFields() {
        assertEquals(
                "Sports facility not set correctly",
                testSportsFacility,
                lesson.getSportsFacility()
        );
        assertEquals("Start time set incorrectly", testStartTime, lesson.getStartTime());
        assertEquals("End time not set correctly", testEndtime, lesson.getEndTime());
    }

    @Test
    public void getSportsFacility_ReturnsCorrectValue() {
        SportsFacility result = lesson.getSportsFacility();
        assertEquals("Incorrect sports facility returned", testSportsFacility, result);
    }

    @Test
    public void getStartTime_ReturnsCorrectValue() {
        Timestamp result = lesson.getStartTime();
        assertEquals("Incorrect start time returned", testStartTime, result);
    }

    @Test
    public void getEndTime_ReturnsCorrectValue() {
        Timestamp result = lesson.getEndTime();
        assertEquals("Incorrect end time returned", testEndtime, result);
    }

    @Test
    public void setSportsFacility_SetsAppropriateField() {
        SportsFacility newSportsFacility = new SportsFacility();
        SportsFacility oldSportsFacility = lesson.getSportsFacility();

        lesson.setSportsFacility(newSportsFacility);
        SportsFacility result = lesson.getSportsFacility();

        assertEquals("Sports facility not set correctly", newSportsFacility, result);
        assertNotEquals(
                "Sports facility should not remain the same",
                oldSportsFacility,
                lesson.getSportsFacility()
        );
    }

    @Test
    public void setStartTime_SetsAppropriateField() {
        Timestamp newTimestamp = new Timestamp(Calendar.getInstance().getTimeInMillis() + 1000);
        lesson.setStartTime(newTimestamp);

        Timestamp result = lesson.getStartTime();
        assertEquals("Start time not set correctly", newTimestamp, result);
    }

    @Test
    public void setEndTime_SetsAppropriateField() {
        Timestamp newTimestamp = new Timestamp(Calendar.getInstance().getTimeInMillis() + 1000);
        lesson.setEndTime(newTimestamp);

        Timestamp result = lesson.getEndTime();
        assertEquals("End time is not set correctly", newTimestamp, result);
    }

    @Test
    public void toString_ReturnsAnActualString() {
        String result = lesson.toString();
        assertNotEquals("Result should not be null", null, result);
        assertNotEquals("Result should not be empty", "", result);
    }
}
