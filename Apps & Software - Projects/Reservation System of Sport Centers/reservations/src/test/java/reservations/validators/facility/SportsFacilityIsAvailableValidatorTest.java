package reservations.validators.facility;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reservations.clients.UsersClient;
import reservations.entities.Reservation;
import reservations.entities.SportsFacility;
import reservations.repositories.ReservationRepository;
import reservations.validators.UncheckedReservation;
import reservations.validators.Validator;

@ExtendWith(MockitoExtension.class)
public class SportsFacilityIsAvailableValidatorTest {
    @Mock
    ReservationRepository reservationRepository;
    @Mock
    UsersClient usersClient;

    @Mock
    Validator next;

    @Autowired
    @InjectMocks
    SportsFacilityIsAvailableValidator validator;

    Calendar calendarInstance = Calendar.getInstance();
    UUID reservationTeamUuid = UUID.fromString("e27b008e-6e48-11ec-90d6-0242ac120003");
    UUID otherTeamUuid = UUID.fromString("ef613412-6e48-11ec-90d6-0242ac120003");

    SportsFacility facility = new SportsFacility("test", 2, 4);
    UncheckedReservation reservation =
            new UncheckedReservation(facility.getUuid(), reservationTeamUuid, null, 1);

    long millisecondsInHour = 3_600_000L;
    List<Reservation> reservations;

    /**
     * Sets the test suite up.
     */
    @BeforeEach
    public void setup() {
        Reservation reservation = new Reservation(otherTeamUuid, facility, null, 1);
        reservations = List.of(reservation);

        this.reservation.setConvertedBookable(facility);
        validator.setNext(next);
    }

    @Test
    public void handle_ThrowsException_WhenCommunicationFails() {
        when(usersClient.getTeamSize(reservationTeamUuid)).thenReturn(
                new ResponseEntity<>(HttpStatus.BAD_GATEWAY)
        );

        assertThrows(InvalidParameterException.class, () -> validator.handle(reservation));
        verify(usersClient, times(1)).getTeamSize(reservationTeamUuid);
    }

    @Test
    public void handle_ThrowsException_WhenTeamSizeSmallerThanMinCapacity() {
        when(usersClient.getTeamSize(reservationTeamUuid)).thenReturn(
                ResponseEntity.of(Optional.of(1))
        );

        assertThrows(InvalidParameterException.class, () -> validator.handle(reservation));
        verify(usersClient, times(1)).getTeamSize(reservationTeamUuid);
    }

    @Test
    public void handle_ThrowsException_WhenTeamSizeBiggerThanMaxCapacity() {
        when(usersClient.getTeamSize(reservationTeamUuid)).thenReturn(
                ResponseEntity.of(Optional.of(5))
        );

        assertThrows(InvalidParameterException.class, () -> validator.handle(reservation));
        verify(usersClient, times(1)).getTeamSize(reservationTeamUuid);
    }

    @Test
    public void handle_ThrowsException_WhenFacilityIsAlreadyBooked() {
        when(usersClient.getTeamSize(reservationTeamUuid)).thenReturn(
                ResponseEntity.of(Optional.of(2))
        );

        Timestamp timestamp = new Timestamp(calendarInstance.getTimeInMillis());
        reservation.setConvertedTimestamp(timestamp);

        long tomorrowMilliseconds = timestamp.getTime();
        Timestamp overlappingStart = new Timestamp(tomorrowMilliseconds - millisecondsInHour + 1);
        Timestamp overlappingEnd = new Timestamp(tomorrowMilliseconds + millisecondsInHour - 1);

        when(reservationRepository.findAllByBookableAndStartTimeBetween(
                facility, overlappingStart, overlappingEnd
        )).thenReturn(reservations);

        assertThrows(InvalidParameterException.class, () -> validator.handle(reservation));

        verify(
                reservationRepository,
                times(1)
        ).findAllByBookableAndStartTimeBetween(
                facility,
                overlappingStart,
                overlappingEnd
        );

        verify(next, never()).handle(reservation);
    }


    @Test
    public void handle_ReturnsTrue_WhenFacilityFree_AndNextReturnsTrue() {
        boolean result = handleSucceeds(true);
        assertTrue(result);
    }

    @Test
    public void handle_ReturnsFalse_WhenFacilityFree_AndNextReturnsFalse() {
        boolean result = handleSucceeds(false);
        assertFalse(result);
    }


    private boolean handleSucceeds(boolean nextReturns) {
        when(usersClient.getTeamSize(reservationTeamUuid)).thenReturn(
                ResponseEntity.of(Optional.of(4))
        );

        Timestamp timestamp = new Timestamp(calendarInstance.getTimeInMillis());
        reservation.setConvertedTimestamp(timestamp);

        long tomorrowMilliseconds = timestamp.getTime();
        Timestamp overlappingStart = new Timestamp(tomorrowMilliseconds - millisecondsInHour + 1);
        Timestamp overlappingEnd = new Timestamp(tomorrowMilliseconds + millisecondsInHour - 1);

        when(reservationRepository.findAllByBookableAndStartTimeBetween(
                facility, overlappingStart, overlappingEnd
        )).thenReturn(List.of());

        when(next.handle(reservation)).thenReturn(nextReturns);

        boolean result = validator.handle(reservation);

        verify(
                reservationRepository,
                times(1)
        ).findAllByBookableAndStartTimeBetween(
                facility,
                overlappingStart,
                overlappingEnd
        );

        verify(next, times(1)).handle(reservation);

        return result;
    }
}
