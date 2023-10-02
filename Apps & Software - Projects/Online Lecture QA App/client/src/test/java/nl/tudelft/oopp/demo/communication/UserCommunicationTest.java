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

import nl.tudelft.oopp.demo.MainApp;
import nl.tudelft.oopp.demo.data.Lecturer;
import nl.tudelft.oopp.demo.data.Moderator;
import nl.tudelft.oopp.demo.data.Student;
import nl.tudelft.oopp.demo.data.User;
import nl.tudelft.oopp.demo.serializers.LocalDateAdapter;
import nl.tudelft.oopp.demo.serializers.LocalDateDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalDateTimeDeserializer;
import nl.tudelft.oopp.demo.serializers.LocalTimeAdapter;
import nl.tudelft.oopp.demo.serializers.LocalTimeDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * The type User communication test.
 */
public class UserCommunicationTest {

    private static User user;
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

    private void setCurrentUserRoomIdTo1() {
        MainApp.setCurrentUser(new Student("student"));
        MainApp.getCurrentUser().setRoomId(1);
    }

    private void createStub(String mode, String relativeUrl, String responseBody) {
        switch (mode) {
            case "post":
                stubFor(post(urlEqualTo(relativeUrl))
                        .willReturn(aResponse().withBody(responseBody).withStatus(200)));
                break;
            case "delete":
                stubFor(delete(urlEqualTo(relativeUrl))
                        .willReturn(aResponse().withBody(responseBody).withStatus(200)));
                break;
            default:
                stubFor(get(urlEqualTo(relativeUrl))
                        .willReturn(aResponse().withBody(responseBody).withStatus(200)));
        }
    }

    /**
     * Init.
     */
    @BeforeAll
    static void init() {
        wireMockServer.start();
        configureFor("localhost", 8080);
        user = new Student("student");
    }

    /**
     * Stop server.
     */
    @AfterAll
    static void stopServer() {
        wireMockServer.stop();
    }

    /**
     * Test delete student.
     */
    @Test
    void testDeleteStudent() {
        String relativeUrl = "/students/delete/1";
        createStub("delete", relativeUrl, gson.toJson(user));
        UserCommunication.deleteStudent(1);
        verify(deleteRequestedFor(urlEqualTo(relativeUrl)));

    }

    /**
     * Test get student.
     */
    @Test
    void testGetStudent() {
        String relativeUrl = "/students/get/1";
        createStub("get", relativeUrl, gson.toJson(user));
        assertEquals(user, UserCommunication.getStudent(1));
        verify(getRequestedFor(urlEqualTo(relativeUrl)));
    }

    /**
     * Test join lecture.
     */
    @Test
    void testJoinLecture() {
        String relativeUrl = "/rooms/join?code=" + 1 + "&name=" + user.getName();
        createStub("get", relativeUrl, gson.toJson(user));
        assertEquals(user, UserCommunication.joinLecture("1",user));
        verify(getRequestedFor(urlEqualTo(relativeUrl)));

    }

    /**
     * Test create user.
     */
    @Test
    void testCreateUser() {
        user = new Student("student");
        String relativeUrl = "/students/insert";
        createStub("post", relativeUrl, gson.toJson(user));
        assertEquals(user, UserCommunication.createUser(user));
        verify(postRequestedFor(urlEqualTo(relativeUrl)));

    }

    /**
     * Test get moderator.
     */
    @Test
    void testGetModerator() {
        user = new Moderator("moderator");
        String relativeUrl = "/moderators/get/1";
        createStub("get", relativeUrl, gson.toJson(user));
        assertEquals(user, UserCommunication.getModerator(1));
        verify(getRequestedFor(urlEqualTo(relativeUrl)));

    }

    /**
     * Test get lecturers.
     */
    @Test
    void testGetLecturers() {
        user = new Lecturer("lecturer");
        MainApp.setCurrentUser(new Student("student"));
        MainApp.getCurrentUser().setRoomId(1);
        String relativeUrl = "/lecturers/findFirstLecturer/1";
        createStub("get", relativeUrl, gson.toJson(user));
        assertEquals(user, UserCommunication.getLecturers());
        verify(getRequestedFor(urlEqualTo(relativeUrl)));

    }

    /**
     * Test get lecturer.
     */
    @Test
    void testGetLecturer() {
        user = new Lecturer("lecturer");
        String relativeUrl = "/lecturers/get/1";
        createStub("get", relativeUrl, gson.toJson(user));
        assertEquals(user, UserCommunication.getLecturer(1));
        verify(getRequestedFor(urlEqualTo(relativeUrl)));
    }
}
