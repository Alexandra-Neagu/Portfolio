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
 * The type Moderator test.
 */
public class ModeratorTest {

    private Moderator m1;
    private Moderator m2;
    private Moderator m3;

    private LocalDateTime ldt;

    /**
     * Before.
     */
    @BeforeEach
    void setUp() {
        ldt = LocalDateTime.of(2021, 1, 1, 10, 30); //01-01-2021, 10:30

        m1 = new Moderator();
        m2 = new Moderator("testName");
        m3 = new Moderator("testName","testIp",2L, ldt);
    }

    /**
     * Test first constructor.
     */
    @Test
    void testFirstConstructor() {
        assertNotNull(m3);
        assertEquals("testName", m3.getName());
        assertEquals("testIp", m3.getIpAddress());
        assertEquals(2L, m3.getRoomId());
        assertEquals(ldt, m3.getTimeOfJoin());
    }

    /**
     * Test third constructor.
     */
    @Test
    void testSecondConstructor() {
        assertNotNull(m1);
        assertNull(m1.getName());
        assertNull(m1.getIpAddress());
        assertEquals(0L, m1.getRoomId());
        assertNull(m1.getTimeOfJoin());
    }

    /**
     * Test second constructor.
     */
    @Test
    void testThirdConstructor() {
        assertNotNull(m2);
        assertEquals(m2.getName(), "testName");
        assertNull(m2.getIpAddress());
        assertEquals(0L, m2.getRoomId());
        assertNull(m2.getTimeOfJoin());
    }

    /**
     * Test get id.
     */
    @Test
    void testGetId() {
        assertEquals(0L, m1.getId());
        assertEquals(0L, m2.getId());
        assertEquals(0L, m3.getId());
    }

    /**
     * Test set id.
     */
    @Test
    void testSetId() {
        m1.setId(15L);
        assertEquals(m1.getId(), 15L);

        // calling the setter again to check whether the field will  actually change
        m1.setId(24L);
        assertEquals(m1.getId(), 24L);
    }

    /**
     * Test set name.
     */
    @Test
    void testSetName() {
        m1.setName("Gigi");
        assertEquals(m1.getName(), "Gigi");

        // calling the setter again to check whether the field will  actually change
        m1.setName("Bloomy");
        assertEquals(m1.getName(), "Bloomy");
    }

    /**
     * Test set ip address.
     */
    @Test
    void testSetIpAddress() {
        m1.setIpAddress("127.0.0.0");
        assertEquals(m1.getIpAddress(), "127.0.0.0");

        // calling the setter again to check whether the field will  actually change
        m1.setIpAddress("234.1.7.18");
        assertEquals(m1.getIpAddress(), "234.1.7.18");
    }

    /**
     * Sets Room Id test.
     */
    @Test
    void testSetRoomId() {
        m2.setRoomId(2L);
        assertEquals(m2.getRoomId(), 2L);

        // calling the setter again to check whether the field will  actually change
        m2.setRoomId(98L);
        assertEquals(m2.getRoomId(), 98L);
    }

    /**
     * Test set time of join.
     */
    @Test
    void testSetTimeOfJoin() {
        LocalDateTime joinTime1 = LocalDateTime.of(2024, 2, 15, 14, 29); //15-02-2024, 14:29
        LocalDateTime joinTime2 = LocalDateTime.of(2056, 10, 17, 23, 42); //17-10-2056, 23:42

        m1.setTimeOfJoin(joinTime1);
        assertEquals(m1.getTimeOfJoin(), joinTime1);

        // calling the setter again to check whether the field will  actually change
        m1.setTimeOfJoin(joinTime2);
        assertEquals(m1.getTimeOfJoin(), joinTime2);
    }

    /**
     * Test to string.
     */
    @Test
    void testToString() {
        String text = "Moderator {name='testName', ipAddress='testIp', roomId='2', timeOfJoin="
                + m3.getTimeOfJoin() + "}";
        assertEquals(m3.toString(), text);
    }

    /**
     * Gets name test.
     */
    @Test
    void testGetName() {
        assertNull(m1.getName());
        assertEquals(m2.getName(), "testName");
        assertEquals(m3.getName(), "testName");
    }

    /**
     * Test get ip address.
     */
    @Test
    void testGetIpAddress() {
        assertNull(m1.getIpAddress());
        assertNull(m2.getIpAddress());
        assertEquals(m3.getIpAddress(), "testIp");
    }

    /**
     * Gets Room Id test.
     */
    @Test
    void testGetRoomId() {
        assertEquals(m1.getRoomId(), 0L);
        assertEquals(m2.getRoomId(), 0L);
        assertEquals(m3.getRoomId(), 2L);
    }

    /**
     * Test get time of join.
     */
    @Test
    void testGetTimeOfJoin() {
        assertNull(m1.getTimeOfJoin());
        assertNull(m2.getTimeOfJoin());
        assertEquals(m3.getTimeOfJoin(), ldt);
    }

    /**
     * Test equals for equal objects.
     */
    @Test
    void testEqualsEqualObj() {
        Moderator mod = new Moderator("testName","testIp",2L, ldt);
        assertTrue(m3.equals(mod));
    }

    /**
     * Test equals for same object.
     */
    @Test
    void testEqualsSameObj() {
        assertTrue(m3.equals(m3));
    }

    /**
     * Test equals for different class null.
     */
    @Test
    void testEqualsNull() {
        Student stud = null;
        assertFalse(m3.equals(stud));
    }

    /**
     * Test equals for different objects.
     */
    @Test
    void testEqualsDiffObj() {
        Moderator mod = new Moderator("anotherName","testIp",2L, ldt);
        assertFalse(m3.equals(mod));
    }
}