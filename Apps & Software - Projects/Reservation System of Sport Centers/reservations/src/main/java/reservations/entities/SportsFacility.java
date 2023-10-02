package reservations.entities;

import java.security.InvalidParameterException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * The SportsFacility class is used for representing halls/fields lessons
 * take place in and that can also be booked. It is a Bookable object.
 */
@Entity
@PrimaryKeyJoinColumn(name = "bookable_uuid")
public class SportsFacility extends Bookable {

    @Column(name = "min_capacity")
    private int minCapacity;

    @Transient
    private final int maxCapacityConstraint = 1;

    @Transient
    private final int minCapacityConstraint = 1;

    /**
     * An empty SportsFacility constructor, which initializes nothing but the UUID. All
     * other fields are set to their default values.
     */
    public SportsFacility() {
        super();
    }

    /**
     * A SportsFacility constructor taking as parameters the name of the facility and its maximum
     * capacity.
     *
     * @param name - the name of the facility
     * @param maxCapacity - the maximum number of people that can use this facility at the same
     *                    time
     */
    public SportsFacility(String name, int maxCapacity) {
        super(name, maxCapacity);
        this.minCapacity = 1;
        checkCapacityConstraints(this.minCapacity, maxCapacity);
    }

    /**
     * A SportsFacility constructor taking as parameters the name of the facility and both its
     * minimum and maximum capacity.
     *
     * @param name - the name of the facility
     * @param maxCapacity - the maximum number of people that can use this facility at the same
     *                    time
     * @param minCapacity - the minimum number of people that can use this facility
     */
    public SportsFacility(String name, int minCapacity, int maxCapacity) {
        super(name, maxCapacity);
        checkCapacityConstraints(minCapacity, maxCapacity);
        this.minCapacity = minCapacity;
    }

    /**
     * Checks whether the minimum capacity is smaller than the maximum capacity.
     * Checks whether minimum and maximum capacity is greater than 1.
     * If not, it throws an InvalidParameterException.
     *
     * @param minCapacity the minimum capacity of the facility
     * @param maxCapacity the maximum capacity of the facility
     */
    private void checkCapacityConstraints(int minCapacity, int maxCapacity) {
        if (maxCapacity < maxCapacityConstraint) {
            throw new InvalidParameterException("Maximum capacity should be at least 1.");
        }
        if (minCapacity < minCapacityConstraint) {
            throw new InvalidParameterException("Minimum capacity should be at least 1.");
        }

        if (maxCapacity < minCapacity) {
            throw new InvalidParameterException(
                    "Max capacity should be bigger than or equal to the min capacity."
            );
        }
    }

    /**
     * Getter for the minimum capacity of this sports facility.
     *
     * @return the minimum capacity of the facility
     */
    public int getMinCapacity() {
        return minCapacity;
    }

    /**
     * Setter for the minimum capacity of the facility.
     *
     * @param minCapacity - the new minimum capacity of the facility
     */
    public void setMinCapacity(int minCapacity) {
        checkCapacityConstraints(minCapacity, this.getMaxCapacity());
        this.minCapacity = minCapacity;
    }

    /**
     * Setter for the maximum capacity of the facility.
     *
     * @param maxCapacity - the new maximum capacity of the bookable
     */
    @Override
    public void setMaxCapacity(int maxCapacity) {
        checkCapacityConstraints(this.minCapacity, maxCapacity);
        super.setMaxCapacity(maxCapacity);
    }

    /**
     * Converts the object to a human-readable string.
     *
     * @return A human-readable string representing the object
     */
    @Override
    public String toString() {
        return "SportsFacility{"
                + "uuid=" + uuid
                + ", name='" + name + '\''
                + ", maxCapacity=" + maxCapacity
                + ", minCapacity=" + minCapacity
                + '}';
    }
}
