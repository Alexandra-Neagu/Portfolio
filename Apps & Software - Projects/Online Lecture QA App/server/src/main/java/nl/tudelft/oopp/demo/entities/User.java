package nl.tudelft.oopp.demo.entities;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The type User.
 */
@Entity(name = "User")
@Table(name = "users")
public abstract class User {

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
            name = "name"
    )
    private String name;

    @Column(
            name = "ip_address"
    )
    private String ipAddress;

    @Column(
            name = "room_id"
    )
    private long roomId;

    @Column(
            name = "time_of_join"
    )
    private LocalDateTime timeOfJoin;


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
     */
    public User() {

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
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
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
     * Gets ip address.
     *
     * @return the ip address
     */
    public String getIpAddress() {
        return ipAddress;
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
     * Gets room id.
     *
     * @return the room id
     */
    public long getRoomId() {
        return roomId;
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
     * Gets time of join.
     *
     * @return the time of join
     */
    public LocalDateTime getTimeOfJoin() {
        return timeOfJoin;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id == user.id && name.equals(user.name)
                && Objects.equals(ipAddress, user.ipAddress)
                && roomId == user.roomId
                && Objects.equals(timeOfJoin, user.timeOfJoin);
    }

    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", ipAddress='" + ipAddress + '\''
                + ", roomId='" + roomId + '\''
                + ", timeOfJoin=" + timeOfJoin
                + "}";
    }


}
