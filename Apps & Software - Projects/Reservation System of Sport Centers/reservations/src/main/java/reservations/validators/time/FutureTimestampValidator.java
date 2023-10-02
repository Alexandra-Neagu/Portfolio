package reservations.validators.time;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.util.Calendar;
import reservations.validators.BaseValidator;
import reservations.validators.UncheckedReservation;

/**
 * This validator checks whether the timestamp, provided
 * in UncheckedReservation is in the future.
 */
public class FutureTimestampValidator extends BaseValidator {
    @Override
    public boolean handle(UncheckedReservation reservation) throws InvalidParameterException {
        Timestamp timestamp = reservation.getConvertedTimestamp();
        Timestamp now = new Timestamp(Calendar.getInstance().getTimeInMillis());
        if (timestamp.before(now)) {
            throw new InvalidParameterException("Time should be in the future.");
        }

        return super.checkNext(reservation);
    }
}
