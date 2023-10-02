package reservations.validators.lesson;

import java.security.InvalidParameterException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reservations.clients.UsersClient;
import reservations.entities.Reservation;
import reservations.repositories.ReservationRepository;
import reservations.validators.BaseValidator;
import reservations.validators.UncheckedReservation;

/**
 * This validator checks whether the lesson
 * is available for booking.
 */
public class LessonIsAvailableValidator extends BaseValidator {
    private final ReservationRepository reservationRepository;
    private final UsersClient usersClient;

    public LessonIsAvailableValidator(
            ReservationRepository repository,
            UsersClient usersClient) {
        this.reservationRepository = repository;
        this.usersClient = usersClient;
    }

    @Override
    public boolean handle(UncheckedReservation reservation) throws InvalidParameterException {
        ResponseEntity<Integer> teamSizeResponse =
                usersClient.getTeamSize(reservation.getTeamUuid());

        if (teamSizeResponse.getStatusCode() != HttpStatus.OK) {
            throw new InvalidParameterException(
                    "Communication cannot be established properly"
            );
        }

        int teamSize = teamSizeResponse.getBody();

        List<Reservation> reservationsOfLesson =
                reservationRepository.findAllByBookable(reservation.getConvertedBookable());

        int bookedSpotsSoFar =
                reservationsOfLesson.stream().mapToInt(Reservation::getAmount).sum();

        if (bookedSpotsSoFar + teamSize > reservation.getConvertedBookable().getMaxCapacity()) {
            throw new InvalidParameterException("There are not enough spots left");
        }

        reservation.setAmount(teamSize);
        return super.checkNext(reservation);
    }
}
