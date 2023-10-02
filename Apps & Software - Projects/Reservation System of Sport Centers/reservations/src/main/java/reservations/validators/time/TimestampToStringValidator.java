package reservations.validators.time;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import reservations.validators.BaseValidator;
import reservations.validators.UncheckedReservation;

public class TimestampToStringValidator extends BaseValidator {
    @Override
    public boolean handle(UncheckedReservation reservation) {
        Timestamp t = reservation.getConvertedTimestamp();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        String result = t.toLocalDateTime().format(formatter) + "+01:00";
        reservation.setTimestampString(result);

        return super.checkNext(reservation);
    }
}
