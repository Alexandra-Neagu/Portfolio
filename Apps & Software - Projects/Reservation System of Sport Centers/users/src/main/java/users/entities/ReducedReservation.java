package users.entities;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * The type Reduced reservation.
 */
@Entity
public class ReducedReservation {

    @Id
    @Column(name = "reservation_uuid")
    private UUID uuid; // String ID, required

    @Column(name = "createdAt")
    private Timestamp createdAt; // required

    public ReducedReservation() { }

    /**
     * Instantiates a new Reduced reservation.
     *
     * @param uuid      the uuid
     * @param createdAt the created at
     */
    public ReducedReservation(UUID uuid, Timestamp createdAt) {
        this.uuid = uuid;
        this.createdAt = createdAt;
    }

    /**
     * Gets uuid.
     *
     * @return the uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Gets createdAt field.
     *
     * @return the created at
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets createdAt.
     *
     * @param createdAt the createdAt field
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Checks if two reduced Reservations objects are equal.
     *
     * @param o Object
     * @return boolean depending on their equality
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)  {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReducedReservation other = (ReducedReservation) o;
        return this.uuid.equals(other.uuid);
    }

    /**
     * Returns the hash code of the object.
     *
     * @return the int hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), getCreatedAt());
    }

    /**
     * Returns the String representation of the object.
     *
     * @return the String representation
     */
    @Override
    public String toString() {
        return "ReducedReservation{"
                + "uuid=" + uuid
                + ", createdAt=" + createdAt
                + '}';
    }
}
