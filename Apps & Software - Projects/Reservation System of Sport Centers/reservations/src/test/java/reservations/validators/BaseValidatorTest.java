package reservations.validators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reservations.validators.lesson.LessonExistsValidator;
import reservations.validators.time.TimestampToStringValidator;

public class BaseValidatorTest {
    Validator next = Mockito.mock(Validator.class);
    BaseValidator validator = new TimestampToStringValidator();
    UncheckedReservation reservation = new UncheckedReservation(null, null, null, 1);

    @Test
    public void checkNext_ReturnsTrue_WhenNoNextValidatorAvailable() {
        boolean result = validator.checkNext(reservation);
        assertTrue(result);
        verify(next, never()).handle(reservation);
    }

    @Test
    public void checkNext_ReturnsTrue_WhenNextReturnsTrue() {
        validator.setNext(next);
        when(next.handle(reservation)).thenReturn(true);
        boolean result = validator.checkNext(reservation);
        assertTrue(result);
        verify(next, times(1)).handle(reservation);
    }

    @Test
    public void checkNext_ReturnsFalse_WhenNextReturnsFalse() {
        validator.setNext(next);
        when(next.handle(reservation)).thenReturn(false);
        boolean result = validator.checkNext(reservation);
        assertFalse(result);
        verify(next, times(1)).handle(reservation);
    }
}
