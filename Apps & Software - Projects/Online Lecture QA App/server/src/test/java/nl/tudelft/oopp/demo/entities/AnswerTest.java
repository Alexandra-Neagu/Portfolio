package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The type Answer test.
 */
class AnswerTest {

    private Answer a1;
    private Answer a2;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        a1 = new Answer("It's not.");
        a2 = new Answer();
    }

    /**
     * Test first constructor.
     */
    @Test
    void testFirstConstructor() {
        assertNotNull(a1);
        assertEquals("It's not.", a1.getContent());
        assertNotNull(a1.getTimePublished());
        assertEquals(0L, a1.getLecturerId());
        assertEquals(0L, a1.getModeratorId());
        assertEquals(0L, a1.getId());
    }

    /**
     * Test second constructor.
     */
    @Test
    void testSecondConstructor() {
        assertNotNull(a2);
        assertNull(a2.getContent());
        assertNotNull(a2.getTimePublished());
        assertEquals(0L, a1.getLecturerId());
        assertEquals(0L, a1.getModeratorId());
        assertEquals(0L, a1.getId());
    }

    /**
     * Test get id.
     */
    @Test
    void testGetId() {
        // setting an ID manually to check the getter
        a1.setId(100L);
        assertEquals(100L, a1.getId());
    }

    /**
     * Test set id.
     */
    @Test
    void testSetId() {
        a1.setId(100L);
        assertEquals(100L, a1.getId());

        // calling the setter again to check whether the field will  actually change
        a1.setId(50L);
        assertEquals(50L, a1.getId());
    }

    /**
     * Test get time published.
     */
    @Test
    void testGetTimePublished() {
        // cannot test with LocalDateTime.now() as set in the constructor
        // as we cannot get it exactly at the time of initialisation of answers
        // due to fractions of seconds passing
        LocalDateTime ldt = LocalDateTime.of(2021, 1, 1, 10, 30); //01-01-2021, 10:30

        a1.setTimePublished(ldt);
        assertEquals(ldt, a1.getTimePublished());
    }

    /**
     * Test set time published.
     */
    @Test
    void testSetTimePublished() {
        LocalDateTime ldt1 = LocalDateTime.of(2021, 1, 1, 10, 30); //01-01-2021, 10:30
        LocalDateTime ldt2 = LocalDateTime.of(2028, 1, 1, 10, 30); //01-01-2028, 10:30

        a1.setTimePublished(ldt1);
        assertEquals(ldt1, a1.getTimePublished());

        // calling the setter again to check whether the field will  actually change
        a1.setTimePublished(ldt2);
        assertEquals(ldt2, a1.getTimePublished());
    }

    /**
     * Test get moderator id.
     */
    @Test
    void testGetModeratorId() {
        assertEquals(0L, a1.getModeratorId());
        assertEquals(0L, a2.getModeratorId());
    }

    /**
     * Test set moderator id.
     */
    @Test
    void testSetModeratorId() {
        a1.setModeratorId(8L);
        assertEquals(8L, a1.getModeratorId());

        // calling the setter again to check whether the field will  actually change
        a1.setModeratorId(15L);
        assertEquals(15L, a1.getModeratorId());
    }

    /**
     * Test get lecture id.
     */
    @Test
    void testGetLectureId() {
        assertEquals(0L, a1.getModeratorId());
        assertEquals(0L, a2.getModeratorId());
    }

    /**
     * Test set lecture id.
     */
    @Test
    void testSetLectureId() {
        a1.setLecturerId(8L);
        assertEquals(8L, a1.getLecturerId());

        // calling the setter again to check whether the field will  actually change
        a1.setLecturerId(15L);
        assertEquals(15L, a1.getLecturerId());
    }

    /**
     * Test get content.
     */
    @Test
    void testGetContent() {
        assertEquals("It's not.", a1.getContent());
        assertNull(a2.getContent());
    }

    /**
     * Test set content.
     */
    @Test
    void testSetContent() {
        a1.setContent("no");
        assertEquals("no", a1.getContent());

        // calling the setter again to check whether the field will  actually change
        a1.setContent("yes");
        assertEquals("yes", a1.getContent());
    }

    /**
     * Test equals for equal objects.
     */
    @Test
    void testEqualsEqualObj() {
        LocalDateTime ldt = LocalDateTime.of(2021, 1, 1, 10, 30); //01-01-2021, 10:30
        Answer a3 = new Answer("It's not.");

        a1.setTimePublished(ldt);
        a3.setTimePublished(ldt);

        assertTrue(a1.equals(a3));
    }

    /**
     * Test equals for same object.
     */
    @Test
    void testEqualsSameObj() {
        assertTrue(a1.equals(a1));
    }

    /**
     * Test equals for different class null.
     */
    @Test
    void testEqualsNull() {
        Lecturer lecturer = null;
        assertFalse(a1.equals(lecturer));
    }

    /**
     * Test equals for different objects.
     */
    @Test
    void testEqualsDiffObj() {
        LocalDateTime ldt = LocalDateTime.of(2021, 1, 1, 10, 30); //01-01-2021, 10:30
        Answer a3 = new Answer("nee");

        a1.setTimePublished(ldt);
        a3.setTimePublished(ldt);

        assertFalse(a1.equals(a3));
    }

    /**
     * Test to string.
     */
    @Test
    void testToString() {
        LocalDateTime ldt = LocalDateTime.of(2021, 1, 1, 10, 30); //01-01-2021, 10:30

        a1.setTimePublished(ldt);
        a1.setId(7L);
        a1.setModeratorId(8L);
        a1.setLecturerId(9L);

        String text = "Answer{id=7, timePublished=" + a1.getTimePublished().toString()
                + ", moderatorId=8, lecturerId=9, content='It's not.'}";
        assertEquals(text, a1.toString());
    }
}