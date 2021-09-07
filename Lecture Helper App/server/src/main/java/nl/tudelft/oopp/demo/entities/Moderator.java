package nl.tudelft.oopp.demo.entities;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The type Moderator.
 */
@Entity(name = "Moderator")
@Table(name = "moderators")
public class Moderator extends User {

    /**
     * Instantiates a new Moderator.
     *
     * @param name       the name
     * @param ipAddress  the ip address
     * @param roomId     the room id
     * @param timeOfJoin the time of join
     */
    public Moderator(String name, String ipAddress, long roomId, LocalDateTime timeOfJoin) {
        super(name, ipAddress, roomId, timeOfJoin);
    }

    /**
     * Instantiates a new Moderator.
     */
    public Moderator() {
    }
}
