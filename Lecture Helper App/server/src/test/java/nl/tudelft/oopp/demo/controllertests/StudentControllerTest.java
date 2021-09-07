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
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.tudelft.oopp.demo.entities.Student;
import nl.tudelft.oopp.demo.serializers.LocalDateAdapter;
import nl.tudelft.oopp.demo.serializers.LocalDateDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalDateTimeDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalTimeAdapter;
import nl.tudelft.oopp.demo.serializers.LocalTimeDeserializer;
import nl.tudelft.oopp.demo.services.StudentService;

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
 * The type Student controller test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.test.context.TestPropertySource(
        locations = "classpath:application-test.properties")
public class StudentControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private StudentService studentService;

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



    private static List<Student> studentList;
    private static Student s1;


    /**
     * Init.
     */
    @org.junit.jupiter.api.BeforeAll
    public static void init() {
        studentList = new ArrayList<Student>();
        for (int i = 1; i < 5; i++) {
            Student s = new Student("name" + i, "100." + i, 1,
                    LocalDateTime.of(2021, 1, 1, 12, i));

            s.setId(i);
            studentList.add(s);
        }

        s1 = new Student("name", "Exampleip", 1, LocalDateTime.of(2021, 1, 1, 12, 1));
        s1.setId(10);
    }

    /**
     * Test find by id.
     *
     * @throws Exception the exception
     */
    @Test
    void testFindById() throws Exception {
        when(studentService.findById(1)).thenReturn(Optional.of(s1));
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/students/get/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        assertEquals(s1, gson.fromJson(result.getResponse().getContentAsString(), Student.class));
        verify(studentService).findById(1);
    }

    /**
     * Test delete byid.
     *
     * @throws Exception the exception
     */
    @Test
    void testDeleteByid() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.delete("/students/delete/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        verify(studentService).deleteById(1);
    }

    /**
     * Test find all.
     *
     * @throws Exception the exception
     */
    @Test
    public void testFindAll() throws Exception {
        List<Student> response = List.of(s1, studentList.get(1));
        List<Student> students = mvcPerformStudentList("/students", "findAll", response, null);
        assertEquals(List.of(s1, studentList.get(1)), students);
    }

    private List<Student> mvcPerformStudentList(String urlTemplate,
                                                String methodName,
                                                List<Student> response,
                                                Long param) throws Exception {
        Method method;
        Object[] obj;
        if (param == null) {
            obj = new Object[0];
            method = studentService.getClass().getDeclaredMethod(methodName);
        } else {
            obj = new Object[1];
            obj[0] = param;
            method = studentService.getClass().getDeclaredMethod(methodName,Long.TYPE);
        }
        when(method.invoke(studentService, obj)).thenReturn(response);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(urlTemplate)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        return gson.fromJson(result.getResponse().getContentAsString(),
                new TypeToken<List<Student>>() {
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
        String request = mapper.writeValueAsString(s1);

        when(studentService.save(s1)).thenReturn(s1);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/students/insert")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Student student = gson.fromJson(result.getResponse()
                .getContentAsString(), Student.class);
        verify(studentService).save(any(Student.class));
        assertEquals(s1, student);
    }


}

