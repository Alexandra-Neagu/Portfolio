package nl.tudelft.oopp.demo.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The type Question test.
 */
public class QuestionTest {

    private Question q1;
    private Question q2;
    private Question q3;

    private LocalDateTime ldt;

    /**
     * Runs Before each test method.
     */
    @BeforeEach
    public void setUp() {
        ldt = LocalDateTime.of(2021, 1, 1, 10, 30); //01-01-2021, 10:30

        q1 = new Question();
        q2 = new Question("content");
        q3 = new Question("content",1L,2L,true,3L,1, ldt);
    }

    /**
     * Test first constructor.
     */
    @Test
    void testFirstConstructor() {
        assertNotNull(q2);
        assertEquals("content", q2.getContent());
        assertEquals(0L, q2.getAuthorId());
        assertEquals(0L, q2.getLectureId());
        assertFalse(q2.isAnswered());
        assertEquals(0L, q2.getAnswerId());
        assertEquals(0, q2.getUpvotes());
        assertNull(q2.getTimePublished());
    }

    /**
     * Test second constructor.
     */
    @Test
    void testSecondConstructor() {
        assertNotNull(q1);
        assertNull(q1.getContent());
        assertEquals(0L, q1.getAuthorId());
        assertEquals(0L, q1.getLectureId());
        assertFalse(q1.isAnswered());
        assertEquals(0L, q1.getAnswerId());
        assertEquals(0, q1.getUpvotes());
        assertNull(q1.getTimePublished());
    }

    /**
     * Test third constructor.
     */
    @Test
    void testThirdConstructor() {
        assertNotNull(q3);
        assertEquals("content", q3.getContent());
        assertEquals(q3.getAuthorId(), 1L);
        assertEquals(q3.getLectureId(), 2L);
        assertTrue(q3.isAnswered());
        assertEquals(q3.getAnswerId(), 3L);
        assertEquals(q3.getUpvotes(), 1);
        assertEquals(q3.getTimePublished(), ldt);
    }

    /**
     * Test get id.
     */
    @Test
    void testGetId() {
        assertEquals(0L, q1.getId());
        assertEquals(0L, q2.getId());
        assertEquals(0L, q3.getId());
    }

    /**
     * Gets content test.
     */
    @Test
    public void testGetContent() {
        assertNull(q1.getContent());
        assertEquals("content", q2.getContent());
        assertEquals("content", q3.getContent());
    }

    /**
     * Gets author id.
     */
    @Test
    void testGetAuthorId() {
        assertEquals(0L, q1.getAuthorId());
        assertEquals(0L, q2.getAuthorId());
        assertEquals(1L, q3.getAuthorId());
    }

    /**
     * Gets lecture id.
     */
    @Test
    void testGetLectureId() {
        assertEquals(0L, q1.getLectureId());
        assertEquals(0L, q2.getLectureId());
        assertEquals(2L, q3.getLectureId());
    }

    /**
     * Is answered.
     */
    @Test
    void testIsAnswered() {
        assertFalse(q1.isAnswered());
        assertFalse(q2.isAnswered());
        assertTrue(q3.isAnswered());
    }

    /**
     * Gets answer id.
     */
    @Test
    void testGetAnswerId() {
        assertEquals(0L, q1.getAnswerId());
        assertEquals(0L, q2.getAnswerId());
        assertEquals(3L, q3.getAnswerId());
    }

    /**
     * Gets upvotes.
     */
    @Test
    void testGetUpvotes() {
        assertEquals(0, q1.getUpvotes());
        assertEquals(0, q2.getUpvotes());
        assertEquals(1, q3.getUpvotes());
    }

    /**
     * Gets time published.
     */
    @Test
    void testGetTimePublished() {
        assertNull(q1.getTimePublished());
        assertNull(q2.getTimePublished());
        assertEquals(ldt, q3.getTimePublished());
    }

    /**
     * Sets content.
     */
    @Test
    void testSetContent() {
        q1.setContent("test");
        assertEquals("test", q1.getContent());

        // calling the setter again to check whether the field will  actually change
        q1.setContent("another");
        assertEquals("another", q1.getContent());
    }

    /**
     * Sets author id.
     */
    @Test
    void testSetAuthorId() {
        q1.setAuthorId(1L);
        assertEquals(1L, q1.getAuthorId());

        // calling the setter again to check whether the field will  actually change
        q1.setAuthorId(34L);
        assertEquals(34L, q1.getAuthorId());
    }

    /**
     * Sets lecture id.
     */
    @Test
    void testSetLectureId() {
        q1.setLectureId(1L);
        assertEquals(1L, q1.getLectureId());

        // calling the setter again to check whether the field will  actually change
        q1.setLectureId(88L);
        assertEquals(88L, q1.getLectureId());
    }

    /**
     * Sets answered.
     */
    @Test
    void testSetAnswered() {
        q1.setAnswered(true);
        assertTrue(q1.isAnswered());

        // calling the setter again to check whether the field will  actually change
        q1.setAnswered(false);
        assertFalse(q1.isAnswered());
    }

    /**
     * Sets answer id.
     */
    @Test
    void testSetAnswerId() {
        q1.setAnswerId(2L);
        assertEquals(2L, q1.getAnswerId());

        // calling the setter again to check whether the field will  actually change
        q1.setAnswerId(14L);
        assertEquals(14L, q1.getAnswerId());
    }

    /**
     * Sets upvotes.
     */
    @Test
    void testSetUpvotes() {
        q1.setUpvotes(4);
        assertEquals(4, q1.getUpvotes());

        // calling the setter again to check whether the field will  actually change
        q1.setUpvotes(69);
        assertEquals(69, q1.getUpvotes());
    }

    /**
     * Sets time published.
     */
    @Test
    void testSetTimePublished() {
        LocalDateTime ldt1 = LocalDateTime.of(2021, 1, 1, 10, 30); //01-01-2021, 10:30
        LocalDateTime ldt2 = LocalDateTime.of(1996, 1, 1, 10, 30); //01-01-1996, 10:30

        q1.setTimePublished(ldt1);
        assertEquals(ldt1, q1.getTimePublished());

        // calling the setter again to check whether the field will  actually change
        q1.setTimePublished(ldt2);
        assertEquals(ldt2, q1.getTimePublished());
    }

    /**
     * Test equals for equal objects.
     */
    @Test
    void testEqualsEqualObj() {
        Question q4 = new Question("content",1L,2L,true,3L,1, ldt);

        assertTrue(q3.equals(q4));
    }

    /**
     * Test equals for same object.
     */
    @Test
    void testEqualsSameObj() {
        assertTrue(q3.equals(q3));
    }

    /**
     * Test equals for different class null.
     */
    @Test
    void testEqualsNull() {
        Lecturer lect = null;
        assertFalse(q1.equals(lect));
    }

    /**
     * Test equals for different objects.
     */
    @Test
    void testEqualsDiffObj() {
        Question q4 = new Question("content",1L,44L,true,3L,1, ldt);

        assertFalse(q3.equals(q4));
    }
}
