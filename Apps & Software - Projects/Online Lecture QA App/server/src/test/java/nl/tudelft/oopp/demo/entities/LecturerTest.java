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
class LecturerTest {

    private Lecturer l1;
    private Lecturer l2;

    private LocalDateTime ldt;

    /**
     * Before.
     */
    @BeforeEach
    void setUp() {
        ldt = LocalDateTime.of(2021, 1, 1, 10, 30);

        l1 = new Lecturer();
        l2 = new Lecturer("testName","testIp",3L, ldt);
    }

    /**
     * Test first constructor.
     */
    @Test
    void testFirstConstructor() {
        assertNotNull(l1);
        assertEquals(0L, l1.getId());
        assertNull(l1.getName());
        assertNull(l1.getIpAddress());
        assertEquals(0L, l1.getRoomId());
        assertNull(l1.getTimeOfJoin());
    }

    /**
     * Test second constructor.
     */
    @Test
    void testSecondConstructor() {
        assertNotNull(l2);
        assertEquals(0L, l2.getId());
        assertEquals("testName", l2.getName());
        assertEquals("testIp", l2.getIpAddress());
        assertEquals(3L, l2.getRoomId());
        assertEquals(ldt, l2.getTimeOfJoin());
    }

    /**
     * Test get id.
     */
    @Test
    void testGetId() {
        // setting an ID manually to check the getter
        l1.setId(5L);
        assertEquals(5L, l1.getId());
    }

    /**
     * Test set id.
     */
    @Test
    void testSetId() {
        l1.setId(3L);
        assertEquals(3L, l1.getId());

        // calling the setter again to check whether the field will  actually change
        l1.setId(10L);
        assertEquals(10L, l1.getId());
    }

    /**
     * Test get name.
     */
    @Test
    void testGetName() {
        assertNull(l1.getName());
        assertEquals("testName", l2.getName());
    }

    /**
     * Test set name.
     */
    @Test
    void testSetName() {
        l1.setName("Max");
        assertEquals("Max", l1.getName());

        //calling the setter again to check whether the field will actually change every time
        l1.setName("Sam");
        assertEquals("Sam", l1.getName());
    }

    /**
     * Test get ip address.
     */
    @Test
    void testGetIpAddress() {
        assertNull(l1.getIpAddress());
        assertEquals("testIp", l2.getIpAddress());
    }

    /**
     * Test set ip address.
     */
    @Test
    void testSetIpAddress() {
        l1.setIpAddress("101.000.127");
        assertEquals("101.000.127", l1.getIpAddress());

        // calling the setter again to check whether the field will  actually change
        l1.setIpAddress("0.0.0");
        assertEquals("0.0.0", l1.getIpAddress());
    }

    /**
     * Test get room id.
     */
    @Test
    void testGetRoomId() {
        assertEquals(0L, l1.getRoomId());
        assertEquals(3L, l2.getRoomId());
    }

    /**
     * Test set room id.
     */
    @Test
    void testSetRoomId() {
        l1.setRoomId(2L);
        assertEquals(2L, l1.getRoomId());

        // calling the setter again to check whether the field will  actually change
        l1.setRoomId(98L);
        assertEquals(98L, l1.getRoomId());
    }

    /**
     * Test get time of join.
     */
    @Test
    void testGetTimeOfJoin() {
        assertNull(l1.getTimeOfJoin());
        assertEquals(ldt, l2.getTimeOfJoin());
    }

    /**
     * Test set time of join.
     */
    @Test
    void testSetTimeOfJoin() {
        LocalDateTime joinTime1 = LocalDateTime.of(2020, 2, 15, 14, 29); //15-02-2020, 14:29
        LocalDateTime joinTime2 = LocalDateTime.of(2050, 10, 17, 23, 40); //17-10-2050, 23:40

        l1.setTimeOfJoin(joinTime1);
        assertEquals(joinTime1, l1.getTimeOfJoin());

        l1.setTimeOfJoin(joinTime2);
        assertEquals(joinTime2, l1.getTimeOfJoin());
    }


    /**
     * Test equals for equal objects.
     */
    @Test
    void testEqualsEqualObj() {
        Lecturer lect = new Lecturer("testName","testIp",3L, ldt);
        assertTrue(l2.equals(lect));
    }

    /**
     * Test equals for same object.
     */
    @Test
    void testEqualsSameObj() {
        assertTrue(l1.equals(l1));
        assertTrue(l2.equals(l2));
    }

    /**
     * Test equals for different class null.
     */
    @Test
    void testEqualsNull() {
        Student stud = null;
        assertFalse(l2.equals(stud));
    }

    /**
     * Test equals for different objects.
     */
    @Test
    void testEqualsDiffObj() {
        Lecturer lect = new Lecturer("anotherName","testIp",2L, ldt);
        assertFalse(l2.equals(lect));
    }

    /**
     * Test to string.
     */
    @Test
    void testToString() {
        String text = "User{"
                + "id=0, name='testName', ipAddress='testIp', roomId='3', timeOfJoin="
                + l2.getTimeOfJoin().toString() + "}";

        assertEquals(text, l2.toString());
    }
}