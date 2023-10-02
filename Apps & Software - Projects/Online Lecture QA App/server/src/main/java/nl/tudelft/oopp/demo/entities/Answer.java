package nl.tudelft.oopp.demo.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The type Answer.
 */
@Entity(name = "Answer")
@Table(name = "answers")
public class Answer {

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
            name = "time_published"
    )
    private LocalDateTime timePublished;

    @Column(
            name = "moderator_id"
    )
    private long moderatorId;

    @Column(
            name = "lecturer"
    )
    private long lecturerId;

    @Column(
            name = "content"
    )
    private String content;


    /**
     * Instantiates a new Answer object.
     *
     * @param content the content
     */
    public Answer(String content) {
        this.timePublished = LocalDateTime.now();
        this.content = content;
    }

    /**
     * Instantiates a new Answer.
     */
    public Answer() {
        this.timePublished = LocalDateTime.now();
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
     * Gets time published.
     *
     * @return the time published
     */
    public LocalDateTime getTimePublished() {
        return timePublished;
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
     * Gets moderator id.
     *
     * @return the moderator id
     */
    public long getModeratorId() {
        return moderatorId;
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
     * Gets lecturer id.
     *
     * @return the lecturer id
     */
    public long getLecturerId() {
        return lecturerId;
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
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
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
    public boolean equals(Object o) {
        boolean result;
        if (this == o) {
            result = true;
        } else if (o == null || getClass() != o.getClass()) {
            result = false;
        } else {
            Answer answer = (Answer) o;
            result = id == answer.id
                    && moderatorId == answer.moderatorId
                    && lecturerId == answer.lecturerId
                    && timePublished.equals(answer.timePublished)
                    && content.equals(answer.content);
        }
        return result;
    }

    @Override
    public String toString() {
        return "Answer{"
                + "id=" + id
                + ", timePublished=" + timePublished
                + ", moderatorId=" + moderatorId
                + ", lecturerId=" + lecturerId
                + ", content='" + content + '\''
                + '}';
    }
}
