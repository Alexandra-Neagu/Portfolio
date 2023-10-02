package nl.tudelft.oopp.demo.data;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The type User.
 */
public abstract class User {

    private long id;
    private String name;
    private String ipAddress;
    private long roomId;
    private LocalDateTime timeOfJoin;

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
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets ip address.
     *
     * @param ipAddress the ip address
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Sets room id.
     *
     * @param roomId the room id
     */
    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    /**
     * Sets time of join.
     *
     * @param timeOfJoin the time of join
     */
    public void setTimeOfJoin(LocalDateTime timeOfJoin) {
        this.timeOfJoin = timeOfJoin;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " {"
                + "name='" + name + '\''
                + ", ipAddress='" + ipAddress + '\''
                + ", roomId='" + roomId + '\''
                + ", timeOfJoin=" + timeOfJoin
                + '}';
    }

    /**
     * Instantiates a new User.
     */
    public User() {
    }

    /**
     * Instantiates a new User.
     *
     * @param name       the name
     * @param ipAddress  the ip address
     * @param roomId     the room id
     * @param timeOfJoin the time of join
     */
    public User(String name, String ipAddress, long roomId, LocalDateTime timeOfJoin) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.roomId = roomId;
        this.timeOfJoin = timeOfJoin;
    }

    /**
     * Instantiates a new User.
     *
     * @param name the name
     */
    public User(String name) {
        this.name = name;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets ip address.
     *
     * @return the ip address
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Gets room id.
     *
     * @return the room id
     */
    public long getRoomId() {
        return roomId;
    }

    /**
     * Gets time of join.
     *
     * @return the time of join
     */
    public LocalDateTime getTimeOfJoin() {
        return timeOfJoin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(ipAddress, user.ipAddress)
                && Objects.equals(roomId, user.roomId)
                && Objects.equals(timeOfJoin, user.timeOfJoin);
    }

}
