package nl.tudelft.oopp.demo.controllers;

import java.util.List;
import java.util.Optional;
import nl.tudelft.oopp.demo.entities.Moderator;
import nl.tudelft.oopp.demo.services.ModeratorService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Moderator controller.
 */
@Controller
@RestController
@RequestMapping("moderators")
public class ModeratorController {

    private final ModeratorService moderatorService;

    /**
     * Instantiates a new Moderator controller.
     *
     * @param moderatorService the moderator service
     */
    public ModeratorController(ModeratorService moderatorService) {
        this.moderatorService = moderatorService;
    }

    /**
     * Save moderator.
     *
     * @param moderator the moderator
     * @return the moderator
     */
    @PostMapping("/insert")
    Moderator save(@RequestBody Moderator moderator) {
        return moderatorService.save(moderator);
    }

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    @GetMapping("/get/{id}")
    public Optional<Moderator> findById(@PathVariable long id) {
        return moderatorService.findById(id);
    }

    /**
     * Find a ll list.
     *
     * @return the list
     */
    @GetMapping
    List<Moderator> findALl() {
        return moderatorService.findAll();
    }
}