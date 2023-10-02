package users.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import users.clients.ReservationsClient;
import users.entities.ReducedReservation;
import users.entities.Team;
import users.entities.User;
import users.repositories.ReducedReservationRepository;
import users.repositories.TeamRepository;
import users.repositories.UserRepository;

/**
 * Team service test class.
 */
@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {
    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReducedReservationRepository reservationRepository;

    @Mock
    private ReservationsClient reservationsClient;

    @Autowired
    @InjectMocks
    private TeamService teamService;

    private static Team team;
    private static User user;
    private static User user2;

    private static UUID uuid = UUID.fromString("12345678-1234-1234-1234-123456789012");
    private static UUID uuid2 = UUID.fromString("abcdefab-abcd-abcd-abcd-abcdefabcdef");

    private static String timestampString = "2021-12-26T19:10:05.500+02:00";
    /**
     * List of test users.
     */
    ArrayList<User> users;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        user = new User.UserBuilder()
                        .withName("john")
                        .withUuid(uuid)
                        .withHasPremium(true)
                        .build();
        user2 = new User.UserBuilder()
                        .withName("mike")
                        .withUuid(uuid2)
                        .withHasPremium(false)
                        .build();
        users = new ArrayList<>();
        users.add(user);
        team = new Team.TeamBuilder()
                        .withName("Team1")
                        .withMembers(users)
                        .build();
    }


    @Test
    public void addTeam_BehavesCorrectly() {
        when(teamRepository.save(any())).thenReturn(team);
        assertEquals(team, teamService.addTeam(team.getName()));
        verify(teamRepository, times(1)).save(any());
    }

    @Test
    public void getTeam_ReturnsCorrectTeam_WhenItExists() {
        when(teamRepository.findById(uuid)).thenReturn(Optional.of(team));
        assertEquals(team, teamService.getTeam(uuid));
    }

    @Test
    public void getTeam_ThrowsException_WhenTeamDoesNotExist() {
        when(teamRepository.findById(uuid)).thenReturn(Optional.empty());
        assertThrows(InvalidParameterException.class, () -> teamService.getTeam(uuid));
    }

    @Test
    public void getUser_ReturnsCorrectTeam_WhenItExists() {
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));
        assertEquals(user, teamService.getUser(uuid));
    }

    @Test
    public void getUser_ThrowsException_WhenUserDoesNotExist() {
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());
        assertThrows(InvalidParameterException.class, () -> teamService.getUser(uuid));
    }

    @Test
    public void getTeamSize_ReturnsCorrectTeamSize() {
        List<User> members = List.of(user, user2);
        team.setMembers(members);

        when(teamRepository.findById(uuid)).thenReturn(Optional.of(team));
        assertEquals(members.size(), teamService.getTeamSize(uuid));
    }

    @Test
    public void addUserToTeamAddsUser_WhenUserAlreadyInTeam() {
        when(teamRepository.findById(uuid)).thenReturn(Optional.of(team));
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));
        teamService.addUserToTeam(uuid, uuid);
        verify(teamRepository, never()).save(any());
    }

    @Test
    public void addUserToTeamAddsUser_WhenUserNotInTeam() {
        when(teamRepository.findById(uuid)).thenReturn(Optional.of(team));
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user2));
        teamService.addUserToTeam(uuid, uuid);
        verify(teamRepository, times(1)).save(any());
    }

    @Test
    public void doesTeamExist_ReturnsFalse_WhenTeamDoesNotExist() {
        boolean expected = false;
        when(teamRepository.findById(uuid)).thenReturn(Optional.empty());
        assertEquals(expected, teamService.doesTeamExist(uuid));
    }

    @Test
    public void doesTeamExist_ReturnsTrue_WhenItExists() {
        boolean expected = true;
        when(teamRepository.findById(uuid)).thenReturn(Optional.of(team));
        assertEquals(expected, teamService.doesTeamExist(uuid));
    }

    @Test
    public void parseStringToDate_ReturnsCorrectDate_WhenStringHasCorrectFormat()
        throws Exception {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2021, Calendar.DECEMBER, 26, 19, 10, 5);
        calendar.set(Calendar.MILLISECOND, 500);
        calendar.set(Calendar.ZONE_OFFSET, 7_200_000);
        Date expectedDate = calendar.getTime();

        Method method = teamService.getClass().getDeclaredMethod("parseStringToDate", String.class);
        method.setAccessible(true);
        Date date = (Date) method.invoke(teamService, timestampString);
        assertEquals(expectedDate, date);
    }

    @Test
    public void parseStringToDate_ThrowsException_WhenStringHasIncorrectFormat()
            throws Exception {

        String incorrectTimeString = "2021-12-28M19:1005.500+02:00";

        Method method = teamService.getClass().getDeclaredMethod("parseStringToDate", String.class);
        method.setAccessible(true);

        try {
            method.invoke(teamService, incorrectTimeString);
            fail();
        } catch (InvocationTargetException e) {
            assertTrue(e.getTargetException().getClass() == InvalidParameterException.class);
        }
    }

    @Test
    public void canTeamBook_ReturnsTrue_ForEmptyTeam() {
        Team team = new Team.TeamBuilder().withName("Empty team").build();
        when(teamRepository.findById(team.getUuid())).thenReturn(Optional.of(team));
        assertTrue(teamService.canTeamBook(team.getUuid(), timestampString));
    }

    @Test
    public void canTeamBook_ReturnsTrue_WhenNoTeamMembersHaveBooked() {
        when(teamRepository.findById(team.getUuid())).thenReturn(Optional.of(team));
        assertTrue(teamService.canTeamBook(team.getUuid(), timestampString));
    }

    @Test
    public void canTeamBook_ReturnsTrue_WhenAPremiumTeamMemberHasRemainingBookings() {
        team.getMembers().add(user2);

        ReducedReservation reservation =
                new ReducedReservation(uuid, parseStringToTimestamp(timestampString));
        user.getReservations().add(reservation);
        user.getReservations().add(reservation);

        when(teamRepository.findById(team.getUuid())).thenReturn(Optional.of(team));
        assertTrue(teamService.canTeamBook(team.getUuid(), timestampString));
    }

    @Test
    public void canTeamBook_ReturnsFalse_WhenNoMemberHasRemainingBookings() {
        team.getMembers().add(user2);

        ReducedReservation reservation =
                new ReducedReservation(uuid, parseStringToTimestamp(timestampString));

        user.getReservations().add(reservation);
        user.getReservations().add(reservation);
        user.getReservations().add(reservation);

        when(teamRepository.findById(team.getUuid())).thenReturn(Optional.of(team));
        assertFalse(teamService.canTeamBook(team.getUuid(), timestampString));
    }

    @Test
    public void canTeamBook_ReturnsFalse_WhenSameDateIsAlreadyBooked() {
        team.setMembers(List.of(user2));

        String reservationTimeString = "2021-12-26T14:00:05.570+02:00";
        ReducedReservation reservation =
                new ReducedReservation(uuid, parseStringToTimestamp(reservationTimeString));

        user2.getReservations().add(reservation);

        when(teamRepository.findById(team.getUuid())).thenReturn(Optional.of(team));
        assertFalse(teamService.canTeamBook(team.getUuid(), timestampString));
    }

    @Test
    public void canTeamBook_ReturnsTrue_WhenReservationYearSameButDateDifferent() {
        team.setMembers(List.of(user2));

        String reservationTimeString = "2021-11-28T14:00:05.570+02:00";
        ReducedReservation reservation =
                new ReducedReservation(uuid, parseStringToTimestamp(reservationTimeString));

        user2.getReservations().add(reservation);

        when(teamRepository.findById(team.getUuid())).thenReturn(Optional.of(team));
        assertTrue(teamService.canTeamBook(team.getUuid(), timestampString));
    }

    @Test
    public void canTeamBook_ReturnsTrue_WhenReservationDateSameButYearDifferent() {
        team.setMembers(List.of(user2));

        String reservationTimeString = "2022-12-26T14:00:05.570+02:00";
        ReducedReservation reservation =
                new ReducedReservation(uuid, parseStringToTimestamp(reservationTimeString));

        user2.getReservations().add(reservation);

        when(teamRepository.findById(team.getUuid())).thenReturn(Optional.of(team));
        assertTrue(teamService.canTeamBook(team.getUuid(), timestampString));
    }

    @Test
    public void addReservationForTeam_Fails_WhenTeamCannotBook() {
        team.getMembers().add(user2);

        ReducedReservation reservation =
                new ReducedReservation(uuid, parseStringToTimestamp(timestampString));

        user.getReservations().add(reservation);
        user.getReservations().add(reservation);
        user.getReservations().add(reservation);

        when(teamRepository.findById(team.getUuid())).thenReturn(Optional.of(team));
        assertThrows(InvalidParameterException.class,
                () -> teamService.addReservationForTeam(team.getUuid(), null, timestampString));
    }

    @Test
    public void addReservationForTeam_Fails_WhenNoSuchReservationExists() {
        team.getMembers().add(user2);

        ReducedReservation reservation =
                new ReducedReservation(uuid, parseStringToTimestamp(timestampString));
        user.getReservations().add(reservation);

        when(teamRepository.findById(team.getUuid())).thenReturn(Optional.of(team));
        when(reservationsClient.doesReservationExist(uuid2, team.getUuid()))
                .thenReturn(ResponseEntity.of(Optional.of(false)));

        assertThrows(InvalidParameterException.class,
                () -> teamService.addReservationForTeam(team.getUuid(), uuid2, timestampString));
    }

    @Test
    public void addReservationForTeam_BehavesCorrectly_WhenParametersCorrect() {
        team.getMembers().add(user2);

        ReducedReservation reservation =
                new ReducedReservation(uuid, parseStringToTimestamp(timestampString));
        user.getReservations().add(reservation);

        when(teamRepository.findById(team.getUuid())).thenReturn(Optional.of(team));
        when(reservationsClient.doesReservationExist(uuid2, team.getUuid()))
                .thenReturn(ResponseEntity.of(Optional.of(true)));

        final int user1ReservationCount = user.getReservations().size();
        final int user2ReservationCount = user2.getReservations().size();

        teamService.addReservationForTeam(team.getUuid(), uuid2, timestampString);

        verify(reservationRepository, times(1)).save(any());
        verify(userRepository, times(2)).save(any());
        assertEquals(user1ReservationCount + 1, user.getReservations().size());
        assertEquals(user2ReservationCount + 1, user2.getReservations().size());
    }

    @Test
    public void removeReservationForTeam_SuccessfullyDeletesAllReservations() {
        team.getMembers().add(user2);
        ReducedReservation reservation =
                new ReducedReservation(uuid2, null);
        ReducedReservation reservation2 =
                new ReducedReservation(uuid, null);

        user.getReservations().addAll(List.of(reservation, reservation2));
        user2.getReservations().add(reservation);

        when(reservationRepository.findById(reservation.getUuid()))
                .thenReturn(Optional.of(reservation));

        when(teamRepository.findById(team.getUuid()))
                .thenReturn(Optional.of(team));

        teamService.deleteReservationForTeam(reservation.getUuid(), team.getUuid());

        verify(userRepository, times(2)).save(any());
        verify(reservationRepository, times(1)).delete(reservation);
        assertEquals(1, user.getReservations().size());
        assertEquals(0, user2.getReservations().size());
    }

    @Test
    public void getReservation_ReturnsCorrectEntity_WhenItExists() {
        ReducedReservation reservation = new ReducedReservation(uuid, null);
        when(reservationRepository.findById(reservation.getUuid()))
                .thenReturn(Optional.of(reservation));

        ReducedReservation returnedReservation = teamService.getReservation(reservation.getUuid());
        assertEquals(reservation, returnedReservation);
    }

    @Test
    public void getReservation_ThrowsException_WhenUuidIsInvalid() {
        ReducedReservation reservation = new ReducedReservation(uuid, null);
        when(reservationRepository.findById(reservation.getUuid()))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidParameterException.class,
                () -> teamService.getReservation(reservation.getUuid())
        );
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

        return new Timestamp(date.getTime());
    }
}
