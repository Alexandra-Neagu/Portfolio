package reservations.validators.equipment;

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
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import reservations.entities.Equipment;
import reservations.entities.Reservation;
import reservations.repositories.ReservationRepository;
import reservations.validators.UncheckedReservation;
import reservations.validators.Validator;

@ExtendWith(MockitoExtension.class)
public class EquipmentIsAvailableValidatorTest {
    @Mock
    ReservationRepository reservationRepository;

    @Mock
    Validator next;

    @Autowired
    @InjectMocks
    EquipmentIsAvailableValidator validator;

    Equipment equipment = new Equipment("test", 7, "test");
    UncheckedReservation reservation =
            new UncheckedReservation(equipment.getUuid(), null, null, 1);

    Calendar calendarInstance = Calendar.getInstance();
    UUID reservationTeamUuid = UUID.fromString("e27b008e-6e48-11ec-90d6-0242ac120003");
    UUID otherTeamUuid = UUID.fromString("ef613412-6e48-11ec-90d6-0242ac120003");
    long millisecondsInHour = 3_600_000L;

    List<Reservation> reservations;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup() {
        Reservation reservation1 = new Reservation(reservationTeamUuid, equipment, null, 2);
        Reservation reservation2 = new Reservation(otherTeamUuid, equipment, null, 1);
        Reservation reservation3 = new Reservation(otherTeamUuid, equipment, null, 1);
        Reservation reservation4 = new Reservation(otherTeamUuid, equipment, null, 1);

        reservations = List.of(reservation1, reservation2, reservation3, reservation4);
        validator.setNext(next);
    }

    @Test
    public void handle_ThrowsException_WhenAmountRequestedGoesOverCapacity() {
        Timestamp timestamp = new Timestamp(calendarInstance.getTimeInMillis());
        reservation.setConvertedTimestamp(timestamp);
        reservation.setConvertedBookable(equipment);

        int amountToBook = 3;
        reservation.setAmount(amountToBook);

        long tomorrowMilliseconds = timestamp.getTime();
        Timestamp overlappingStart = new Timestamp(tomorrowMilliseconds - millisecondsInHour + 1);
        Timestamp overlappingEnd = new Timestamp(tomorrowMilliseconds + millisecondsInHour - 1);

        when(reservationRepository.findAllByBookableAndStartTimeBetween(
                equipment, overlappingStart, overlappingEnd
        )).thenReturn(reservations);

        assertThrows(InvalidParameterException.class, () -> validator.handle(reservation));

        verify(
                reservationRepository,
                times(1)
        ).findAllByBookableAndStartTimeBetween(
                equipment,
                overlappingStart,
                overlappingEnd
        );

        verify(next, never()).handle(reservation);
    }

    @Test
    public void handle_Succeeds_WhenAmountRequestedIsRightOnBoundary() {
        boolean result = handleSucceeds(2, true);
        assertTrue(result);
    }

    @Test
    public void handle_Succeeds_WhenAmountRequestedCanFit() {
        boolean result = handleSucceeds(1, false);
        assertFalse(result);
    }


    private boolean handleSucceeds(int amountToBook, boolean nextReturns) {
        Timestamp timestamp = new Timestamp(calendarInstance.getTimeInMillis());
        reservation.setConvertedTimestamp(timestamp);
        reservation.setConvertedBookable(equipment);

        reservation.setAmount(amountToBook);

        long tomorrowMilliseconds = timestamp.getTime();
        Timestamp overlappingStart = new Timestamp(tomorrowMilliseconds - millisecondsInHour + 1);
        Timestamp overlappingEnd = new Timestamp(tomorrowMilliseconds + millisecondsInHour - 1);

        when(reservationRepository.findAllByBookableAndStartTimeBetween(
                equipment, overlappingStart, overlappingEnd
        )).thenReturn(reservations);

        when(next.handle(reservation)).thenReturn(nextReturns);

        boolean result = validator.handle(reservation);

        verify(reservationRepository, times(1))
                .findAllByBookableAndStartTimeBetween(equipment, overlappingStart, overlappingEnd);

        verify(next, times(1)).handle(reservation);

        return result;
    }
}
