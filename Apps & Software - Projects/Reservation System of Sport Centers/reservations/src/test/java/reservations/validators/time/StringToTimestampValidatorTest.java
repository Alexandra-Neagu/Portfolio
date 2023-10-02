package reservations.validators.time;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
public class StringToTimestampValidatorTest {
    @Mock
    Validator next;

    StringToTimestampValidator validator = new StringToTimestampValidator();

    UncheckedReservation reservation =
            new UncheckedReservation(null, null, "2022-01-05T21:11:10.111+00:00", 1);

    /**
     * Sets up the test suite.
     */
    @BeforeEach
    public void setup() {
        validator.setNext(next);
    }

    @Test
    public void handle_ThrowsException_WhenTimeCannotBeParsed() {
        // String is in a different format
        String improperTimestampString = "2022-01-05 21:11:10.111";
        reservation.setTimestampString(improperTimestampString);

        assertThrows(
                InvalidParameterException.class,
                () -> validator.handle(reservation)
        );

        verify(next, never()).handle(reservation);
    }

    @Test
    public void handle_ReturnsFalse_WhenStringCanBeParsed_AndNextReturnsFalse() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 5, 21, 11, 10);
        calendar.set(Calendar.MILLISECOND, 111);
        calendar.set(Calendar.ZONE_OFFSET, 0);
        Timestamp expected = new Timestamp(calendar.getTimeInMillis());

        when(next.handle(reservation)).thenReturn(false);
        boolean returned = validator.handle(reservation);
        Timestamp result = reservation.getConvertedTimestamp();

        assertFalse(returned);
        assertEquals(expected, result);
        verify(next, times(1)).handle(reservation);
    }

    @Test
    public void handle_ReturnsTrue_WhenStringCanBeParsed_AndNextReturnsTrue() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 5, 21, 11, 10);
        calendar.set(Calendar.MILLISECOND, 111);
        calendar.set(Calendar.ZONE_OFFSET, 0);
        Timestamp expected = new Timestamp(calendar.getTimeInMillis());

        when(next.handle(reservation)).thenReturn(true);
        boolean returned = validator.handle(reservation);
        Timestamp result = reservation.getConvertedTimestamp();

        assertTrue(returned);
        assertEquals(expected, result);
        verify(next, times(1)).handle(reservation);
    }
}
