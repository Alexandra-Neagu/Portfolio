package reservations.validators.lesson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import reservations.entities.Lesson;
import reservations.repositories.LessonRepository;
import reservations.validators.UncheckedReservation;
import reservations.validators.Validator;

@ExtendWith(MockitoExtension.class)
public class LessonExistsValidatorTest {
    @Mock
    LessonRepository lessonRepository;

    @Mock
    Validator next;

    @Autowired
    @InjectMocks
    LessonExistsValidator validator;

    Lesson lesson;
    UncheckedReservation reservation;

    /**
     * Sets up the test suite.
     */
    @BeforeEach
    public void setup() {
        Calendar now = Calendar.getInstance();
        Timestamp start = new Timestamp(now.getTimeInMillis());
        now.add(Calendar.HOUR, 1);
        Timestamp end = new Timestamp(now.getTimeInMillis());
        lesson = new Lesson("lesson", 3, null, start, end);
        reservation = new UncheckedReservation(lesson.getUuid(), null, null, 1);
        validator.setNext(next);
    }

    @Test
    public void handle_ThrowsException_WhenLessonDoesNotExist() {
        when(lessonRepository.findById(lesson.getUuid())).thenReturn(Optional.empty());
        assertThrows(InvalidParameterException.class, () -> validator.handle(reservation));
    }

    @Test
    public void handleReturnsTrue_WhenFacilityExists_AndNextReturnsTrue() {
        when(lessonRepository.findById(lesson.getUuid())).thenReturn(Optional.of(lesson));
        when(next.handle(reservation)).thenReturn(true);

        boolean result = validator.handle(reservation);
        assertTrue(result);
        assertEquals(lesson, reservation.getConvertedBookable());
        assertEquals(lesson.getStartTime(), reservation.getConvertedTimestamp());
    }

    @Test
    public void handleReturnsFalse_WhenEquipmentExists_AndNextReturnsFalse() {
        when(lessonRepository.findById(lesson.getUuid())).thenReturn(Optional.of(lesson));
        when(next.handle(reservation)).thenReturn(false);

        boolean result = validator.handle(reservation);
        assertFalse(result);
        assertEquals(lesson, reservation.getConvertedBookable());
        assertEquals(lesson.getStartTime(), reservation.getConvertedTimestamp());
    }
}
