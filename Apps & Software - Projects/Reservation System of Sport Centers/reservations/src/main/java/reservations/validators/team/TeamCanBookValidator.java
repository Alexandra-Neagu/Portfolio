package reservations.validators.team;

import java.security.InvalidParameterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reservations.clients.UsersClient;
import reservations.validators.BaseValidator;
import reservations.validators.UncheckedReservation;

/**
 * This validator checks whether a team with the provided UUID can still book.
 */
public class TeamCanBookValidator extends BaseValidator {
    private final UsersClient usersClient;

    public TeamCanBookValidator(UsersClient client) {
        this.usersClient = client;
    }

    @Override
    public boolean handle(UncheckedReservation reservation) throws InvalidParameterException {
        ResponseEntity<Boolean> canTeamBook =
                usersClient.canTeamBook(
                        reservation.getTeamUuid(),
                        reservation.getTimestampString()
                );

        if (canTeamBook.getStatusCode() != HttpStatus.OK) {
            throw new InvalidParameterException(
                    "Communication cannot be established properly"
            );
        }

        if (!canTeamBook.getBody()) {
            throw new InvalidParameterException(
                    "Team with this UUID cannot book"
            );
        }

        return super.checkNext(reservation);
    }
}
