package nl.tudelft.oopp.demo.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The type Poll.
 */
@Entity(name = "Poll")
@Table(name = "polls")
public class Poll {

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
            name = "lectureId"
    )
    private long lectureId;

    @ElementCollection
    private List<String> answers;

    @ElementCollection
    private List<Integer> votes;

    @Column(
            name = "question"
    )
    private String question;

    @Column(
            name = "correctAnswerNo"
    )
    private Integer correctAnswerNo;

    @Column(
            name = "open"
    )
    private boolean open;

    @Column(
            name = "sharing"
    )
    private boolean sharing;

    /**
     * Instantiates a new Poll.
     *
     * @param lectureId       the lecture id
     * @param answers         the answers
     * @param question        the question
     * @param correctAnswerNo the correct answer no
     * @param sharing         the sharing
     */
    public Poll(long lectureId,
                ArrayList<String> answers,
                String question,
                Integer correctAnswerNo,
                boolean sharing) {

        this.lectureId = lectureId;
        this.answers = answers;
        this.question = question;
        this.sharing = sharing;

        this.votes = new ArrayList<>(Collections.nCopies(answers.size(), 0));
        this.open = true;

        // if the correctAnswerNo argument is null
        // it means this is a quiz, which doesn't have a correct answer
        // then this.correctAnswerNo will be set to -1
        // else, if the correctAnswerNo is not null,
        // it means this is a poll, which has a correct answer
        // then this.correctAnswerNo stores the index of the correct answer
        this.correctAnswerNo = (correctAnswerNo != null) ? correctAnswerNo : -1;
    }

    /**
     * Instantiates a new Poll.
     */
    public Poll() {

    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return this.id;
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
     * Gets lecture id.
     *
     * @return the lecture id
     */
    public long getLectureId() {
        return this.lectureId;
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
     * Gets answers.
     *
     * @return the answers
     */
    public List<String> getAnswers() {
        return this.answers;
    }

    /**
     * Sets answers.
     *
     * @param answers the answers
     */
    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    /**
     * Gets votes.
     *
     * @return the votes
     */
    public List<Integer> getVotes() {
        return this.votes;
    }

    /**
     * Gets question.
     *
     * @return the question
     */
    public String getQuestion() {
        return this.question;
    }

    /**
     * Sets question.
     *
     * @param question the question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Gets correct answer no.
     *
     * @return the correct answer no
     */
    public Integer getCorrectAnswerNo() {
        return this.correctAnswerNo;
    }

    /**
     * Sets correct answer no.
     *
     * @param correctAnswerNo the correct answer no
     */
    public void setCorrectAnswerNo(Integer correctAnswerNo) {
        this.correctAnswerNo = (correctAnswerNo != null) ? correctAnswerNo : -1;
    }

    /**
     * Is open boolean.
     *
     * @return the boolean
     */
    public boolean getOpen() {
        return this.open;
    }

    /**
     * Is open boolean.
     *
     * @return the boolean
     */
    public boolean getSharing() {
        return this.sharing;
    }

    /**
     * Sets answers.
     *
     * @param sharing the answers
     */
    public void setSharing(boolean sharing) {
        this.sharing = sharing;
    }

    /**
     * Sets open.
     *
     * @param open the open
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Poll)) {
            return false;
        }
        Poll poll = (Poll) o;
        return id == poll.id
                && lectureId == poll.lectureId
                && Objects.equals(answers, poll.answers)
                && Objects.equals(votes, poll.votes)
                && Objects.equals(question, poll.question)
                && Objects.equals(correctAnswerNo, poll.correctAnswerNo)
                && Objects.equals(sharing, poll.sharing);
    }

    @Override
    public String toString() {
        return "Poll{"
                + "id=" + id
                + ", lectureId=" + lectureId
                + ", answers=" + answers
                + ", votes=" + votes
                + ", question='" + question + '\''
                + ", correctAnswerNo=" + correctAnswerNo
                + ", sharings=" + sharing
                + '}';
    }

    /**
     * Init answers.
     *
     * @param noOfAnswers the no of answers
     */
    public void initAnswers(int noOfAnswers) {
        this.answers = new ArrayList<>(noOfAnswers);
    }

    /**
     * Init votes.
     *
     * @param noOfAnswers the no of answers
     */
    public void initVotes(int noOfAnswers) {
        this.votes = new ArrayList<>(Collections.nCopies(noOfAnswers, 0));
    }


    /**
     * Add vote int.
     *
     * @param answerNo the answer no
     * @return the int
     */
    public int addVote(int answerNo) {
        int newValue = votes.get(answerNo) + 1;
        votes.set(answerNo, newValue);
        return votes.get(answerNo);
    }
}
