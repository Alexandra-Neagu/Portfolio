package users.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
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
import users.clients.AuthenticationClient;
import users.entities.Team;
import users.entities.User;
import users.middleware.AuthenticationInterceptor;
import users.services.TeamService;

@WebMvcTest(value = TeamAPI.class)
public class TeamAPITest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    TeamService teamService;

    @MockBean
    AuthenticationInterceptor authenticationInterceptor;

    @MockBean
    AuthenticationClient authenticationClient;

    Team team;
    User user;
    List<User> teamMembers;

    String teamApiUrl = "/api/v1/team/";
    String reservationTime = "2022-01-04T16:13:55.333+01:00";
    UUID uuid = UUID.fromString("12345678-1234-1234-1234-123456789012");
    UUID uuid2 = UUID.fromString("a4bce591-ce34-328b-ff1a-a8b3c7d9b00a");

    private final String teamString = "teamUuid";

    /**
     * Setup.
     */
    @BeforeEach
    void setUp() throws IOException {
        when(authenticationInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        user = new User.UserBuilder().withName("john").withUuid(uuid).withHasPremium(true).build();
        teamMembers = new ArrayList<>();
        teamMembers.add(user);
        team = new Team.TeamBuilder().withName("Team1").withMembers(teamMembers).build();
    }

    @Test
    public void testInvalidParameterExceptionHandler() throws Exception {
        when(teamService.addTeam("team 1")).thenThrow(new InvalidParameterException("Exception"));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(teamApiUrl + "addTeam")
                .param("name", "team 1");

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Exception"));
    }

    @Test
    public void addTeam_BehavesCorrectly() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(teamApiUrl + "addTeam")
                .param("name", team.getName());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(teamService, times(1)).addTeam(team.getName());
    }

    @Test
    public void addUserToTeam_BehavesCorrectly() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(teamApiUrl + "addUserToTeam")
                .param("userUuid", uuid.toString())
                .param(teamString, team.getUuid().toString());

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(teamService, times(1)).addUserToTeam(uuid, team.getUuid());
    }

    @Test
    public void doesTeamExist_BehavesCorrectly() throws Exception {
        when(teamService.doesTeamExist(team.getUuid())).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(teamApiUrl + "exists")
                .param(teamString, team.getUuid().toString());

        boolean result = Boolean.parseBoolean(
                mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
        );

        assertTrue(result);
        verify(teamService, times(1)).doesTeamExist(team.getUuid());
    }

    @Test
    public void canTeamBook_BehavesCorrectly() throws Exception {
        when(teamService.canTeamBook(team.getUuid(), reservationTime)).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(teamApiUrl + "canTeamBook")
                .param(teamString, team.getUuid().toString())
                .param("reservationTime", reservationTime);

        boolean result = Boolean.parseBoolean(
                mockMvc.perform(requestBuilder)
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
        );

        assertTrue(result);
        verify(teamService, times(1))
                .canTeamBook(team.getUuid(), reservationTime);
    }

    @Test
    public void getTeamSize_BehavesCorrectly() throws Exception {
        int teamSize = 2;
        when(teamService.getTeamSize(team.getUuid())).thenReturn(teamSize);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(teamApiUrl + "getTeamSize")
                .param(teamString, team.getUuid().toString());

        int result = Integer.parseInt(
                mockMvc.perform(requestBuilder)
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
        );

        assertEquals(teamSize, result);

        verify(teamService, times(1))
                .getTeamSize(team.getUuid());
    }

    @Test
    public void addReservationForTeam_BehavesCorrectly() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(teamApiUrl + "addReservation")
                .param(teamString, team.getUuid().toString())
                .param("reservationUuid", uuid2.toString())
                .param("timestamp", reservationTime);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        verify(teamService, times(1))
                .addReservationForTeam(team.getUuid(), uuid2, reservationTime);
    }

    @Test
    public void deleteReservationForTeam_BehavesCorrectly() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(teamApiUrl + "deleteReservation")
                .param(teamString, team.getUuid().toString())
                .param("reservationUuid", uuid2.toString());

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        verify(teamService, times(1))
                .deleteReservationForTeam(uuid2, team.getUuid());
    }
}
