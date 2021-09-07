package nl.tudelft.oopp.demo.services;

import java.util.List;
import java.util.Optional;
import nl.tudelft.oopp.demo.entities.Answer;
import nl.tudelft.oopp.demo.repositories.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * The type Answer service.
 */
@Service
public class AnswerService {

    /**
     * The Answer repository.
     */
    AnswerRepository answerRepository;

    /**
     * Instantiates a new Answer service.
     *
     * @param answerRepository the answer repository
     */
    @Autowired
    public AnswerService(@Qualifier("AnswerRepository")AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    /**
     * Save answer.
     *
     * @param answer the answer
     * @return the answer
     */
    public Answer save(Answer answer) {
        return answerRepository.save(answer);
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    public List<Answer> findAll() {
        return answerRepository.findAll();
    }

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    public Optional<Answer> findById(long id) {
        return answerRepository.findById(id);
    }

}
