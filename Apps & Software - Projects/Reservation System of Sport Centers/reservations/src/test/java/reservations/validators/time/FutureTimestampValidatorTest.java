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
import java.util.Calendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reservations.validators.UncheckedReservation;
import reservations.validators.Validator;

@ExtendWith(MockitoExtension.class)
public class FutureTimestampValidatorTest {
    @Mock
    Validator next;

    FutureTimestampValidator validator = new FutureTimestampValidator();

    UncheckedReservation reservation =
            new UncheckedReservation(null, null, null, 1);

    /**
     * Sets up the test suite.
     */
    @BeforeEach
    public void setup() {
        validator.setNext(next);
    }

    @Test
    public void handle_ThrowsException_WhenTimeBeforeCurrentTime() {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        Timestamp timestamp = new Timestamp(yesterday.getTimeInMillis());
        reservation.setConvertedTimestamp(timestamp);

        assertThrows(InvalidParameterException.class, () -> validator.handle(reservation));
        verify(next, never()).handle(reservation);
    }

    @Test
    public void handle_ReturnsFalse_WhenTimeIsOneHourFromNow_AndNextReturnsFalse() {
        Calendar oneHourLater = Calendar.getInstance();
        oneHourLater.add(Calendar.HOUR, 1);
        Timestamp timestamp = new Timestamp(oneHourLater.getTimeInMillis());
        reservation.setConvertedTimestamp(timestamp);

        when(next.handle(reservation)).thenReturn(false);
        boolean result = validator.handle(reservation);

        assertFalse(result);
        verify(next, times(1)).handle(reservation);
    }

    @Test
    public void handle_ReturnsTrue_WhenTimeIsTomorrow_AndNextReturnsTrue() {
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        Timestamp timestamp = new Timestamp(tomorrow.getTimeInMillis());
        reservation.setConvertedTimestamp(timestamp);

        when(next.handle(reservation)).thenReturn(true);
        boolean result = validator.handle(reservation);

        assertTrue(result);
        verify(next, times(1)).handle(reservation);
    }
}
