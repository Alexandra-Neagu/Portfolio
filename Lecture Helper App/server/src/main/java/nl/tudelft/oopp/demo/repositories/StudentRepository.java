package nl.tudelft.oopp.demo.repositories;

import nl.tudelft.oopp.demo.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Student repository.
 */
@Repository("StudentRepository")
public interface StudentRepository extends JpaRepository<Student, Long> {
}
