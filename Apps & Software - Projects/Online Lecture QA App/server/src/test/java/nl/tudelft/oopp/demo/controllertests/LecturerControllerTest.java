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

import nl.tudelft.oopp.demo.entities.Lecturer;
import nl.tudelft.oopp.demo.serializers.LocalDateAdapter;
import nl.tudelft.oopp.demo.serializers.LocalDateDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalDateTimeDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalTimeAdapter;
import nl.tudelft.oopp.demo.serializers.LocalTimeDeserializer;
import nl.tudelft.oopp.demo.services.LecturerService;

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
 * The type Lecturer controller test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.test.context.TestPropertySource(
        locations = "classpath:application-test.properties")
public class LecturerControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private LecturerService lecturerService;

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

    private static List<Lecturer> lecturerList;
    private static  Lecturer l1;

    /**
     * Init.
     */
    @org.junit.jupiter.api.BeforeAll
    public static void init() {
        lecturerList = new ArrayList<Lecturer>();
        for (int i = 1; i < 5; i++) {
            Lecturer l = new Lecturer("name" + i, "100." + i, 1,
                    LocalDateTime.of(2021, 1, 1, 12, i));

            l.setId(i);
            lecturerList.add(l);
        }

        l1 = new Lecturer("name", "Exampleip", 1, LocalDateTime.of(2021, 1, 1, 12, 1));
        l1.setId(10);
    }


    /**
     * Test find by id.
     *
     * @throws Exception the exception
     */
    @Test
    void testFindById() throws Exception {
        when(lecturerService.findById(1)).thenReturn(Optional.of(l1));
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/lecturers/get/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertEquals(l1, gson.fromJson(result.getResponse().getContentAsString(), Lecturer.class));
        verify(lecturerService).findById(1);
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
        String request = mapper.writeValueAsString(l1);

        when(lecturerService.save(l1)).thenReturn(l1);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/lecturers/insert")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Lecturer lecturer = gson.fromJson(result.getResponse()
                .getContentAsString(), Lecturer.class);
        verify(lecturerService).save(any(Lecturer.class));
        assertEquals(l1, lecturer);
    }

}
