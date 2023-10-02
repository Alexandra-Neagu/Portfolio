package users.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Team entity test class.
 */
public class TeamTest {

    /**
     * Test user 1.
     */
    User user1;
    /**
     * Test user 2.
     */
    User user2;

    /**
     * The first test user list.
     */
    ArrayList<User> users;
    /**
     * The second test user list.
     */
    ArrayList<User> users2;

    UUID userUuid = UUID.fromString("12345678-1234-1234-1234-123456789012");

    private final String teamLiteral = "Team1";

    /**
     * Setup.
     */
    @BeforeEach
    public void beforeEach() {

        user1 = new User.UserBuilder()
                        .withName("John")
                        .withUuid(userUuid)
                        .withHasPremium(true)
                        .build();
        user2 = new User.UserBuilder()
                        .withName("Doe")
                        .withUuid(userUuid)
                        .withHasPremium(true)
                        .build();
        users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
    }


    /**
     * Test getter UUID.
     */
    @Test
    public void testGetterUuid() {
        Team team = new Team.TeamBuilder().withName(teamLiteral).withMembers(users).build();
        assertNotNull(team.getUuid());
    }

    /**
     * Test getter setter name.
     */
    @Test
    public void testGetterSetterName() {
        Team team = new Team.TeamBuilder().withName(teamLiteral).withMembers(users).build();
        assertEquals(teamLiteral, team.getName());
        team.setName("Team2");
        assertEquals("Team2", team.getName());
    }

    /**
     * Test getter and setter of the team members.
     */
    @Test
    public void testGetterSetterMembers() {
        Team team = new Team.TeamBuilder().withName(teamLiteral).withMembers(users).build();
        users2 = new ArrayList<>();
        users2.add(user2);
        assertEquals(users, team.getMembers());
        team.setMembers(users2);
        assertEquals(users2, team.getMembers());
    }

    /**
     * Test equals where we check if the team equals itself.
     */
    @Test
    public void testEqualsSameObject() {
        Team team = new Team.TeamBuilder().withName(teamLiteral).withMembers(users).build();
        assertEquals(team, team);
    }

    /**
     * Test equals false same class.
     */
    @Test
    public void testEqualsFalseSameClass() {
        Team team = new Team.TeamBuilder().withName(teamLiteral).withMembers(users).build();
        Team team2 = new Team.TeamBuilder().withName("Team2").withMembers(users).build();
        assertNotEquals(team, team2);
    }

    /**
     * Test equals for two objects with different classes.
     */
    @Test
    public void testEqualsFalseDifferentClass() {
        Team team = new Team.TeamBuilder().withName(teamLiteral).withMembers(users).build();
        User user = new User.UserBuilder().withUuid(userUuid).withName("Gaga").build();
        assertNotEquals(team, user);
    }

    /**
     * Test equals where we check if a team equals null.
     */
    @Test
    public void testEqualsNull() {
        Team team = new Team.TeamBuilder().withName(teamLiteral).withMembers(users).build();
        Team team2 = null;
        assertNotEquals(team, team2);
    }

    /**
     * Test hash code.
     */
    @Test
    public void testHashCode() {
        Team team = new Team.TeamBuilder().withName(teamLiteral).withMembers(users).build();
        assertNotNull(team.hashCode());
    }

    /**
     * Test the to string method.
     */
    @Test
    public void testToString() {
        users2 = new ArrayList<>();
        Team team = new Team.TeamBuilder().withName(teamLiteral).withMembers(users2).build();
        assertEquals("Team{uuid="
                + team.getUuid() + ", name='Team1', members=[]}", team.toString());
    }

    /**
     * Test that an IllegalArgumentException is thrown
     * when trying to create an invalid team with a null name.
     */
    @Test
    public void invalidTeamNullName() {
        assertThrows(IllegalArgumentException.class, () -> new Team.TeamBuilder()
                                                                    .withName(null)
                                                                    .withMembers(new ArrayList<>())
                                                                    .build());
    }

    /**
     * Test that an IllegalArgumentException is thrown
     * when trying to create an invalid team with a null members.
     */
    @Test
    public void invalidTeamNullMembers() {
        assertThrows(IllegalArgumentException.class, () -> new Team.TeamBuilder()
                                                                    .withName("The Falcons")
                                                                    .withMembers(null)
                                                                    .build());
    }

    /**
     * Test that an IllegalArgumentException is thrown
     * when trying to set the members of a team to null.
     */
    @Test
    public void setMembersException() {
        Team team = new Team.TeamBuilder()
                            .withName(teamLiteral)
                            .withMembers(new ArrayList<>())
                            .build();
        assertThrows(IllegalArgumentException.class, () -> team.setMembers(null));
    }

}
