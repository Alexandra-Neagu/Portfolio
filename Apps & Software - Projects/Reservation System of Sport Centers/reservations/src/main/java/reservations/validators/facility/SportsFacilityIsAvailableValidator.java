package reservations.validators.facility;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reservations.clients.UsersClient;
import reservations.entities.Reservation;
import reservations.entities.SportsFacility;
import reservations.repositories.ReservationRepository;
import reservations.validators.BaseValidator;
import reservations.validators.UncheckedReservation;

/**
 * This validator checks whether the sports facility
 * is available for booking.
 */
public class SportsFacilityIsAvailableValidator extends BaseValidator {
    private final ReservationRepository reservationRepository;
    private final UsersClient usersClient;

    public SportsFacilityIsAvailableValidator(
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

        SportsFacility facility = (SportsFacility) reservation.getConvertedBookable();

        if (teamSize < facility.getMinCapacity()) {
            throw new InvalidParameterException(
                    "The team is too small to book this sports facility.\n"
                  + "The minimum allowed number of people is " + facility.getMinCapacity());
        } else if (teamSize > facility.getMaxCapacity()) {
            throw new InvalidParameterException(
                    "The team is too big to book this sports facility.\n"
                  + "The maximum allowed number of people is " + facility.getMaxCapacity());
        }

        long millisecondsInHour = 3_600_000L;

        Timestamp startTimestamp = reservation.getConvertedTimestamp();

        Timestamp overlappingReservationStart =
                new Timestamp(startTimestamp.getTime() - millisecondsInHour + 1);
        Timestamp overlappingReservationEnd =
                new Timestamp(startTimestamp.getTime() + millisecondsInHour - 1);

        List<Reservation> reservationsOfSportsFacility =
                reservationRepository.findAllByBookableAndStartTimeBetween(
                        facility,
                        overlappingReservationStart,
                        overlappingReservationEnd
                );

        if (!reservationsOfSportsFacility.isEmpty()) {
            throw new InvalidParameterException(
                    "The facility is already booked during that time period"
            );
        }

        return super.checkNext(reservation);
    }
}
