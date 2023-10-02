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
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import reservations.clients.AuthenticationClient;
import reservations.entities.SportsFacility;
import reservations.middleware.AuthenticationInterceptor;
import reservations.services.SportsFacilityService;

@WebMvcTest(value = SportsFacilityAPI.class)
public class SportsFacilityAPITest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthenticationInterceptor authenticationInterceptor;

    @MockBean
    AuthenticationClient authenticationClient;

    @MockBean
    SportsFacilityService sportsFacilityService;

    Gson gson = new Gson();

    String testSportsFacilityUuid = "abcdef12-1234-5678-90ab-0123456789ab";
    String testSportsFacilityName = "test name";
    int testMinCapacity = 2;
    int testMaxCapacity = 4;

    String testTeamUuid = "fedcba21-4321-5678-90ab-0123456789ab";
    String testReservationUuid = "bcddef12-1234-5678-90ab-0123456789ab";

    String testStartTime = "31-12-2021T12:00:00.000Z";
    String sportsFacilityApiUrl = "/api/v1/facility/";

    SportsFacility hall1 = new SportsFacility("hall1", 2, 5);
    SportsFacility hall2 = new SportsFacility("hall2", 1, 3);
    SportsFacility hall3 = new SportsFacility("hall3", 1, 2);


    List<SportsFacility> sportsFacilityList = List.of(hall1, hall2, hall3);

    String exceptionString = "Mock exception";

    String sportsFacilityUuid = "sportsFacilityUuid";
    String name = "name";
    String nonExistentParameter = "non-existent-parameter";
    String minCapacity = "minCapacity";

    @BeforeEach
    public void setup() throws IOException {
        when(authenticationInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    public void testInvalidParameterExceptionHandler() throws Exception {
        when(sportsFacilityService.getSportsFacility(UUID.fromString(testSportsFacilityUuid)))
                .thenThrow(new InvalidParameterException(exceptionString));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(sportsFacilityApiUrl + "get")
                .param(sportsFacilityUuid, testSportsFacilityUuid);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().string(exceptionString));
    }

    @Test
    public void addSportsFacility_BehavesCorrectly() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(sportsFacilityApiUrl + "add")
                .param("name", testSportsFacilityName)
                .param(minCapacity, Integer.toString(testMinCapacity))
                .param("maxCapacity", Integer.toString(testMaxCapacity));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(sportsFacilityService, times(1))
                .addSportsFacility(testSportsFacilityName, testMinCapacity, testMaxCapacity);
    }

    @Test
    public void addSportsFacility_ThrowsException_WhenParametersNotCorrect() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(sportsFacilityApiUrl + "add")
                .param("name", testSportsFacilityName)
                .param(minCapacity, Integer.toString(testMinCapacity))
                .param(nonExistentParameter, Integer.toString(testMaxCapacity));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        verify(sportsFacilityService, never())
                .addSportsFacility(testSportsFacilityName, testMinCapacity, testMaxCapacity);
    }

    @Test
    public void deleteSportsFacility_BehavesCorrectly() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(sportsFacilityApiUrl + "delete")
                .param(sportsFacilityUuid, testSportsFacilityUuid);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(sportsFacilityService, times(1))
                .deleteSportsFacility(UUID.fromString(testSportsFacilityUuid));
    }

    @Test
    public void deleteSportsFacility_ThrowsException_WhenParametersNotCorrect() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(sportsFacilityApiUrl + "delete")
                .param(nonExistentParameter, testSportsFacilityUuid);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        verify(sportsFacilityService, never())
                .deleteSportsFacility(UUID.fromString(testSportsFacilityUuid));
    }

    @Test
    public void updateSportsFacility_BehavesCorrectly() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(sportsFacilityApiUrl + "update")
                .param(sportsFacilityUuid, testSportsFacilityUuid)
                .param(name, testSportsFacilityName)
                .param(minCapacity, String.valueOf(testMinCapacity))
                .param("maxCapacity", String.valueOf(testMaxCapacity));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(sportsFacilityService, times(1)).updateSportsFacility(
                UUID.fromString(testSportsFacilityUuid),
                testSportsFacilityName,
                testMinCapacity,
                testMaxCapacity
        );
    }

    @Test
    public void updateSportsFacility_ThrowsException_WhenParametersNotCorrect() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(sportsFacilityApiUrl + "update")
                .param(nonExistentParameter, testSportsFacilityUuid)
                .param(name, testSportsFacilityName)
                .param(minCapacity, String.valueOf(testMinCapacity))
                .param("maxCapacity", String.valueOf(testMaxCapacity));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        verify(sportsFacilityService, never()).updateSportsFacility(
                UUID.fromString(testSportsFacilityUuid),
                testSportsFacilityName,
                testMinCapacity,
                testMaxCapacity
        );
    }

    @Test
    public void getSportsFacility_ReturnsCorrectEntity() throws Exception {
        UUID hall1Uuid = hall1.getUuid();

        when(sportsFacilityService.getSportsFacility(hall1Uuid)).thenReturn(hall1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(sportsFacilityApiUrl + "get")
                .param(sportsFacilityUuid, hall1Uuid.toString());

        String responseBody = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        SportsFacility returnedSportsFacility = gson.fromJson(responseBody, SportsFacility.class);

        assertEquals("Uuids do not match", hall1.getUuid(), returnedSportsFacility.getUuid());
        assertEquals("Names do not match", hall1.getName(), returnedSportsFacility.getName());
        assertEquals(
                "Min capacity does not match",
                hall1.getMinCapacity(),
                returnedSportsFacility.getMinCapacity()
        );
        assertEquals(
                "Max capacity does not match",
                hall1.getMaxCapacity(),
                returnedSportsFacility.getMaxCapacity()
        );

        verify(sportsFacilityService, times(1)).getSportsFacility(hall1Uuid);
    }

    @Test
    public void bookSportsFacility_BehavesCorrectly() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(sportsFacilityApiUrl + "book")
                .param("teamUuid", testTeamUuid)
                .param(sportsFacilityUuid, testSportsFacilityUuid)
                .param("startTime", testStartTime);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        verify(sportsFacilityService, times(1)).bookSportsFacility(
                UUID.fromString(testTeamUuid),
                UUID.fromString(testSportsFacilityUuid),
                testStartTime
        );
    }

    @Test
    public void bookSportsFacility_ThrowsException_WhenParametersNotCorrect() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(sportsFacilityApiUrl + "book")
                .param("teamUuid", testTeamUuid)
                .param(nonExistentParameter, testSportsFacilityUuid)
                .param("startTime", testStartTime);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        verify(sportsFacilityService, never()).bookSportsFacility(
                UUID.fromString(testTeamUuid),
                UUID.fromString(testSportsFacilityUuid),
                testStartTime
        );
    }

    @Test
    public void removeSportsFacilityBooking_Works() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(sportsFacilityApiUrl + "deleteBooking")
                .param("reservationUuid", testReservationUuid)
                .param("teamUuid", testTeamUuid);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        verify(
                sportsFacilityService,
                times(1)
        ).removeSportsFacilityBooking(
                UUID.fromString(testReservationUuid),
                UUID.fromString(testTeamUuid)
        );
    }

    @Test
    public void removeSportsFacilityBooking_ThrowsException_ForIncorrectParameter()
            throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(sportsFacilityApiUrl + "deleteBooking")
                .param("reservationUuid", testReservationUuid)
                .param(nonExistentParameter, testTeamUuid);

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());

        verify(sportsFacilityService, never()).removeSportsFacilityBooking(
                UUID.fromString(testReservationUuid),
                UUID.fromString(testTeamUuid)
        );
    }

    @Test
    public void getAllSportsFacilities_ReturnsAllSportsFacilities() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(sportsFacilityApiUrl + "getAll");

        when(sportsFacilityService.getAllSportsFacilities()).thenReturn(sportsFacilityList);

        String returnedJSON = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<SportsFacility> returnedSportsFacilitiesList =
                gson.fromJson(returnedJSON, new TypeToken<List<SportsFacility>>(){}.getType());

        assertThat(returnedSportsFacilitiesList).containsOnly(hall1, hall2, hall3);
        verify(sportsFacilityService, times(1)).getAllSportsFacilities();
    }

    @Test
    public void getAllAvailableSportsFacilities_ReturnsAllAvailableSportsFacility()
            throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(sportsFacilityApiUrl + "getAllAvailable")
                .param("startTime", testStartTime);

        when(sportsFacilityService.getAllAvailableSportsFacilities(testStartTime))
                .thenReturn(sportsFacilityList);

        String returnedJSON = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<SportsFacility> returnedSportsFacilitiesList =
                gson.fromJson(returnedJSON, new TypeToken<List<SportsFacility>>(){}.getType());

        assertThat(returnedSportsFacilitiesList).containsOnly(hall1, hall2, hall3);
        verify(sportsFacilityService, times(1))
                .getAllAvailableSportsFacilities(testStartTime);
    }

    @Test
    public void getAllAvailableSportsFacilities_ThrowsException_ForIncorrectParameter()
            throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(sportsFacilityApiUrl + "getAllAvailable")
                .param("non-existent-param", testStartTime);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        verify(sportsFacilityService, never()).getAllAvailableSportsFacilities(testStartTime);
    }
}
