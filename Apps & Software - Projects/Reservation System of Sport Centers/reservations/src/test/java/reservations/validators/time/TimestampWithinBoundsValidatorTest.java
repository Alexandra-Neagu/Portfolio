package reservations.validators.time;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Calendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reservations.validators.UncheckedReservation;
import reservations.validators.Validator;

@ExtendWith(MockitoExtension.class)
public class TimestampWithinBoundsValidatorTest {
    @Mock
    Validator next;

    LocalTime start = LocalTime.of(9, 0);
    LocalTime end = LocalTime.of(16, 0);

    TimestampWithinBoundsValidator validator =
            new TimestampWithinBoundsValidator(start, end, "test");

    UncheckedReservation reservation =
            new UncheckedReservation(null, null, null, 1);

    Calendar instance = Calendar.getInstance();

    /**
     * Sets up the test suite.
     */
    @BeforeEach
    public void setup() {
        validator.setNext(next);
    }

    @Test
    public void handle_ReturnsFalse_WhenTimestampInBounds_AndWhenNextReturnsFalse() {
        when(next.handle(reservation)).thenReturn(false);

        instance.set(Calendar.HOUR_OF_DAY, 15);
        Timestamp timestamp = new Timestamp(instance.getTimeInMillis());
        reservation.setConvertedTimestamp(timestamp);

        boolean result = validator.handle(reservation);
        assertFalse(result);
        verify(next, times(1)).handle(reservation);
    }

    @Test
    public void handle_ReturnsFalse_WhenTimestampOnLowerBoundary_AndWhenNextReturnsFalse() {
        when(next.handle(reservation)).thenReturn(false);

        instance.set(Calendar.HOUR_OF_DAY, 9);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        Timestamp timestamp = new Timestamp(instance.getTimeInMillis());
        reservation.setConvertedTimestamp(timestamp);

        boolean result = validator.handle(reservation);
        assertFalse(result);
        verify(next, times(1)).handle(reservation);
    }

    @Test
    public void handle_ReturnsTrue_WhenTimestampOnUpperBoundary_AndWhenNextReturnsTrue() {
        when(next.handle(reservation)).thenReturn(true);

        instance.set(Calendar.HOUR_OF_DAY, 16);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        Timestamp timestamp = new Timestamp(instance.getTimeInMillis());
        reservation.setConvertedTimestamp(timestamp);

        boolean result = validator.handle(reservation);
        assertTrue(result);
        verify(next, times(1)).handle(reservation);
    }

    @Test
    public void handle_ThrowsException_WhenTimestampOutOfBounds() {
        instance.set(Calendar.HOUR_OF_DAY, 8);
        instance.set(Calendar.MINUTE, 59);
        instance.set(Calendar.SECOND, 59);
        instance.set(Calendar.MILLISECOND, 999);
        Timestamp timestamp = new Timestamp(instance.getTimeInMillis());
        reservation.setConvertedTimestamp(timestamp);

        assertThrows(InvalidParameterException.class, () -> validator.handle(reservation));
        verify(next, never()).handle(reservation);
    }
}
