package reservations.entities;

import java.security.InvalidParameterException;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * The Bookable class summarizes everything that can be booked.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Bookable {
    @Id
    @Column(name = "bookable_uuid")
    protected UUID uuid; // String ID

    @Column(name = "name", length = 128)
    protected String name;

    @Column(name = "max_capacity")
    protected int maxCapacity;

    /**
     * An empty Bookable constructor, which initializes nothing but the UUID. All
     * other values are set to null.
     */
    public Bookable() {
        this.uuid = UUID.randomUUID();
    }

    /**
     * A Bookable constructor, which takes as parameters the name and the maximum
     * capacity of the object.
     *
     * @param name - the name of the bookable object
     * @param maxCapacity - the maximum capacity this object has
     */
    public Bookable(String name, int maxCapacity) {
        checkMaxCapacityConstraints(maxCapacity);
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.maxCapacity = maxCapacity;
    }

    /**
     * Checks whether maximum capacity is greater than 0.
     * If not, it throws an InvalidParameterException.
     *
     * @param maxCapacity the maximum capacity of the facility
     */
    private void checkMaxCapacityConstraints(int maxCapacity) {
        if (maxCapacity < 0) {
            throw new InvalidParameterException("Max capacity should not be negative.");
        }
    }

    /**
     * Getter for the name of the bookable.
     *
     * @return The name of the bookable
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name of the bookable.
     *
     * @param name - the new name to use
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the uuid of the bookable.
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Getter for the maximum capacity of the bookable.
     *
     * @return The maximum capacity of the bookable
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Setter for the maximum capacity of the bookable.
     *
     * @param maxCapacity - the new maximum capacity to use
     */
    public void setMaxCapacity(int maxCapacity) {
        checkMaxCapacityConstraints(maxCapacity);
        this.maxCapacity = maxCapacity;
    }

    /**
     * Hashes this bookable object, using its UUID, name, description and maximum capacity.
     *
     * @return The hash code of the bookable object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), getName(), getMaxCapacity());
    }

    /**
     * Checks whether this bookable is equal to another object (by uuid).
     *
     * @param o - the object to compare this one to.
     * @return Whether this bookable is equal to the other object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bookable)) {
            return false;
        }
        Bookable bookable = (Bookable) o;
        return getUuid().equals(bookable.getUuid());
    }

    /**
     * Converts this bookable to a human-readable format.
     *
     * @return A string of the bookable in a human-readable format
     */
    @Override
    public String toString() {
        return "Bookable{"
                + "uuid=" + uuid
                + ", name='" + name + '\''
                + ", capacity=" + maxCapacity
                + '}';
    }
}
