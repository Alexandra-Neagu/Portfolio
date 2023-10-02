package users.services;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import org.springframework.stereotype.Service;
import users.clients.ReservationsClient;
import users.entities.ReducedReservation;
import users.entities.Team;
import users.entities.User;
import users.repositories.ReducedReservationRepository;
import users.repositories.TeamRepository;
import users.repositories.UserRepository;

/**
 * The Team service.
 */
@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ReducedReservationRepository reducedReservationRepository;

    private final ReservationsClient reservationsClient;

    /**
     * Instantiates a new Team service.
     *
     * @param teamRepository the team repository
     */
    public TeamService(
            TeamRepository teamRepository,
            UserRepository userRepository,
            ReducedReservationRepository reducedReservationRepository,
            ReservationsClient reservationsClient
    ) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.reducedReservationRepository = reducedReservationRepository;
        this.reservationsClient = reservationsClient;
    }

    /**
     * Adds a new team to the database.
     *
     * @param name the name of the team
     * @return the team
     */
    public Team addTeam(String name) {
        Team teamToAdd = new Team.TeamBuilder().withName(name).build();
        return teamRepository.save(teamToAdd);
    }

    /**
     * Add user to team.
     */
    public void addUserToTeam(UUID userUuid, UUID teamUuid) {
        Team team = getTeam(teamUuid);
        User user = getUser(userUuid);
        if (team.getMembers().contains(user)) {
            return;
        }

        team.getMembers().add(user);
        teamRepository.save(team);
    }

    /**
     * Checks if the team is allowed to make the reservation.
     *
     * @param teamUuid the UUID of the team that wants to make the reservation
     * @param reservationTime the time of the reservation
     * @return true/false if the team can validly make the reservation
     */
    public boolean canTeamBook(UUID teamUuid, String reservationTime) {
        Team team = getTeam(teamUuid);
        Date date = parseStringToDate(reservationTime);
        Calendar day = Calendar.getInstance();
        day.setTime(date);

        int sameDayCounter;
        for (User user : team.getMembers()) {
            sameDayCounter = 0;
            for (ReducedReservation reservation : user.getReservations()) {
                if (checkIfSameDate(reservation, day)) {
                    sameDayCounter++;
                }
            }
            if ((!user.getHasPremium() && sameDayCounter >= 1)
                    || (user.getHasPremium() && sameDayCounter >= 3)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the reservation is on the same day.
     *
     * @param reservation the reservation to be checked
     * @param day the current day
     * @return true/false if the reservation is on the same day
     */
    private boolean checkIfSameDate(ReducedReservation reservation, Calendar day) {
        boolean isYearTheSame = day.get(Calendar.YEAR)
                == reservation.getCreatedAt().toLocalDateTime().getYear();

        boolean isDateTheSame = day.get(Calendar.DAY_OF_YEAR)
                == reservation.getCreatedAt().toLocalDateTime().getDayOfYear();

        return isYearTheSame && isDateTheSame;
    }

    /**
     * Returns the team with the provided UUID or throws an exception.
     *
     * @param teamUuid the UUID of the team that wants to make the reservation
     * @return the team with that UUID
     */
    public Team getTeam(UUID teamUuid) {
        return teamRepository.findById(teamUuid).orElseThrow(
                () -> new InvalidParameterException("A team with this id does not exist")
        );
    }

    /**
     * Retrieves a user from the repository, which corresponds to the provided user id.
     *
     * @param userUuid The id of the user to look for.
     * @return The user with the provided id.
     */
    public User getUser(UUID userUuid) {
        return userRepository.findById(userUuid).orElseThrow(
            () -> new InvalidParameterException("A user with this id does not exist")
        );
    }

    /**
     * Parses the time provided as a String in a Calendar (helper method).
     *
     * @param time the time as a String
     * @return the time as a Calendar object
     */
    private Date parseStringToDate(String time) {
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

        return date;
    }

    /**
     * Returns the size of the team with the provided UUID.
     *
     * @param teamUuid the UUID of the team
     * @return the size of the team
     */
    public int getTeamSize(UUID teamUuid) {
        Team team = getTeam(teamUuid);
        return team.getMembers().size();
    }

    /**
     * Adds a reservation for all of the users in the given team.
     *
     * @param teamUuid The uuid of the team that is trying to make the reservation.
     * @param reservationUuid The uuid of the reservation that the team is making. This will
     *                        be checked with the reservations microservice
     * @param timestampString The starting time of the reservation
     */
    public void addReservationForTeam(UUID teamUuid, UUID reservationUuid, String timestampString) {
        if (!canTeamBook(teamUuid, timestampString)) {
            throw new InvalidParameterException("This team cannot make a reservation");
        }
        Team team = getTeam(teamUuid);

        if (!reservationsClient.doesReservationExist(reservationUuid, teamUuid).getBody()) {
            throw new InvalidParameterException("A reservation with these UUIDs does not exist");
        }

        Timestamp timestamp = new Timestamp(parseStringToDate(timestampString).getTime());
        ReducedReservation reservation = new ReducedReservation(reservationUuid, timestamp);
        reducedReservationRepository.save(reservation);

        for (User user : team.getMembers()) {
            user.getReservations().add(reservation);
            userRepository.save(user);
        }
    }

    /**
     * Checks whether a team with the provided UUID exists.
     *
     * @param teamUuid The id of the team to check for
     * @return Whether the team with this uuid exists
     */
    public boolean doesTeamExist(UUID teamUuid) {
        try {
            getTeam(teamUuid);
        } catch (InvalidParameterException e) {
            return false;
        }
        return true;
    }

    /**
     * Removes reservations for a given team.
     *
     * @param reservationUuid The Uuid of the reservation
     * @param teamUuid The Uuid of the team
     */
    public void deleteReservationForTeam(UUID reservationUuid, UUID teamUuid) {
        Team team = getTeam(teamUuid);
        ReducedReservation reservation = getReservation(reservationUuid);

        for (User user : team.getMembers()) {
            user.getReservations().remove(reservation);
            userRepository.save(user);
        }

        reducedReservationRepository.delete(reservation);
    }

    /**
     * Retrieves a reservation, given its Uuid.
     *
     * @param reservationUuid The Uuid of the reservation to retrieve
     * @return The reservation
     */
    public ReducedReservation getReservation(UUID reservationUuid) {
        return reducedReservationRepository.findById(reservationUuid).orElseThrow(
                () -> new InvalidParameterException("Reservation with this UUID does not exist")
        );
    }
}
