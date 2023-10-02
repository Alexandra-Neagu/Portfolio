package nl.tudelft.oopp.demo.entities;

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

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        q1 = new Question("what");
        q2 = new Question();
    }

    /**
     * Test first constructor.
     */
    @Test
    void testFirstConstructor() {
        assertNotNull(q1);
        assertEquals(0L, q1.getId());
        assertEquals("what", q1.getContent());
        assertEquals(0L, q1.getAuthorId());
        assertNotNull(q1.getTimePublished());
        assertEquals(0L, q1.getLectureId());
        assertFalse(q1.isAnswered());
        assertEquals(0L, q1.getAnswerId());
        assertEquals(0, q1.getUpvotes());
    }

    /**
     * Test second constructor.
     */
    @Test
    void testSecondConstructor() {
        assertNotNull(q2);
        assertEquals(0L, q2.getId());
        assertNull(q2.getContent());
        assertEquals(0L, q2.getAuthorId());
        assertNotNull(q2.getTimePublished());
        assertEquals(0L, q2.getLectureId());
        assertFalse(q2.isAnswered());
        assertEquals(0L, q2.getAnswerId());
        assertEquals(0, q2.getUpvotes());
    }

    /**
     * Test set id.
     */
    @Test
    void testSetId() {
        q1.setId(100L);
        assertEquals(100L, q1.getId());

        // calling the setter again to check whether the field will  actually change
        q1.setId(50L);
        assertEquals(50L, q1.getId());
    }

    /**
     * Test set time published.
     */
    @Test
    void testSetTimePublished() {
        LocalDateTime ldt1 = LocalDateTime.of(2021, 1, 1, 10, 30); //01-01-2021, 10:30
        LocalDateTime ldt2 = LocalDateTime.of(2028, 1, 1, 10, 30); //01-01-2028, 10:30

        q1.setTimePublished(ldt1);
        assertEquals(ldt1, q1.getTimePublished());

        // calling the setter again to check whether the field will  actually change
        q1.setTimePublished(ldt2);
        assertEquals(ldt2, q1.getTimePublished());
    }

    /**
     * Test set upvotes.
     */
    @Test
    void testSetUpvotes() {
        q1.setUpvotes(2);
        assertEquals(2, q1.getUpvotes());

        // calling the setter again to check whether the field will  actually change
        q1.setUpvotes(14);
        assertEquals(14, q1.getUpvotes());
    }

    /**
     * Test get id.
     */
    @Test
    void testGetId() {
        // setting an ID manually to check the getter
        q1.setId(100L);
        assertEquals(100L, q1.getId());
    }

    /**
     * Test get content.
     */
    @Test
    void testGetContent() {
        assertEquals("what", q1.getContent());
        assertNull(q2.getContent());
    }

    /**
     * Test set content.
     */
    @Test
    void testSetContent() {
        q1.setContent("no");
        assertEquals("no", q1.getContent());

        // calling the setter again to check whether the field will  actually change
        q1.setContent("yes");
        assertEquals("yes", q1.getContent());
    }

    /**
     * Test get author id.
     */
    @Test
    void testGetAuthorId() {
        assertEquals(0L, q1.getAuthorId());
        assertEquals(0L, q2.getAuthorId());

    }

    /**
     * Test set author id.
     */
    @Test
    void testSetAuthorId() {
        q1.setAuthorId(6L);
        assertEquals(6L, q1.getAuthorId());

        // calling the setter again to check whether the field will  actually change
        q1.setAuthorId(13L);
        assertEquals(13L, q1.getAuthorId());
    }

    /**
     * Test get lecture id.
     */
    @Test
    void testGetLectureId() {
        assertEquals(0L, q1.getLectureId());
        assertEquals(0L, q2.getLectureId());
    }

    /**
     * Test set lecture id.
     */
    @Test
    void testSetLectureId() {
        q1.setLectureId(6L);
        assertEquals(6L, q1.getLectureId());

        // calling the setter again to check whether the field will  actually change
        q1.setLectureId(13L);
        assertEquals(13L, q1.getLectureId());
    }

    /**
     * Test is answered.
     */
    @Test
    void testIsAnswered() {
        assertFalse(q1.isAnswered());
        assertFalse(q2.isAnswered());
    }

    /**
     * Test set answered.
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
     * Test get answer id.
     */
    @Test
    void testGetAnswerId() {
        assertEquals(0L, q1.getAnswerId());
        assertEquals(0L, q2.getAnswerId());
    }

    /**
     * Test set answer id.
     */
    @Test
    void testSetAnswerId() {
        q1.setAnswerId(4L);
        assertEquals(4L, q1.getAnswerId());

        // calling the setter again to check whether the field will  actually change
        q1.setAnswerId(20L);
        assertEquals(20L, q1.getAnswerId());
    }

    /**
     * Test get time published.
     */
    @Test
    void testGetTimePublished() {
        // cannot test with LocalDateTime.now() as set in the constructor
        // as we cannot get it exactly at the time of initialisation of questions
        // due to fractions of seconds passing
        LocalDateTime ldt = LocalDateTime.of(2021, 1, 1, 10, 30); //01-01-2021, 10:30

        q1.setTimePublished(ldt);
        assertEquals(ldt, q1.getTimePublished());
    }

    /**
     * Test get upvotes.
     */
    @Test
    void testGetUpvotes() {
        assertEquals(0, q1.getUpvotes());
        assertEquals(0, q2.getUpvotes());
    }

    /**
     * Test upvote.
     */
    @Test
    void testUpvote() {
        assertEquals(0, q1.getUpvotes());
        q1.upvote();
        assertEquals(1, q1.getUpvotes());
    }

    /**
     * Test equals for equal objects.
     */
    @Test
    void testEqualsEqualObj() {
        LocalDateTime ldt = LocalDateTime.of(2021, 1, 1, 10, 30); //01-01-2021, 10:30
        Question q3 = new Question("what");

        q1.setTimePublished(ldt);
        q3.setTimePublished(ldt);

        assertTrue(q1.equals(q3));
    }

    /**
     * Test equals for same object.
     */
    @Test
    void testEqualsSameObj() {
        assertTrue(q1.equals(q1));
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
        LocalDateTime ldt = LocalDateTime.of(2021, 1, 1, 10, 30); //01-01-2021, 10:30
        Question q3 = new Question("diff content");

        q1.setTimePublished(ldt);
        q3.setTimePublished(ldt);

        assertFalse(q1.equals(q3));
    }

    /**
     * Test to string.
     */
    @Test
    void testToString() {
        String text = "Question{id=0, content='what', authorId=0, timePublished="
                + q1.getTimePublished().toString()
                + ", lectureId=0, isAnswered=false, answerId=0, upvotes=0}";

        assertEquals(text, q1.toString());
    }

    /**
     * Test to string for file.
     */
    @Test
    void testToStringForFile() {
        LocalDateTime ldt = LocalDateTime.of(2021, 1, 1, 10, 30); //01-01-2021, 10:30
        q1.setTimePublished(ldt);

        String text = "what - " + ldt.toLocalDate().toString() + ", 10:30\n";

        assertEquals(text, q1.toStringForFile());
    }
}
