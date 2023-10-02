package nl.tudelft.oopp.demo.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Student.
 */
public class Student extends User {

    private List<Long> upvotedQuestions;
    private List<Long> attandedPolls;
    private Integer votedAnswer = -1;

    /**
     * Instantiates a new Student.
     *
     * @param name       the name
     * @param ipAddress  the ip address
     * @param roomId     the room id
     * @param timeOfJoin the time of join
     */
    public Student(String name, String ipAddress, long roomId, LocalDateTime timeOfJoin) {
        super(name, ipAddress, roomId, timeOfJoin);
        upvotedQuestions = new ArrayList<>();
    }

    /**
     * Instantiates a new Student.
     *
     * @param name the name
     */
    public Student(String name) {
        super(name);
        upvotedQuestions = new ArrayList<>();
    }

    /**
     * Instantiates a new Student.
     */
    public Student() {
        super();
        upvotedQuestions = new ArrayList<>();
        attandedPolls = new ArrayList<>();
    }

    /**
     * Gets upvoted questions.
     *
     * @return the upvoted questions
     */
    public List<Long> getUpvotedQuestions() {
        return upvotedQuestions;
    }


    /**
     * Gets attended polls.
     *
     * @return the attended polls
     */
    public List<Long> getAttendedPolls() {
        return attandedPolls;
    }

    /**
     * Gets voted answer.
     *
     * @return the voted answer
     */
    public Integer getVotedAnswer() {
        return votedAnswer;
    }

    /**
     * allows you to change the number of the
     * question that has been voted for.
     *
     * @param votedAnswer the number of the voted question
     */
    public void setVotedAnswer(Integer votedAnswer) {
        this.votedAnswer = votedAnswer;
    }
}