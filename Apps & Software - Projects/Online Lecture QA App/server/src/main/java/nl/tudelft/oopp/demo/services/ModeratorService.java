package nl.tudelft.oopp.demo.services;

import java.util.List;
import java.util.Optional;
import nl.tudelft.oopp.demo.entities.Moderator;
import nl.tudelft.oopp.demo.repositories.ModeratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Moderator service.
 */
@Service
public class ModeratorService {

    /**
     * The Moderator repository.
     */
    ModeratorRepository moderatorRepository;

    /**
     * Instantiates a new Moderator service.
     *
     * @param moderatorRepository the moderator repository
     */
    @Autowired
    public ModeratorService(ModeratorRepository moderatorRepository) {
        this.moderatorRepository = moderatorRepository;
    }

    /**
     * Save moderator.
     *
     * @param moderator the moderator
     * @return the moderator
     */
    public Moderator save(Moderator moderator) {
        return moderatorRepository.save(moderator);
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    public List<Moderator> findAll() {
        return moderatorRepository.findAll();
    }

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    public Optional<Moderator> findById(long id) {
        return moderatorRepository.findById(id);
    }

}