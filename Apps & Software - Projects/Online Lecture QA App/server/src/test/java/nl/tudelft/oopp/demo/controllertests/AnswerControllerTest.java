package nl.tudelft.oopp.demo.controllertests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.tudelft.oopp.demo.entities.Answer;
import nl.tudelft.oopp.demo.serializers.LocalDateAdapter;
import nl.tudelft.oopp.demo.serializers.LocalDateDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalDateTimeDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalTimeAdapter;
import nl.tudelft.oopp.demo.serializers.LocalTimeDeserializer;
import nl.tudelft.oopp.demo.services.AnswerService;
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
 * The type Answer controller test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.test.context.TestPropertySource(
        locations = "classpath:application-test.properties")
public class AnswerControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private AnswerService answerService;

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


    private static List<Answer> answerList;
    private static Answer a1;

    /**
     * Init.
     */
    @org.junit.jupiter.api.BeforeAll
    public static void init() {
        answerList = new ArrayList<Answer>();
        for (int i = 1; i < 21; i++) {
            Answer a = new Answer("Question " + i);
            a.setModeratorId(i);
            a.setTimePublished(LocalDateTime.of(1980, 1, i, 12, i));
            a.setId(i);
            answerList.add(a);
        }

        a1 = new Answer("Answer 1?");
        a1.setModeratorId(1);
        a1.setTimePublished(LocalDateTime.of(1980, 1, 1, 12, 1));
        a1.setId(1);
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
        String request = mapper.writeValueAsString(a1);

        when(answerService.save(a1)).thenReturn(a1);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/answers/insert")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Answer answer = gson.fromJson(result.getResponse()
                .getContentAsString(), Answer.class);
        verify(answerService).save(any(Answer.class));
        assertEquals(a1, answer);
    }

    /**
     * Test find by id.
     *
     * @throws Exception the exception
     */
    @Test
    void testFindById() throws Exception {
        when(answerService.findById(1)).thenReturn(Optional.of(a1));
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/answers/get/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertEquals(a1, gson.fromJson(result.getResponse().getContentAsString(), Answer.class));
        verify(answerService).findById(1);
    }

}

