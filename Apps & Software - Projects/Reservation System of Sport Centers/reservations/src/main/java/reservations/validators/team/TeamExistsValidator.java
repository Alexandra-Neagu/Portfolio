package reservations.validators.team;

import java.security.InvalidParameterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reservations.clients.UsersClient;
import reservations.validators.BaseValidator;
import reservations.validators.UncheckedReservation;

/**
 * This validator checks whether a team with the provided UUID exists.
 */
public class TeamExistsValidator extends BaseValidator {
    private final UsersClient usersClient;

    public TeamExistsValidator(UsersClient client) {
        this.usersClient = client;
    }

    @Override
    public boolean handle(UncheckedReservation reservation) throws InvalidParameterException {
        ResponseEntity<Boolean> doesTeamExist =
                usersClient.doesTeamExist(reservation.getTeamUuid());

        if (doesTeamExist.getStatusCode() != HttpStatus.OK) {
            throw new InvalidParameterException(
                    "Communication cannot be established properly"
            );
        }

        if (!doesTeamExist.getBody()) {
            throw new InvalidParameterException(
                    "Team with this UUID does not exist"
            );
        }

        return super.checkNext(reservation);
    }
}
