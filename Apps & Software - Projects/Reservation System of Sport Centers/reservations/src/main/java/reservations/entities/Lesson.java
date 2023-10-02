package reservations.entities;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * The Lesson class is used for representing lessons. It is a Bookable.
 */
@Entity
@PrimaryKeyJoinColumn(name = "bookable_uuid")
public class Lesson extends Bookable {
    @ManyToOne
    @JoinColumn(name = "sports_facility")
    private SportsFacility sportsFacility;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;

    /**
     * An empty constructor. Will just set the UUID of the Bookable object and leave everything
     * else with its default values.
     */
    public Lesson() {
        super();
    }

    /**
     * A Lesson constructor which takes as parameters the name of the lesson, its maximum capacity,
     * a sports facility it is held in, a starting date and time and an end date and time.
     *
     * @param name - the name of the lesson
     * @param maxCapacity - the maximum capacity of the lesson
     * @param sportsFacility - the facility that is used for the lesson
     * @param startTime - the starting time of the lesson
     * @param endTime - the ending time of the lesson
     */
    public Lesson(
            String name,
            int maxCapacity,
            SportsFacility sportsFacility,
            Timestamp startTime,
            Timestamp endTime) {

        super(name, maxCapacity);
        this.sportsFacility = sportsFacility;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Getter for the sports facility this lesson is taking place in.
     *
     * @return A sports facility in which the lesson is taking place in
     */
    public SportsFacility getSportsFacility() {
        return sportsFacility;
    }

    /**
     * Setter for the sports facility this lesson is taking place in.
     *
     * @param sportsFacility - the new sports facility to use
     */
    public void setSportsFacility(SportsFacility sportsFacility) {
        this.sportsFacility = sportsFacility;
    }

    /**
     * Getter for the starting time of this lesson.
     *
     * @return A timestamp, indicating the starting time of this lesson
     */
    public Timestamp getStartTime() {
        return startTime;
    }

    /**
     * Setter for the starting time of this lesson.
     *
     * @param startTime - a timestamp, indicating the starting time of this lesson
     */
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    /**
     * Getter for the ending time of this lesson.
     *
     * @return A timestamp, indicating the ending time of this lesson
     */
    public Timestamp getEndTime() {
        return endTime;
    }

    /**
     * Setter for the ending time of this lesson.
     *
     * @param endTime - a timestamp, indicating the ending time of this lesson
     */
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    /**
     * Converts the object to a human-readable string.
     *
     * @return A human-readable string representing the object
     */
    @Override
    public String toString() {
        return "Lesson{"
                + "uuid=" + uuid
                + ", name='" + name + '\''
                + ", maxCapacity=" + maxCapacity
                + ", sportsFacility=" + sportsFacility
                + ", startTime=" + startTime
                + ", endTime=" + endTime
                + '}';
    }
}
