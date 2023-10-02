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
 * The type Lecturer test.
 */
public class LecturerTest {

    private Lecturer l1;
    private Lecturer l2;
    private Lecturer l3;

    private LocalDateTime ldt;

    /**
     * Before.
     */
    @BeforeEach
    void setUp() {
        ldt = LocalDateTime.of(2021, 1, 1, 10, 30); //01-01-2021, 10:30

        l1 = new Lecturer();
        l2 = new Lecturer("testName");
        l3 = new Lecturer("testName","testIp",2L, ldt);
    }

    /**
     * Test first constructor.
     */
    @Test
    void testFirstConstructor() {
        assertNotNull(l3);
        assertEquals("testName", l3.getName());
        assertEquals("testIp", l3.getIpAddress());
        assertEquals(2L, l3.getRoomId());
        assertEquals(ldt, l3.getTimeOfJoin());
    }

    /**
     * Test third constructor.
     */
    @Test
    void testSecondConstructor() {
        assertNotNull(l1);
        assertNull(l1.getName());
        assertNull(l1.getIpAddress());
        assertEquals(0L, l1.getRoomId());
        assertNull(l1.getTimeOfJoin());
    }

    /**
     * Test second constructor.
     */
    @Test
    void testThirdConstructor() {
        assertNotNull(l2);
        assertEquals(l2.getName(), "testName");
        assertNull(l2.getIpAddress());
        assertEquals(0L, l2.getRoomId());
        assertNull(l2.getTimeOfJoin());
    }

    /**
     * Test get id.
     */
    @Test
    void testGetId() {
        assertEquals(0L, l1.getId());
        assertEquals(0L, l2.getId());
        assertEquals(0L, l3.getId());
    }

    /**
     * Test set id.
     */
    @Test
    void testSetId() {
        l1.setId(15L);
        assertEquals(l1.getId(), 15L);

        // calling the setter again to check whether the field will  actually change
        l1.setId(24L);
        assertEquals(l1.getId(), 24L);
    }

    /**
     * Test set name.
     */
    @Test
    void testSetName() {
        l1.setName("Gigi");
        assertEquals(l1.getName(), "Gigi");

        // calling the setter again to check whether the field will  actually change
        l1.setName("Bloomy");
        assertEquals(l1.getName(), "Bloomy");
    }

    /**
     * Test set ip address.
     */
    @Test
    void testSetIpAddress() {
        l1.setIpAddress("127.0.0.0");
        assertEquals(l1.getIpAddress(), "127.0.0.0");

        // calling the setter again to check whether the field will  actually change
        l1.setIpAddress("234.1.7.18");
        assertEquals(l1.getIpAddress(), "234.1.7.18");
    }

    /**
     * Sets Room Id test.
     */
    @Test
    void testSetRoomId() {
        l2.setRoomId(2L);
        assertEquals(l2.getRoomId(), 2L);

        // calling the setter again to check whether the field will  actually change
        l2.setRoomId(98L);
        assertEquals(l2.getRoomId(), 98L);
    }

    /**
     * Test set time of join.
     */
    @Test
    void testSetTimeOfJoin() {
        LocalDateTime joinTime1 = LocalDateTime.of(2024, 2, 15, 14, 29); //15-02-2024, 14:29
        LocalDateTime joinTime2 = LocalDateTime.of(2056, 10, 17, 23, 42); //17-10-2056, 23:42

        l1.setTimeOfJoin(joinTime1);
        assertEquals(l1.getTimeOfJoin(), joinTime1);

        // calling the setter again to check whether the field will  actually change
        l1.setTimeOfJoin(joinTime2);
        assertEquals(l1.getTimeOfJoin(), joinTime2);
    }

    /**
     * Test to string.
     */
    @Test
    void testToString() {
        String text = "Lecturer {name='testName', ipAddress='testIp', roomId='2', timeOfJoin="
                + l3.getTimeOfJoin() + "}";
        assertEquals(l3.toString(), text);
    }

    /**
     * Gets name test.
     */
    @Test
    void testGetName() {
        assertNull(l1.getName());
        assertEquals(l2.getName(), "testName");
        assertEquals(l3.getName(), "testName");
    }

    /**
     * Test get ip address.
     */
    @Test
    void testGetIpAddress() {
        assertNull(l1.getIpAddress());
        assertNull(l2.getIpAddress());
        assertEquals(l3.getIpAddress(), "testIp");
    }

    /**
     * Gets Room Id test.
     */
    @Test
    void testGetRoomId() {
        assertEquals(l1.getRoomId(), 0L);
        assertEquals(l2.getRoomId(), 0L);
        assertEquals(l3.getRoomId(), 2L);
    }

    /**
     * Test get time of join.
     */
    @Test
    void testGetTimeOfJoin() {
        assertNull(l1.getTimeOfJoin());
        assertNull(l2.getTimeOfJoin());
        assertEquals(l3.getTimeOfJoin(), ldt);
    }

    /**
     * Test equals for equal objects.
     */
    @Test
    void testEqualsEqualObj() {
        Lecturer lect = new Lecturer("testName","testIp",2L, ldt);
        assertTrue(l3.equals(lect));
    }

    /**
     * Test equals for same object.
     */
    @Test
    void testEqualsSameObj() {
        assertTrue(l3.equals(l3));
    }

    /**
     * Test equals for different class null.
     */
    @Test
    void testEqualsNull() {
        Student stud = null;
        assertFalse(l3.equals(stud));
    }

    /**
     * Test equals for different objects.
     */
    @Test
    void testEqualsDiffObj() {
        Lecturer lect = new Lecturer("anotherName","testIp",2L, ldt);
        assertFalse(l3.equals(lect));
    }
}
