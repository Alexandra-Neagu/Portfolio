package nl.tudelft.oopp.demo.data;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The type Question.
 */
public class Question {

    private long id;
    private String content;
    private long authorId;
    private long lectureId;
    private boolean answered;
    private long answerId;
    private int upvotes;
    private LocalDateTime timePublished;

    /**
     * Instantiates a new Question.
     *
     * @param content the content
     */
    public Question(String content) {
        this.content = content;
    }

    /**
     * Instantiates a new Question.
     */
    public Question() {
    }

    /**
     * Instantiates a new Question.
     *
     * @param content       the content
     * @param authorId      the author id
     * @param lectureId     the lecture id
     * @param answered      the is answered
     * @param answerId      the answer id
     * @param upvotes       the upvotes
     * @param timePublished the time published
     */
    public Question(String content,
                    long authorId,
                    long lectureId,
                    boolean answered,
                    long answerId,
                    int upvotes,
                    LocalDateTime timePublished) {
        this.content = content;
        this.authorId = authorId;
        this.lectureId = lectureId;
        this.answered = answered;
        this.answerId = answerId;
        this.upvotes = upvotes;
        this.timePublished = timePublished;
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
     * Gets author id.
     *
     * @return the author id
     */
    public long getAuthorId() {
        return authorId;
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
     * Is answered boolean.
     *
     * @return the boolean
     */
    public boolean isAnswered() {
        return answered;
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
     * Gets upvotes.
     *
     * @return the upvotes
     */
    public int getUpvotes() {
        return upvotes;
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
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
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
     * Sets lecture id.
     *
     * @param lectureId the lecture id
     */
    public void setLectureId(long lectureId) {
        this.lectureId = lectureId;
    }

    /**
     * Sets answered.
     *
     * @param answered the answered
     */
    public void setAnswered(boolean answered) {
        this.answered = answered;
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
     * Sets upvotes.
     *
     * @param upvotes the upvotes
     */
    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    /**
     * Sets time published.
     *
     * @param timePublished the time published
     */
    public void setTimePublished(LocalDateTime timePublished) {
        this.timePublished = timePublished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        return id == question.id
                && authorId == question.authorId
                && lectureId == question.lectureId;
    }

    @Override
    public String toString() {
        return "Question{"
                + "id=" + id
                + ", content='" + content + '\''
                + ", authorId=" + authorId
                + ", lectureId=" + lectureId
                + ", answered=" + answered
                + ", answerId=" + answerId
                + ", upvotes=" + upvotes
                + '}';
    }
}