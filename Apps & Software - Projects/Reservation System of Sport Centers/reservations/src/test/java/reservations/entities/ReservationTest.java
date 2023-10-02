package reservations.entities;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class ReservationTest {
    private UUID teamUuid = UUID.randomUUID();
    private Bookable bookable = new Bookable("testBookable", 20);
    private Timestamp startTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
    private int amount = 5;
    private Reservation reservation = new Reservation(teamUuid, bookable, startTime, amount);

    @Test
    public void emptyConstructor_SetsOnlyUuid() {
        reservation = new Reservation();

        assertNotEquals(
                "Reservation UUID should not be null",
                null,
                reservation.getReservationUUID()
        );
        assertEquals("Team UUID should be null", null, reservation.getTeamUUID());
        assertEquals("Start time should be null", null, reservation.getStartTime());
        assertEquals("Bookable should be null", null, reservation.getBookable());
        assertEquals("Amount should be 0", 0, reservation.getAmount());
    }

    @Test
    public void constructorWithoutAmount_SetsProperFields() {
        reservation = new Reservation(teamUuid, bookable, startTime);

        assertNotEquals(
                "Reservation UUID should not be null",
                null,
                reservation.getReservationUUID()
        );
        assertEquals("Team UUID is not set correctly", teamUuid, reservation.getTeamUUID());
        assertEquals("Start time not set correctly", startTime, reservation.getStartTime());
        assertEquals("Bookable not set correctly", bookable, reservation.getBookable());
        assertEquals("Amount not set correctly", 1, reservation.getAmount());
    }

    @Test
    public void fullConstructor_SetsProperFields() {
        assertNotEquals(
                "Reservation UUID should not be null",
                null,
                reservation.getReservationUUID()
        );
        assertEquals("Team UUID not set correctly", teamUuid, reservation.getTeamUUID());
        assertEquals("Start time not set correctly", startTime, reservation.getStartTime());
        assertEquals("Bookable not set correctly", bookable, reservation.getBookable());
        assertEquals("Amount not set correctly", amount, reservation.getAmount());
    }

    @Test
    public void fullConstructor_ThrowsException_WhenAmountIsNegative() {
        assertThrows(
                InvalidParameterException.class,
                () -> new Reservation(teamUuid, bookable, startTime, -1)
        );
    }

    @Test
    public void getReservationUuid_ReturnsANonNullValue() {
        UUID result = reservation.getReservationUUID();

        assertNotEquals(
            "Reservation UUID not retrieved correctly",
                null,
                result
        );
    }

    @Test
    public void getTeamUUID_ReturnsCorrectValue() {
        UUID result = reservation.getTeamUUID();
        assertEquals("Team UUID not retrieved correctly", teamUuid, result);
    }

    @Test
    public void getStartTime_ReturnsCorrectValue() {
        Timestamp result = reservation.getStartTime();
        assertEquals("Timestamp not retrieved correctly", startTime, result);
    }

    @Test
    public void getBookable_ReturnsCorrectValue() {
        Bookable result = reservation.getBookable();
        assertEquals("Bookable not retrieved correctly", bookable, result);
    }

    @Test
    public void getAmount_ReturnsCorrectValue() {
        int result = reservation.getAmount();
        assertEquals("Amount not retrieved correctly", amount, result);
    }

    @Test
    public void setReservationUuid_SetsCorrectValueToField() {
        UUID newUUID = UUID.randomUUID();
        reservation.setReservationUUID(newUUID);

        UUID result = reservation.getReservationUUID();
        assertEquals("Team UUID not set correctly", newUUID, result);
    }

    @Test
    public void setTeamUuid_SetsCorrectValueToField() {
        UUID newUUID = UUID.randomUUID();
        reservation.setTeamUUID(newUUID);

        UUID result = reservation.getTeamUUID();
        assertEquals("Team UUID not set correctly", newUUID, result);
    }

    @Test
    public void setStartTime_SetsCorrectValueToField() {
        Timestamp newTimestamp = new Timestamp(Calendar.getInstance().getTimeInMillis() + 1000);
        reservation.setStartTime(newTimestamp);

        Timestamp result = reservation.getStartTime();
        assertEquals("Start time not set correctly", newTimestamp, result);
    }

    @Test
    public void setBookable_ReturnsCorrectValue() {
        Bookable newBookable = new Bookable();
        reservation.setBookable(newBookable);

        Bookable result = reservation.getBookable();
        assertEquals("Bookable not set correctly", newBookable, result);
    }

    @Test
    public void setAmount_ReturnsCorrectValue() {
        int newAmount = 2;
        reservation.setAmount(newAmount);

        int result = reservation.getAmount();
        assertEquals("Amount not set correctly", newAmount, result);
    }

    @Test
    public void setAmount_ThrowsException_WhenAmountIsNegative() {
        int newAmount = -1;
        assertThrows(
                InvalidParameterException.class,
                () -> reservation.setAmount(newAmount)
        );
    }

    @Test
    public void toString_ReturnsAnActualString() {
        String result = reservation.toString();

        assertNotEquals("Result should not be null", null, result);
        assertNotEquals("Result should not be empty", "", result);
    }

    @Test
    public void equals_ReturnsTrue_WhenComparedWithSameObject() {
        boolean expected = true;
        assertEquals("Object not equal to itself", expected, reservation.equals(reservation));
    }

    @Test
    public void equals_ReturnsFalse_WhenComparedWithOtherClass() {
        boolean expected = false;
        assertEquals(
                "Object should not be equal to an object of another class",
                expected,
                reservation.equals(2)
        );
    }

    @Test
    public void equals_ReturnsTrue_WhenObjectsHaveTheSameUuid() {
        Reservation other = new Reservation();
        other.setReservationUUID(reservation.getReservationUUID());
        boolean expected = true;
        assertEquals(
                "Objects with the same UUID should be equal",
                expected,
                reservation.equals(other)
        );
    }

    @Test
    public void checkAmountConstraints_ThrowsException_WhenAmountIsNegative()
            throws Exception {
        Method method =
                reservation.getClass().getDeclaredMethod("checkAmountConstraints", int.class);

        method.setAccessible(true);
        try {
            method.invoke(reservation, -1);
        } catch (InvocationTargetException e) {
            assertEquals(
                    "Different exception type thrown",
                    InvalidParameterException.class,
                    e.getTargetException().getClass()
            );
        }
    }

    @Test
    public void equals_ReturnsFalse_WhenObjectsHaveDifferentUuids() {
        Reservation other = new Reservation();
        boolean expected = false;
        assertEquals(
                "Object with different UUIDs should NOT be equal",
                expected,
                reservation.equals(other));
    }


    @Test
    public void hashCode_ReturnsCorrectHash() {
        int expected = Objects.hash(
                reservation.getReservationUUID()
        );

        assertEquals("Hashes do not match", expected, reservation.hashCode());
    }
}
