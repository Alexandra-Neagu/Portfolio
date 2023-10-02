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
 * The type Student test.
 */
public class StudentTest {

    private Student s1;
    private Student s2;
    private Student s3;

    private LocalDateTime ldt;

    /**
     * Before.
     */
    @BeforeEach
    void setUp() {
        ldt = LocalDateTime.of(2021, 1, 1, 10, 30); //01-01-2021, 10:30

        s1 = new Student();
        s2 = new Student("testName");
        s3 = new Student("testName","testIp",2L, ldt);
    }

    /**
     * Test first constructor.
     */
    @Test
    void testFirstConstructor() {
        assertNotNull(s3);
        assertEquals("testName", s3.getName());
        assertEquals("testIp", s3.getIpAddress());
        assertEquals(2L, s3.getRoomId());
        assertEquals(ldt, s3.getTimeOfJoin());
    }

    /**
     * Test third constructor.
     */
    @Test
    void testSecondConstructor() {
        assertNotNull(s1);
        assertNull(s1.getName());
        assertNull(s1.getIpAddress());
        assertEquals(0L, s1.getRoomId());
        assertNull(s1.getTimeOfJoin());
    }

    /**
     * Test second constructor.
     */
    @Test
    void testThirdConstructor() {
        assertNotNull(s2);
        assertEquals(s2.getName(), "testName");
        assertNull(s2.getIpAddress());
        assertEquals(0L, s2.getRoomId());
        assertNull(s2.getTimeOfJoin());
    }

    /**
     * Test get id.
     */
    @Test
    void testGetId() {
        assertEquals(0L, s1.getId());
        assertEquals(0L, s2.getId());
        assertEquals(0L, s3.getId());
    }

    /**
     * Test set id.
     */
    @Test
    void testSetId() {
        s1.setId(15L);
        assertEquals(s1.getId(), 15L);

        // calling the setter again to check whether the field will  actually change
        s1.setId(24L);
        assertEquals(s1.getId(), 24L);
    }

    /**
     * Test set name.
     */
    @Test
    void testSetName() {
        s1.setName("Gigi");
        assertEquals(s1.getName(), "Gigi");

        // calling the setter again to check whether the field will  actually change
        s1.setName("Bloomy");
        assertEquals(s1.getName(), "Bloomy");
    }

    /**
     * Test set ip address.
     */
    @Test
    void testSetIpAddress() {
        s1.setIpAddress("127.0.0.0");
        assertEquals(s1.getIpAddress(), "127.0.0.0");

        // calling the setter again to check whether the field will  actually change
        s1.setIpAddress("234.1.7.18");
        assertEquals(s1.getIpAddress(), "234.1.7.18");
    }

    /**
     * Sets Room Id test.
     */
    @Test
    void testSetRoomId() {
        s2.setRoomId(2L);
        assertEquals(s2.getRoomId(), 2L);

        // calling the setter again to check whether the field will  actually change
        s2.setRoomId(98L);
        assertEquals(s2.getRoomId(), 98L);
    }

    /**
     * Test set time of join.
     */
    @Test
    void testSetTimeOfJoin() {
        LocalDateTime joinTime1 = LocalDateTime.of(2024, 2, 15, 14, 29); //15-02-2024, 14:29
        LocalDateTime joinTime2 = LocalDateTime.of(2056, 10, 17, 23, 42); //17-10-2056, 23:42

        s1.setTimeOfJoin(joinTime1);
        assertEquals(s1.getTimeOfJoin(), joinTime1);

        // calling the setter again to check whether the field will  actually change
        s1.setTimeOfJoin(joinTime2);
        assertEquals(s1.getTimeOfJoin(), joinTime2);
    }

    /**
     * Test to string.
     */
    @Test
    void testToString() {
        String text = "Student {name='testName', ipAddress='testIp', roomId='2', timeOfJoin="
                + s3.getTimeOfJoin() + "}";
        assertEquals(s3.toString(), text);
    }

    /**
     * Gets name test.
     */
    @Test
    void testGetName() {
        assertNull(s1.getName());
        assertEquals(s2.getName(), "testName");
        assertEquals(s3.getName(), "testName");
    }

    /**
     * Test get ip address.
     */
    @Test
    void testGetIpAddress() {
        assertNull(s1.getIpAddress());
        assertNull(s2.getIpAddress());
        assertEquals(s3.getIpAddress(), "testIp");
    }

    /**
     * Gets Room Id test.
     */
    @Test
    void testGetRoomId() {
        assertEquals(s1.getRoomId(), 0L);
        assertEquals(s2.getRoomId(), 0L);
        assertEquals(s3.getRoomId(), 2L);
    }

    /**
     * Test get time of join.
     */
    @Test
    void testGetTimeOfJoin() {
        assertNull(s1.getTimeOfJoin());
        assertNull(s2.getTimeOfJoin());
        assertEquals(s3.getTimeOfJoin(), ldt);
    }

    /**
     * Test equals for equal objects.
     */
    @Test
    void testEqualsEqualObj() {
        Student stud = new Student("testName","testIp",2L, ldt);
        assertTrue(s3.equals(stud));
    }

    /**
     * Test equals for same object.
     */
    @Test
    void testEqualsSameObj() {
        assertTrue(s3.equals(s3));
    }

    /**
     * Test equals for different class null.
     */
    @Test
    void testEqualsNull() {
        Lecturer lect = null;
        assertFalse(s3.equals(lect));
    }

    /**
     * Test equals for different objects.
     */
    @Test
    void testEqualsDiffObj() {
        Student stud = new Student("anotherName","testIp",2L, ldt);
        assertFalse(s3.equals(stud));
    }
}