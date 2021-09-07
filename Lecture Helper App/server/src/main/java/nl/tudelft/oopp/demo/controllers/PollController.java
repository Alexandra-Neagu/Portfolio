package nl.tudelft.oopp.demo.controllers;

import java.util.List;
import java.util.Optional;
import nl.tudelft.oopp.demo.entities.Poll;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.services.PollService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Poll controller.
 */
@RestController
@RequestMapping("polls")
public class PollController {

    private final PollService pollService;

    /**
     * Instantiates a new Poll controller.
     *
     * @param pollService the poll service
     */
    public PollController(PollService pollService) {
        this.pollService = pollService;
    }


    /**
     * Find a ll list.
     *
     * @return the list
     */
    @GetMapping("/getAll")
    List<Poll> findALl() {
        return pollService.findAll();
    }

    /**
     * Save poll.
     *
     * @param poll the poll
     * @return the poll
     */
    @PostMapping("/insert")
    public Poll save(@RequestBody Poll poll) {
        return pollService.save(poll);
    }

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    @GetMapping("/get/{id}")
    public Optional<Poll> findById(@PathVariable long id) {
        return pollService.findById(id);
    }

    /**
     * Delete byid.
     *
     * @param id the id
     */
    @DeleteMapping("/delete/{id}")
    public void deleteByid(@PathVariable long id) {
        pollService.deleteById(id);
    }

    /**
     * Find by lecture id list.
     *
     * @param lectureId the lecture id
     * @return the list
     */
    @GetMapping("/getAllForRoom/{lectureId}")
    public List<Poll> findByLectureId(@PathVariable long lectureId) {
        return pollService.findAllByLectureId(lectureId);
    }

    /**
     * Add vote integer.
     *
     * @param pollId   the poll id
     * @param answerNo the answer no
     * @return the integer
     */
    @GetMapping("/addVote/{pollId}/{answerNo}")
    public Integer addVote(@PathVariable long pollId,
                           @PathVariable int answerNo) {
        return pollService.addVoteToPoll(pollId, answerNo);
    }

    /**
     * Open poll poll.
     *
     * @param id the id
     * @return the poll
     */
    @GetMapping("/open/{id}")
    public Poll openPoll(@PathVariable long id) {
        return pollService.openPoll(id);
    }

    /**
     * Close poll poll.
     *
     * @param id the id
     * @return the poll
     */
    @GetMapping("/close/{id}")
    public Poll closePoll(@PathVariable long id) {
        return pollService.closePoll(id);
    }
}
