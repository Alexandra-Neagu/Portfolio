package reservations.validators.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import reservations.entities.Lesson;
import reservations.entities.Reservation;
import reservations.repositories.ReservationRepository;
import reservations.validators.UncheckedReservation;
import reservations.validators.Validator;

@ExtendWith(MockitoExtension.class)
public class LessonIsAvailableValidatorTest {
    @Mock
    ReservationRepository reservationRepository;
    @Mock
    UsersClient usersClient;

    @Mock
    Validator next;

    @Autowired
    @InjectMocks
    LessonIsAvailableValidator validator;

    UUID reservationTeamUuid = UUID.fromString("e27b008e-6e48-11ec-90d6-0242ac120003");
    UUID otherTeamUuid = UUID.fromString("ef613412-6e48-11ec-90d6-0242ac120003");

    Lesson lesson;
    UncheckedReservation reservation;

    long millisecondsInHour = 3_600_000L;
    List<Reservation> reservations;

    /**
     * Sets the test suite up.
     */
    @BeforeEach
    public void setup() {
        Calendar now = Calendar.getInstance();
        Timestamp start = new Timestamp(now.getTimeInMillis());
        now.add(Calendar.HOUR, 1);
        Timestamp end = new Timestamp(now.getTimeInMillis());
        lesson = new Lesson("lesson", 10, null, start, end);

        this.reservation =
                new UncheckedReservation(lesson.getUuid(), reservationTeamUuid, null, 1);
        this.reservation.setConvertedBookable(lesson);

        Reservation reservation = new Reservation(otherTeamUuid, lesson, null, 5);
        Reservation reservation2 = new Reservation(otherTeamUuid, lesson, null, 2);
        reservations = List.of(reservation, reservation2);

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
    public void handle_ThrowsException_WhenTeamSizeMoreThanAvailableSpots() {
        when(usersClient.getTeamSize(reservationTeamUuid)).thenReturn(
                ResponseEntity.of(Optional.of(5))
        );

        when(reservationRepository.findAllByBookable(lesson))
                .thenReturn(reservations);

        assertThrows(InvalidParameterException.class, () -> validator.handle(reservation));
        verify(usersClient, times(1)).getTeamSize(reservationTeamUuid);
        verify(reservationRepository, times(1)).findAllByBookable(lesson);
    }


    @Test
    public void handle_ReturnsTrue_WhenLessonHasEnoughSpots_AndNextReturnsTrue() {
        boolean result = handleSucceeds(true, 1);
        assertTrue(result);
    }

    @Test
    public void handle_ReturnsFalse_WhenLessonHasEnoughSpotsOnBoundary_AndNextReturnsFalse() {
        boolean result = handleSucceeds(false, 3);
        assertFalse(result);
    }


    private boolean handleSucceeds(boolean nextReturns, int teamSize) {
        when(usersClient.getTeamSize(reservationTeamUuid)).thenReturn(
                ResponseEntity.of(Optional.of(teamSize))
        );

        when(next.handle(reservation)).thenReturn(nextReturns);

        when(reservationRepository.findAllByBookable(lesson))
                .thenReturn(reservations);

        boolean result = validator.handle(reservation);

        verify(next, times(1)).handle(reservation);
        assertEquals(teamSize, reservation.getAmount());

        return result;
    }
}
