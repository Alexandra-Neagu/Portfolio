package nl.tudelft.oopp.demo.entities;

import java.time.LocalTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The type Banned ip.
 */
@Entity(name = "BannedIp")
@Table(name = "banned_ips")
public class BannedIp {

    @Id
    @Column(
            name = "ip_address",
            updatable = false
    )
    private String ipAddress;

    @Column(
            name = "banned_at"
    )
    private LocalTime bannedAt;

    @Column(
            name = "room_id"
    )
    private long roomId;

    /**
     * Instantiates a new Banned ip.
     *
     * @param ipAddress the ip address
     * @param bannedAt  the banned at
     * @param roomId    the room id
     */
    public BannedIp(String ipAddress, LocalTime bannedAt, long roomId) {
        this.ipAddress = ipAddress;
        this.bannedAt = bannedAt;
        this.roomId = roomId;
    }

    /**
     * Instantiates a new Banned ip.
     */
    public BannedIp() {
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
     * Gets banned at.
     *
     * @return the banned at
     */
    public LocalTime getBannedAt() {
        return bannedAt;
    }

    /**
     * Sets banned at.
     *
     * @param bannedAt the banned at
     */
    public void setBannedAt(LocalTime bannedAt) {
        this.bannedAt = bannedAt;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BannedIp bannedIp = (BannedIp) o;
        return ipAddress.equals(bannedIp.ipAddress)
                && bannedAt.equals(bannedIp.bannedAt)
                && roomId == bannedIp.roomId;
    }

    @Override
    public String toString() {
        return "BannedIp{"
                + "ip_address= " + ipAddress
                + ", bannedAt= " + bannedAt
                + ", roomId= " + roomId
                + "}";
    }
}
