package reservations.validators.lesson;

import java.security.InvalidParameterException;
import reservations.entities.Lesson;
import reservations.repositories.LessonRepository;
import reservations.validators.BaseValidator;
import reservations.validators.UncheckedReservation;

/**
 * Checks whether there is a lesson with the bookable uuid
 * provided in the Unchecked reservation object. If the object does not
 * exist, an exception is thrown.
 */
public class LessonExistsValidator extends BaseValidator {
    private final LessonRepository lessonRepository;

    public LessonExistsValidator(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @Override
    public boolean handle(UncheckedReservation reservation) throws InvalidParameterException {
        Lesson lesson = lessonRepository.findById(reservation.getBookableUuid())
                .orElseThrow(
                        () -> new InvalidParameterException("The lesson doesn't exist.")
                );

        reservation.setConvertedBookable(lesson);
        reservation.setConvertedTimestamp(lesson.getStartTime());
        return super.checkNext(reservation);
    }
}
