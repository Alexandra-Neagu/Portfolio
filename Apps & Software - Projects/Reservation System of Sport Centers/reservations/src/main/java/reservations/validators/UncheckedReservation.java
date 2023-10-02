package reservations.validators;

import java.sql.Timestamp;
import java.util.UUID;
import reservations.entities.Bookable;

public class UncheckedReservation {
    private UUID bookableUuid;
    private UUID teamUuid;
    private String timestampString;
    private int amount;

    private Bookable convertedBookable;
    private Timestamp convertedTimestamp;

    /**
     * Constructor for this entity.
     */
    public UncheckedReservation(
            UUID bookableUuid,
            UUID teamUuid,
            String timestampString,
            int amount
    ) {
        this.bookableUuid = bookableUuid;
        this.teamUuid = teamUuid;
        this.timestampString = timestampString;
        this.amount = amount;
    }

    public UUID getBookableUuid() {
        return bookableUuid;
    }

    public void setBookableUuid(UUID bookableUuid) {
        this.bookableUuid = bookableUuid;
    }

    public String getTimestampString() {
        return timestampString;
    }

    public void setTimestampString(String timestampString) {
        this.timestampString = timestampString;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Bookable getConvertedBookable() {
        return convertedBookable;
    }

    public void setConvertedBookable(Bookable convertedBookable) {
        this.convertedBookable = convertedBookable;
    }

    public Timestamp getConvertedTimestamp() {
        return convertedTimestamp;
    }

    public void setConvertedTimestamp(Timestamp convertedTimestamp) {
        this.convertedTimestamp = convertedTimestamp;
    }

    public UUID getTeamUuid() {
        return teamUuid;
    }

    public void setTeamUuid(UUID teamUuid) {
        this.teamUuid = teamUuid;
    }

}
