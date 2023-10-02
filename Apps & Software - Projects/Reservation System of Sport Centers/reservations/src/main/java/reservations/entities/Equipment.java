package reservations.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * The equipment class is used for representing equipment. It is a Bookable.
 */
@Entity
@Table(name = "equipment")
@PrimaryKeyJoinColumn(name = "bookable_uuid")
public class Equipment extends Bookable {
    @Column(name = "related_sport")
    private String relatedSport;

    /**
     * An empty constructor. Will just set the UUID of the Bookable object and leave everything
     * else unchanged.
     */
    public Equipment() {
        super();
    }

    /**
     * A constructor taking a name, an amount and a sport that this equipment is related to in
     * the form of a string.
     *
     * @param name - the name of the equipment
     * @param amount - the amount available (i.e. maximum capacity in Bookable)
     * @param relatedSport - the sport this piece of equipment is related to
     */
    public Equipment(String name, int amount, String relatedSport) {
        super(name, amount);
        this.relatedSport = relatedSport;
    }

    /**
     * A getter for the sport this piece of equipment is related to.
     *
     * @return The sport this piece of equipment is related to
     */
    public String getRelatedSport() {
        return relatedSport;
    }

    /**
     * A setter for the sport this piece of equipment is related to.
     *
     * @param relatedSport - the new sport this piece of equipment is related to.
     */
    public void setRelatedSport(String relatedSport) {
        this.relatedSport = relatedSport;
    }

    /**
     * Converts the object to a human-readable string.
     *
     * @return A human-readable string representing the object
     */
    @Override
    public String toString() {
        return "Equipment{"
                + "uuid=" + uuid
                + ", name='" + name + '\''
                + ", amount=" + maxCapacity
                + ", relatedSport='" + relatedSport + '\''
                + '}';
    }
}

