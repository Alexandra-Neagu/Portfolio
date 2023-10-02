package reservations.apis;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reservations.entities.Reservation;
import reservations.services.ReservationService;

@RestController
@RequestMapping("api/v1/reservation")
public class ReservationAPI {

    public final ReservationService reservationService;

    public ReservationAPI(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * A GET endpoint to retrieve all reservations for a given bookable.
     *
     * @param bookableUuid The uuid of the bookable to retrieve the reservations for
     * @return A List of reservations, corresponding to the bookable
     */
    @GetMapping("getByBookable")
    public ResponseEntity<List<Reservation>> retrieveReservationsByBookable(
            @RequestParam("bookableUuid") UUID bookableUuid
    ) {
        List<Reservation> reservations =
                reservationService.retrieveReservationsByBookable(bookableUuid);

        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve all reservations for a specific team, excluding the ones
     * that have already taken place.
     *
     * @param teamUuid The UUID of the team to retrieve the upcoming reservations for
     * @return A list of reservation objects, representing the upcoming reservations for
     *     the specific team. The reservations will be sorted in descendign chronological
     *     order
     */
    @GetMapping("getAllUpcoming")
    public ResponseEntity<List<Reservation>> getAllUpcomingReservations(
            @RequestParam("teamUuid") UUID teamUuid
    ) {
        List<Reservation> reservations = reservationService.retrieveAllUpcoming(teamUuid);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve all reservations for a specific team, including the ones
     * that have already taken place.
     *
     * @param teamUuid The UUID of the team to retrieve all the reservations for
     * @return A list of reservation objects, representing all reservations for
     *     the specific team. The reservations will be sorted in descendign chronological
     *     order
     */
    @GetMapping("getAll")
    public ResponseEntity<List<Reservation>> getAllReservationsForTeam(
            @RequestParam("teamUuid") UUID teamUuid
    ) {
        List<Reservation> reservations =
                reservationService.retrieveAllReservationsForTeam(teamUuid);

        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("exists")
    public ResponseEntity<Boolean> doesReservationExist(
            @RequestParam("reservationUuid") UUID reservationUuid,
            @RequestParam("teamUuid") UUID teamUuid
    ) {
        boolean result = reservationService.doesReservationExist(reservationUuid, teamUuid);
        return new ResponseEntity<>(result, HttpStatus.OK);
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
