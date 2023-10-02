package nl.tudelft.oopp.demo.controllertests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.serializers.LocalDateAdapter;
import nl.tudelft.oopp.demo.serializers.LocalDateDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalDateTimeDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalTimeAdapter;
import nl.tudelft.oopp.demo.serializers.LocalTimeDeserializer;
import nl.tudelft.oopp.demo.services.RoomService;
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
 * The Room controller test.
 * All that a controller does is manage the request we get,
 * and send back whatever we get from the service.
 * Therefore that is what we test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.test.context.TestPropertySource(
        locations = "classpath:application-test.properties")
public class RoomControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private RoomService roomService;

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


    private static List<Room> roomList;
    private static Room r1;

    /**
     * Init.
     */
    @org.junit.jupiter.api.BeforeAll
    public static void init() {
        r1 = new Room();
        r1.setId(1);
        r1.setScheduledDate(LocalDate.of(2000, 1, 1));
        r1.setLectureName("Git");
        r1.setCourseName("OOPP");
        r1.setStartTime(LocalTime.of(10, 30));
        r1.setEndTime(LocalTime.of(12, 0));
        r1.setHasBeenClosed(false);

        roomList = new ArrayList<Room>();
        for (int i = 1; i < 15; i++) {
            Room r = new Room();
            r.setId(1);
            r.setScheduledDate(LocalDate.of(2000, 1, i));
            r.setLectureName("Lecture " + i);
            r.setCourseName("Course " + i);
            r.setStartTime(LocalTime.of(10, i));
            r.setEndTime(LocalTime.of(12, i));
            r.setHasBeenClosed(false);
            roomList.add(r);
        }
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
        String request = mapper.writeValueAsString(r1);

        when(roomService.save(r1)).thenReturn(r1);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/rooms/insert")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Room room = gson.fromJson(result.getResponse()
                .getContentAsString(), Room.class);
        verify(roomService).save(any(Room.class));
        assertEquals(r1, room);
    }

    /**
     * Test increment too fast.
     *
     * @throws Exception the exception
     */
    @Test
    void testIncrementTooFast() throws Exception {
        r1.setTooFastCount(1);

        when(roomService.incrementTooFast(1)).thenReturn(2);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/rooms/incrementTooFast/1"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Integer tooFastCount = gson.fromJson(result.getResponse()
                .getContentAsString(), Integer.class);
        assertEquals(2, tooFastCount);
    }

    /**
     * Test increment too slow.
     *
     * @throws Exception the exception
     */
    @Test
    void testIncrementTooSlow() throws Exception {
        r1.setTooSlowCount(1);

        when(roomService.incrementTooSlow(1)).thenReturn(2);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/rooms/incrementTooSlow/1"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Integer tooSlowCount = gson.fromJson(result.getResponse()
                .getContentAsString(), Integer.class);
        assertEquals(2, tooSlowCount);
    }

    /**
     * Test get too slow.
     *
     * @throws Exception the exception
     */
    @Test
    void testGetTooSlow() throws Exception {
        r1.setTooSlowCount(1);

        when(roomService.getTooSlow(1)).thenReturn(r1.getTooSlowCount());

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/rooms/getTooSlow/1"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Integer tooSlowCount = gson.fromJson(result.getResponse()
                .getContentAsString(), Integer.class);
        assertEquals(1, tooSlowCount);
    }

    /**
     * Test get too fast.
     *
     * @throws Exception the exception
     */
    @Test
    void testGetTooFast() throws Exception {
        r1.setTooFastCount(1);

        when(roomService.getTooFast(1)).thenReturn(r1.getTooFastCount());

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/rooms/getTooFast/1"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Integer tooFastCount = gson.fromJson(result.getResponse()
                .getContentAsString(), Integer.class);
        assertEquals(1, tooFastCount);
    }


    /**
     * Test increment student count.
     *
     * @throws Exception the exception
     */
    @Test
    void testIncrementStudentCount() throws Exception {

        when(roomService.incrementStudentCount(1)).thenReturn(1);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/rooms/increaseStudentCount/1"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Integer studentCount = gson.fromJson(result.getResponse()
                .getContentAsString(), Integer.class);
        assertEquals(1, studentCount);

    }

    /**
     * Test decrement student count.
     *
     * @throws Exception the exception
     */
    @Test
    void testDecrementStudentCount() throws Exception {

        r1.increaseStudentCount();
        r1.increaseStudentCount();
        when(roomService.decrementStudentCount(1)).thenReturn(1);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/rooms/decreaseStudentCount/1"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Integer studentCount = gson.fromJson(result.getResponse()
                .getContentAsString(), Integer.class);
        assertEquals(1, studentCount);
    }

    /**
     * Test get student count.
     *
     * @throws Exception the exception
     */
    @Test
    void testGetStudentCount() throws Exception {

        roomList.get(0).increaseStudentCount();
        roomList.get(0).increaseStudentCount();
        when(roomService.getStudentCount(1)).thenReturn(roomList.get(0).getStudentCount());

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/rooms/getStudentCount/1"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Integer studentCount = gson.fromJson(result.getResponse()
                .getContentAsString(), Integer.class);
        assertEquals(2, studentCount);
    }


    /**
     * Test close room.
     *
     * @throws Exception the exception
     */
    @Test
    void testCloseRoom() throws Exception {

        r1.setStudentRoomId("ExampleCode");

        when(roomService.findById(1)).thenReturn(Optional.ofNullable(r1));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/rooms/close/1"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Room room = gson.fromJson(result.getResponse()
                .getContentAsString(), Room.class);
        assertNull(room);
    }

    /**
     * Test reopen room for mod lect.
     *
     * @throws Exception the exception
     */
    @Test
    void testReopenRoomForModLect() throws Exception {

        r1.setStudentRoomId("ExampleCode");
        roomService.closeRoom(1);
        r1.setHasBeenClosed(true);

        when(roomService.reopenRoomForModLect(1)).thenReturn(r1);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/rooms/reopenForModLect/1"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Room room = gson.fromJson(result.getResponse()
                .getContentAsString(), Room.class);
        assertEquals(r1, room);
        assertNotNull(r1);
    }

    /**
     * Test find by id.
     *
     * @throws Exception the exception
     */
    @Test
    void testFindById() throws Exception {
        when(roomService.findById(1)).thenReturn(Optional.of(r1));
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/rooms/get/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        assertEquals(r1, gson.fromJson(result.getResponse().getContentAsString(), Room.class));
        verify(roomService).findById(1);
    }


    /**
     * Findall with one room.
     *
     * @throws Exception the exception
     */
    @Test
    public void findall_withOneRoom() throws Exception {
        List<Room> response = List.of(r1);
        when(roomService.findAll()).thenReturn(response);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/rooms")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        List<Room> rooms = gson.fromJson(result.getResponse().getContentAsString(),
                new TypeToken<List<Room>>() {
                }.getType());
        assertEquals(r1, rooms.get(0));
    }

    /**
     * Findall with two rooms.
     *
     * @throws Exception the exception
     */
    @Test
    public void findall_withTwoRooms() throws Exception {
        List<Room> response = List.of(r1, roomList.get(0));
        when(roomService.findAll()).thenReturn(response);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/rooms")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        List<Room> rooms = gson.fromJson(result.getResponse().getContentAsString(),
                new TypeToken<List<Room>>() {
                }.getType());
        assertEquals(response, rooms);
    }


}
