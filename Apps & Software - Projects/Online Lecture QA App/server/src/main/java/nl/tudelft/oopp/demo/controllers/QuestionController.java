package nl.tudelft.oopp.demo.controllers;

import java.util.List;
import java.util.Optional;

import nl.tudelft.oopp.demo.entities.Answer;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.StringResponse;
import nl.tudelft.oopp.demo.services.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Question controller.
 */
@Controller
@RestController
@RequestMapping("questions")
public class QuestionController {

    private final QuestionService questionService;

    /**
     * Instantiates a new Question controller.
     *
     * @param questionService the question service
     */
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    /**
     * Save question.
     *
     * @param question the question
     * @return the question
     */
    @PostMapping("/insert")
    Question save(@RequestBody Question question) {
        return questionService.save(question);
    }

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    @GetMapping("/get/{id}")
    public Optional<Question> findById(@PathVariable long id) {
        return questionService.findById(id);
    }

    /**
     * Find a ll list.
     *
     * @return the list
     */
    @GetMapping
    List<Question> findALl() {
        return questionService.findAll();
    }

    /**
     * Find all by room id list.
     *
     * @param roomId the room id
     * @return the list
     */
    @GetMapping("/getAllinRoom/{roomId}")
    List<Question> findAllByRoomId(@PathVariable long roomId) {
        return questionService.findAllByRoomId(roomId);
    }

    /**
     * Gets all answered.
     *
     * @param roomId the room id
     * @return the all answered
     */
    @GetMapping("/getAllAnswered/{roomId}")
    List<Question> getAllAnswered(@PathVariable long roomId) {
        return questionService.findAllAnswered(roomId);
    }

    /**
     * Gets all unanswered.
     *
     * @param roomId the room id
     * @return the all unanswered
     */
    @GetMapping("/getAllUnanswered/{roomId}")
    List<Question> getAllUnanswered(@PathVariable long roomId) {
        return questionService.findAllUnanswered(roomId);
    }

    /**
     * Upvote question integer.
     *
     * @param id the id
     * @return the integer
     */
    @GetMapping("/upvote/{id}")
    public Integer upvoteQuestion(@PathVariable long id) {
        return questionService.upvoteQuestion(id);
    }

    /**
     * Delete byid.
     *
     * @param id the id
     */
    @DeleteMapping("/delete/{id}")
    public void deleteByid(@PathVariable long id) {
        questionService.deleteById(id);
    }

    /**
     * Mark question answered.
     *
     * @param id the id
     */
    @GetMapping("/markasanswered/{id}")
    public void markQuestionAnswered(@PathVariable long id) {
        questionService.markQuestionAnswered(id);
    }

    /**
     * Select by student id list.
     *
     * @param authorId the author id
     * @return the list
     */
    @GetMapping("/showbystudentid/{authorId}")
    List<Question> selectByStudentId(@PathVariable long authorId) {
        return questionService.selectByAuthorId(authorId);
    }


    /**
     * Sort by age list.
     *
     * @param roomId the room id
     * @return the list
     */
    @GetMapping("/sortbyage/{roomId}")
    List<Question> sortByAge(@PathVariable long roomId) {
        return questionService.sortByAge(roomId);
    }

    /**
     * Sort by age unanswered list.
     *
     * @param roomId the room id
     * @return the list
     */
    @GetMapping("/sortbyageunanswered/{roomId}")
    List<Question> sortByAgeUnanswered(@PathVariable long roomId) {
        return questionService.sortByAgeUnanswered(roomId);
    }

    /**
     * Sort by age answered list.
     *
     * @param roomId the room id
     * @return the list
     */
    @GetMapping("/sortbyageanswered/{roomId}")
    List<Question> sortByAgeAnswered(@PathVariable long roomId) {
        return questionService.sortByAgeAnswered(roomId);
    }

    /**
     * Select by upvotes list.
     *
     * @param roomId the room id
     * @return the list
     */
    @GetMapping("/sortByUpvotes/{roomId}")
    List<Question> selectByUpvotes(@PathVariable long roomId) {
        return questionService.selectByUpvotes(roomId);
    }

    /**
     * Select by upvotes unanswered list.
     *
     * @param roomId the room id
     * @return the list
     */
    @GetMapping("/sortByUpvotesUnanswered/{roomId}")
    List<Question> selectByUpvotesUnanswered(@PathVariable long roomId) {
        return questionService.selectByUpvotesUnanswered(roomId);
    }

    /**
     * Select by upvotes answered list.
     *
     * @param roomId the room id
     * @return the list
     */
    @GetMapping("/sortByUpvotesAnswered/{roomId}")
    List<Question> selectByUpvotesAnswered(@PathVariable long roomId) {
        return questionService.selectByUpvotesAnswered(roomId);
    }

    /**
     * Select by popularity list.
     *
     * @param roomId the room id
     * @return the list
     */
    @GetMapping("/sortByPopularity/{roomId}")
    List<Question> selectByPopularity(@PathVariable long roomId) {
        return questionService.selectByPopularity(roomId);
    }

    /**
     * Select by popularity unanswered list.
     *
     * @param roomId the room id
     * @return the list
     */
    @GetMapping("/sortByPopularityUnanswered/{roomId}")
    List<Question> selectByPopularityUnanswered(@PathVariable long roomId) {
        return questionService.selectByPopularityUnanswered(roomId);
    }

    /**
     * Select by popularity answered list.
     *
     * @param roomId the room id
     * @return the list
     */
    @GetMapping("/sortByPopularityAnswered/{roomId}")
    List<Question> selectByPopularityAnswered(@PathVariable long roomId) {
        return questionService.selectByPopularityAnswered(roomId);
    }

    /**
     * Rephrase question string response.
     *
     * @param id         the id
     * @param newContent the new content
     * @return the string response
     */
    @PostMapping("/rephrase/{id}")
    StringResponse rephraseQuestion(@PathVariable long id, @RequestBody String newContent) {
        return questionService.rephraseQuestion(id, newContent);
    }

    /**
     * Sets answer id.
     *
     * @param id       the id
     * @param answerId the answer id
     * @return the answer id
     */
    @PostMapping("/setAnswerId/{id}")
    Question setAnswerId(@PathVariable long id, @RequestBody long answerId) {
        return questionService.setAnswerId(id, answerId);
    }

}