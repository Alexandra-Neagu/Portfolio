package nl.tudelft.oopp.demo.data;

import java.time.LocalDateTime;

/**
 * The type Answer.
 */
public class Answer {

    private LocalDateTime timePublished;
    private long moderatorId;
    private long lecturerId;
    private String content;
    private long id;

    /**
     * Instantiates a new Answer.
     */
    public Answer() {
    }

    /**
     * Instantiates a new Answer.
     *
     * @param timePublished the time published
     * @param moderatorId   the moderator id
     * @param lecturerId    the lecturer id
     * @param content       the content
     */
    public Answer(LocalDateTime timePublished, long moderatorId, long lecturerId, String content) {
        this.lecturerId = lecturerId;
        this.timePublished = timePublished;
        this.moderatorId = moderatorId;
        this.content = content;
    }

    /**
     * Instantiates a new Answer.
     *
     * @param content the content
     */
    public Answer(String content) {
        this.content = content;
    }

    /**
     * Gets time published.
     *
     * @return the time published
     */
    public LocalDateTime getTimePublished() {
        return timePublished;
    }

    /**
     * Gets moderator id.
     *
     * @return the moderator id
     */
    public long getModeratorId() {
        return moderatorId;
    }

    /**
     * Gets lecturer id.
     *
     * @return the lecturer id
     */
    public long getLecturerId() {
        return lecturerId;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
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
     * Sets time published.
     *
     * @param timePublished the time published
     */
    public void setTimePublished(LocalDateTime timePublished) {
        this.timePublished = timePublished;
    }

    /**
     * Sets moderator id.
     *
     * @param moderatorId the moderator id
     */
    public void setModeratorId(long moderatorId) {
        this.moderatorId = moderatorId;
    }

    /**
     * Sets lecturer id.
     *
     * @param lecturerId the lecturer id
     */
    public void setLecturerId(long lecturerId) {
        this.lecturerId = lecturerId;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Answer{"
                + "moderatorId="
                + moderatorId
                + ", lecturerId=" + lecturerId
                + ", content='"
                + content + '\''
                + ", id=" + id
                + '}';
    }
}
