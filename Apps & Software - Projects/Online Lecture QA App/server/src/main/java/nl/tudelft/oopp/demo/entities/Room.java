package nl.tudelft.oopp.demo.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import nl.tudelft.oopp.demo.services.RoomService;

/**
 * The type Room.
 */
@Entity(name = "Room")
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            name = "id",
            updatable = false
    )
    private long id;

    @Column(
            name = "course_name"
    )
    private String courseName;

    @Column(
            name = "lecture_name"
    )
    private String lectureName;

    @Column(
            name = "scheduled_date"
    )
    private LocalDate scheduledDate;

    @Column(
            name = "start_time"
    )
    private LocalTime startTime;

    @Column(
            name = "end_time"
    )
    private LocalTime endTime;

    @Column(
            name = "Stu_room_id"
    )
    private String studentRoomId;

    @Column(
            name = "Mod_room_id"
    )
    private String moderatorRoomId;

    @Column(
            name = "Lect_room_id"
    )
    private String lecturerRoomId;

    @Column(
            name = "too_Fast_count"
    )
    private int tooFastCount = 0;

    @Column(
            name = "too_Slow_count"
    )
    private int tooSlowCount = 0;

    @Column(
            name = "student_count"
    )
    private int studentCount = 0;

    @Column(
            name = "hasBeenClosed"
    )
    private boolean hasBeenClosed;

    /**
     * The constant studentCodeLength.
     */
    public static final int studentRoomCodeLength = 5;

    /**
     * The constant moderatorRoomCodeLength.
     */
    public static final int moderatorRoomCodeLength = 7;

    /**
     * The constant lecturerCodeLength.
     */
    public static final int lecturerCodeLength = 9;

    /**
     * Instantiates a new Room.
     */
    public Room() {
        this.studentRoomId = RoomService.generateRoomCode(studentRoomCodeLength);
        this.moderatorRoomId = RoomService.generateRoomCode(moderatorRoomCodeLength);
        this.lecturerRoomId = RoomService.generateRoomCode(lecturerCodeLength);
        this.hasBeenClosed = false;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets student room id.
     *
     * @return the student room id
     */
    public String getStudentRoomId() {
        return studentRoomId;
    }

    /**
     * Sets student room id.
     *
     * @param studentRoomId the student room id
     */
    public void setStudentRoomId(String studentRoomId) {
        this.studentRoomId = studentRoomId;
    }

    /**
     * Gets moderator room id.
     *
     * @return the moderator room id
     */
    public String getModeratorRoomId() {
        return moderatorRoomId;
    }

    /**
     * Sets moderator room id.
     *
     * @param moderatorRoomId the moderator room id
     */
    public void setModeratorRoomId(String moderatorRoomId) {
        this.moderatorRoomId = moderatorRoomId;
    }

    /**
     * Gets lecturer room id.
     *
     * @return the lecturer room id
     */
    public String getLecturerRoomId() {
        return lecturerRoomId;
    }

    /**
     * Sets lecturer room id.
     *
     * @param lecturerRoomId the lecturer room id
     */
    public void setLecturerRoomId(String lecturerRoomId) {
        this.lecturerRoomId = lecturerRoomId;
    }

    /**
     * Gets course name.
     *
     * @return the course name
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Sets course name.
     *
     * @param courseName the course name
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * Gets lecture name.
     *
     * @return the lecture name
     */
    public String getLectureName() {
        return lectureName;
    }

    /**
     * Sets lecture name.
     *
     * @param lectureName the lecture name
     */
    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    /**
     * Gets scheduled date.
     *
     * @return the scheduled date
     */
    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    /**
     * Sets scheduled date.
     *
     * @param scheduledDate the scheduled date
     */
    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    /**
     * Gets start time.
     *
     * @return the start time
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Sets start time.
     *
     * @param startTime the start time
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets end time.
     *
     * @return the end time
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Sets end time.
     *
     * @param endTime the end time
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets hasBeenClosed boolean.
     *
     * @return the boolean
     */
    public boolean getHasBeenClosed() {
        return hasBeenClosed;
    }

    /**
     * Sets hasBeenClosed.
     *
     * @param hasBeenClosed the has been closed
     */
    public void setHasBeenClosed(boolean hasBeenClosed) {
        this.hasBeenClosed = hasBeenClosed;
    }

    /**
     * Increment too fast count int.
     */
    public void incrementTooFastCount() {
        tooFastCount++;
    }

    /**
     * Increment too slow count int.
     */
    public void incrementTooSlowCount() {
        tooSlowCount++;
    }

    /**
     * Gets too fast count.
     *
     * @return the too fast count
     */
    public int getTooFastCount() {
        return tooFastCount;
    }

    /**
     * Sets too fast count.
     *
     * @param tooFastCount the too fast count
     */
    public void setTooFastCount(int tooFastCount) {
        this.tooFastCount = tooFastCount;
    }

    /**
     * Gets too slow count.
     *
     * @return the too slow count
     */
    public int getTooSlowCount() {
        return tooSlowCount;
    }

    /**
     * Sets too slow count.
     *
     * @param tooSlowCount the too slow count
     */
    public void setTooSlowCount(int tooSlowCount) {
        this.tooSlowCount = tooSlowCount;
    }

    /**
     * Increment student count.
     */
    public void increaseStudentCount() {
        studentCount++;
    }

    /**
     * Decrement student count.
     */
    public void decreaseStudentCount() {
        studentCount--;
    }

    /**
     * Gets student count.
     *
     * @return the student count
     */
    public int getStudentCount() {
        return studentCount;
    }

    /**
     * Compares the equality of two rooms.
     *
     * @return the boolean result of the comparison
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        Room room = (Room) o;
        return id == room.id
                && tooFastCount == room.tooFastCount
                && tooSlowCount == room.tooSlowCount
                && studentCount == room.studentCount
                && hasBeenClosed == room.hasBeenClosed
                && Objects.equals(courseName, room.courseName)
                && Objects.equals(lectureName, room.lectureName)
                && Objects.equals(scheduledDate, room.scheduledDate)
                && Objects.equals(startTime, room.startTime)
                && Objects.equals(endTime, room.endTime)
                && Objects.equals(studentRoomId, room.studentRoomId)
                && Objects.equals(moderatorRoomId, room.moderatorRoomId)
                && Objects.equals(lecturerRoomId, room.lecturerRoomId);
    }

    /**
     * To string for file string.
     *
     * @return the string
     */
    public String toStringForFile() {

        return courseName
                + " - " + lectureName + "\n"
                + scheduledDate + ", " + startTime.toString() + " - " + endTime.toString() + "\n";
    }
}
