package nl.tudelft.oopp.demo.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The type Answer test.
 */
class AnswerTest {

    private Answer a1;
    private Answer a2;
    private Answer a3;

    private LocalDateTime ldt;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        ldt = LocalDateTime.of(2021, 1, 1, 10, 30); //01-01-2021, 10:30

        a1 = new Answer();
        a2 = new Answer(ldt, 7L, 1L,"solution");
        a3 = new Answer("content");
    }

    /**
     * Test first constructor.
     */
    @Test
    void testFirstConstructor() {
        assertNotNull(a1);
        assertNull(a1.getTimePublished());
        assertEquals(0L, a1.getModeratorId());
        assertEquals(0L, a1.getLecturerId());
        assertNull(a1.getContent());
    }

    /**
     * Test second constructor.
     */
    @Test
    void testSecondConstructor() {
        assertNotNull(a2);
        assertEquals(ldt, a2.getTimePublished());
        assertEquals(7L, a2.getModeratorId());
        assertEquals(1L, a2.getLecturerId());
        assertEquals("solution", a2.getContent());
    }

    /**
     * Test third constructor.
     */
    @Test
    void testThirdConstructor() {
        assertNotNull(a3);
        assertNull(a3.getTimePublished());
        assertEquals(0L, a3.getModeratorId());
        assertEquals(0L, a3.getLecturerId());
        assertEquals("content", a3.getContent());
    }

    /**
     * Test get time published.
     */
    @Test
    void testGetTimePublished() {
        assertNull(a1.getTimePublished());
        assertEquals(ldt, a2.getTimePublished());
        assertNull(a3.getTimePublished());
    }

    /**
     * Test get moderator id.
     */
    @Test
    void testGetModeratorId() {
        assertEquals(0L, a1.getModeratorId());
        assertEquals(7L, a2.getModeratorId());
        assertEquals(0L, a3.getModeratorId());
    }

    /**
     * Test get lecturer id.
     */
    @Test
    void testGetLecturerId() {
        assertEquals(0L, a1.getLecturerId());
        assertEquals(1L, a2.getLecturerId());
        assertEquals(0L, a3.getLecturerId());
    }

    /**
     * Test get content.
     */
    @Test
    void testGetContent() {
        assertNull(a1.getContent());
        assertEquals("solution", a2.getContent());
        assertEquals("content", a3.getContent());
    }

    /**
     * Test get id.
     */
    @Test
    void testGetId() {
        assertEquals(0L, a1.getId());
        assertEquals(0L, a2.getId());
        assertEquals(0L, a3.getId());
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

}
