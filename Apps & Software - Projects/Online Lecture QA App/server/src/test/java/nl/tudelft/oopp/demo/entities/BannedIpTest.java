package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * The type Banned ip test.
 */
class BannedIpTest {

    private BannedIp b1;
    private BannedIp b2;

    private LocalTime lt;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        lt = LocalTime.of(10,15); // 10:15

        b1 = new BannedIp();
        b2 = new BannedIp("127.000.1", lt, 6L);
    }

    /**
     * Test first constructor.
     */
    void testFirstConstructor() {
        assertNotNull(b2);
        assertEquals("127.000.1", b2.getIpAddress());
        assertEquals(lt, b2.getBannedAt());
        assertEquals(6L, b2.getRoomId());
    }

    /**
     * Test second constructor.
     */
    @Test
    void testSecondConstructor() {
        assertNotNull(b1);
        assertNull(b1.getIpAddress());
        assertNull(b1.getBannedAt());
        assertEquals(0L, b1.getRoomId());
    }

    /**
     * Test get ip address.
     */
    @Test
    void testGetIpAddress() {
        assertNull(b1.getIpAddress());
        assertEquals(b2.getIpAddress(), "127.000.1");
    }

    /**
     * Test set ip address.
     */
    @Test
    void testSetIpAddress() {
        b1.setIpAddress("101.000.127");
        assertEquals(b1.getIpAddress(),"101.000.127");

        // calling the setter again to check whether the field will  actually change
        b1.setIpAddress("0.0.0");
        assertEquals("0.0.0", b1.getIpAddress());
    }

    /**
     * Test get banned at.
     */
    @Test
    void testGetBannedAt() {
        assertNull(b1.getBannedAt());
        assertEquals(b2.getBannedAt(), lt);
    }

    /**
     * Test set banned at.
     */
    @Test
    void testSetBannedAt() {
        LocalTime time1 = LocalTime.of(14, 20); //14:20
        LocalTime time2 = LocalTime.of(19, 54); // 19:54

        b1.setBannedAt(time1);
        assertEquals(time1, b1.getBannedAt());

        // calling the setter again to check whether the field will  actually change
        b1.setBannedAt(time2);
        assertEquals(time2, b1.getBannedAt());
    }

    /**
     * Test get room id.
     */
    @Test
    void testGetRoomId() {
        assertEquals(b1.getRoomId(),0);
        assertEquals(b2.getRoomId(), 6);
    }

    /**
     * Test set room id.
     */
    @Test
    void testSetRoomId() {
        b1.setRoomId(3L);
        assertEquals(3L, b1.getRoomId());

        // calling the setter again to check whether the field will  actually change
        b1.setRoomId(10L);
        assertEquals(10L, b1.getRoomId());
    }

    /**
     * Test equals for equal objects.
     */
    @Test
    void testEqualsEqualObj() {
        BannedIp b3 = new BannedIp("127.000.1", lt, 6L);
        assertTrue(b2.equals(b3));
    }

    /**
     * Test equals same object.
     */
    @Test
    void testEqualsSameObj() {
        assertTrue(b1.equals(b1));
        assertTrue(b2.equals(b2));
    }

    /**
     * Test equals null.
     */
    @Test
    void testEqualsNull() {
        BannedIp ip = null;
        assertFalse(b1.equals(ip));
    }

    /**
     * Test equals for different objects.
     */
    @Test
    void testEqualsDiffObj() {
        BannedIp b3 = new BannedIp("127.000.0", lt, 6L);;
        assertFalse(b2.equals(b3));
    }

    /**
     * Test to string.
     */
    @Test
    void testToString() {
        assertEquals(b2.toString(),
                "BannedIp{ip_address= 127.000.1, bannedAt= 10:15, roomId= 6}");
    }
}