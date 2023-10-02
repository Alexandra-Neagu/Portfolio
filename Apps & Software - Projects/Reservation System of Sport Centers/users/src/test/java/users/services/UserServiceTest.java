package users.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import users.entities.Team;
import users.entities.User;
import users.repositories.TeamRepository;
import users.repositories.UserRepository;

/**
 * The type User service test class.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Autowired
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamRepository teamRepository;

    private static User user;

    private UUID uuid = UUID.fromString("12345678-1234-1234-1234-123456789012");

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        user = new User.UserBuilder().withName("john").withUuid(uuid).withHasPremium(true).build();
    }

    /**
     * Test add user.
     */
    @Test
    public void testAddUser() {
        when(userRepository.save(user)).thenReturn(user);

        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        Team team = new Team.TeamBuilder().withName("Team1").withMembers(users).build();

        when(teamRepository.save(any())).thenReturn(team);

        assertEquals(user, userService.addUser(
               uuid,
                "testName"
        ));
    }

    /**
     * Test delete user.
     */
    @Test
    public void testDeleteUser() {
        userService.deleteUser(user);
        verify(userRepository, times(1)).delete(user);
    }

    /**
     * Test loadUserByID, and the user exists.
     */
    @Test
    public void loadUserByIDFound() {
        when(userRepository.findById(uuid)).thenReturn(Optional.ofNullable(user));
        assertEquals(user, userService.loadUserByID(uuid));
    }

    /**
     * Test loadUserByID, and the user doesn't exist.
     */
    @Test
    public void loadUserByIDNotFound() {
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());
        assertThrows(InvalidParameterException.class, () -> userService.loadUserByID(uuid));
    }

    /**
     * Test getTeamsOfUser, there are teams that the user is part of.
     */
    @Test
    public void getTeamsOfUser() {
        List<Team> allTeams = new ArrayList<>();
        User user = new User.UserBuilder()
                            .withUuid(uuid)
                            .withName("Bibi")
                            .build();
        List<User> members = new ArrayList<>();
        Team teamOfUser = new Team.TeamBuilder()
                                    .withName("withUser")
                                    .withMembers(members)
                                    .build();

        members.add(user);
        allTeams.add(new Team.TeamBuilder()
                                .withName("notUser")
                                .withMembers(new ArrayList<User>())
                                .build());
        allTeams.add(teamOfUser);

        List<Team> usersTeams = new ArrayList<>();

        usersTeams.add(teamOfUser);

        when(teamRepository.findAll()).thenReturn(allTeams);
        when(userRepository.findById(uuid)).thenReturn(Optional.ofNullable(user));

        assertEquals(usersTeams, userService.getTeamsOfUser(uuid));
    }

    /**
     * Test getUser, user exists.
     */
    @Test
    public void getUserExists() {
        User user = new User.UserBuilder()
                            .withUuid(uuid)
                            .withName("Bibi")
                            .build();
        when(userRepository.findById(uuid)).thenReturn(Optional.ofNullable(user));

        assertEquals(user, userService.getUser(uuid));
    }

    /**
     * Test getUser, user doesn't exist.
     */
    @Test
    public void getUserDoesntExist() {
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(InvalidParameterException.class, () -> userService.getUser(uuid));
    }

    /**
     * Test changeSubscription.
     */
    @Test
    public void changeSubscription() {
        User user = new User.UserBuilder()
                            .withUuid(uuid)
                            .withName("Bibi")
                            .withHasPremium(false)
                            .build();

        assertFalse(user.getHasPremium());

        when(userRepository.findById(uuid)).thenReturn(Optional.ofNullable(user));

        userService.changeSubscription(uuid, true);

        assertTrue(user.getHasPremium());
    }

    /**
     * Test deleteUser, check that teams are deleted or saved accordingly.
     */
    @Test
    public void deleteUserCheckTeams() {
        User anotherUser = new User.UserBuilder()
                                    .withUuid(UUID.randomUUID())
                                    .withName("Josh")
                                    .build();
        Team teamWithMoreUsers = new Team.TeamBuilder()
                                            .withName("moreUser")
                                            .withMembers(new ArrayList<User>())
                                            .build();
        Team teamOfUser = new Team.TeamBuilder()
                                    .withName("withUser")
                                    .withMembers(new ArrayList<>())
                                    .build();

        User user = new User.UserBuilder()
                .withUuid(uuid)
                .withName("Max")
                .build();

        teamOfUser.getMembers().add(user);

        teamWithMoreUsers.getMembers().add(user);
        teamWithMoreUsers.getMembers().add(anotherUser);

        List<Team> allTeams = new ArrayList<>();

        allTeams.add(teamOfUser);
        allTeams.add(teamWithMoreUsers);

        when(teamRepository.findAll()).thenReturn(allTeams);

        userService.deleteUser(user);

        verify(userRepository, times(1)).delete(user);
        verify(teamRepository, times(1)).delete(teamOfUser);
        verify(teamRepository, times(1)).save(teamWithMoreUsers);
    }


}
