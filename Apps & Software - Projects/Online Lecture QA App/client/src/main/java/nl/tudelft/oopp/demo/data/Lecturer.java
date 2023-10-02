package nl.tudelft.oopp.demo.data;

import java.time.LocalDateTime;

/**
 * The type Lecturer.
 */
public class Lecturer extends User {

    /**
     * Instantiates a new Lecturer.
     *
     * @param name       the name
     * @param ipAddress  the ip address
     * @param roomId     the room id
     * @param timeOfJoin the time of join
     */
    public Lecturer(String name, String ipAddress, long roomId, LocalDateTime timeOfJoin) {
        super(name, ipAddress, roomId, timeOfJoin);
    }

    /**
     * Instantiates a new Lecturer.
     */
    public Lecturer() {
    }

    /**
     * Instantiates a new Lecturer.
     *
     * @param name the name
     */
    public Lecturer(String name) {
        super(name);
    }
}
