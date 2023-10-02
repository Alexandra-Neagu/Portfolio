package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The type Room test.
 */
public class RoomTest {

    private Room room;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        room = new Room();
    }

    /**
     * Test constructor.
     */
    @Test
    void testConstructor() {
        assertNotNull(room);
        assertEquals(0L, room.getId());
        assertNull(room.getCourseName());
        assertNull(room.getLectureName());
        assertNull(room.getScheduledDate());
        assertNull(room.getStartTime());
        assertNull(room.getEndTime());
        assertNotNull(room.getStudentRoomId());
        assertNotNull(room.getModeratorRoomId());
        assertNotNull(room.getLecturerRoomId());
        assertEquals(0, room.getTooFastCount());
        assertEquals(0, room.getTooSlowCount());
        assertEquals(0, room.getStudentCount());
        assertFalse(room.getHasBeenClosed());
    }

    /**
     * Test get id.
     */
    @Test
    void testGetId() {
        // setting an ID manually to check the getter
        room.setId(100L);
        assertEquals(100L, room.getId());
    }

    /**
     * Test set id.
     */
    @Test
    void testSetId() {
        room.setId(100L);
        assertEquals(100L, room.getId());

        // calling the setter again to check whether the field will  actually change
        room.setId(50L);
        assertEquals(50L, room.getId());
    }

    /**
     * Test get student room id.
     */
    @Test
    void testGetStudentRoomId() {
        assertEquals(5, room.getStudentRoomId().length());
    }

    /**
     * Test set student room id.
     */
    @Test
    void testSetStudentRoomId() {
        room.setStudentRoomId("hello");
        assertEquals("hello", room.getStudentRoomId());

        // calling the setter again to check whether the field will  actually change
        room.setStudentRoomId("mishu");
        assertEquals("mishu", room.getStudentRoomId());
    }

    /**
     * Test get moderator room id.
     */
    @Test
    void testGetModeratorRoomId() {
        assertEquals(7, room.getModeratorRoomId().length());
    }

    /**
     * Test set moderator id.
     */
    @Test
    void testSetModeratorId() {
        room.setModeratorRoomId("yescode");
        assertEquals("yescode", room.getModeratorRoomId());

        // calling the setter again to check whether the field will  actually change
        room.setModeratorRoomId("noseven");
        assertEquals("noseven", room.getModeratorRoomId());
    }

    /**
     * Test get lecturer room id.
     */
    @Test
    void testGetLecturerRoomId() {
        assertEquals(9, room.getLecturerRoomId().length());
    }

    /**
     * Test set lecturer room id.
     */
    @Test
    void testSetLecturerRoomId() {
        room.setLecturerRoomId("alexander");
        assertEquals("alexander", room.getLecturerRoomId());

        // calling the setter again to check whether the field will  actually change
        room.setLecturerRoomId("johnsmith");
        assertEquals("johnsmith", room.getLecturerRoomId());
    }

    /**
     * Test get course name.
     */
    @Test
    void testGetCourseName() {
        assertNull(room.getCourseName());
    }

    /**
     * Test set course name.
     */
    @Test
    void testSetCourseName() {
        room.setCourseName("math");
        assertEquals("math", room.getCourseName());

        // calling the setter again to check whether the field will  actually change
        room.setCourseName("bio");
        assertEquals("bio", room.getCourseName());
    }

    /**
     * Test get lecture name.
     */
    @Test
    void testGetLectureName() {
        assertNull(room.getLectureName());
    }

    /**
     * Test set lecture name.
     */
    @Test
    void testSetLectureName() {
        room.setLectureName("calculus");
        assertEquals("calculus", room.getLectureName());

        // calling the setter again to check whether the field will  actually change
        room.setLectureName("english");
        assertEquals("english", room.getLectureName());
    }

    /**
     * Test get scheduled date.
     */
    @Test
    void testGetScheduledDate() {
        assertNull(room.getScheduledDate());
    }

    /**
     * Test set scheduled date.
     */
    @Test
    void testSetScheduledDate() {
        LocalDate ld1 = LocalDate.of(2019, 11, 3); // 03-11-2019
        LocalDate ld2 = LocalDate.of(1995, 2, 23); // 23-02-1995

        room.setScheduledDate(ld1);
        assertEquals(ld1, room.getScheduledDate());

        // calling the setter again to check whether the field will  actually change
        room.setScheduledDate(ld2);
        assertEquals(ld2, room.getScheduledDate());
    }

    /**
     * Test get start time.
     */
    @Test
    void testGetStartTime() {
        assertNull(room.getStartTime());
    }

    /**
     * Test set start time.
     */
    @Test
    void testSetStartTime() {
        LocalTime lt1 = LocalTime.of(19, 30); // 19:30
        LocalTime lt2 = LocalTime.of(14, 1); // 14:01

        room.setStartTime(lt1);
        assertEquals(lt1, room.getStartTime());

        // calling the setter again to check whether the field will  actually change
        room.setStartTime(lt2);
        assertEquals(lt2, room.getStartTime());
    }

    /**
     * Test get end time.
     */
    @Test
    void testGetEndTime() {
        assertNull(room.getEndTime());
    }

    /**
     * Test set end time.
     */
    @Test
    void testSetEndTime() {
        LocalTime lt1 = LocalTime.of(19, 30); // 19:30
        LocalTime lt2 = LocalTime.of(14, 1); // 14:01

        room.setEndTime(lt1);
        assertEquals(lt1, room.getEndTime());

        // calling the setter again to check whether the field will  actually change
        room.setEndTime(lt2);
        assertEquals(lt2, room.getEndTime());
    }

    /**
     * Test get has been closed.
     */
    @Test
    void testGetHasBeenClosed() {
        assertFalse(room.getHasBeenClosed());
    }

    /**
     * Test set has been closed.
     */
    @Test
    void testSetHasBeenClosed() {
        room.setHasBeenClosed(true);
        assertTrue(room.getHasBeenClosed());

        // calling the setter again to check whether the field will  actually change
        room.setHasBeenClosed(false);
        assertFalse(room.getHasBeenClosed());
    }

    /**
     * Test increment too fast count.
     */
    @Test
    void testIncrementTooFastCount() {
        assertEquals(0, room.getTooFastCount());
        room.incrementTooFastCount();
        assertEquals(1, room.getTooFastCount());
    }

    /**
     * Test increment too slow count.
     */
    @Test
    void testIncrementTooSlowCount() {
        assertEquals(0, room.getTooSlowCount());
        room.incrementTooSlowCount();
        assertEquals(1, room.getTooSlowCount());
    }

    /**
     * Test get too fast count.
     */
    @Test
    void testGetTooFastCount() {
        assertEquals(0, room.getTooFastCount());
    }

    /**
     * Test set too fast count.
     */
    @Test
    void testSetTooFastCount() {
        room.setTooFastCount(9);
        assertEquals(9, room.getTooFastCount());

        // calling the setter again to check whether the field will  actually change
        room.setTooFastCount(17);
        assertEquals(17, room.getTooFastCount());
    }

    /**
     * Test get too slow count.
     */
    @Test
    void testGetTooSlowCount() {
        assertEquals(0, room.getTooSlowCount());
    }

    /**
     * Test set too slow count.
     */
    @Test
    void testSetTooSlowCount() {
        room.setTooSlowCount(5);
        assertEquals(5, room.getTooSlowCount());

        // calling the setter again to check whether the field will  actually change
        room.setTooSlowCount(23);
        assertEquals(23, room.getTooSlowCount());
    }

    /**
     * Test increase student count.
     */
    @Test
    void testIncreaseStudentCount() {
        assertEquals(0, room.getStudentCount());
        room.increaseStudentCount();
        assertEquals(1, room.getStudentCount());
    }

    /**
     * Test decrease student count.
     */
    @Test
    void testDecreaseStudentCount() {
        assertEquals(0, room.getStudentCount());
        room.decreaseStudentCount();
        assertEquals(-1, room.getStudentCount());
    }

    /**
     * Test get student count.
     */
    @Test
    void testGetStudentCount() {
        assertEquals(0, room.getStudentCount());
    }

    /**
     * Test equals for equal objects.
     */
    @Test
    void testEqualsEqualObj() {
        Room r2 = new Room();

        room.setStudentRoomId("s");
        r2.setStudentRoomId("s");

        room.setModeratorRoomId("m");
        r2.setModeratorRoomId("m");

        room.setLecturerRoomId("l");
        r2.setLecturerRoomId("l");

        assertTrue(room.equals(r2));
    }

    /**
     * Test equals for same object.
     */
    @Test
    void testEqualsSameObj() {
        assertTrue(room.equals(room));
    }

    /**
     * Test equals for different class null.
     */
    @Test
    void testEqualsNull() {
        Lecturer lect = null;
        assertFalse(room.equals(lect));
    }

    /**
     * Test equals for different objects.
     */
    @Test
    void testEqualsDiffObj() {
        Room r2 = new Room();

        // even though codes are randomly generated
        // there is still a (very) small chance for r and r2 to have the same codes
        // this way, we ensure they are different
        room.setStudentRoomId("s");
        r2.setStudentRoomId("diff code");

        assertFalse(room.equals(r2));
    }

    /**
     * Test to string for file.
     */
    @Test
    void testToStringForFile() {
        room.setCourseName("Calculus");
        room.setLectureName("Similarity");
        room.setScheduledDate(LocalDate.of(2019, 11, 3)); // 03-11-2019
        room.setStartTime(LocalTime.of(17, 0)); // 17:00
        room.setEndTime(LocalTime.of(19, 30)); // 19:30

        String text = "Calculus - Similarity\n" + room.getScheduledDate().toString()
                    + ", 17:00 - 19:30\n";

        assertEquals(text, room.toStringForFile());
    }
}
