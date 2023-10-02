package nl.tudelft.oopp.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nl.tudelft.oopp.demo.entities.Lecturer;
import nl.tudelft.oopp.demo.repositories.LecturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Lecturer service.
 */
@Service
public class LecturerService {

    /**
     * The Lecturer repository.
     */
    LecturerRepository lecturerRepository;

    /**
     * Instantiates a new Lecturer service.
     *
     * @param lecturerRepository the lecturer repository
     */
    @Autowired
    public LecturerService(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    /**
     * Save lecturer.
     *
     * @param lecturer the lecturer
     * @return the lecturer
     */
    public Lecturer save(Lecturer lecturer) {
        return lecturerRepository.save(lecturer);
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    public List<Lecturer> findAll() {
        return lecturerRepository.findAll();
    }

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    public Optional<Lecturer> findById(long id) {
        return lecturerRepository.findById(id);
    }

    /**
     * Find the first lecturer, which is the one creating the room.
     *
     * @param roomId the room id
     * @return the lecturer
     */
    public Lecturer findFirstLecturer(long roomId) {
        List<Lecturer> list = findAll().stream()
                .filter(x -> x.getRoomId() == roomId).collect(Collectors.toList());
        return list.get(0);
    }

}