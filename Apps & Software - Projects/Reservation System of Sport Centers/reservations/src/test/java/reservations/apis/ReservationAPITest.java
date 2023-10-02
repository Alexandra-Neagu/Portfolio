package reservations.apis;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.util.AssertionErrors.fail;
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
import reservations.entities.Bookable;
import reservations.entities.Equipment;
import reservations.entities.Reservation;
import reservations.middleware.AuthenticationInterceptor;
import reservations.services.ReservationService;

@WebMvcTest(value = ReservationAPI.class)
public class ReservationAPITest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthenticationInterceptor authenticationInterceptor;
    
    @MockBean
    ReservationService reservationService;

    Gson gson = new Gson();

    String testTeamUuid = "fedcba21-4321-5678-90ab-0123456789ab";
    String testReservationUuid = "bcddef12-1234-5678-90ab-0123456789ab";
    String reservationApiUrl = "/api/v1/reservation/";

    UUID uuid = UUID.fromString("12345678-1234-1234-1234-123456789012");
    Bookable bookable = new Equipment("equipment", 3, "basketball");

    Reservation reservation = new Reservation(uuid, bookable, null);
    Reservation reservation2 = new Reservation(uuid, bookable, null);
    Reservation reservation3 = new Reservation(uuid, bookable, null);

    List<Reservation> reservationList = List.of(reservation, reservation2, reservation3);
    String teamUuidLiteral = "teamUuid";

    @BeforeEach
    public void setup() throws IOException {
        when(authenticationInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }
    
    @Test
    public void testInvalidParameterExceptionHandler() throws Exception {
        when(reservationService.doesReservationExist(
                UUID.fromString(testReservationUuid),
                UUID.fromString(testTeamUuid)
        )).thenThrow(new InvalidParameterException("Exception"));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(reservationApiUrl + "exists")
                .param("reservationUuid", testReservationUuid)
                .param(teamUuidLiteral, testTeamUuid);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Exception"));
    }

    @Test
    public void doesReservationExists_ReturnsTrue_WhenItExists() throws Exception {
        when(reservationService.doesReservationExist(
                UUID.fromString(testReservationUuid),
                UUID.fromString(testTeamUuid)
        )).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(reservationApiUrl + "exists")
                .param("reservationUuid", testReservationUuid)
                .param(teamUuidLiteral, testTeamUuid);

        boolean returnedResult = Boolean.parseBoolean(
                mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
        );

        assertTrue("Expected true, but got false", returnedResult);

        verify(reservationService, times(1)).doesReservationExist(
                UUID.fromString(testReservationUuid),
                UUID.fromString(testTeamUuid)
        );
    }

    @Test
    public void doesReservationExists_ThrowsException_WhenParametersNotCorrect() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(reservationApiUrl + "exists")
                .param("reservationUuid", testReservationUuid)
                .param("non-existent-parameter", testTeamUuid);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        verify(reservationService, never())
                .doesReservationExist(any(), any());
    }

    @Test
    public void retrieveReservationsByBookable_ReturnsCorrectList() throws Exception {
        List<Reservation> reservationList = List.of(reservation, reservation2, reservation3);

        when(reservationService.retrieveReservationsByBookable(uuid))
                .thenReturn(reservationList);

        RequestBuilder request = MockMvcRequestBuilders
                .get(reservationApiUrl + "getByBookable")
                .param("bookableUuid", uuid.toString());

        String returnedJsonString = mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Reservation> returnedReservations =
                gson.fromJson(returnedJsonString, new TypeToken<List<Reservation>>(){}.getType());

        checkWhetherListIsFine(returnedReservations, reservationList);

        verify(reservationService, times(1)).retrieveReservationsByBookable(uuid);
    }

    @Test
    public void getAllUpcomingReservations_ReturnsListCorrectly() throws Exception {
        when(reservationService.retrieveAllUpcoming(uuid))
                .thenReturn(reservationList);

        RequestBuilder request = MockMvcRequestBuilders
                .get(reservationApiUrl + "getAllUpcoming")
                .param(teamUuidLiteral, uuid.toString());

        String returnedJsonString = mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Reservation> returnedReservations =
                gson.fromJson(returnedJsonString, new TypeToken<List<Reservation>>(){}.getType());

        checkWhetherListIsFine(returnedReservations, reservationList);

        verify(reservationService, times(1)).retrieveAllUpcoming(uuid);
    }

    @Test
    public void getAllReservationsForTeam_ReturnsListCorrectly() throws Exception {
        when(reservationService.retrieveAllReservationsForTeam(uuid))
                .thenReturn(reservationList);

        RequestBuilder request = MockMvcRequestBuilders
                .get(reservationApiUrl + "getAll")
                .param(teamUuidLiteral, uuid.toString());

        String returnedJsonString = mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Reservation> returnedReservations =
                gson.fromJson(returnedJsonString, new TypeToken<List<Reservation>>(){}.getType());

        checkWhetherListIsFine(returnedReservations, reservationList);

        verify(reservationService, times(1)).retrieveAllReservationsForTeam(uuid);
    }

    private void checkWhetherListIsFine(List<Reservation> returned, List<Reservation> expected) {
        if (returned.size() != expected.size()) {
            fail("Returned list is of different size than expected");
        }

        for (int i = 0; i < returned.size(); i++) {
            assertEquals(
                    "Reservation with index " + i + " is different",
                    expected.get(i),
                    returned.get(i)
            );
        }
    }
}
