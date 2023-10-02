package users.services;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import users.entities.Team;
import users.entities.User;
import users.repositories.TeamRepository;
import users.repositories.UserRepository;

/**
 * The type User service.
 */

@Service
public class UserService {
    private final UserRepository userRepository;

    private final TeamRepository teamRepository;

    /**
     * Instantiates a new User service.
     *
     * @param userRepository the user repository
     */
    public UserService(UserRepository userRepository, TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
        this.userRepository =  userRepository;
    }

    /**
     * Adds a new user to the database.
     *
     * @param userUuid the UUID of the user
     * @param name the name of the User
     */
    public User addUser(UUID userUuid, String name) {
        User userToAdd = new User.UserBuilder().withName(name).withUuid(userUuid).build();
        userRepository.save(userToAdd);

        ArrayList<User> users = new ArrayList<>();
        users.add(userToAdd);
        Team team = new Team.TeamBuilder().withName(userToAdd.getName() + "'s Team")
                .withMembers(users).build();
        teamRepository.save(team);

        return userToAdd;
    }

    /**
     * Deletes a User from the database.
     *
     * @param user the user
     */
    public void deleteUser(User user) {
        List<Team> teams = teamRepository.findAll();

        for (Team team : teams) {
            team.getMembers().remove(user);
            if (team.getMembers().isEmpty()) {
                teamRepository.delete(team);
            } else {
                teamRepository.save(team);
            }
        }

        userRepository.delete(user);
    }

    /**
     * Loads the User matching the UUID.
     *
     * @param uuid the uuid of the user
     * @return the user matching the UUID, or an exception if not found.
     */
    public User loadUserByID(UUID uuid) throws InvalidParameterException {
        if (userRepository.findById(uuid).isEmpty()) {
            throw new InvalidParameterException("User not found.");
        }
        return userRepository.findById(uuid).get();
    }

    /**
     * Gets the teams that the user is part of.
     *
     * @param userUuid the user uuid
     * @return the teams of the user
     */
    public List<Team> getTeamsOfUser(UUID userUuid) {
        User user = loadUserByID(userUuid);
        List<Team> allTeams = teamRepository.findAll();
        List<Team> userTeams = new ArrayList<>();

        for (Team team : allTeams) {
            if (team.getMembers().contains(user)) {
                userTeams.add(team);
            }
        }

        return userTeams;
    }

    /**
     * Returns the user with the provided UUID or throws an exception.
     *
     * @param userUuid the UUID of the user
     * @return the user with that UUID
     */
    public User getUser(UUID userUuid) {
        return userRepository.findById(userUuid).orElseThrow(
                () -> new InvalidParameterException("A team with this id does not exist")
        );
    }

    /**
     * Change subscription.
     *
     * @param userUuid   the user UUID
     * @param hasPremium the hasPremium
     */
    public void changeSubscription(UUID userUuid, boolean hasPremium) {
        User user = getUser(userUuid);
        user.setHasPremium(hasPremium);
        userRepository.save(user);
    }
}
