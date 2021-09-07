package nl.tudelft.oopp.demo.controllertests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.StringResponse;
import nl.tudelft.oopp.demo.serializers.LocalDateAdapter;
import nl.tudelft.oopp.demo.serializers.LocalDateDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalDateTimeDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalTimeAdapter;
import nl.tudelft.oopp.demo.serializers.LocalTimeDeserializer;
import nl.tudelft.oopp.demo.services.QuestionService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;


/**
 * The Question controller test.
 * All that a controller does is manage the request we get,
 * and send back whatever we get from the service.
 * Therefore that is what we test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.test.context.TestPropertySource(
        locations = "classpath:application-test.properties")
public class QuestionControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private QuestionService questionService;

    @Autowired
    private MockMvc mvc;

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeDeserializer())
            .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
            .create();


    private static List<Question> questionList;
    private static Question q1;

    /**
     * Init.
     */
    @org.junit.jupiter.api.BeforeEach
    void init() {
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
     * Find all with one question.
     *
     * @throws Exception the exception
     */
    @Test
    public void findall_withOneQuestion() throws Exception {
        // set the response to a list of questions containing 1 question.
        // This is what we want to receive from the service.
        List<Question> response = List.of(q1);
        // when the findall method of questionservice is called, return the response.
        when(questionService.findAll()).thenReturn(response);
        // magically perform the get request
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/questions")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        List<Question> questions = gson.fromJson(result.getResponse().getContentAsString(),
                new TypeToken<List<Question>>() {
                }.getType()); // get the question from the JSON
        assertEquals(q1, questions.get(0));
    }

    /**
     * Findall with two questions.
     *
     * @throws Exception the exception
     */
    @Test
    public void findall_withTwoQuestions() throws Exception {
        List<Question> response = List.of(q1, questionList.get(1));
        List<Question> questions = mvcPerformQuestionList("/questions", "findAll", response, null);
        assertEquals(List.of(q1, questionList.get(1)), questions);
    }

    private List<Question> mvcPerformQuestionList(String urlTemplate,
                                                  String methodName,
                                                  List<Question> response,
                                                  Long param) throws Exception {
        Method method;
        Object[] obj;
        if (param == null) {
            obj = new Object[0];
            method = questionService.getClass().getDeclaredMethod(methodName);
        } else {
            obj = new Object[1];
            obj[0] = param;
            method = questionService.getClass().getDeclaredMethod(methodName,Long.TYPE);
        }
        when(method.invoke(questionService, obj)).thenReturn(response);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(urlTemplate)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        return gson.fromJson(result.getResponse().getContentAsString(),
                new TypeToken<List<Question>>() {
                }.getType());
    }

    /**
     * Test save.
     *
     * @throws Exception the exception
     */
    @Test
    void testSave() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String request = mapper.writeValueAsString(q1);

        when(questionService.save(q1)).thenReturn(q1);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/questions/insert")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Question question = gson.fromJson(result.getResponse()
                .getContentAsString(), Question.class);
        verify(questionService).save(any(Question.class));
        assertEquals(q1, question);
    }

    /**
     * Test find by id.
     *
     * @throws Exception the exception
     */
    @Test
    void testFindById() throws Exception {
        when(questionService.findById(1)).thenReturn(Optional.of(q1));
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/questions/get/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        assertEquals(q1, gson.fromJson(result.getResponse().getContentAsString(), Question.class));
        verify(questionService).findById(1);
    }


    /**
     * Test find all by room id.
     *
     * @throws Exception the exception
     */
    @Test
    void testFindAllByRoomId() throws Exception {
        assertEquals(List.of(q1, questionList.get(0)),
                mvcPerformQuestionList("/questions/getAllinRoom/1",
                        "findAllByRoomId",
                        List.of(q1, questionList.get(0)), 1L));
    }

    /**
     * Test get all answered.
     *
     * @throws Exception the exception
     */
    @Test
    void testGetAllAnswered() throws Exception {
        assertEquals(List.of(q1, questionList.get(0)),
                mvcPerformQuestionList("/questions/getAllAnswered/1",
                        "findAllAnswered",
                        List.of(q1, questionList.get(0)), 1L));
    }

    /**
     * Test get all unanswered.
     *
     * @throws Exception the exception
     */
    @Test
    void testGetAllUnanswered() throws Exception {
        assertEquals(List.of(q1, questionList.get(0)),
                mvcPerformQuestionList("/questions/getAllUnanswered/1",
                        "findAllUnanswered",
                        List.of(q1, questionList.get(0)), 1L));
    }

    /**
     * Test upvote question.
     *
     * @throws Exception the exception
     */
    @Test
    void testUpvoteQuestion() throws Exception {
        when(questionService.upvoteQuestion(1)).thenReturn(1);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/questions/upvote/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        assertEquals(1, gson.fromJson(result.getResponse().getContentAsString(), Integer.class));
    }

    /**
     * Test delete byid.
     *
     * @throws Exception the exception
     */
    @Test
    void testDeleteByid() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.delete("/questions/delete/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        verify(questionService).deleteById(1);
    }

    /**
     * Test mark question answered.
     *
     * @throws Exception the exception
     */
    @Test
    void testMarkQuestionAnswered() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/questions/markasanswered/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        verify(questionService).markQuestionAnswered(1);
    }

    /**
     * Test select by student id.
     *
     * @throws Exception the exception
     */
    @Test
    void testSelectByStudentId() throws Exception {
        assertEquals(List.of(q1, questionList.get(0)),
                mvcPerformQuestionList("/questions/showbystudentid/1",
                        "selectByAuthorId",
                        List.of(q1, questionList.get(0)), 1L));
    }

    /**
     * Test sort by age.
     *
     * @throws Exception the exception
     */
    @Test
    void testSortByAge() throws Exception {
        assertEquals(List.of(q1, questionList.get(0)),
                mvcPerformQuestionList("/questions/sortbyage/1",
                        "sortByAge",
                        List.of(q1, questionList.get(0)), 1L));
    }

    /**
     * Test sort by age unanswered.
     *
     * @throws Exception the exception
     */
    @Test
    void testSortByAgeUnanswered() throws Exception {
        assertEquals(List.of(q1, questionList.get(0)),
                mvcPerformQuestionList("/questions/sortbyageunanswered/1",
                        "sortByAgeUnanswered",
                        List.of(q1, questionList.get(0)), 1L));
    }

    /**
     * Test sort by age answered.
     *
     * @throws Exception the exception
     */
    @Test
    void testSortByAgeAnswered() throws Exception {
        assertEquals(List.of(q1, questionList.get(0)),
                mvcPerformQuestionList("/questions/sortbyageanswered/1",
                        "sortByAgeAnswered",
                        List.of(q1, questionList.get(0)), 1L));
    }

    /**
     * Test select by upvotes.
     *
     * @throws Exception the exception
     */
    @Test
    void testSelectByUpvotes() throws Exception {
        assertEquals(List.of(q1, questionList.get(0)),
                mvcPerformQuestionList("/questions/sortByUpvotes/1",
                        "selectByUpvotes",
                        List.of(q1, questionList.get(0)), 1L));
    }

    /**
     * Test select by upvotes unanswered.
     *
     * @throws Exception the exception
     */
    @Test
    void testSelectByUpvotesUnanswered() throws Exception {
        assertEquals(List.of(q1, questionList.get(0)),
                mvcPerformQuestionList("/questions/sortByUpvotesUnanswered/1",
                        "selectByUpvotesUnanswered",
                        List.of(q1, questionList.get(0)), 1L));
    }

    /**
     * Test select by upvotes answered.
     *
     * @throws Exception the exception
     */
    @Test
    void testSelectByUpvotesAnswered() throws Exception {
        assertEquals(List.of(q1, questionList.get(0)),
                mvcPerformQuestionList("/questions/sortByUpvotesAnswered/1",
                        "selectByUpvotesAnswered",
                        List.of(q1, questionList.get(0)), 1L));
    }

    /**
     * Test select by popularity.
     *
     * @throws Exception the exception
     */
    @Test
    void testSelectByPopularity() throws Exception {
        assertEquals(List.of(q1, questionList.get(0)),
                mvcPerformQuestionList("/questions/sortByPopularity/1",
                        "selectByPopularity",
                        List.of(q1, questionList.get(0)), 1L));
    }

    /**
     * Test select by popularity unanswered.
     *
     * @throws Exception the exception
     */
    @Test
    void testSelectByPopularityUnanswered() throws Exception {
        assertEquals(List.of(q1, questionList.get(0)),
                mvcPerformQuestionList("/questions/sortByPopularityUnanswered/1",
                        "selectByPopularityUnanswered",
                        List.of(q1, questionList.get(0)), 1L));
    }

    /**
     * Test select by popularity answered.
     *
     * @throws Exception the exception
     */
    @Test
    void testSelectByPopularityAnswered() throws Exception {
        assertEquals(List.of(q1, questionList.get(0)),
                mvcPerformQuestionList("/questions/sortByPopularityAnswered/1",
                        "selectByPopularityAnswered",
                        List.of(q1, questionList.get(0)), 1L));
    }

    /**
     * Test rephrase question.
     *
     * @throws Exception the exception
     */
    @Test
    void testRephraseQuestion() throws Exception {
        String newContent = "New";

        when(questionService.rephraseQuestion(1, "New")).thenReturn(new StringResponse("New"));
        when(questionService.save(q1)).thenReturn(q1);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/questions/rephrase/1")
                .content(newContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        StringResponse sr = gson.fromJson(result.getResponse()
                .getContentAsString(), StringResponse.class);

        assertEquals(sr.getResponse(), newContent);
    }

    /**
     * Test set answer id.
     *
     * @throws Exception the exception
     */
    @Test
    void testSetAnswerId() throws Exception {

        Question q2 = new Question("Question 1?");
        q2.setAnswered(false);
        q2.setAuthorId(1);
        q2.setLectureId(1);
        q2.setUpvotes(1);
        q2.setTimePublished(LocalDateTime.of(1980, 1, 1, 12, 1));
        q2.setId(1);
        q2.setAnswerId(2);

        when(questionService.setAnswerId(1, 2)).thenReturn(q2);
        when(questionService.save(q1)).thenReturn(q1);

        long answerId = 2;
        String request = gson.toJson(answerId);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/questions/setAnswerId/1")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Question question = gson.fromJson(result.getResponse()
                .getContentAsString(), Question.class);

        assertEquals(answerId, question.getAnswerId());
    }
}