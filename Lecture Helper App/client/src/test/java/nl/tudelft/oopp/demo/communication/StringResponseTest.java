package nl.tudelft.oopp.demo.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The type String response test.
 */
public class StringResponseTest {

    private StringResponse sr;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        sr = new StringResponse("correct");
    }

    /**
     * Test constructor.
     */
    @Test
    void testConstructor() {
        assertNotNull(sr);
        assertEquals("correct", sr.getResponse());
    }

    /**
     * Test get response.
     */
    @Test
    void testGetResponse() {
        assertEquals("correct", sr.getResponse());
    }
}
