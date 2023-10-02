package nl.tudelft.oopp.demo.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The type Banned ip test.
 */
public class BannedIpTest {

    private BannedIp b1;
    private BannedIp b2;

    private LocalTime lt;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        lt = LocalTime.of(10,0); // 10:00

        b1 = new BannedIp();
        b2 = new BannedIp("ipAddress", lt, 7L);
    }

    /**
     * Test first constructor.
     */
    @Test
    void testFirstConstructor() {
        assertNotNull(b1);
        assertNull(b1.getIpAddress());
        assertNull(b1.getBannedAt());
        assertNull(b1.getRoomId());
    }

    /**
     * Test second constructor.
     */
    @Test
    void testSecondConstructor() {
        assertNotNull(b2);
        assertEquals("ipAddress", b2.getIpAddress());
        assertEquals(lt, b2.getBannedAt());
        assertEquals(7L, b2.getRoomId());
    }

    /**
     * Test get ip address.
     */
    @Test
    void testGetIpAddress() {
        assertNull(b1.getIpAddress());
        assertEquals("ipAddress", b2.getIpAddress());
    }

    /**
     * Test get banned at.
     */
    @Test
    void testGetBannedAt() {
        assertNull(b1.getBannedAt());
        assertEquals(lt, b2.getBannedAt());
    }

    /**
     * Test get room id.
     */
    @Test
    void testGetRoomId() {
        assertNull(b1.getRoomId());
        assertEquals(7L, b2.getRoomId());
    }
}
