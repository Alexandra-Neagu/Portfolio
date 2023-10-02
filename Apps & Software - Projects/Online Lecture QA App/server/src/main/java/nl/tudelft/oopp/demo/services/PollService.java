package nl.tudelft.oopp.demo.services;

import java.util.List;
import java.util.Optional;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.repositories.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Poll service.
 */
@Service
public class PollService {

    private final PollRepository pollRepository;

    /**
     * Instantiates a new Poll service.
     *
     * @param pollRepository the poll repository
     */
    @Autowired
    public PollService(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    /**
     * Save poll.
     *
     * @param poll the poll
     * @return the poll
     */
    public Poll save(Poll poll) {
        return pollRepository.save(poll);
    }

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    public Optional<Poll> findById(long id) {
        return pollRepository.findById(id);
    }

    /**
     * Delete by id.
     *
     * @param id the id
     */
    public void deleteById(long id) {
        pollRepository.deleteAll(pollRepository.findAllById(List.of(id)));
    }

    /**
     * Find all by lecture id list.
     *
     * @param lectureId the lecture id
     * @return the list
     */
    public List<Poll> findAllByLectureId(long lectureId) {
        return pollRepository.findAllByLectureIdEquals(lectureId);
    }

    /**
     * Add vote to poll integer.
     *
     * @param pollId   the poll id
     * @param answerNo the answer no
     * @return the integer
     */
    public Integer addVoteToPoll(long pollId, int answerNo) {
        if (this.findById(pollId).isPresent()) {
            Poll poll = this.findById(pollId).get();
            poll.addVote(answerNo);
            pollRepository.save(poll);
            return poll.getVotes().get(answerNo);
        }
        return null;
    }

    /**
     * Close poll.
     *
     * @param pollId the poll id
     * @return the poll
     */
    public Poll closePoll(long pollId) {
        if (this.findById(pollId).isPresent()) {
            Poll poll = this.findById(pollId).get();
            poll.setOpen(false);
            pollRepository.save(poll);
            return poll;
        }
        return null;
    }

    /**
     * Open poll.
     *
     * @param pollId the poll id
     * @return the poll
     */
    public Poll openPoll(long pollId) {
        if (this.findById(pollId).isPresent()) {
            Poll poll = this.findById(pollId).get();
            poll.setOpen(true);
            pollRepository.save(poll);
            return poll;
        }
        return null;
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    public List<Poll> findAll() {
        return pollRepository.findAll();
    }

    //    /**
    //     * Open poll.
    //     *
    //     * @param pollId the poll id
    //     * @return the poll
    //     */
    //    public boolean enableSharing(long pollId) {
    //        if (this.findById(pollId).isPresent()) {
    //            Poll poll = this.findById(pollId).get();
    //            poll.setSharing(true);
    //            pollRepository.save(poll);
    //            return poll.checkSharing();
    //        }
    //        return false;
    //    }

}
