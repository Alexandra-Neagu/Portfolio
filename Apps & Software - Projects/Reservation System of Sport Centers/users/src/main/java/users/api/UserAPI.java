package users.api;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import users.entities.Team;
import users.entities.User;
import users.services.UserService;



/**
 * The type User api.
 */

@RestController
@RequestMapping("api/v1/user")
public class UserAPI {
    private final UserService userService;

    private final String userLiteral = "userUuid";

    /**
     * Instantiates a new User API.
     *
     * @param userService the user service
     */
    public UserAPI(UserService userService) {

        this.userService = userService;
    }

    /**
     * Adds a User entity to the User repository.
     *
     * @param userUuid the user
     */
    @PutMapping("add")
    public void addUser(
            @RequestParam(userLiteral) UUID userUuid,
            @RequestParam("name") String name
    ) {
        User user = userService.addUser(userUuid, name);
    }

    /**
     * Deletes a User entity from the User repository.
     *
     * @param userUuid the user
     */
    @DeleteMapping("delete")
    public void deleteUser(
            @RequestParam(userLiteral) UUID userUuid
    ) {
        User user = userService.loadUserByID(userUuid);

        userService.deleteUser(user);
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

    /**
     * Gets the teams that the provided user is part of.
     *
     * @param userUuid the user UUID
     * @return the teams of the user
     */
    @GetMapping("getUserTeams")
    public ResponseEntity<List<Team>> getUserTeams(@RequestParam(userLiteral) UUID userUuid) {
        List<Team> teams = userService.getTeamsOfUser(userUuid);
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    /**
     * Sets the subscription of the user with the provided UUID
     * into the provided subscription.
     *
     * @param userUuid   the user UUID
     * @param hasPremium the hasPremium
     */
    @PutMapping("changeSubscription")
    public void changeSubscription(
            @RequestParam(userLiteral) UUID userUuid,
            @RequestParam("hasPremium") boolean hasPremium
    ) {
        userService.changeSubscription(userUuid, hasPremium);
    }
}
