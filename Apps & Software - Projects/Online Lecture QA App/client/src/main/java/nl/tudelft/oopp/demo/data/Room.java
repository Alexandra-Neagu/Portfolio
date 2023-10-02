package nl.tudelft.oopp.demo.data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * The type Room.
 */
public class Room {

    private String courseName;
    private String lectureName;

    private LocalDate scheduledDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private String studentRoomId;
    private String moderatorRoomId;
    private String lecturerRoomId;

    private int tooFastCount;
    private int tooSlowCount;

    private long id;

    private boolean hasBeenClosed;

    /**
     * Sets course name.
     *
     * @param courseName the course name
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
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
     * Sets scheduled date.
     *
     * @param scheduledDate the scheduled date
     */
    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
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
     * Sets end time.
     *
     * @param endTime the end time
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
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
     * Sets moderator room id.
     *
     * @param moderatorRoomId the moderator room id
     */
    public void setModeratorRoomId(String moderatorRoomId) {
        this.moderatorRoomId = moderatorRoomId;
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
     * Is has been closed boolean.
     *
     * @return the boolean
     */
    public boolean getHasBeenClosed() {
        return hasBeenClosed;
    }

    /**
     * Sets has been closed.
     *
     * @param hasBeenClosed the has been closed
     */
    public void setHasBeenClosed(boolean hasBeenClosed) {
        this.hasBeenClosed = hasBeenClosed;
    }

    /**
     * Instantiates a new Room.
     *
     * @param studentRoomId   the student room id
     * @param moderatorRoomId the moderator room id
     * @param lecturerRoomId  the lecturer room id
     */
    public Room(String studentRoomId, String moderatorRoomId, String lecturerRoomId) {
        this.studentRoomId = studentRoomId;
        this.moderatorRoomId = moderatorRoomId;
        this.lecturerRoomId = lecturerRoomId;
    }

    /**
     * Instantiates a new Room.
     *
     * @param courseName      the course name
     * @param lectureName     the lecture name
     * @param scheduledDate   the scheduled date
     * @param startTime       the start time
     * @param endTime         the end time
     * @param studentRoomId   the student room id
     * @param moderatorRoomId the moderator room id
     * @param lecturerRoomId  the lecturer room id
     * @param tooFastCount    the too fast count
     * @param tooSlowCount    the too slow count
     */
    public Room(String courseName,
                String lectureName,
                LocalDate scheduledDate,
                LocalTime startTime,
                LocalTime endTime,
                String studentRoomId,
                String moderatorRoomId,
                String lecturerRoomId,
                int tooFastCount,
                int tooSlowCount) {
        this.courseName = courseName;
        this.lectureName = lectureName;
        this.scheduledDate = scheduledDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.studentRoomId = studentRoomId;
        this.moderatorRoomId = moderatorRoomId;
        this.lecturerRoomId = lecturerRoomId;
        this.tooFastCount = tooFastCount;
        this.tooSlowCount = tooSlowCount;
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
     * Gets too slow count.
     *
     * @return the too slow count
     */
    public int getTooSlowCount() {
        return tooSlowCount;
    }

    /**
     * Instantiates a new Room.
     *
     * @param courseName    the course name
     * @param lectureName   the lecture name
     * @param scheduledDate the scheduled date
     * @param startTime     the start time
     * @param endTime       the end time
     */
    public Room(String courseName,
                String lectureName,
                LocalDate scheduledDate,
                LocalTime startTime,
                LocalTime endTime) {
        this.courseName = courseName;
        this.lectureName = lectureName;
        this.scheduledDate = scheduledDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        Room room = (Room) o;
        return tooFastCount == room.tooFastCount
                && tooSlowCount == room.tooSlowCount
                && id == room.id
                && Objects.equals(courseName, room.courseName)
                && Objects.equals(lectureName, room.lectureName)
                && Objects.equals(scheduledDate, room.scheduledDate)
                && Objects.equals(startTime, room.startTime)
                && Objects.equals(endTime, room.endTime)
                && Objects.equals(studentRoomId, room.studentRoomId)
                && Objects.equals(moderatorRoomId, room.moderatorRoomId)
                && Objects.equals(lecturerRoomId, room.lecturerRoomId);
    }
    
    @Override
    public String toString() {
        return "Room{"
                + "id='" + id + '\''
                + ", courseName='" + courseName + '\''
                + ", lectureName='" + lectureName + '\''
                + ", scheduledDate=" + scheduledDate
                + ", startTime=" + startTime
                + ", endTime=" + endTime
                + ", studentRoomId=" + studentRoomId
                + ", moderatorRoomId=" + moderatorRoomId
                + ", lecturerRoomId=" + lecturerRoomId
                + '}';
    }

    /**
     * Instantiates a new Room.
     *
     * @param courseName  the course name
     * @param lectureName the lecture name
     * @param startTime   the start time
     * @param endTime     the end time
     */
    public Room(String courseName, String lectureName, LocalTime startTime, LocalTime endTime) {
        this.courseName = courseName;
        this.lectureName = lectureName;
        this.startTime = startTime;
        this.endTime = endTime;
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
     * Gets lecture name.
     *
     * @return the lecture name
     */
    public String getLectureName() {
        return lectureName;
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
     * Gets start time.
     *
     * @return the start time
     */
    public LocalTime getStartTime() {
        return startTime;
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
     * Gets student room id.
     *
     * @return the student room id
     */
    public String getStudentRoomId() {
        return studentRoomId;
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
     * Gets lecturer room id.
     *
     * @return the lecturer room id
     */
    public String getLecturerRoomId() {
        return lecturerRoomId;
    }
}
