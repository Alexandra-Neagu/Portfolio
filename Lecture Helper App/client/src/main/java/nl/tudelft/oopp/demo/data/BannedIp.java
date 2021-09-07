package nl.tudelft.oopp.demo.data;

import java.time.LocalTime;

/**
 * The type Banned ip.
 */
public class BannedIp {

    private String ipAddress;
    private LocalTime bannedAt;
    private Long roomId;

    /**
     * Instantiates a new Banned ip.
     */
    public BannedIp() {
    }

    /**
     * Instantiates a new Banned ip.
     *
     * @param ipAddress the ip address
     * @param bannedAt  the banned at
     * @param roomId    the room id
     */
    public BannedIp(String ipAddress, LocalTime bannedAt, Long roomId) {
        this.ipAddress = ipAddress;
        this.bannedAt = bannedAt;
        this.roomId = roomId;
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
     * Gets banned at.
     *
     * @return the banned at
     */
    public LocalTime getBannedAt() {
        return bannedAt;
    }

    /**
     * Gets the banned room id.
     *
     * @return room id
     */
    public Long getRoomId() {
        return roomId;
    }
}
