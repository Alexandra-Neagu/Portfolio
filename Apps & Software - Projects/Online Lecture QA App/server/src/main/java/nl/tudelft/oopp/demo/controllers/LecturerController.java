package nl.tudelft.oopp.demo.controllers;

import java.util.List;
import java.util.Optional;
import nl.tudelft.oopp.demo.entities.Lecturer;
import nl.tudelft.oopp.demo.services.LecturerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Lecturer controller.
 */
@Controller
@RestController
@RequestMapping("lecturers")
public class LecturerController {

    private final LecturerService lecturerService;

    /**
     * Instantiates a new Lecturer controller.
     *
     * @param lecturerService the lecturer service
     */
    public LecturerController(LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }

    /**
     * Save lecturer.
     *
     * @param lecturer the lecturer
     * @return the lecturer
     */
    @PostMapping("/insert")
    Lecturer save(@RequestBody Lecturer lecturer) {
        return lecturerService.save(lecturer);
    }

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    @GetMapping("/get/{id}")
    public Optional<Lecturer> findById(@PathVariable long id) {
        return lecturerService.findById(id);
    }

    /**
     * Find a ll list.
     *
     * @return the list
     */
    @GetMapping
    List<Lecturer> findALl() {
        return lecturerService.findAll();
    }

    /**
     * Find first lecturer lecturer.
     *
     * @param roomId the room id
     * @return the lecturer
     */
    @GetMapping("/findFirstLecturer/{roomId}")
    public Lecturer findFirstLecturer(@PathVariable long roomId) {
        return lecturerService.findFirstLecturer(roomId);
    }
}