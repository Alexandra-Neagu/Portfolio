package nl.tudelft.oopp.demo.controllertests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.serializers.LocalDateAdapter;
import nl.tudelft.oopp.demo.serializers.LocalDateDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalDateTimeDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalTimeAdapter;
import nl.tudelft.oopp.demo.serializers.LocalTimeDeserializer;
import nl.tudelft.oopp.demo.services.PollService;
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
 * The type Poll controller test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.test.context.TestPropertySource(
        locations = "classpath:application-test.properties")
public class PollControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private PollService pollService;

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


    private static List<Poll> pollList;
    private static Poll p1;

    /**
     * Init.
     */
    @org.junit.jupiter.api.BeforeAll
    public static void init() {

        List<String> answerList = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
            String a = "Answer " + i;
            answerList.add(a);
        }

        pollList = new ArrayList<Poll>();
        for (int i = 1; i < 21; i++) {
            Poll p = new Poll();
            p.setLectureId(i);
            p.setQuestion("Poll? " + i);
            p.setCorrectAnswerNo(1);
            p.setAnswers(answerList);
            pollList.add(p);
        }

        p1 = new Poll();
        p1.setLectureId(1);
        p1.setQuestion("Poll? " + 1);
        p1.setCorrectAnswerNo(1);
        p1.setAnswers(answerList);
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
        String request = mapper.writeValueAsString(p1);

        when(pollService.save(p1)).thenReturn(p1);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/polls/insert")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Poll poll = gson.fromJson(result.getResponse()
                .getContentAsString(), Poll.class);
        verify(pollService).save(any(Poll.class));
        assertEquals(p1, poll);
    }

    /**
     * Test delete byid.
     *
     * @throws Exception the exception
     */
    @Test
    void testDeleteByid() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.delete("/polls/delete/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        verify(pollService).deleteById(1);
    }

    /**
     * Test close poll.
     *
     * @throws Exception the exception
     */
    @Test
    void testClosePoll() throws Exception {

        when(pollService.findById(1)).thenReturn(Optional.ofNullable(p1));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/polls/close/1"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Poll poll = gson.fromJson(result.getResponse()
                .getContentAsString(), Poll.class);
        assertNull(poll);
    }

    /**
     * Test open poll.
     *
     * @throws Exception the exception
     */
    @Test
    void testOpenPoll() throws Exception {

        when(pollService.openPoll(1)).thenReturn(p1);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/polls/open/1"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Poll poll = gson.fromJson(result.getResponse()
                .getContentAsString(), Poll.class);
        assertNotNull(poll);
    }

    /**
     * Test add vote to poll.
     *
     * @throws Exception the exception
     */
    @Test
    void testAddVoteToPoll() throws Exception {

        when(pollService.openPoll(1)).thenReturn(p1);
        when(pollService.addVoteToPoll(1,1)).thenReturn(1);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/polls/addVote/1/1"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Integer vote = gson.fromJson(result.getResponse()
                .getContentAsString(), Integer.class);
        assertEquals(1, vote);
    }


    /**
     * Findall with one poll.
     *
     * @throws Exception the exception
     */
    @Test
    public void findall_withOnePoll() throws Exception {
        // set the response to a list of polls containing 1 poll.
        // This is what we want to receive from the service.
        List<Poll> response = List.of(p1);
        // when the findall method of pollservice is called, return the response.
        when(pollService.findAll()).thenReturn(response);
        // magically perform the get request
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/polls/getAll")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        List<Poll> polls = gson.fromJson(result.getResponse().getContentAsString(),
                new TypeToken<List<Poll>>() {
                }.getType()); // get the question from the JSON
        assertEquals(p1, polls.get(0));
    }


    /**
     * Findall with two polls.
     *
     * @throws Exception the exception
     */
    @Test
    public void findall_withTwoPolls() throws Exception {
        List<Poll> response = List.of(p1, pollList.get(1));
        List<Poll> polls = mvcPerformPollList("/polls/getAll", "findAll", response, null);
        assertEquals(List.of(p1, pollList.get(1)), polls);
    }

    private List<Poll> mvcPerformPollList(String urlTemplate,
                                          String methodName,
                                          List<Poll> response,
                                          Long param) throws Exception {
        Method method;
        Object[] obj;
        if (param == null) {
            obj = new Object[0];
            method = pollService.getClass().getDeclaredMethod(methodName);
        } else {
            obj = new Object[1];
            obj[0] = param;
            method = pollService.getClass().getDeclaredMethod(methodName,Long.TYPE);
        }
        when(method.invoke(pollService, obj)).thenReturn(response);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(urlTemplate)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        return gson.fromJson(result.getResponse().getContentAsString(),
                new TypeToken<List<Poll>>() {
                }.getType());
    }
}
