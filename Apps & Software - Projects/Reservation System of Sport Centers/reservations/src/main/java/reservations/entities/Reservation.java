package reservations.entities;

import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * A Reservation entity represents a reservation record in the database. It links a team to a
 * reservation and also specifies the start time of the reservation. The end time can be
 * automatically inferred since it is always one hour after the start time as per the requirements.
 */
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @Column(name = "reservation_uuid")
    private UUID reservationUUID;

    // TODO: Check if this needs to be changed to 'userUUID'
    @Column(name = "team_uuid")
    private UUID teamUUID;

    @ManyToOne
    @JoinColumn(name = "bookable_uuid")
    private Bookable bookable;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "amount")
    private int amount;

    @Transient
    private final int minAmount = 1;

    /**
     * An empty Reservation constructor. Only randomizes the UUID. All other fields are left to
     * their default values.
     */
    public Reservation() {
        this.reservationUUID = UUID.randomUUID();
    }

    /**
     * A Reservation constructor, taking as parameters the team uuid of the team that has made
     * the reservation, the Bookable that is being reserved and a start time of the reservation.
     * End time is automatically inferred to be one hour after the start time. Amount to be
     * booked is set to the default value of 1.
     *
     * @param teamUUID - the reserving team's UUID
     * @param bookable - the bookable object to reserve
     * @param startTime - the starting date and time of the reservation
     */
    public Reservation(UUID teamUUID, Bookable bookable, Timestamp startTime) {
        this.reservationUUID = UUID.randomUUID();
        this.teamUUID = teamUUID;
        this.bookable = bookable;
        this.startTime = startTime;
        this.amount = 1;
    }

    /**
     * A Reservation constructor, taking as parameters the team uuid of the team that has made
     * the reservation, the Bookable that is being reserved, a start time of the reservation, and
     * the amount to be booked. End time is automatically inferred to be one hour after the start
     * time.
     *
     * @param teamUUID - the reserving team's UUID
     * @param bookable - the bookable object to reserve
     * @param startTime - the starting date and time of the reservation
     * @param amount - the amount of objects to reserve
     */
    public Reservation(UUID teamUUID, Bookable bookable, Timestamp startTime, int amount) {
        this.reservationUUID = UUID.randomUUID();
        this.teamUUID = teamUUID;
        this.bookable = bookable;
        this.startTime = startTime;
        checkAmountConstraints(amount);
        this.amount = amount;
    }

    /**
     * Getter for the UUID of the reservation.
     *
     * @return the UUID of the reservation
     */
    public UUID getReservationUUID() {
        return reservationUUID;
    }

    /**
     * Getter for the UUID of the team that made the reservation.
     *
     * @return The UUID of the team
     */
    public UUID getTeamUUID() {
        return teamUUID;
    }

    /**
     * Setter of the UUID of the team that made the reservation.
     *
     * @param teamUUID - the UUID of the team that made the reservation
     */
    public void setTeamUUID(UUID teamUUID) {
        this.teamUUID = teamUUID;
    }

    /**
     * Getter for the thing that the team reserved, i.e. the Bookable.
     *
     * @return The Bookable that the team reserved
     */
    public Bookable getBookable() {
        return bookable;
    }

    protected void setReservationUUID(UUID reservationUUID) {
        this.reservationUUID = reservationUUID;
    }

    /**
     * Setter for the thing that the team reserved, i.e. the Bookable.
     *
     * @param bookable The Bookable that the team reserved
     */
    public void setBookable(Bookable bookable) {
        this.bookable = bookable;
    }

    /**
     * Getter for the starting date and time of the reservation.
     *
     * @return A timestamp, representing the starting date and time of the reservation
     */
    public Timestamp getStartTime() {
        return startTime;
    }

    /**
     * Setter for the starting date and time of the reservation.
     *
     * @param startTime - the new timestamp, indicating the starting date and time of the
     *                  reservation
     */
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    /**
     * Getter for the amount of objects booked.
     *
     * @return An int, representing the amount of objects booked
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Setter for the amount of objects booked.
     *
     * @param amount - the new amount of objects booked
     *
     */
    public void setAmount(int amount) {
        checkAmountConstraints(amount);
        this.amount = amount;
    }

    /**
     * Checks whether the amount is bigger or equal to 1.
     *
     * @param amount The amount to check.
     */
    private void checkAmountConstraints(int amount) {
        if (amount < minAmount) {
            throw new InvalidParameterException("Amount cannot be less than 1");
        }
    }

    /**
     * Converts the object to a human-readable string.
     *
     * @return A human-readable string representing the object
     */
    @Override
    public String toString() {
        return "Reservation{"
                + "reservationUUID=" + reservationUUID
                + ", teamUUID=" + teamUUID
                + ", bookable=" + bookable
                + ", startTime=" + startTime
                + ", amount= " + amount
                + '}';
    }

    /**
     * Checks whether this reservation is equal to another object (by uuid).
     *
     * @param o - the object to compare this one to.
     * @return Whether this reservation is equal to the other object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation)) {
            return false;
        }
        Reservation that = (Reservation) o;
        return getReservationUUID().equals(that.getReservationUUID());
    }

    /**
     * Hashes this reservation object, using its UUID.
     *
     * @return The hash code of the reservation object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getReservationUUID());
    }
}
