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
 * This is the Question Entity.
 */
@Entity(name = "Question")
@Table(name = "questions")
public class Question {

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
            name = "content",
            nullable = false
    )
    private String content;

    @Column(
            name = "authorId"
    )
    private long authorId;

    @Column(
            name = "time_published"
    )
    private LocalDateTime timePublished;

    @Column(
            name = "lectureId"
    )
    private long lectureId;

    @Column(
            name = "isAnswered"
    )
    private boolean isAnswered;

    @Column(
            name = "answerId"
    )
    private long answerId;

    @Column(
            name = "upvotes"
    )
    private int upvotes;

    /**
     * Constructor for the Question object.
     *
     * @param content Question text
     */
    public Question(String content) {
        this.content = content;
        this.upvotes = 0;
        this.timePublished = LocalDateTime.now();
    }

    /**
     * Constructor for an empty Question object.
     */
    public Question() {
        this.upvotes = 0;
        this.timePublished = LocalDateTime.now();
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
     * Sets time published.
     *
     * @param timePublished the time published
     */
    public void setTimePublished(LocalDateTime timePublished) {
        this.timePublished = timePublished;
    }

    /**
     * Sets upvotes.
     *
     * @param upvotes the upvotes
     */
    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
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

    /**
     * Gets author id.
     *
     * @return the author id
     */
    public long getAuthorId() {
        return authorId;
    }

    /**
     * Sets author id.
     *
     * @param authorId the author id
     */
    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    /**
     * Gets lecture id.
     *
     * @return the lecture id
     */
    public long getLectureId() {
        return lectureId;
    }

    /**
     * Sets lecture id.
     *
     * @param lectureId the lecture id
     */
    public void setLectureId(long lectureId) {
        this.lectureId = lectureId;
    }

    /**
     * Is answered boolean.
     *
     * @return the boolean
     */
    public boolean isAnswered() {
        return isAnswered;
    }

    /**
     * Sets answered.
     *
     * @param isAnswered the is answered
     */
    public void setAnswered(boolean isAnswered) {
        this.isAnswered = isAnswered;
    }

    /**
     * Gets answer id.
     *
     * @return the answer id
     */
    public long getAnswerId() {
        return answerId;
    }

    /**
     * Sets answer id.
     *
     * @param answerId the answer id
     */
    public void setAnswerId(long answerId) {
        this.answerId = answerId;
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
     * Gets upvotes.
     *
     * @return the upvotes
     */
    public int getUpvotes() {
        return this.upvotes;
    }

    /**
     * Upvote.
     */
    public void upvote() {
        this.upvotes++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        Question question = (Question) o;
        return id == question.id
                && authorId == question.authorId
                && lectureId == question.lectureId
                && isAnswered == question.isAnswered
                && answerId == question.answerId
                && upvotes == question.upvotes
                && Objects.equals(content, question.content)
                && Objects.equals(timePublished, question.timePublished);
    }

    @Override
    public String toString() {
        return "Question{"
                + "id=" + id
                + ", content='" + content + '\''
                + ", authorId=" + authorId
                + ", timePublished=" + timePublished
                + ", lectureId=" + lectureId
                + ", isAnswered=" + isAnswered
                + ", answerId=" + answerId
                + ", upvotes=" + upvotes
                + '}';
    }

    /**
     * To string for file string.
     *
     * @return the string
     */
    public String toStringForFile() {

        StringBuilder str = new StringBuilder();

        str.append(content);
        str.append(" - ");
        str.append(timePublished.toLocalDate().toString());
        str.append(", ");
        if (timePublished.getHour() < 10) {
            str.append("0");
        }
        str.append(timePublished.getHour());
        str.append(":");
        if (timePublished.getMinute() < 10) {
            str.append("0");
        }
        str.append(timePublished.getMinute());
        str.append("\n");

        return str.toString();
    }
}
