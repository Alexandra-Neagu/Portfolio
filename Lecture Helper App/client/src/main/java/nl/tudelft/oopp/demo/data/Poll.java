package nl.tudelft.oopp.demo.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The type Poll.
 */
public class Poll {

    private long lectureId;
    private List<String> answers;
    private List<Integer> votes;
    private String question;
    private Integer correctAnswerNo = -1;
    private boolean open = true;
    private long id;
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

        this.votes = new ArrayList<>(Collections.nCopies(answers.size(), 0));
        this.open = true;
        this.sharing = sharing;

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
     * Gets sharing.
     *
     * @return the sharing
     */
    public boolean getSharing() {
        return this.sharing;
    }

    /**
     * Sets sharing.
     *
     * @param sharing the sharing
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

    @Override
    public String toString() {
        return "Poll{"
                + "answers=" + answers
                + ", votes=" + votes
                + ", question='" + question
                + '\'' + ", correctAnswerNo=" + correctAnswerNo
                + ", open=" + open
                + ", id=" + id
                + ", shareResult=" + sharing
                + '}';
    }
}
