package nl.tudelft.oopp.demo.servicetests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.tudelft.oopp.demo.entities.Student;
import nl.tudelft.oopp.demo.repositories.StudentRepository;
import nl.tudelft.oopp.demo.services.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * The type Student service test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@org.springframework.test.context.TestPropertySource(
        locations = "classpath:application-test.properties")
class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @MockBean
    private StudentRepository studentRepository;

    private static Student s1;
    private static List<Student> studentList;

    /**
     * Initializing the tests.
     */
    @org.junit.jupiter.api.BeforeAll
    public static void init() {
        studentList = new ArrayList<Student>();
        for (int i = 1; i < 5; i++) {
            Student s = new Student("name" + i, "100." + i, 1,
                    LocalDateTime.of(2021, 1, 1, 12, i));

            s.setId(i);
            studentList.add(s);
        }

        s1 = new Student("name", "Exampleip", 1, LocalDateTime.of(2021, 1, 1, 12, 1));
        s1.setId(10);
    }

    /**
     * Test Save.
     */
    @Test
    void save() {
        when(studentRepository.save(s1)).thenReturn(s1);
        assertEquals(s1, studentService.save(s1));
    }

    /**
     * Test Find all.
     */
    @Test
    void findAll() {
        when(studentRepository.findAll()).thenReturn(studentList);
        assertEquals(studentList, studentService.findAll());
    }

    /**
     * Test Find by id.
     */
    @Test
    void findById() {
        Student s = new Student("name1", "100.1", 1,
                LocalDateTime.of(2021, 1, 1, 12, 1));
        s.setId(1);
        when(studentRepository.findById(1L)).thenReturn(java.util.Optional.of(s));
        assertEquals(Optional.of(s), studentService.findById(1));
    }


    /**
     * Deleteby id.
     */
    @Test
    void deletebyId() {
        when(studentRepository.findAllById(List.of(10L))).thenReturn(List.of(s1));
        studentService.deleteById(10L);
        verify(studentRepository, times(1)).deleteAll(List.of(s1));
    }
}