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
 * The type Question communication test.
 */
class QuestionCommunicationTest {

    private static Question q1;
    private static Question q2;
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
        q1 = new Question("Question 1?");
        q1.setLectureId(1);
        q2 = new Question("Question 1?");
        q2.setLectureId(1);
        q2.setAnswerId(1);
        configureFor("localhost", 8080);
    }

    /**
     * Stop server.
     */
    @AfterAll
    static void stopServer() {
        wireMockServer.stop();
    }

    /**
     * Test get question.
     */
    @Test
    public void testGetQuestion() {
        stubFor(get(urlEqualTo("/questions/get/1"))
                .willReturn(aResponse().withBody(gson.toJson(q1)).withStatus(200)));
        Question ret = QuestionCommunication.getQuestion(1);
        assertEquals(ret, q1);
    }

    /**
     * Test mark as answered.
     */
    @Test
    public void testMarkAsAnswered() {
        stubFor(get(urlEqualTo("/questions/markasanswered/1"))
                .willReturn(aResponse().withStatus(200)));
        QuestionCommunication.markAsAnswered(1);
        verify(getRequestedFor(urlEqualTo("/questions/markasanswered/1")));
    }

    /**
     * Test upvote question.
     */
    @Test
    public void testUpvoteQuestion() {
        stubFor(get(urlEqualTo("/questions/upvote/1"))
                .willReturn(aResponse().withStatus(200).withBody("1")));
        assertEquals(1, QuestionCommunication.upvoteQuestion(1));
        verify(getRequestedFor(urlEqualTo("/questions/upvote/1")));
    }

    /**
     * Test rephrase question.
     */
    @Test
    public void testRephraseQuestion() {
        stubFor(post(urlEqualTo("/questions/rephrase/1"))
                .willReturn(aResponse().withBody(gson.toJson(new StringResponse("ABBA is cool")))
                        .withStatus(200)));
        assertEquals("ABBA is cool", QuestionCommunication.rephraseQuestion(1, "ABBA is cool"));
        verify(postRequestedFor(urlEqualTo("/questions/rephrase/1")));
    }

    /**
     * Gets questions.
     */
    @Test
    public void getQuestions() {
        setCurrentUserRoomIdTo1();
        createStub("get", "/questions/getAllinRoom/1", gson.toJson(List.of(q1)),200);
        assertEquals(q1, QuestionCommunication.getQuestions().get(0));
        verify(getRequestedFor(urlEqualTo("/questions/getAllinRoom/1")));
    }

    /**
     * Test get ans questions.
     */
    @Test
    public void testGetAnsQuestions() {
        setCurrentUserRoomIdTo1();
        q1.setAnswered(true);
        createStub("get","/questions/getAllAnswered/1", gson.toJson(List.of(q1)), 200);
        assertEquals(q1,QuestionCommunication.getAnsQuestions().get(0));
        verify(getRequestedFor(urlEqualTo("/questions/getAllAnswered/1")));
    }

    /**
     * Test get unans questions.
     */
    @Test
    public void testGetUnansQuestions() {
        setCurrentUserRoomIdTo1();
        createStub("get","/questions/getAllUnanswered/1", gson.toJson(List.of(q1)), 200);
        assertEquals(q1,QuestionCommunication.getUnansQuestions().get(0));
        verify(getRequestedFor(urlEqualTo("/questions/getAllUnanswered/1")));
    }

    /**
     * Test get questions sorted by age.
     */
    @Test
    public void testGetQuestionsSortedByAge() {
        setCurrentUserRoomIdTo1();
        createStub("get","/questions/sortbyage/1", gson.toJson(List.of(q1)), 200);
        assertEquals(List.of(q1), QuestionCommunication.getQuestionsSortedByAge());
        verify(getRequestedFor(urlEqualTo("/questions/sortbyage/1")));
    }


    /**
     * Test get qsba unanswered.
     */
    @Test
    public void testGetQsbaUnanswered() {
        setCurrentUserRoomIdTo1();
        createStub("get","/questions/sortbyageunanswered/1", gson.toJson(List.of(q1)), 200);
        assertEquals(List.of(q1), QuestionCommunication.getQsbaUnanswered());
        verify(getRequestedFor(urlEqualTo("/questions/sortbyageunanswered/1")));
    }

    /**
     * Test get qsba ansered.
     */
    @Test
    public void testGetQsbaAnsered() {
        setCurrentUserRoomIdTo1();
        createStub("get","/questions/sortbyageanswered/1", gson.toJson(List.of(q1)), 200);
        assertEquals(List.of(q1), QuestionCommunication.getQsbaAnswered());
        verify(getRequestedFor(urlEqualTo("/questions/sortbyageanswered/1")));
    }

    /**
     * Gets questions sorted by upvotes.
     */
    @Test
    void getQuestionsSortedByUpvotes() {
        setCurrentUserRoomIdTo1();
        String relativeUrl = "/questions/sortByUpvotes/1";
        createStub("get",relativeUrl, gson.toJson(List.of(q1)), 200);
        assertEquals(List.of(q1), QuestionCommunication.getQuestionsSortedByUpvotes());
        verify(getRequestedFor(urlEqualTo(relativeUrl)));
    }

    /**
     * Gets qsbu unanswered.
     */
    @Test
    void getQsbuUnanswered() {
        setCurrentUserRoomIdTo1();
        String relativeUrl = "/questions/sortByUpvotesUnanswered/1";
        createStub("get",relativeUrl, gson.toJson(List.of(q1)), 200);
        assertEquals(List.of(q1), QuestionCommunication.getQsbuUnanswered());
        verify(getRequestedFor(urlEqualTo(relativeUrl)));
    }

    /**
     * Gets qsbu answered.
     */
    @Test
    void getQsbuAnswered() {
        setCurrentUserRoomIdTo1();
        String relativeUrl = "/questions/sortByUpvotesAnswered/1";
        createStub("get",relativeUrl, gson.toJson(List.of(q1)), 200);
        assertEquals(List.of(q1), QuestionCommunication.getQsbuAnswered());
        verify(getRequestedFor(urlEqualTo(relativeUrl)));
    }

    /**
     * Gets questions sorted by pop.
     */
    @Test
    void getQuestionsSortedByPop() {
        setCurrentUserRoomIdTo1();
        String relativeUrl = "/questions/sortByPopularity/1";
        createStub("get",relativeUrl, gson.toJson(List.of(q1)), 200);
        assertEquals(List.of(q1), QuestionCommunication.getQuestionsSortedByPop());
        verify(getRequestedFor(urlEqualTo(relativeUrl)));
    }

    /**
     * Gets qsbp answered.
     */
    @Test
    void getQsbpAnswered() {
        setCurrentUserRoomIdTo1();
        String relativeUrl = "/questions/sortByPopularityAnswered/1";
        createStub("get",relativeUrl, gson.toJson(List.of(q1)), 200);
        assertEquals(List.of(q1), QuestionCommunication.getQsbpAnswered());
        verify(getRequestedFor(urlEqualTo(relativeUrl)));
    }

    /**
     * Gets qsbp unanswered.
     */
    @Test
    void getQsbpUnanswered() {
        setCurrentUserRoomIdTo1();
        String relativeUrl = "/questions/sortByPopularityUnanswered/1";
        createStub("get",relativeUrl, gson.toJson(List.of(q1)), 200);
        assertEquals(List.of(q1), QuestionCommunication.getQsbpUnanswered());
        verify(getRequestedFor(urlEqualTo(relativeUrl)));
    }

    /**
     * Delete question.
     */
    @Test
    void deleteQuestion() {
        String relativeUrl = "/questions/delete/1";
        createStub("delete",relativeUrl, "", 200);
        QuestionCommunication.deleteQuestion(1);
        verify(deleteRequestedFor(urlEqualTo(relativeUrl)));
    }

    /**
     * Post question.
     */
    @Test
    void postQuestion() {
        String relativeUrl = "/questions/insert";
        createStub("post",relativeUrl, gson.toJson(q1), 200);
        assertEquals(q1, QuestionCommunication.postQuestion(q1));
        verify(postRequestedFor(urlEqualTo(relativeUrl)));
    }

    /**
     * Test set answer id.
     */
    @Test
    public void testSetAnswerId() {
        stubFor(post(urlEqualTo("/questions/setAnswerId/1"))
                .willReturn(aResponse().withBody(gson.toJson(q2))
                        .withStatus(200)));
        assertEquals(1, QuestionCommunication.setAnswerId(1, 1).getAnswerId());
        verify(postRequestedFor(urlEqualTo("/questions/setAnswerId/1")));
    }
}
