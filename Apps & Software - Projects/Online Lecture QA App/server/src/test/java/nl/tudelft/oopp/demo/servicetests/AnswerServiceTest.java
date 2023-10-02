package nl.tudelft.oopp.demo.servicetests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.tudelft.oopp.demo.entities.Answer;
import nl.tudelft.oopp.demo.repositories.AnswerRepository;
import nl.tudelft.oopp.demo.services.AnswerService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * The type Answer service test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@org.springframework.test.context.TestPropertySource(
        locations = "classpath:application-test.properties")
class AnswerServiceTest {

    @Autowired
    private AnswerService answerService;

    @MockBean
    private AnswerRepository answerRepository;

    private static Answer a1;
    private static List<Answer> answerList;

    /**
     * Initializing the tests.
     */
    @org.junit.jupiter.api.BeforeAll
    /*public static void init() {
        answerList = new ArrayList<Answer>();
        for (int i = 1; i < 5; i++) {
            Answer l = new Answer(LocalDateTime.of(2021, 1, 1, 12, i),
                    i,"answer" + i);

            l.setId(i);
            answerList.add(l);
        }

        a1 = new Answer(LocalDateTime.of(2021, 1, 1, 12, 10),
                10, "example answer content");
        a1.setId(10);
    }**/
    public static void init() {
        answerList = new ArrayList<Answer>();
        for (int i = 1; i < 5; i++) {
            Answer l = new Answer("answer");

            l.setId(i);
            answerList.add(l);
        }

        a1 = new Answer("content");
        a1.setId(10);
    }

    /**
     * Test Save.
     */
    @Test
    void save() {
        when(answerRepository.save(a1)).thenReturn(a1);
        assertEquals(a1, answerService.save(a1));
    }

    /**
     * Test Find all.
     */
    @Test
    void findAll() {
        when(answerRepository.findAll()).thenReturn(answerList);
        assertEquals(answerList, answerService.findAll());
    }

    /**
     * Test Find by id.
     */
    @Test
    void findById() {
        when(answerRepository.findById(10L)).thenReturn(java.util.Optional.of(a1));
        assertEquals(Optional.of(a1), answerService.findById(10));
    }

}