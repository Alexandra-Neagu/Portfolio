package reservations.validators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import reservations.entities.Bookable;

public class UncheckedReservationTest {
    UUID bookableUuid = UUID.fromString("12345678-1234-1234-1234-123456789012");
    UUID teamUuid = UUID.fromString("abcdefab-abcd-abcd-abcd-abcdefabcdef");
    String timestampString = "2022-01-05T18:29:00.000+01:00";
    int amount = 2;
    Bookable convertedBookable = new Bookable("name", 1);
    Timestamp convertedTimestamp = new Timestamp(Calendar.getInstance().getTimeInMillis());

    UncheckedReservation reservation =
            new UncheckedReservation(null, null, null, 0);

    @Test
    public void constructor_SetsProperFields() {
        UncheckedReservation reservation =
                new UncheckedReservation(bookableUuid, teamUuid, timestampString, amount);

        assertEquals(bookableUuid, reservation.getBookableUuid());
        assertEquals(teamUuid, reservation.getTeamUuid());
        assertEquals(timestampString, reservation.getTimestampString());
        assertEquals(amount, reservation.getAmount());
        assertNull(reservation.getConvertedBookable());
        assertNull(reservation.getConvertedTimestamp());
    }

    @Test
    public void getAndSetOnBookableUuid_WorkAsExpected() {
        reservation.setBookableUuid(bookableUuid);
        UUID result = reservation.getBookableUuid();
        assertEquals(bookableUuid, result);
    }

    @Test
    public void getAndSetOnTimestampString_WorkAsExpected() {
        reservation.setTimestampString(timestampString);
        String result = reservation.getTimestampString();
        assertEquals(timestampString, result);
    }

    @Test
    public void getAndSetOnAmount_WorkAsExpected() {
        reservation.setAmount(amount);
        int result = reservation.getAmount();
        assertEquals(amount, result);
    }

    @Test
    public void getAndSetOnConvertedBookable_WorkAsExpected() {
        reservation.setConvertedBookable(convertedBookable);
        Bookable result = reservation.getConvertedBookable();
        assertEquals(convertedBookable, result);
    }

    @Test
    public void getAndSetOnConvertedTimestamp_WorkAsExpected() {
        reservation.setConvertedTimestamp(convertedTimestamp);
        Timestamp result = reservation.getConvertedTimestamp();
        assertEquals(convertedTimestamp, result);
    }

    @Test
    public void getAndSetOnTeamUuid_WorkAsExpected() {
        reservation.setTeamUuid(teamUuid);
        UUID result = reservation.getTeamUuid();
        assertEquals(teamUuid, result);
    }
}
