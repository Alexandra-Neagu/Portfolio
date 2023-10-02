package users.api;

import java.security.InvalidParameterException;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import users.services.TeamService;

@RestController

@RequestMapping("api/v1/team")
public class TeamAPI {
    private final TeamService teamService;

    // fixes PMD warning related to repeated String literal
    private final String teamString = "teamUuid";

    /**
     * Instantiates a new Team api.
     *
     * @param teamService the team service
     */
    public TeamAPI(TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * Adds a Team entity to the Team repository.
     *
     * @param name the name of the Team
     */
    @PutMapping("addTeam")
    public void addTeam(@RequestParam("name") String name) {
        teamService.addTeam(name);
    }

    /**
     * Adds a user to an existing team.
     *
     * @param userUuid The UUID of the user to add to the team
     * @param teamUuid The UUID of the team to add the user to
     */
    @PutMapping("addUserToTeam")
    public void addUserToTeam(
            @RequestParam("userUuid") UUID userUuid,
            @RequestParam(teamString) UUID teamUuid
    ) {
        teamService.addUserToTeam(userUuid, teamUuid);
    }

    /**
     * Checks whether a team with the provided UUID already exists.
     *
     * @param teamUuid The UUID of the team to check.
     * @return A boolean value, indicating whether a team with this UUID is already in the database
     */
    @GetMapping("exists")
    @ResponseBody ResponseEntity<Boolean> doesTeamExist(@RequestParam(teamString) UUID teamUuid) {
        boolean result = teamService.doesTeamExist(teamUuid);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Checks whether a team can book during the provided time.
     *
     * @param teamUuid The UUID of the team to check for
     * @param reservationTime The time of the reservation to check for
     * @return A boolean value, indicating whether the team can book during the provided time
     */
    @GetMapping("canTeamBook")
    public ResponseEntity<Boolean> canTeamBook(
            @RequestParam(teamString) UUID teamUuid,
            @RequestParam("reservationTime") String reservationTime
    ) {
        boolean result = teamService.canTeamBook(teamUuid, reservationTime);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieves the size of a team.
     *
     * @param teamUuid The UUID of the team, whose size will be retrieved
     * @return An integer, representing the size of the team
     */
    @GetMapping("getTeamSize")
    public ResponseEntity<Integer> getTeamSize(@RequestParam(teamString) UUID teamUuid) {
        int result = teamService.getTeamSize(teamUuid);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Adds a new reservation for all members of the provided team.
     *
     * @param teamUuid The uuid of the team to add the reservation for
     * @param reservationUuid The uuid of the reservation to add for each team member
     * @param timestamp The timestamp, representing the time of the reservation. Should be
     *                  in the following format: "yyyy-MM-dd'T'hh:mm:ss.SSSZZZ"
     */
    @PutMapping("addReservation")
    public void addReservationForTeam(
            @RequestParam(teamString) UUID teamUuid,
            @RequestParam("reservationUuid") UUID reservationUuid,
            @RequestParam("timestamp") String timestamp
    ) {
        teamService.addReservationForTeam(teamUuid, reservationUuid, timestamp);
    }

    /**
     * Deletes a certain team's reservation.
     *
     * @param reservationUuid The UUID of the reservation
     * @param teamUuid The UUID of the team, who has the reservation
     */
    @DeleteMapping("deleteReservation")
    public void deleteReservationForTeam(
            @RequestParam("reservationUuid") UUID reservationUuid,
            @RequestParam(teamString) UUID teamUuid
    ) {
        teamService.deleteReservationForTeam(reservationUuid, teamUuid);
    }

    /**
     * An exception handler for when one or more of the request parameters are invalid.
     *
     * @param exception The exception that was originally thrown
     * @return A `BAD_REQUEST` response with a message, corresponding to the one in the
     *         exception that was thrown
     */
    @ExceptionHandler(InvalidParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<Object> badParameterExceptionHandler(
            InvalidParameterException exception
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }
}
