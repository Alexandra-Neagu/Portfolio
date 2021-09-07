package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Lecturer repository.
 */
@Repository("LecturerRepository")
public interface LecturerRepository extends JpaRepository<Lecturer, Long> {
}
