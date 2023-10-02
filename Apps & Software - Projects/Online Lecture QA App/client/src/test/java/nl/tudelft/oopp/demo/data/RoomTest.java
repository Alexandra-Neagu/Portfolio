package nl.tudelft.oopp.demo.data;

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
class RoomTest {

    private Room r1;
    private Room r2;
    private Room r3;
    private Room r4;

    private LocalDate ld;
    private LocalTime start;
    private LocalTime end;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        ld = LocalDate.of(2021,1,1); // 01-01-2021
        start = LocalTime.of(10,0); // 10:00
        end = LocalTime.of(12,0); // 12:00

        r1 = new Room("stuCode", "modCode", "lectCode");
        r2 = new Room("OOPP","git", ld, start, end);
        r3 = new Room("OOPP","gitlab", ld, start, end,"stuCode", "modCode", "lectCode",1,2);
        r4 = new Room("OOPP", "git", start, end);
    }

    /**
     * Test first constructor.
     */
    @Test
    void testFirstConstructor() {
        assertNotNull(r1);
        assertNull(r1.getCourseName());
        assertNull(r1.getLectureName());
        assertNull(r1.getScheduledDate());
        assertNull(r1.getStartTime());
        assertNull(r1.getEndTime());
        assertEquals("stuCode", r1.getStudentRoomId());
        assertEquals("modCode", r1.getModeratorRoomId());
        assertEquals("lectCode", r1.getLecturerRoomId());
        assertEquals(0, r1.getTooFastCount());
        assertEquals(0, r1.getTooSlowCount());
        assertEquals(0L, r1.getId());
        assertFalse(r1.getHasBeenClosed());
    }

    /**
     * Test second constructor.
     */
    @Test
    void testSecondConstructor() {
        assertNotNull(r3);
        assertEquals("OOPP", r3.getCourseName());
        assertEquals("gitlab", r3.getLectureName());
        assertEquals(ld, r3.getScheduledDate());
        assertEquals(start, r3.getStartTime());
        assertEquals(end, r3.getEndTime());
        assertEquals("stuCode", r3.getStudentRoomId());
        assertEquals("modCode", r3.getModeratorRoomId());
        assertEquals("lectCode", r3.getLecturerRoomId());
        assertEquals(1, r3.getTooFastCount());
        assertEquals(2, r3.getTooSlowCount());
        assertEquals(0L, r3.getId());
        assertFalse(r3.getHasBeenClosed());
    }

    /**
     * Test third constructor.
     */
    @Test
    void testThirdConstructor() {
        assertNotNull(r2);
        assertEquals("OOPP", r2.getCourseName());
        assertEquals("git", r2.getLectureName());
        assertEquals(ld, r2.getScheduledDate());
        assertEquals(start, r2.getStartTime());
        assertEquals(end, r2.getEndTime());
        assertNull(r2.getStudentRoomId());
        assertNull(r2.getModeratorRoomId());
        assertNull(r2.getLecturerRoomId());
        assertEquals(0, r2.getTooFastCount());
        assertEquals(0, r2.getTooSlowCount());
        assertEquals(0L, r2.getId());
        assertFalse(r2.getHasBeenClosed());
    }

    /**
     * Test fourth constructor.
     */
    @Test
    void testFourthConstructor() {
        assertNotNull(r4);
        assertEquals("OOPP", r4.getCourseName());
        assertEquals("git", r4.getLectureName());
        assertNull(r4.getScheduledDate());
        assertEquals(start, r4.getStartTime());
        assertEquals(end, r4.getEndTime());
        assertNull(r4.getStudentRoomId());
        assertNull(r4.getModeratorRoomId());
        assertNull(r4.getLecturerRoomId());
        assertEquals(0, r4.getTooFastCount());
        assertEquals(0, r4.getTooSlowCount());
        assertEquals(0L, r4.getId());
        assertFalse(r4.getHasBeenClosed());
    }

    /**
     * Test equals for equal objects.
     */
    @Test
    void testEqualsEqualObj() {
        LocalDate ld = LocalDate.of(2021,1,1);
        LocalTime st = LocalTime.of(10,0);
        LocalTime e = LocalTime.of(12,0);
        Room room = new Room("OOPP","git", ld, st, e);

        assertTrue(room.equals(r2));
    }

    /**
     * Test equals same object.
     */
    @Test
    void testEqualsSameObj() {
        assertTrue(r1.equals(r1));
    }

    /**
     * Test equals for different class null.
     */
    @Test
    void testEqualsNull() {
        Lecturer lect = null;
        assertFalse(r1.equals(lect));
    }

    /**
     * Test equals for different objects.
     */
    @Test
    void testEqualsDiffObj() {
        LocalDate ld = LocalDate.of(2021,1,1);
        LocalTime st = LocalTime.of(10,0);
        LocalTime e = LocalTime.of(12,0);
        Room room = new Room("Text","git",ld,st,e);

        assertFalse(room.equals(r2));
    }

    /**
     * Gets course name.
     */
    @Test
    void testGetCourseName() {
        assertNull(r1.getCourseName());
        assertEquals("OOPP", r2.getCourseName());
        assertEquals("OOPP", r3.getCourseName());
        assertEquals("OOPP", r4.getCourseName());
    }

    /**
     * Gets lecture name.
     */
    @Test
    void testGetLectureName() {
        assertNull(r1.getLectureName());
        assertEquals("git", r2.getLectureName());
        assertEquals("gitlab", r3.getLectureName());
        assertEquals("git", r4.getLectureName());
    }

    /**
     * Gets scheduled date.
     */
    @Test
    void testGetScheduledDate() {
        assertNull(r1.getScheduledDate());
        assertEquals(ld, r2.getScheduledDate());
        assertEquals(ld, r3.getScheduledDate());
        assertNull(r4.getScheduledDate());
    }

    /**
     * Gets start time.
     */
    @Test
    void testGetStartTime() {
        assertNull(r1.getStartTime());
        assertEquals(start, r2.getStartTime());
        assertEquals(start, r3.getStartTime());
        assertEquals(start, r4.getStartTime());
    }

    /**
     * Gets end time.
     */
    @Test
    void testGetEndTime() {
        assertNull(r1.getEndTime());
        assertEquals(end, r2.getEndTime());
        assertEquals(end, r3.getEndTime());
        assertEquals(end, r4.getEndTime());
    }

    /**
     * Gets student room id.
     */
    @Test
    void testGetStudentRoomId() {
        assertEquals("stuCode", r1.getStudentRoomId());
        assertNull(r2.getStudentRoomId());
        assertEquals("stuCode", r3.getStudentRoomId());
        assertNull(r4.getStudentRoomId());
    }

    /**
     * Gets moderator room id.
     */
    @Test
    void testGetModeratorRoomId() {
        assertEquals("modCode", r1.getModeratorRoomId());
        assertNull(r2.getModeratorRoomId());
        assertEquals("modCode", r3.getModeratorRoomId());
        assertNull(r4.getModeratorRoomId());
    }

    /**
     * Gets lecturer room id.
     */
    @Test
    void testGetLecturerRoomId() {
        assertEquals("lectCode", r1.getLecturerRoomId());
        assertNull(r2.getLecturerRoomId());
        assertEquals("lectCode", r3.getLecturerRoomId());
        assertNull(r4.getLecturerRoomId());
    }

    /**
     * Gets too fast count.
     */
    @Test
    void testGetTooFastCount() {
        assertEquals(0, r1.getTooFastCount());
        assertEquals(0, r2.getTooFastCount());
        assertEquals(1, r3.getTooFastCount());
        assertEquals(0, r4.getTooFastCount());
    }

    /**
     * Gets too slow count.
     */
    @Test
    void testGetTooSlowCount() {
        assertEquals(0, r1.getTooSlowCount());
        assertEquals(0, r2.getTooSlowCount());
        assertEquals(2, r3.getTooSlowCount());
        assertEquals(0, r4.getTooSlowCount());
    }

    /**
     * Test set course name.
     */
    @Test
    void testSetCourseName() {
        r1.setCourseName("math");
        assertEquals(r1.getCourseName(), "math");

        // calling the setter again to check whether the field will  actually change
        r1.setCourseName("bio");
        assertEquals(r1.getCourseName(), "bio");
    }

    /**
     * Test set lecture name.
     */
    @Test
    void testSetLectureName() {
        r1.setLectureName("Gram-Schmidt");
        assertEquals(r1.getLectureName(), "Gram-Schmidt");

        // calling the setter again to check whether the field will  actually change
        r1.setLectureName("limits");
        assertEquals(r1.getLectureName(), "limits");
    }

    /**
     * Test set scheduled date.
     */
    @Test
    void testSetScheduledDate() {
        LocalDate ld1 = LocalDate.of(2050,3,7); // 07-03-2050
        LocalDate ld2 = LocalDate.of(2041,3,7); // 07-03-2041

        r1.setScheduledDate(ld1);
        assertEquals(ld1, r1.getScheduledDate());

        // calling the setter again to check whether the field will  actually change
        r1.setScheduledDate(ld2);
        assertEquals(ld2, r1.getScheduledDate());
    }

    /**
     * Test set start time.
     */
    @Test
    void testSetStartTime() {
        LocalTime st1 = LocalTime.of(9,25); // 09:25
        LocalTime st2 = LocalTime.of(17,25); // 17:25

        r1.setStartTime(st1);
        assertEquals(st1, r1.getStartTime());

        // calling the setter again to check whether the field will  actually change
        r1.setStartTime(st2);
        assertEquals(st2, r1.getStartTime());
    }

    /**
     * Test set end time.
     */
    @Test
    void testSetEndTime() {
        LocalTime et1 = LocalTime.of(20,31); // 20:31
        LocalTime et2 = LocalTime.of(13,31); // 13:31

        r1.setEndTime(et1);
        assertEquals(et1, r1.getEndTime());

        // calling the setter again to check whether the field will  actually change
        r1.setEndTime(et2);
        assertEquals(et2, r1.getEndTime());
    }

    /**
     * Test set student room id.
     */
    @Test
    void testSetStudentRoomId() {
        r1.setStudentRoomId("go");
        assertEquals("go", r1.getStudentRoomId());

        // calling the setter again to check whether the field will  actually change
        r1.setStudentRoomId("another");
        assertEquals("another", r1.getStudentRoomId());
    }

    /**
     * Test set moderator room id.
     */
    @Test
    void testSetModeratorRoomId() {
        r1.setModeratorRoomId("yes");
        assertEquals("yes", r1.getModeratorRoomId());

        // calling the setter again to check whether the field will  actually change
        r1.setModeratorRoomId("no");
        assertEquals("no", r1.getModeratorRoomId());
    }

    /**
     * Test set lecturer room id.
     */
    @Test
    void testSetLecturerRoomId() {
        r1.setLecturerRoomId("yes");
        assertEquals("yes", r1.getLecturerRoomId());

        // calling the setter again to check whether the field will  actually change
        r1.setLecturerRoomId("no");
        assertEquals("no", r1.getLecturerRoomId());
    }

    /**
     * Test get id.
     */
    @Test
    void testGetId() {
        assertEquals(0L, r1.getId());
        assertEquals(0L, r2.getId());
        assertEquals(0L, r3.getId());
        assertEquals(0L, r4.getId());
    }

    /**
     * Test set id.
     */
    @Test
    void testSetId() {
        r1.setId(7L);
        assertEquals(r1.getId(), 7L);

        // calling the setter again to check whether the field will  actually change
        r1.setId(23L);
        assertEquals(r1.getId(), 23L);
    }

    /**
     * Test to string.
     */
    @Test
    void testToString() {
        r3.setId(1);
        String text = "Room{id='1', courseName='OOPP', lectureName='gitlab', scheduledDate="
                        + r3.getScheduledDate()
                        + ", startTime=" + r3.getStartTime()
                        + ", endTime=" + r3.getEndTime()
                        + ", studentRoomId=stuCode, moderatorRoomId=modCode, "
                        + "lecturerRoomId=lectCode}";
        assertEquals(r3.toString(), text);
    }

    /**
     * Test get has been closed.
     */
    @Test
    void testGetHasBeenClosed() {
        assertFalse(r1.getHasBeenClosed());
        assertFalse(r2.getHasBeenClosed());
        assertFalse(r3.getHasBeenClosed());
        assertFalse(r4.getHasBeenClosed());
    }

    /**
     * Test set has been closed.
     */
    @Test
    void testSetHasBeenClosed() {
        r1.setHasBeenClosed(true);
        assertTrue(r1.getHasBeenClosed());

        // calling the setter again to check whether the field will  actually change
        r1.setHasBeenClosed(false);
        assertFalse(r1.getHasBeenClosed());
    }



}