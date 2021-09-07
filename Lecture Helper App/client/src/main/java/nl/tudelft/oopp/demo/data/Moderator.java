package nl.tudelft.oopp.demo.data;

import java.time.LocalDateTime;

/**
 * The type Moderator.
 */
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
        super();
    }

    /**
     * Instantiates a new Moderator.
     *
     * @param name the name
     */
    public Moderator(String name) {
        super(name);
    }
}
