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
 * The type Student test.
 */
class ModeratorTest {

    private Moderator m1;
    private Moderator m2;

    private LocalDateTime ldt;

    /**
     * Before.
     */
    @BeforeEach
    void setUp() {
        ldt = LocalDateTime.of(2021, 1, 1, 10, 30);

        m1 = new Moderator();
        m2 = new Moderator("testName","testIp",3L, ldt);
    }

    /**
     * Test first constructor.
     */
    @Test
    void testFirstConstructor() {
        assertNotNull(m1);
        assertEquals(0L, m1.getId());
        assertNull(m1.getName());
        assertNull(m1.getIpAddress());
        assertEquals(0L, m1.getRoomId());
        assertNull(m1.getTimeOfJoin());
    }

    /**
     * Test second constructor.
     */
    @Test
    void testSecondConstructor() {
        assertNotNull(m2);
        assertEquals(0L, m2.getId());
        assertEquals("testName", m2.getName());
        assertEquals("testIp", m2.getIpAddress());
        assertEquals(3L, m2.getRoomId());
        assertEquals(ldt, m2.getTimeOfJoin());
    }

    /**
     * Test get id.
     */
    @Test
    void testGetId() {
        // setting an ID manually to check the getter
        m1.setId(5L);
        assertEquals(5L, m1.getId());
    }

    /**
     * Test set id.
     */
    @Test
    void testSetId() {
        m1.setId(3L);
        assertEquals(3L, m1.getId());

        // calling the setter again to check whether the field will  actually change
        m1.setId(10L);
        assertEquals(10L, m1.getId());
    }

    /**
     * Test get name.
     */
    @Test
    void testGetName() {
        assertNull(m1.getName());
        assertEquals("testName", m2.getName());
    }

    /**
     * Test set name.
     */
    @Test
    void testSetName() {
        m1.setName("Max");
        assertEquals("Max", m1.getName());

        //calling the setter again to check whether the field will actually change every time
        m1.setName("Sam");
        assertEquals("Sam", m1.getName());
    }

    /**
     * Test get ip address.
     */
    @Test
    void testGetIpAddress() {
        assertNull(m1.getIpAddress());
        assertEquals("testIp", m2.getIpAddress());
    }

    /**
     * Test set ip address.
     */
    @Test
    void testSetIpAddress() {
        m1.setIpAddress("101.000.127");
        assertEquals("101.000.127", m1.getIpAddress());

        // calling the setter again to check whether the field will  actually change
        m1.setIpAddress("0.0.0");
        assertEquals("0.0.0", m1.getIpAddress());
    }

    /**
     * Test get room id.
     */
    @Test
    void testGetRoomId() {
        assertEquals(0L, m1.getRoomId());
        assertEquals(3L, m2.getRoomId());
    }

    /**
     * Test set room id.
     */
    @Test
    void testSetRoomId() {
        m1.setRoomId(2L);
        assertEquals(2L, m1.getRoomId());

        // calling the setter again to check whether the field will  actually change
        m1.setRoomId(98L);
        assertEquals(98L, m1.getRoomId());
    }

    /**
     * Test get time of join.
     */
    @Test
    void testGetTimeOfJoin() {
        assertNull(m1.getTimeOfJoin());
        assertEquals(ldt, m2.getTimeOfJoin());
    }

    /**
     * Test set time of join.
     */
    @Test
    void testSetTimeOfJoin() {
        LocalDateTime joinTime1 = LocalDateTime.of(2020, 2, 15, 14, 29); //15-02-2020, 14:29
        LocalDateTime joinTime2 = LocalDateTime.of(2050, 10, 17, 23, 40); //17-10-2050, 23:40

        m1.setTimeOfJoin(joinTime1);
        assertEquals(joinTime1, m1.getTimeOfJoin());

        m1.setTimeOfJoin(joinTime2);
        assertEquals(joinTime2, m1.getTimeOfJoin());
    }


    /**
     * Test equals for equal objects.
     */
    @Test
    void testEqualsEqualObj() {
        Moderator mod = new Moderator("testName","testIp",3L, ldt);
        assertTrue(m2.equals(mod));
    }

    /**
     * Test equals for same object.
     */
    @Test
    void testEqualsSameObj() {
        assertTrue(m1.equals(m1));
        assertTrue(m2.equals(m2));
    }

    /**
     * Test equals for different class null.
     */
    @Test
    void testEqualsNull() {
        Student stud = null;
        assertFalse(m2.equals(stud));
    }

    /**
     * Test equals for different objects.
     */
    @Test
    void testEqualsDiffObj() {
        Moderator mod = new Moderator("anotherName","testIp",2L, ldt);
        assertFalse(m2.equals(mod));
    }

    /**
     * Test to string.
     */
    @Test
    void testToString() {
        String text = "User{"
                + "id=0, name='testName', ipAddress='testIp', roomId='3', timeOfJoin="
                + m2.getTimeOfJoin().toString() + "}";

        assertEquals(text, m2.toString());
    }
}