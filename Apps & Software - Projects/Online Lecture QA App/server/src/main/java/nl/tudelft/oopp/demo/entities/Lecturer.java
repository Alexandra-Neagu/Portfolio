package nl.tudelft.oopp.demo.entities;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The type Lecturer.
 */
@Entity(name = "Lecturer")
@Table(name = "lecturers")
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
}
