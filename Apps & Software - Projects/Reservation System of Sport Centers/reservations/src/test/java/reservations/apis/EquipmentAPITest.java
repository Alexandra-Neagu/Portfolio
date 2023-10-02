package reservations.apis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import reservations.entities.Equipment;
import reservations.middleware.AuthenticationInterceptor;
import reservations.services.EquipmentService;

@WebMvcTest(value = EquipmentAPI.class)
public class EquipmentAPITest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthenticationInterceptor authenticationInterceptor;

    @MockBean
    AuthenticationClient authenticationClient;

    @MockBean
    EquipmentService equipmentService;

    Gson gson = new Gson();

    String testEquipmentUuid = "abcdef12-1234-5678-90ab-0123456789ab";
    String testEquipmentName = "test name";
    String testSport = "test sport";
    int testCapacity = 10;

    String testTeamUuid = "fedcba21-4321-5678-90ab-0123456789ab";
    String testReservationUuid = "bcddef12-1234-5678-90ab-0123456789ab";

    String testStartTime = "31-12-2021T12:00:00.000Z";
    String equipmentApiUrl = "/api/v1/equipment/";

    Equipment bat = new Equipment("bat", 5, "baseball");
    Equipment ball = new Equipment("ball", 3, "volleyball");
    Equipment stick = new Equipment("stick", 2, "hockey");

    List<Equipment> equipmentList = List.of(bat, ball, stick);

    String exceptionString = "Mock exception";

    String equipmentUuid = "equipmentUuid";
    String name = "name";
    String relatedSport = "relatedSport";
    String nonExistentParameter = "non-existent-parameter";

    @BeforeEach
    public void setup() throws IOException {
        when(authenticationInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    public void testInvalidParameterExceptionHandler() throws Exception {
        when(equipmentService.getEquipment(UUID.fromString(testEquipmentUuid)))
                .thenThrow(new InvalidParameterException(exceptionString));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(equipmentApiUrl + "get")
                .param(equipmentUuid, testEquipmentUuid);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().string(exceptionString));
    }

    @Test
    public void addEquipment_BehavesCorrectly() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(equipmentApiUrl + "add")
                .param(name, testEquipmentName)
                .param("capacity", Integer.toString(testCapacity))
                .param(relatedSport, testSport);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(equipmentService, times(1)).addEquipment(testEquipmentName, testCapacity, testSport);
    }

    @Test
    public void addEquipment_ThrowsException_WhenParametersNotCorrect() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(equipmentApiUrl + "add")
                .param(name, testEquipmentName)
                .param(nonExistentParameter, Integer.toString(testCapacity))
                .param(relatedSport, testSport);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        verify(equipmentService, never()).addEquipment(testEquipmentName, testCapacity, testSport);
    }

    @Test
    public void deleteEquipment_BehavesCorrectly() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(equipmentApiUrl + "delete")
                .param(equipmentUuid, testEquipmentUuid);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(equipmentService, times(1)).deleteEquipment(UUID.fromString(testEquipmentUuid));
    }

    @Test
    public void deleteEquipment_ThrowsException_WhenParametersNotCorrect() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(equipmentApiUrl + "delete")
                .param(nonExistentParameter, testEquipmentUuid);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        verify(equipmentService, never()).deleteEquipment(UUID.fromString(testEquipmentUuid));
    }

    @Test
    public void updateEquipment_BehavesCorrectly() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(equipmentApiUrl + "update")
                .param(equipmentUuid, testEquipmentUuid)
                .param(name, testEquipmentName)
                .param("capacity", String.valueOf(testCapacity))
                .param(relatedSport, testSport);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(equipmentService, times(1)).updateEquipment(
                UUID.fromString(testEquipmentUuid),
                testEquipmentName,
                testCapacity,
                testSport
        );
    }

    @Test
    public void updateEquipment_ThrowsException_WhenParametersNotCorrect() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(equipmentApiUrl + "update")
                .param(nonExistentParameter, testEquipmentUuid)
                .param(name, testEquipmentName)
                .param("capacity", String.valueOf(testCapacity))
                .param(relatedSport, testSport);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        verify(equipmentService, never()).updateEquipment(
                UUID.fromString(testEquipmentUuid),
                testEquipmentName,
                testCapacity,
                testSport
        );
    }

    @Test
    public void getEquipment_ReturnsCorrectEntity() throws Exception {
        UUID batUuid = bat.getUuid();

        when(equipmentService.getEquipment(batUuid)).thenReturn(bat);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(equipmentApiUrl + "get")
                .param(equipmentUuid, batUuid.toString());

        String responseBody = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Equipment returnedEquipment = gson.fromJson(responseBody, Equipment.class);

        assertEquals("Uuids do not match", bat.getUuid(), returnedEquipment.getUuid());
        assertEquals("Names do not match", bat.getName(), returnedEquipment.getName());
        assertEquals(
                "Capacity does not match",
                bat.getMaxCapacity(),
                returnedEquipment.getMaxCapacity()
        );
        assertEquals(
                "Sports do not match",
                bat.getRelatedSport(),
                returnedEquipment.getRelatedSport()
        );

        verify(equipmentService, times(1)).getEquipment(batUuid);
    }

    @Test
    public void bookEquipment_BehavesCorrectly() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(equipmentApiUrl + "book")
                .param("teamUuid", testTeamUuid)
                .param(equipmentUuid, testEquipmentUuid)
                .param("startTime", testStartTime)
                .param("amount", Integer.toString(testCapacity));

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        verify(equipmentService, times(1)).bookEquipment(
                UUID.fromString(testTeamUuid),
                UUID.fromString(testEquipmentUuid),
                testStartTime, testCapacity
        );
    }

    @Test
    public void bookEquipment_ThrowsException_WhenParametersNotCorrect() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(equipmentApiUrl + "book")
                .param("teamUuid", testTeamUuid)
                .param(nonExistentParameter, testEquipmentUuid)
                .param("startTime", testStartTime)
                .param("amount", Integer.toString(testCapacity));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        verify(equipmentService, never()).bookEquipment(
                UUID.fromString(testTeamUuid),
                UUID.fromString(testEquipmentUuid),
                testStartTime, testCapacity
        );
    }

    @Test
    public void removeEquipmentBooking_Works() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(equipmentApiUrl + "deleteBooking")
                .param("reservationUuid", testReservationUuid)
                .param("teamUuid", testTeamUuid);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        verify(equipmentService, times(1)).removeEquipmentBooking(
                UUID.fromString(testReservationUuid),
                UUID.fromString(testTeamUuid)
        );
    }

    @Test
    public void removeEquipmentBooking_ThrowsException_ForIncorrectParameter() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(equipmentApiUrl + "deleteBooking")
                .param("reservationUuid", testReservationUuid)
                .param(nonExistentParameter, testTeamUuid);

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());

        verify(equipmentService, never()).removeEquipmentBooking(
                UUID.fromString(testReservationUuid),
                UUID.fromString(testTeamUuid)
        );
    }

    @Test
    public void getAllEquipment_ReturnsAllEquipment() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(equipmentApiUrl + "getAll");

        when(equipmentService.getAllEquipment()).thenReturn(equipmentList);

        String returnedJSON = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Equipment> returnedEquipmentList =
                gson.fromJson(returnedJSON, new TypeToken<List<Equipment>>(){}.getType());

        assertThat(returnedEquipmentList).containsOnly(bat, ball, stick);
        verify(equipmentService, times(1)).getAllEquipment();
    }

    @Test
    public void getAllAvailableEquipment_ReturnsAllAvailableEquipment() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(equipmentApiUrl + "getAllAvailable")
                .param("startTime", testStartTime);

        when(equipmentService.getAllAvailableEquipment(testStartTime)).thenReturn(equipmentList);

        String returnedJSON = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Equipment> returnedEquipmentList =
                gson.fromJson(returnedJSON, new TypeToken<List<Equipment>>(){}.getType());

        assertThat(returnedEquipmentList).containsOnly(bat, ball, stick);
        verify(equipmentService, times(1)).getAllAvailableEquipment(testStartTime);
    }

    @Test
    public void getAllAvailableEquipment_ThrowsException_ForIncorrectParameter() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(equipmentApiUrl + "getAllAvailable")
                .param(nonExistentParameter, testStartTime);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());

        verify(equipmentService, never()).getAllAvailableEquipment(testStartTime);
    }
}
