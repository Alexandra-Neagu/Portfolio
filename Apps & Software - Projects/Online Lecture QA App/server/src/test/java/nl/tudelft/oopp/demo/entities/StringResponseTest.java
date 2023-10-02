package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The type String response test.
 */
class StringResponseTest {

    private StringResponse sr;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        sr = new StringResponse("example answer");
    }

    /**
     * Test constructor.
     */
    @Test
    void testConstructor() {
        assertNotNull(sr);
        assertEquals("example answer", sr.getResponse());
    }

    /**
     * Test get response.
     */
    @Test
    void testGetResponse() {
        assertEquals("example answer", sr.getResponse());
    }

    /**
     * Test set response.
     */
    @Test
    void testSetResponse() {
        sr.setResponse("another test String");
        assertEquals("another test String", sr.getResponse());

        // calling the setter again to check whether the field will  actually change
        sr.setResponse("test 3");
        assertEquals("test 3", sr.getResponse());
    }

    /**
     * Test equals for equal objects.
     */
    @Test
    void testEqualsEqualObj() {
        StringResponse sr2 = new StringResponse("example answer");
        assertTrue(sr.equals(sr2));
    }

    /**
     * Test equals for same object.
     */
    @Test
    void testEqualsSameObj() {
        assertTrue(sr.equals(sr));
    }

    /**
     * Test equals for different class null.
     */
    @Test
    void testEqualsNull() {
        Lecturer lecturer = null;
        assertFalse(sr.equals(lecturer));
    }

    /**
     * Test equals for different objects.
     */
    @Test
    void testEqualsDiffObj() {
        StringResponse sr2 = new StringResponse("example wrong answer");
        assertFalse(sr.equals(sr2));
    }

    /**
     * Test to string.
     */
    @Test
    void testToString() {
        assertEquals("example answer", sr.toString());
    }
}