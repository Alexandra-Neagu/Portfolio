package reservations.validators.time;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import reservations.validators.BaseValidator;
import reservations.validators.UncheckedReservation;

/**
 * A class that converts the timestamp String in an Unchecked Reservation
 * into a Timestamp object, which is passed to the convertedTimestamp field
 * of the Unchecked reservation object. Format of the string to parse should
 * be "yyyy-MM-dd'T'HH:mm:ss.SSSXXX".
 */
public class StringToTimestampValidator extends BaseValidator {
    @Override
    public boolean handle(UncheckedReservation reservation) throws InvalidParameterException {
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                Locale.GERMANY
        );
        Date date = null;

        try {
            date = format.parse(reservation.getTimestampString());
        } catch (Exception e) {
            throw new InvalidParameterException("Timestamp cannot be parsed.");
        }

        Timestamp timestamp = new Timestamp(date.getTime());
        reservation.setConvertedTimestamp(timestamp);

        return super.checkNext(reservation);
    }
}
