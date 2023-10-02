package nl.tudelft.oopp.demo.repositorytests;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * The type Question repository test.
 */
@org.springframework.test.context.TestPropertySource(
        locations = "classpath:application-test.properties")
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestEntityManager
class QuestionRepositoryTest {

    /**
     * The Entity manager.
     */
    @Autowired
    TestEntityManager entityManager;

    /**
     * The Question repository.
     */
    @Autowired
    QuestionRepository questionRepository;

    private static boolean firstTest;
    private static Question q1;
    private static Question q2;

    /**
     * Init.
     */
    @BeforeAll
    public static void init() {
        q1 = new Question("Question 1?");
        q1.setAnswered(false);
        q1.setAuthorId(1);
        q1.setLectureId(1);
        q1.setUpvotes(1);
        q1.setTimePublished(LocalDateTime.of(1980, 1, 1, 12, 1));
        q1.setAnswerId(1);

        q2 = new Question("Question 2?");
        q2.setAnswered(false);
        q2.setAuthorId(2);
        q2.setLectureId(2);
        q2.setUpvotes(2);
        q2.setTimePublished(LocalDateTime.of(1980, 1, 1, 13, 1));
        q2.setAnswerId(2);
        firstTest = true;
    }

    /**
     * Sets before.
     */
    @BeforeEach
    public void setupBefore() {
        persist();
    }

    /**
     * Tear down.
     */
    @AfterEach
    public void tearDown() {
        q1 = new Question("Question 1?");
        q1.setAnswered(false);
        q1.setAuthorId(1);
        q1.setLectureId(1);
        q1.setUpvotes(1);
        q1.setTimePublished(LocalDateTime.of(1980, 1, 1, 12, 1));
        q1.setAnswerId(1);

        q2 = new Question("Question 2?");
        q2.setAnswered(false);
        q2.setAuthorId(2);
        q2.setLectureId(2);
        q2.setUpvotes(2);
        q2.setTimePublished(LocalDateTime.of(1980, 1, 1, 13, 1));
        q2.setAnswerId(2);
    }


    /**
     * Persist.
     */
    public void persist() {
        q1 = entityManager.persist(q1);
        q2 = entityManager.persistAndFlush(q2);
    }

    /**
     * Select by author id.
     */
    @Test
    void selectByAuthorId() {
        assertEquals(q1, questionRepository.selectByAuthorId(1).get(0));
    }

    /**
     * Sort by age.
     */
    @Test
    void sortByAge() {
        assertEquals(List.of(q2, q1), questionRepository.sortByAge());
    }

    /**
     * Select by upvotes.
     */
    @Test
    void selectByUpvotes() {
        persist();
        assertEquals(List.of(q2, q1), questionRepository.selectByUpvotes());
    }

    /**
     * Select answered.
     */
    @Test
    void selectAnswered() {
        q1.setAnswered(true);
        q2.setAnswered(false);
        assertEquals(List.of(q1), questionRepository.selectAnswered());
    }

    /**
     * Select not answered.
     */
    @Test
    void selectNotAnswered() {
        q1.setAnswered(true);
        q2.setAnswered(false);
        assertEquals(List.of(q2), questionRepository.selectNotAnswered());
    }

    /**
     * Sort by age unanswered.
     */
    @Test
    void sortByAgeUnanswered() {
        q1.setAnswered(false);
        q2.setAnswered(false);
        assertEquals(List.of(q2,q1), questionRepository.sortByAgeUnanswered());
    }

    /**
     * Sort by age answered.
     */
    @Test
    void sortByAgeAnswered() {
        q1.setAnswered(true);
        q2.setAnswered(true);
        assertEquals(List.of(q2, q1), questionRepository.sortByAgeAnswered());
    }

    /**
     * Select by upvotes unanswered.
     */
    @Test
    void selectByUpvotesUnanswered() {
        q1.setAnswered(false);
        q2.setAnswered(false);
        assertEquals(List.of(q2, q1), questionRepository.selectByUpvotesUnanswered());
    }

    /**
     * Select by upvotes answered.
     */
    @Test
    void selectByUpvotesAnswered() {
        q1.setAnswered(true);
        q2.setAnswered(true);
        assertEquals(List.of(q1, q2), questionRepository.selectAnswered());
    }

    /**
     * Test find all.
     */
    @Test
    void testFindAll() {
        assertEquals(List.of(q1, q2), questionRepository.findAll());
    }
}