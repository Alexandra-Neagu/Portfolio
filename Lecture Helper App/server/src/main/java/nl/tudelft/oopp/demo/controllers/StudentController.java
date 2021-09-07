package nl.tudelft.oopp.demo.controllers;

import java.util.List;
import java.util.Optional;
import nl.tudelft.oopp.demo.entities.Student;
import nl.tudelft.oopp.demo.services.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Student controller.
 */
@Controller
@RestController
@RequestMapping("students")
public class StudentController {

    private final StudentService studentService;

    /**
     * Instantiates a new Student controller.
     *
     * @param studentService the student service
     */
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Save student.
     *
     * @param student the student
     * @return the student
     */
    @PostMapping("/insert")
    Student save(@RequestBody Student student) {
        return studentService.save(student);
    }

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    @GetMapping("/get/{id}")
    public Optional<Student> findById(@PathVariable long id) {
        return studentService.findById(id);
    }

    /**
     * Find a ll list.
     *
     * @return the list
     */
    @GetMapping
    List<Student> findALl() {
        return studentService.findAll();
    }

    /**
     * Delete byid.
     *
     * @param id the id
     */
    @DeleteMapping("/delete/{id}")
    public void deleteByid(@PathVariable long id) {
        studentService.deleteById(id);
    }
}