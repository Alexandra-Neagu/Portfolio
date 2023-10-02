package reservations.validators.time;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.time.LocalTime;
import reservations.validators.BaseValidator;
import reservations.validators.UncheckedReservation;

/**
 * This timestamp validates that a timestamp, passed in an unchecked reservation
 * is between some bounds start and end.
 */
public class TimestampWithinBoundsValidator extends BaseValidator {
    private final LocalTime start;
    private final LocalTime end;
    private final String action;

    /**
     * Constructor.
     */
    public TimestampWithinBoundsValidator(
            LocalTime start,
            LocalTime end,
            String action
    ) {
        this.start = start;
        this.end = end;
        this.action = action;
    }

    @Override
    public boolean handle(UncheckedReservation reservation) throws InvalidParameterException {
        Timestamp timestamp = reservation.getConvertedTimestamp();
        LocalTime localTime = timestamp.toLocalDateTime().toLocalTime();

        boolean isInBounds = localTime.isAfter(start) && localTime.isBefore(end)
                || localTime.equals(start) || localTime.equals(end);

        if (!isInBounds) {
            throw new InvalidParameterException(
                    "You can only " + action + " between " + start + " and " + end
            );
        }

        return super.checkNext(reservation);
    }
}
