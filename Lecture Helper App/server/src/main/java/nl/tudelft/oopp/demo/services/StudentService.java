package nl.tudelft.oopp.demo.services;

import java.util.List;
import java.util.Optional;
import nl.tudelft.oopp.demo.entities.Student;
import nl.tudelft.oopp.demo.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Student service.
 */
@Service
public class StudentService {

    /**
     * The Student repository.
     */
    StudentRepository studentRepository;

    /**
     * Instantiates a new Student service.
     *
     * @param studentRepository the student repository
     */
    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Save student.
     *
     * @param student the student
     * @return the student
     */
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    /**
     * Find all list.
     *
     * @return the list
     */
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    public Optional<Student> findById(long id) {
        return studentRepository.findById(id);
    }

    /**
     * Delete by id.
     *
     * @param id the id
     */
    public void deleteById(long id) {
        studentRepository.deleteAll(studentRepository.findAllById(List.of(id)));
    }
}