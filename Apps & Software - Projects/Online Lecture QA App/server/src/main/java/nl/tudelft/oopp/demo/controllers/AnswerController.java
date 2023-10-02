package nl.tudelft.oopp.demo.controllers;

import java.util.List;
import java.util.Optional;
import nl.tudelft.oopp.demo.entities.Answer;
import nl.tudelft.oopp.demo.services.AnswerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Answer controller.
 */
@Controller
@RestController
@RequestMapping("answers")
public class AnswerController {

    private final AnswerService answerService;

    /**
     * Instantiates a new Answer controller.
     *
     * @param answerService the answer service
     */
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    /**
     * Save answer.
     *
     * @param answer the answer
     * @return the answer
     */
    @PostMapping("/insert")
    Answer save(@RequestBody Answer answer) {
        return answerService.save(answer);
    }

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    @GetMapping("/get/{id}")
    public Optional<Answer> findById(@PathVariable long id) {
        return answerService.findById(id);
    }

    /**
     * Find a ll list.
     *
     * @return the list
     */
    @GetMapping
    List<Answer> findALl() {
        return answerService.findAll();
    }
}
