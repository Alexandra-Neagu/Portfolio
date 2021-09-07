package nl.tudelft.oopp.demo.communication;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import nl.tudelft.oopp.demo.MainApp;
import nl.tudelft.oopp.demo.data.Question;
import nl.tudelft.oopp.demo.data.Room;
import nl.tudelft.oopp.demo.data.Student;
import nl.tudelft.oopp.demo.serializers.LocalDateAdapter;
import nl.tudelft.oopp.demo.serializers.LocalDateDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalDateTimeDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalTimeAdapter;
import nl.tudelft.oopp.demo.serializers.LocalTimeDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * The type Room communication test.
 */
public class RoomCommunicationTest {

    private static Room room1;
    /**
     * The Wire mock server.
     */
    static WireMockServer wireMockServer = new WireMockServer();

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeDeserializer())
            .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
            .create();

    private void createStub(String mode, String relativeUrl, String responseBody, int statusCode) {
        switch (mode) {
            case "post":
                stubFor(post(urlEqualTo(relativeUrl))
                        .willReturn(aResponse().withBody(responseBody).withStatus(statusCode)));
                break;
            case "delete":
                stubFor(delete(urlEqualTo(relativeUrl))
                        .willReturn(aResponse().withBody(responseBody).withStatus(200)));
                break;
            default:
                stubFor(get(urlEqualTo(relativeUrl))
                        .willReturn(aResponse().withBody(responseBody).withStatus(statusCode)));
        }
    }

    /**
     * Init.
     */
    @BeforeAll
    static void init() {
        wireMockServer.start();
        room1 = new Room("Calculus",
                "Numbers",
                LocalDate.now(),
                LocalTime.now(),
                LocalTime.now(),
                "1", "2", "3",0,0);
        configureFor("localhost", 8080);
    }

    /**
     * Stop server.
     */
    @AfterAll
    static void stopServer() {
        wireMockServer.stop();
    }

    private void setCurrentRoom() {
        MainApp.setCurrentUser(new Student("student"));
        MainApp.getCurrentUser().setRoomId(1);
    }

    /**
     * Test create room.
     */
    @Test
    void testCreateRoom() {
        createStub("post", "/rooms/insert", gson.toJson(room1), 200);
        assertEquals(room1, RoomCommunication.createRoom(room1));
        verify(postRequestedFor(urlEqualTo("/rooms/insert")));
    }

    /**
     * Test get room.
     */
    @Test
    void testGetRoom() {
        createStub("get", "/rooms/get/1", gson.toJson(room1), 200);
        assertEquals(room1, RoomCommunication.getRoom(1));
        verify(getRequestedFor(urlEqualTo("/rooms/get/1")));
    }

    /**
     * Test increment too fast count.
     */
    @Test
    void testIncrementTooFastCount() {
        setCurrentRoom();
        createStub("get", "/rooms/incrementTooFast/1", "1", 200);
        assertEquals(1, RoomCommunication.incrementTooFastCount());
        verify(getRequestedFor(urlEqualTo("/rooms/incrementTooFast/1")));
    }

    /**
     * Test increment too slow count.
     */
    @Test
    void testIncrementTooSlowCount() {
        setCurrentRoom();
        createStub("get", "/rooms/incrementTooSlow/1", "1", 200);
        assertEquals(1, RoomCommunication.incrementTooSlowCount());
        verify(getRequestedFor(urlEqualTo("/rooms/incrementTooSlow/1")));
    }

    /**
     * Test get too fast count.
     */
    @Test
    void testGetTooFastCount() {
        setCurrentRoom();
        String relativeUrl = "/rooms/getTooFast/1";
        createStub("get", relativeUrl, "1", 200);
        assertEquals(1, RoomCommunication.getTooFastCount());
        verify(getRequestedFor(urlEqualTo(relativeUrl)));
    }

    /**
     * Test set too fast count.
     */
    @Test
    void testSetTooFastCount() {
        setCurrentRoom();
        String relativeUrl = "/rooms/setTooFast/1";
        createStub("get", relativeUrl, "1", 200);
        assertEquals(1, RoomCommunication.setTooFastCount());
        verify(getRequestedFor(urlEqualTo(relativeUrl)));
    }

    /**
     * Test get too slow count.
     */
    @Test
    void testGetTooSlowCount() {
        setCurrentRoom();
        String relativeUrl = "/rooms/getTooSlow/1";
        createStub("get", relativeUrl, "1", 200);
        assertEquals(1, RoomCommunication.getTooSlowCount());
        verify(getRequestedFor(urlEqualTo(relativeUrl)));

    }

    /**
     * Test set too slow count.
     */
    @Test
    void testSetTooSlowCount() {
        setCurrentRoom();
        String relativeUrl = "/rooms/setTooSlow/1";
        createStub("get", relativeUrl, "1", 200);
        assertEquals(1, RoomCommunication.setTooSlowCount());
        verify(getRequestedFor(urlEqualTo(relativeUrl)));
    }

    /**
     * Test increase student count.
     */
    @Test
    void testIncreaseStudentCount() {
        setCurrentRoom();
        String relativeUrl = "/rooms/increaseStudentCount/1";
        createStub("get", relativeUrl, "1", 200);
        assertEquals(1, RoomCommunication.increaseStudentCount());
        verify(getRequestedFor(urlEqualTo(relativeUrl)));
    }

    /**
     * Test decrease student count.
     */
    @Test
    void testDecreaseStudentCount() {
        setCurrentRoom();
        String relativeUrl = "/rooms/decreaseStudentCount/1";
        createStub("get", relativeUrl, "1", 200);
        assertEquals(1, RoomCommunication.decreaseStudentCount());
        verify(getRequestedFor(urlEqualTo(relativeUrl)));
    }

    /**
     * Test get student count.
     */
    @Test
    void testGetStudentCount() {
        setCurrentRoom();
        String relativeUrl = "/rooms/getStudentCount/1";
        createStub("get", relativeUrl, "1", 200);
        assertEquals(1, RoomCommunication.getStudentCount());
        verify(getRequestedFor(urlEqualTo(relativeUrl)));
    }

    /**
     * Test close room.
     */
    @Test
    void testCloseRoom() {
        setCurrentRoom();
        String relativeUrl = "/rooms/close/1";
        createStub("get", relativeUrl, gson.toJson(room1), 200);
        assertEquals(room1, RoomCommunication.closeRoom());
        verify(getRequestedFor(urlEqualTo(relativeUrl)));
    }

    /**
     * Test reopen room for mod lect.
     */
    @Test
    void testReopenRoomForModLect() {
        setCurrentRoom();
        String relativeUrl = "/rooms/reopenForModLect/1";
        createStub("get", relativeUrl, gson.toJson(room1), 200);
        assertEquals(room1, RoomCommunication.reopenRoomForModLect());
        verify(getRequestedFor(urlEqualTo(relativeUrl)));
    }

}
