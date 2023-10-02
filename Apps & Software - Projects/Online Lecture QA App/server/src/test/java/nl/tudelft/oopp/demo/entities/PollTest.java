package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The type Poll test.
 */
public class PollTest {

    private Poll p1;
    private Poll p2;
    private ArrayList<String> answers;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        answers = new ArrayList<>();
        answers.add("answer 1");
        answers.add("answer 2");
        answers.add("answer 3");

        p1 = new Poll(1L, answers, "What?", 1, true);
        p2 = new Poll();
    }

    /**
     * Test first constructor.
     */
    @Test
    void testFirstConstructor() {
        assertNotNull(p1);
        assertEquals(1L, p1.getLectureId());
        assertEquals(answers, p1.getAnswers());
        assertEquals("What?", p1.getQuestion());
        assertEquals(1, p1.getCorrectAnswerNo());
        assertTrue(p1.getSharing());
    }

    /**
     * Test first constructor correct ans null.
     */
    @Test
    void testFirstConstructorCorrectAnsNull() {
        Poll p3 = new Poll(1L, answers, "What?", null, false);

        assertNotNull(p3);
        assertEquals(1L, p3.getLectureId());
        assertEquals(answers, p3.getAnswers());
        assertEquals("What?", p3.getQuestion());
        assertEquals(-1, p3.getCorrectAnswerNo());
        assertFalse(p3.getSharing());
    }

    /**
     * Test second constructor.
     */
    @Test
    void testSecondConstructor() {
        assertNotNull(p2);
        assertEquals(0L, p2.getLectureId());
        assertNull(p2.getAnswers());
        assertNull(p2.getQuestion());
        assertNull(p2.getCorrectAnswerNo());
    }

    /**
     * Test get id.
     */
    @Test
    void testGetId() {
        assertEquals(0L, p1.getId());
        assertEquals(0L, p2.getId());
    }

    /**
     * Test get lecture id.
     */
    @Test
    void testGetLectureId() {
        assertEquals(1L, p1.getLectureId());
        assertEquals(0L, p2.getLectureId());
    }

    /**
     * Test set lecture id.
     */
    @Test
    void testSetLectureId() {
        p1.setLectureId(19L);
        assertEquals(19L, p1.getLectureId());

        // calling the setter again to check whether the field will  actually change
        p1.setLectureId(3L);
        assertEquals(3L, p1.getLectureId());
    }

    /**
     * Test get answers.
     */
    @Test
    void testGetAnswers() {
        assertEquals(answers, p1.getAnswers());
        assertNull(p2.getAnswers());
    }

    /**
     * Test set answers.
     */
    @Test
    void testSetAnswers() {
        ArrayList<String> anotherAns1 = new ArrayList<>();
        anotherAns1.add("correct ans");
        anotherAns1.add("wrong ans");

        p1.setAnswers(anotherAns1);
        assertEquals(anotherAns1, p1.getAnswers());

        // calling the setter again to check whether the field will  actually change
        ArrayList<String> anotherAns2 = new ArrayList<>();
        anotherAns2.add("yes");
        anotherAns2.add("no");

        p1.setAnswers(anotherAns2);
        assertEquals(anotherAns2, p1.getAnswers());
    }

    /**
     * Test get votes.
     */
    @Test
    void testGetVotes() {
        assertEquals(new ArrayList<>(Collections.nCopies(p1.getAnswers().size(), 0)),
                p1.getVotes());
        assertNull(p2.getVotes());
    }

    /**
     * Test get question.
     */
    @Test
    void testGetQuestion() {
        assertEquals("What?", p1.getQuestion());
        assertNull(p2.getQuestion());
    }

    /**
     * Test set question.
     */
    @Test
    void testSetQuestion() {
        p1.setQuestion("Does it work?");
        assertEquals("Does it work?", p1.getQuestion());

        // calling the setter again to check whether the field will  actually change
        p1.setQuestion("Who?");
        assertEquals("Who?", p1.getQuestion());
    }

    /**
     * Test get correct answer no.
     */
    @Test
    void testGetCorrectAnswerNo() {
        assertEquals(1, p1.getCorrectAnswerNo());
        assertNull(p2.getCorrectAnswerNo());
    }

    /**
     * Test get correct answer no null.
     */
    @Test
    void testGetCorrectAnswerNoNull() {
        Poll p3 = new Poll(1, answers, "What?", null, false);

        assertEquals(-1, p3.getCorrectAnswerNo());
    }

    /**
     * Test set correct answer no.
     */
    @Test
    void testSetCorrectAnswerNo() {
        p1.setCorrectAnswerNo(8);
        assertEquals(8, p1.getCorrectAnswerNo());

        // calling the setter again to check whether the field will  actually change
        p1.setCorrectAnswerNo(2);
        assertEquals(2, p1.getCorrectAnswerNo());
    }

    /**
     * Test set correct answer no null.
     */
    @Test
    void testSetCorrectAnswerNoNull() {
        p1.setCorrectAnswerNo(null);
        assertEquals(-1, p1.getCorrectAnswerNo());
    }

    /**
     * Test is open.
     */
    @Test
    void testGetOpen() {
        assertTrue(p1.getOpen());
        assertFalse(p2.getOpen());
    }

    /**
     * Test get sharing.
     */
    @Test
    void testGetSharing() {
        assertTrue(p1.getSharing());
        assertFalse(p2.getSharing());
    }

    /**
     * Test set sharing.
     */
    @Test
    void testSetSharing() {
        p1.setSharing(false);
        assertFalse(p1.getSharing());

        // calling the setter again to check whether the field will  actually change
        p1.setSharing(true);
        assertTrue(p1.getSharing());
    }

    /**
     * Test set open.
     */
    @Test
    void testSetOpen() {
        p1.setOpen(false);
        assertFalse(p1.getOpen());

        // calling the setter again to check whether the field will  actually change
        p1.setOpen(true);
        assertTrue(p1.getOpen());
    }

    /**
     * Test equals for equal objects.
     */
    @Test
    void testEqualsEqualObj() {
        Poll p3 = new Poll(1L, answers, "What?", 1, true);

        assertTrue(p1.equals(p3));
    }

    /**
     * Test equals for same object.
     */
    @Test
    void testEqualsSameObj() {
        assertTrue(p1.equals(p1));
    }

    /**
     * Test equals for different class null.
     */
    @Test
    void testEqualsNull() {
        Lecturer lect = null;
        assertFalse(p1.equals(lect));
    }

    /**
     * Test equals for different objects.
     */
    @Test
    void testEqualsDiffObj() {
        Poll p3 = new Poll(1L, answers, "Wrong?", 1, true);

        assertFalse(p1.equals(p3));
    }

    /**
     * Test to string.
     */
    @Test
    void testToString() {
        String text = "Poll{id=0, lectureId=1, answers=" + p1.getAnswers().toString()
                        + ", votes=" + p1.getVotes().toString()
                        + ", question='What?', correctAnswerNo=1, sharings=true}";

        assertEquals(text, p1.toString());
    }

    /**
     * Test init answers.
     */
    @Test
    void testInitAnswers() {
        assertNull(p2.getAnswers());
        p2.initAnswers(4);
        assertNotNull(p2.getAnswers());
    }

    /**
     * Test init votes.
     */
    @Test
    void testInitVotes() {
        assertNull(p2.getVotes());
        p2.initVotes(4);
        assertNotNull(p2.getVotes());

        List<Integer> votes = new ArrayList<>(Collections.nCopies(4, 0));
        assertEquals(votes, p2.getVotes());
    }

    /**
     * Test add vote.
     */
    @Test
    void testAddVote() {
        assertEquals(0, p1.getVotes().get(0));
        p1.addVote(0);
        assertEquals(1, p1.getVotes().get(0));
    }
}
