package nl.tudelft.oopp.demo.servicetests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.StringResponse;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import nl.tudelft.oopp.demo.services.QuestionService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * The type Question service test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.test.context.TestPropertySource(
        locations = "classpath:application-test.properties")
public class QuestionServiceTest {
    @Autowired
    private QuestionService questionService;

    @MockBean
    private QuestionRepository questionRepository;

    private static List<Question> questionList;
    private static Question q1;

    /**
     * Init.
     */
    @org.junit.jupiter.api.BeforeEach
    public void init() {
        questionList = new ArrayList<Question>();
        for (int i = 1; i < 21; i++) {
            Question q = new Question("Question " + i);
            q.setAnswered(false);
            q.setAuthorId(i);
            q.setLectureId(i);
            q.setUpvotes(i);
            q.setTimePublished(LocalDateTime.of(1980, 1, i, 12, i));
            q.setId(i);
            q.setAnswerId(i);
            questionList.add(q);
        }

        q1 = new Question("Question 1?");
        q1.setAnswered(false);
        q1.setAuthorId(1);
        q1.setLectureId(1);
        q1.setUpvotes(1);
        q1.setTimePublished(LocalDateTime.of(1980, 1, 1, 12, 1));
        q1.setId(1);
        q1.setAnswerId(1);
    }

    /**
     * Test save.
     */
    @Test
    public void testSave() {
        when(questionRepository.save(q1)).thenReturn(q1);
        assertEquals(q1, questionService.save(q1));
    }

    /**
     * Test find all.
     */
    @Test
    public void testFindAll() {
        when(questionRepository.findAll()).thenReturn(questionList);
        assertEquals(questionList, questionService.findAll());
    }

    /**
     * Test find all by room id.
     */
    @Test
    public void testfindAllByRoomId() {
        questionList.stream().filter(q -> q.getId() < 11).forEach(q -> q.setLectureId(1));
        when(questionRepository.findAll()).thenReturn(questionList);
        assertEquals(questionList.subList(0,10), questionService.findAllByRoomId(1));
    }

    /**
     * Test find all answered.
     */
    @Test
    public void testfindAllAnswered() {
        questionList.stream()
                .filter(q -> q.getId() < 11)
                .forEach(q -> {
                    q.setAnswered(true);
                    if (q.getId() < 7) {
                        q.setLectureId(2);
                    } else {
                        q.setLectureId(1);
                    }
                });
        List<Question> ret = questionList.stream()
                .filter(Question::isAnswered).collect(Collectors.toList());
        when(questionRepository.selectAnswered()).thenReturn(ret);
        assertEquals(ret.subList(6, ret.size()), questionService.findAllAnswered(1));
    }

    /**
     * Test find all unanswered.
     */
    @Test
    public void testfindAllUnanswered() {
        questionList.forEach(q -> {
            if (q.getId() < 11) {
                q.setLectureId(1);
            } else {
                q.setLectureId(2);
            }
        });
        List<Question> ret = questionList.stream()
                .filter(q -> !q.isAnswered()).collect(Collectors.toList());
        when(questionRepository.selectNotAnswered()).thenReturn(ret);
        assertEquals(questionList.subList(0, 10), questionService.findAllUnanswered(1));
    }

    /**
     * Find by id.
     */
    @Test
    void findById() {
        when(questionRepository.findById(1L)).thenReturn(java.util.Optional.of(q1));
        assertEquals(Optional.of(q1), questionService.findById(1));
    }

    /**
     * Upvote question.
     */
    @Test
    void upvoteQuestion() {
        q1.setUpvotes(4);
        when(questionRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(q1));
        //the upvote count is expected to be incremented by 1, which is 5
        assertEquals(questionService.upvoteQuestion(1),5);
    }

    /**
     * Upvote question test null.
     */
    @Test
    void upvoteQuestionTestNull() {
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());
        assertNull(questionService.upvoteQuestion(1));
    }

    /**
     * Delete by id.
     */
    @Test
    void deleteById() {
        when(questionRepository.findAllById(List.of(1L))).thenReturn(List.of(q1));
        questionService.deleteById(1L);
        verify(questionRepository, times(1)).deleteAll(List.of(q1));
    }

    /**
     * Mark question answered.
     */
    @Test
    void markQuestionAnswered() {
        q1.setAnswered(false);
        questionService = spy(questionService);
        when(questionRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(q1));
        questionService.markQuestionAnswered(1);
        verify(questionService, times(1)).markQuestionAnswered(1);
    }

    /**
     * Select by author id.
     */
    @Test
    void selectByAuthorId() {
        questionList.forEach(q -> {
            if (q.getId() < 11) {
                q.setAuthorId(1);
            } else {
                q.setAuthorId(2);
            }
        });
        when(questionRepository.selectByAuthorId(1)).thenReturn(questionList.subList(0, 10));
        assertEquals(questionList.subList(0, 10), questionService.selectByAuthorId(1));
    }

    /**
     * Rephrase question.
     */
    @Test
    void rephraseQuestion() {
        when(questionRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(q1));
        q1.setContent("new");
        assertEquals(new StringResponse("new"),
                questionService.rephraseQuestion(1,"new"));

        when(questionRepository.findById(60L)).thenReturn(Optional.empty());
        assertNull(questionService.rephraseQuestion(60L,"new"));
    }

    /**
     * Sets answer id.
     */
    @Test
    void setAnswerId() {
        when(questionRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(q1));
        Question q2 = q1;
        q2.setAnswerId(25);
        assertEquals(q2, questionService.setAnswerId(1,25));

        when(questionRepository.findById(60L)).thenReturn(Optional.empty());
        assertNull(questionService.setAnswerId(60,1));
    }

    /**
     * Sort by age.
     */
    @Test
    void sortByAge() {
        questionList.stream()
                .forEach(q -> {
                    if (q.getId() < 7) {
                        q.setLectureId(1);
                    } else {
                        q.setLectureId(2);
                    }
                });
        List<Question> ret = questionList.stream()
                .sorted(Comparator.comparing(Question::getTimePublished))
                .collect(Collectors.toList());
        when(questionRepository.sortByAge()).thenReturn(ret);
        ret = ret.stream().filter(q -> q.getLectureId() == 1).collect(Collectors.toList());
        assertEquals(ret, questionService.sortByAge(1));
    }

    /**
     * Sort by age unanswered.
     */
    @Test
    void sortByAgeUnanswered() {
        questionList.stream()
                .filter(q -> q.getId() < 11)
                .forEach(q -> {
                    if (q.getId() < 7) {
                        q.setLectureId(1);
                        if (q.getId() < 3) {
                            q.setAnswered(true);
                        }
                    } else {
                        q.setLectureId(2);
                    }
                });
        List<Question> ret = questionList.stream()
                .filter(x -> x.isAnswered() == false)
                .sorted(Comparator.comparing(Question::getTimePublished))
                .collect(Collectors.toList());
        when(questionRepository.sortByAgeUnanswered()).thenReturn(ret);
        ret = ret.stream().filter(q -> q.getLectureId() == 1).collect(Collectors.toList());
        assertEquals(ret, questionService.sortByAgeUnanswered(1));
    }

    /**
     * Sort by age answered.
     */
    @Test
    void sortByAgeAnswered() {
        questionList.stream()
                .filter(q -> q.getId() < 11)
                .forEach(q -> {
                    if (q.getId() < 7) {
                        q.setLectureId(1);
                        if (q.getId() < 3) {
                            q.setAnswered(true);
                        }
                    } else {
                        q.setLectureId(2);
                    }
                });
        List<Question> ret = questionList.stream()
                .filter(Question::isAnswered)
                .sorted(Comparator.comparing(Question::getTimePublished))
                .collect(Collectors.toList());
        when(questionRepository.sortByAgeAnswered()).thenReturn(ret);
        ret = ret.stream().filter(q -> q.getLectureId() == 1).collect(Collectors.toList());
        assertEquals(ret, questionService.sortByAgeAnswered(1));
    }

    /**
     * Select by upvotes.
     */
    @Test
    void selectByUpvotes() {
        questionList.stream()
                .forEach(q -> {
                    if (q.getId() < 7) {
                        q.setLectureId(1);
                    } else {
                        q.setLectureId(2);
                    }

                });
        List<Question> ret = questionList.stream()
                .sorted(Comparator.comparingInt(Question::getUpvotes)).collect(Collectors.toList());
        when(questionRepository.selectByUpvotes()).thenReturn(ret);
        ret = ret.stream().filter(q -> q.getLectureId() == 1).collect(Collectors.toList());
        assertEquals(ret, questionService.selectByUpvotes(1));
    }

    /**
     * Select by upvotes unanswered.
     */
    @Test
    void selectByUpvotesUnanswered() {
        questionList.stream()
                .filter(q -> q.getId() < 11)
                .forEach(q -> {
                    if (q.getId() < 7) {
                        q.setLectureId(1);
                        if (q.getId() < 3) {
                            q.setAnswered(true);
                        }
                    } else {
                        q.setLectureId(2);
                    }
                });
        List<Question> ret = questionList.stream().filter(x -> x.isAnswered() == false)
                .sorted(Comparator.comparingInt(Question::getUpvotes)).collect(Collectors.toList());
        when(questionRepository.selectByUpvotesUnanswered()).thenReturn(ret);
        ret = ret.stream().filter(q -> q.getLectureId() == 1).collect(Collectors.toList());
        assertEquals(ret, questionService.selectByUpvotesUnanswered(1));
    }

    /**
     * Select by upvotes answered.
     */
    @Test
    void selectByUpvotesAnswered() {
        questionList.stream()
                .filter(q -> q.getId() < 11)
                .forEach(q -> {
                    if (q.getId() < 7) {
                        q.setLectureId(1);
                        if (q.getId() < 5) {
                            q.setAnswered(true);
                        }
                    } else {
                        q.setLectureId(2);
                    }
                });
        List<Question> ret = questionList.stream().filter(Question::isAnswered)
                .sorted(Comparator.comparingInt(Question::getUpvotes)).collect(Collectors.toList());
        when(questionRepository.selectByUpvotesAnswered()).thenReturn(ret);
        ret = ret.stream().filter(q -> q.getLectureId() == 1).collect(Collectors.toList());
        assertEquals(ret, questionService.selectByUpvotesAnswered(1));
    }

    /**
     * Sort by popularity.
     */
    @Test
    void sortByPopularity() {
        Question q2 = new Question("Question 2?");
        q2.setUpvotes(15);
        q2.setTimePublished(LocalDateTime.of(1980, 1, 1, 11, 50));
        q2.setId(2);
        Question q3 = new Question("Question 2?");
        q3.setUpvotes(1);
        q3.setTimePublished(LocalDateTime.of(1980, 1, 1, 12, 02));
        q3.setId(2);
        List<Question> list = new ArrayList<>();
        list.add(q1);   //q1 --> 12:01, 1 upvotes
        list.add(q2);   //q2 --> 11:50, 15 upvotes
        list.add(q3);   //q3 --> 12:02, 1 upvotes
        assertEquals(List.of(q2,q3,q1), questionService.sortByPopularity(list));
    }

    /**
     * Select by popularity.
     */
    @Test
    void selectByPopularity() {
        questionList.stream().filter(q -> q.getId() < 5).forEach(q -> q.setLectureId(1));
        when(questionRepository.findAll()).thenReturn(questionList);
        List<Question> ret = questionList.stream()
                .filter(q -> q.getLectureId() == 1).collect(Collectors.toList());
        ret = questionService.sortByPopularity(ret);
        assertEquals(ret, questionService.selectByPopularity(1));
    }

    /**
     * Select by popularity unanswered.
     */
    @Test
    void selectByPopularityUnanswered() {
        questionList.stream().filter(q -> q.getId() < 6).forEach(q -> q.setLectureId(1));
        questionList.stream().filter(q -> q.getId() < 3).forEach(q -> q.setAnswered(true));
        List<Question> ret = questionList.stream().filter(x -> x.isAnswered() == false)
                .sorted(Comparator.comparingInt(Question::getUpvotes)).collect(Collectors.toList());
        when(questionRepository.selectByUpvotesUnanswered()).thenReturn(ret);
        ret = ret.stream()
                .filter(q -> q.getLectureId() == 1).collect(Collectors.toList());
        ret = questionService.sortByPopularity(ret);
        assertEquals(ret, questionService.selectByPopularityUnanswered(1));
    }

    /**
     * Select by popularity answered.
     */
    @Test
    void selectByPopularityAnswered() {
        questionList.stream().filter(q -> q.getId() < 6).forEach(q -> q.setLectureId(1));
        questionList.stream().filter(q -> q.getId() < 3).forEach(q -> q.setAnswered(true));
        List<Question> ret = questionList.stream().filter(x -> x.isAnswered() == true)
                .sorted(Comparator.comparingInt(Question::getUpvotes)).collect(Collectors.toList());
        when(questionRepository.selectByUpvotesAnswered()).thenReturn(ret);
        ret = ret.stream()
                .filter(q -> q.getLectureId() == 1).collect(Collectors.toList());
        ret = questionService.sortByPopularity(ret);
        assertEquals(ret, questionService.selectByPopularityAnswered(1));
    }
}
