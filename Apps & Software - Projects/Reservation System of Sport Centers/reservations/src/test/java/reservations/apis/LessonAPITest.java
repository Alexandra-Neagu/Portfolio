package reservations.apis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import reservations.entities.Lesson;
import reservations.entities.SportsFacility;
import reservations.middleware.AuthenticationInterceptor;
import reservations.services.LessonService;

@WebMvcTest(value = LessonAPI.class)
public class LessonAPITest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthenticationInterceptor authenticationInterceptor;

    @MockBean
    LessonService lessonService;

    Gson gson = new Gson();

    String testLessonUuid = "abcdef12-1234-5678-90ab-0123456789ab";
    String testLessonName = "test name";
    int testMinCapacity = 2;
    int testMaxCapacity = 4;

    String testTeamUuid = "fedcba21-4321-5678-90ab-0123456789ab";
    String testReservationUuid = "bcddef12-1234-5678-90ab-0123456789ab";

    String testStartTime = "2022-12-01T12:00:00.000+01:00";
    String testEndTime = "2022-12-01T13:00:00.000+01:00";
    Timestamp testStartTimeTimestamp = parseStringToTimestamp(testStartTime);
    Timestamp testEndTimeTimestamp = parseStringToTimestamp(testEndTime);

    String lessonApiUrl = "/api/v1/lesson/";
    String startTimeLiteralString = "startTime";
    SportsFacility hall1 = new SportsFacility("hall1", 2, 5);
    SportsFacility hall2 = new SportsFacility("hall2", 1, 3);
    SportsFacility hall3 = new SportsFacility("hall3", 1, 2);

    Lesson lesson1 = new Lesson(
            "lesson1",
            5,
            hall1,
            testStartTimeTimestamp,
            testEndTimeTimestamp);
    Lesson lesson2 = new Lesson("lesson2", 5, hall1, testStartTimeTimestamp, testEndTimeTimestamp);
    Lesson lesson3 = new Lesson("lesson3", 5, hall1, testStartTimeTimestamp, testEndTimeTimestamp);

    List<Lesson> lessonList = List.of(lesson1, lesson2, lesson3);

    String lessonUuidLiteral = "lessonUuid";
    String name = "name";
    String nonExistentParameter = "non-existent-parameter";
    String maxCapacityLiteral = "maxCapacity";
    String endTimeLiteral = "endTime";
    
    String exceptionString = "Mock exception";

    @BeforeEach
    public void setup() throws IOException {
        when(authenticationInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    public void testInvalidParameterExceptionHandler() throws Exception {
        when(lessonService.getLesson(UUID.fromString(testLessonUuid)))
                .thenThrow(new InvalidParameterException(exceptionString));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(lessonApiUrl + "get")
                .param(lessonUuidLiteral, testLessonUuid);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().string(exceptionString));
    }

    @Test
    public void addLesson_BehavesCorrectly() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(lessonApiUrl + "add")
                .param(name, testLessonName)
                .param(maxCapacityLiteral, Integer.toString(testMaxCapacity))
                .param("sportsFacilityUuid", hall1.getUuid().toString())
                .param(startTimeLiteralString, testStartTime)
                .param(endTimeLiteral, testEndTime);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(lessonService, times(1)).addLesson(
                testLessonName,
                testMaxCapacity,
                hall1.getUuid(),
                testStartTime,
                testEndTime
        );
    }

    @Test
    public void addLesson_ThrowsException_WhenParametersNotCorrect() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
            .put(lessonApiUrl + "add")
            .param(name, testLessonName)
            .param(maxCapacityLiteral, Integer.toString(testMaxCapacity))
            .param(nonExistentParameter, hall1.getUuid().toString())
            .param(startTimeLiteralString, testStartTime)
            .param(endTimeLiteral, testEndTime);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        verify(lessonService, never()).addLesson(
            testLessonName,
            testMaxCapacity,
            hall1.getUuid(),
            testStartTime,
            testEndTime
        );
    }

    @Test
    public void deleteLesson_BehavesCorrectly() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(lessonApiUrl + "delete")
                .param(lessonUuidLiteral, testLessonUuid);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(lessonService, times(1))
                .deleteLesson(UUID.fromString(testLessonUuid));
    }

    @Test
    public void deleteLesson_ThrowsException_WhenParametersNotCorrect() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(lessonApiUrl + "delete")
                .param(nonExistentParameter, testLessonUuid);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        verify(lessonService, never())
                .deleteLesson(UUID.fromString(testLessonUuid));
    }

    @Test
    public void updateLesson_BehavesCorrectly() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(lessonApiUrl + "update")
                .param(lessonUuidLiteral, testLessonUuid)
                .param(name, testLessonName)
                .param(maxCapacityLiteral, String.valueOf(testMaxCapacity))
                .param("sportsFacilityUuid", hall1.getUuid().toString())
                .param("startTime", testStartTime)
                .param(endTimeLiteral, testEndTime);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(lessonService, times(1)).updateLesson(
                UUID.fromString(testLessonUuid),
                testLessonName,
                testMaxCapacity,
                hall1.getUuid(),
                testStartTime,
                testEndTime
        );
    }

    @Test
    public void updateLesson_ThrowsException_WhenParametersNotCorrect() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(lessonApiUrl + "update")
                .param(nonExistentParameter, testLessonUuid)
                .param(name, testLessonName)
                .param(maxCapacityLiteral, String.valueOf(testMaxCapacity))
                .param("sportsFacilityUuid", hall1.getUuid().toString())
                .param("startTime", testStartTime)
                .param(endTimeLiteral, testEndTime);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        verify(lessonService, never()).updateLesson(
                UUID.fromString(testLessonUuid),
                testLessonName,
                testMaxCapacity,
                hall1.getUuid(),
                testStartTime,
                testEndTime
        );
    }

    @Test
    public void getLesson_ReturnsCorrectEntity() throws Exception {
        UUID lesson1uuid = lesson1.getUuid();

        when(lessonService.getLesson(lesson1uuid)).thenReturn(lesson1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(lessonApiUrl + "get")
                .param(lessonUuidLiteral, lesson1uuid.toString());

        String responseBody = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Lesson returnedLesson = gson.fromJson(responseBody, Lesson.class);

        assertEquals("Uuids do not match", lesson1.getUuid(), returnedLesson.getUuid());
        assertEquals("Names do not match", lesson1.getName(), returnedLesson.getName());
        assertEquals(
                "Max capacity does not match",
                lesson1.getMaxCapacity(),
                returnedLesson.getMaxCapacity()
        );
        assertEquals(
                "Sport facilities do not match",
                lesson1.getSportsFacility(),
                returnedLesson.getSportsFacility()
        );

        verify(lessonService, times(1)).getLesson(lesson1uuid);
    }

    @Test
    public void bookLesson_BehavesCorrectly() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(lessonApiUrl + "book")
                .param("teamUuid", testTeamUuid)
                .param(lessonUuidLiteral, testLessonUuid)
                .param(startTimeLiteralString, testStartTime);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        verify(lessonService, times(1)).bookLesson(
                UUID.fromString(testTeamUuid),
                UUID.fromString(testLessonUuid)
        );
    }

    @Test
    public void bookLesson_ThrowsException_WhenParametersNotCorrect() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(lessonApiUrl + "book")
                .param("teamUuid", testTeamUuid)
                .param(nonExistentParameter, testLessonUuid)
                .param(startTimeLiteralString, testStartTime);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        verify(lessonService, never()).bookLesson(
                UUID.fromString(testTeamUuid),
                UUID.fromString(testLessonUuid)
        );
    }

    @Test
    public void removeLessonBooking_Works() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(lessonApiUrl + "deleteBooking")
                .param("reservationUuid", testReservationUuid)
                .param("teamUuid", testTeamUuid);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        verify(
                lessonService,
                times(1)
        ).removeLessonBooking(
                UUID.fromString(testReservationUuid),
                UUID.fromString(testTeamUuid)
        );
    }

    @Test
    public void removeLessonBooking_ThrowsException_ForIncorrectParameter()
            throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(lessonApiUrl + "deleteBooking")
                .param("reservationUuid", testReservationUuid)
                .param(nonExistentParameter, testTeamUuid);

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());

        verify(lessonService, never()).removeLessonBooking(
                UUID.fromString(testReservationUuid),
                UUID.fromString(testTeamUuid)
        );
    }

    @Test
    public void getAllLessons_ReturnsAllLessons() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(lessonApiUrl + "getAll");

        when(lessonService.getAllLessons()).thenReturn(lessonList);

        String returnedJSON = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Lesson> returnedLessonsList =
                gson.fromJson(returnedJSON, new TypeToken<List<Lesson>>(){}.getType());

        assertThat(returnedLessonsList).containsOnly(lesson1, lesson2, lesson3);
        verify(lessonService, times(1)).getAllLessons();
    }

    @Test
    public void getAllAvailableLessons_ReturnsAllAvailableLessons()
            throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(lessonApiUrl + "getAllAvailable")
                .param(startTimeLiteralString, testStartTime)
                .param(endTimeLiteral, testEndTime);

        when(lessonService.getAllAvailableLessons(testStartTime, testEndTime))
                .thenReturn(lessonList);

        String returnedJSON = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Lesson> returnedLessonsList =
                gson.fromJson(returnedJSON, new TypeToken<List<Lesson>>(){}.getType());

        assertThat(returnedLessonsList).containsOnly(lesson1, lesson2, lesson3);
        verify(lessonService, times(1))
                .getAllAvailableLessons(testStartTime, testEndTime);
    }

    @Test
    public void getAllAvailableLessons_ThrowsException_ForIncorrectParameter()
            throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(lessonApiUrl + "getAllAvailable")
                .param("non-existent-param", testStartTime);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        verify(lessonService, never()).getAllAvailableLessons(testStartTime, testEndTime);
    }

    private Timestamp parseStringToTimestamp(String time) {
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                Locale.GERMANY
        );
        Date date = null;

        try {
            date = format.parse(time);
        } catch (Exception e) {
            throw new InvalidParameterException("Timestamp cannot be parsed.");
        }

        Timestamp timestamp = new Timestamp(date.getTime());

        return timestamp;
    }
}
