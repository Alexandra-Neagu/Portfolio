package reservations.validators.time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

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
public class TimestampToStringValidatorTest {
    @Mock
    Validator next;

    TimestampToStringValidator validator = new TimestampToStringValidator();

    UncheckedReservation reservation =
            new UncheckedReservation(null, null, null, 1);

    Timestamp timestamp;
    String expectedResult;

    /**
     * Sets up the test suite.
     */
    @BeforeEach
    public void setup() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.JANUARY, 5, 21, 11, 10);
        calendar.set(Calendar.MILLISECOND, 111);
        timestamp = new Timestamp(calendar.getTimeInMillis());
        reservation.setConvertedTimestamp(timestamp);

        expectedResult = "2022-01-05T21:11:10.111+01:00";
        validator.setNext(next);
    }

    @Test
    public void handle_ReturnsFalse_WhenNextReturnsFalse() {
        when(next.handle(reservation)).thenReturn(false);
        boolean result = validator.handle(reservation);
        assertFalse(result);
        assertEquals(expectedResult, reservation.getTimestampString());
    }

    @Test
    public void handle_ReturnsTrue_WhenNextReturnsTrue() {
        when(next.handle(reservation)).thenReturn(true);
        boolean result = validator.handle(reservation);
        assertTrue(result);
        assertEquals(expectedResult, reservation.getTimestampString());
    }
}
