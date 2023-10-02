package reservations.services;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reservations.entities.Bookable;
import reservations.entities.Reservation;
import reservations.repositories.BookableRepository;
import reservations.repositories.ReservationRepository;

@Service
public class ReservationService {
    private final BookableRepository bookableRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(
            BookableRepository bookableRepository,
            ReservationRepository reservationRepository
    ) {
        this.bookableRepository = bookableRepository;
        this.reservationRepository = reservationRepository;
    }

    /**
     * Given an UUID for a Bookable, this endpoint retrieves all
     * reservations made for the specific bookable.
     *
     * @param bookableUuid the Bookable's UUID
     * @return  a list of reservations made for the Bookable based on its UUID,
     *          sorted in inverse chronological order
     */
    public List<Reservation> retrieveReservationsByBookable(UUID bookableUuid) {
        Bookable bookableToRetrieveReservationsFor = getBookable(bookableUuid);
        List<Reservation> reservations =
                reservationRepository.findAllByBookable(bookableToRetrieveReservationsFor);

        reservations.sort((o1, o2) -> -o1.getStartTime().compareTo(o2.getStartTime()));

        return reservations;
    }

    /**
     * Retrieves a Bookable based on its UUID.
     *
     * @param uuid - the UUID of the Bookable
     * @return the Bookable based on its UUID
     */
    public Bookable getBookable(UUID uuid) {
        return bookableRepository.findById(uuid).orElseThrow(
                () -> new InvalidParameterException("A bookable with this UUID does not exist")
        );
    }

    /**
     * Retrieves all reservations for a specific team that have not yet taken place.
     * The returned list will be sorted in reverse chronological order.
     *
     * @param teamUuid The UUID of the team to retrieve the reservations for
     * @return A list of reservations for the specific team
     */
    public List<Reservation> retrieveAllUpcoming(UUID teamUuid) {
        Calendar now = Calendar.getInstance();
        Timestamp currentTimestamp = new Timestamp(now.getTimeInMillis());
        List<Reservation> reservations = reservationRepository
                .findAllByTeamUUIDAndStartTimeAfter(teamUuid, currentTimestamp);

        reservations.sort((o1, o2) -> -o1.getStartTime().compareTo(o2.getStartTime()));

        return reservations;
    }

    /**
     * Retrieves all reservations for a specific team.
     * The returned list will be sorted in reverse chronological order.
     *
     * @param teamUuid The UUID of the team to retrieve the reservations for
     * @return A list of reservations for the specific team
     */
    public List<Reservation> retrieveAllReservationsForTeam(UUID teamUuid) {
        List<Reservation> reservations = reservationRepository.findAllByTeamUUID(teamUuid);
        reservations.sort((o1, o2) -> -o1.getStartTime().compareTo(o2.getStartTime()));

        return reservations;
    }

    /**
     * Checks whether a reservation with the provided UUIDs exists.
     *
     * @param reservationUuid The ID of the reservation, whose existence will be checked
     * @param teamUuid The UUID of the team that has made the reservation
     * @return Whether such a reservation exists or not
     */
    public boolean doesReservationExist(UUID reservationUuid, UUID teamUuid) {
        Reservation reservation = null;

        try {
            reservation = getReservation(reservationUuid);
        } catch (InvalidParameterException e) {
            return false;
        }

        return reservation.getTeamUUID().equals(teamUuid);
    }

    /**
     * Retrieves a reservation from the provided UUID.
     *
     * @param reservationUuid The UUID of the reservation to look for
     * @return The Reservation, corresponding to the provided UUID
     */
    public Reservation getReservation(UUID reservationUuid) {
        return reservationRepository.findById(reservationUuid).orElseThrow(
                () -> new InvalidParameterException("A reservation with this id does not exist")
        );
    }
}
