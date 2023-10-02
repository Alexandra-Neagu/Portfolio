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
class StudentTest {

    private Student s1;
    private Student s2;

    private LocalDateTime ldt;

    /**
     * Before.
     */
    @BeforeEach
    void before() {
        ldt = LocalDateTime.of(2021, 1, 1, 10, 30);
        s1 = new Student();
        s2 = new Student("testName","testIp",3, ldt);
    }

    /**
     * Test first constructor.
     */
    @Test
    void testFirstConstructor() {
        assertNotNull(s1);
        assertEquals(0L, s1.getId());
        assertNull(s1.getName());
        assertNull(s1.getIpAddress());
        assertEquals(0L, s1.getRoomId());
        assertNull(s1.getTimeOfJoin());
    }

    /**
     * Test second constructor.
     */
    @Test
    void testSecondConstructor() {
        assertNotNull(s2);
        assertEquals(0L, s2.getId());
        assertEquals("testName", s2.getName());
        assertEquals("testIp", s2.getIpAddress());
        assertEquals(3L, s2.getRoomId());
        assertEquals(ldt, s2.getTimeOfJoin());
    }

    /**
     * Test get id.
     */
    @Test
    void testGetId() {
        //setting an id manually to check getter
        s1.setId(5);
        assertEquals(s1.getId(),5);
    }

    /**
     * Test set id.
     */
    @Test
    void testSetId() {
        s1.setId(3L);
        assertEquals(3L, s1.getId());

        // calling the setter again to check whether the field will  actually change
        s1.setId(10L);
        assertEquals(10L, s1.getId());
    }

    /**
     * Test get name.
     */
    @Test
    void testGetName() {
        assertNull(s1.getName());
        assertEquals(s2.getName(),"testName");
    }

    /**
     * Test set name.
     */
    @Test
    void testSetName() {
        s1.setName("Max");
        assertEquals(s1.getName(),"Max");

        //calling the setter again to check whether the field will actually change every time
        s1.setName("Sam");
        assertEquals(s1.getName(),"Sam");
    }

    /**
     * Test get ip address.
     */
    @Test
    void testGetIpAddress() {
        assertNull(s1.getIpAddress());
        assertEquals(s2.getIpAddress(),"testIp");
    }

    /**
     * Test set ip address.
     */
    @Test
    void testSetIpAddress() {
        s1.setIpAddress("101.000.127");
        assertEquals("101.000.127", s1.getIpAddress());

        // calling the setter again to check whether the field will  actually change
        s1.setIpAddress("0.0.0");
        assertEquals("0.0.0", s1.getIpAddress());
    }

    /**
     * Test get room id.
     */
    @Test
    void testGetRoomId() {
        assertEquals(s1.getRoomId(),0);
        assertEquals(s2.getRoomId(),3);
    }

    /**
     * Test set room id.
     */
    @Test
    void testSetRoomId() {
        s1.setRoomId(2L);
        assertEquals(s1.getRoomId(), 2);

        // calling the setter again to check whether the field will  actually change
        s1.setRoomId(98L);
        assertEquals(s1.getRoomId(), 98);
    }

    /**
     * Test get time of join.
     */
    @Test
    void testGetTimeOfJoin() {
        assertNull(s1.getTimeOfJoin());
        assertEquals(ldt, s2.getTimeOfJoin());
    }

    /**
     * Test set time of join.
     */
    @Test
    void testSetTimeOfJoin() {
        LocalDateTime joinTime1 = LocalDateTime.of(2020, 2, 15, 14, 29); //15-02-2020, 14:29
        LocalDateTime joinTime2 = LocalDateTime.of(2050, 10, 17, 23, 40); //17-10-2050, 23:40

        s1.setTimeOfJoin(joinTime1);
        assertEquals(joinTime1, s1.getTimeOfJoin());

        s1.setTimeOfJoin(joinTime2);
        assertEquals(joinTime2, s1.getTimeOfJoin());
    }


    /**
     * Test equals for equal objects.
     */
    @Test
    void testEqualsEqualObj() {
        Student stud = new Student("testName","testIp",3L, ldt);
        assertTrue(s2.equals(stud));
    }

    /**
     * Test equals for same object.
     */
    @Test
    void testEqualsSameObj() {
        assertTrue(s1.equals(s1));
        assertTrue(s2.equals(s2));
    }

    /**
     * Test equals for different class null.
     */
    @Test
    void testEqualsNull() {
        Lecturer lecturer = null;
        assertFalse(s2.equals(lecturer));
    }

    /**
     * Test equals for different objects.
     */
    @Test
    void testEqualsDiffObj() {
        Student stud = new Student("anotherName","testIp",2L, ldt);
        assertFalse(s2.equals(stud));
    }

    /**
     * Test to string.
     */
    @Test
    void testToString() {
        String text = "User{"
                + "id=0, name='testName', ipAddress='testIp', roomId='3', timeOfJoin="
                + s2.getTimeOfJoin().toString() + "}";

        assertEquals(text, s2.toString());
    }
}