package nl.tudelft.oopp.demo.entities;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The type Student.
 */
@Entity(name = "Student")
@Table(name = "students")
public class Student extends User {

    /**
     * Instantiates a new Student.
     *
     * @param name       the name
     * @param ipAddress  the ip address
     * @param roomId     the room id
     * @param timeOfJoin the time of join
     */
    public Student(String name, String ipAddress, long roomId, LocalDateTime timeOfJoin) {
        super(name, ipAddress, roomId, timeOfJoin);
    }

    /**
     * Instantiates a new Student.
     */
    public Student() {
        super();
    }
}
