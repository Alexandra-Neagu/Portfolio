package nl.tudelft.oopp.demo.servicetests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import nl.tudelft.oopp.demo.entities.Lecturer;
import nl.tudelft.oopp.demo.repositories.LecturerRepository;
import nl.tudelft.oopp.demo.services.LecturerService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * The type Lecturer service test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@org.springframework.test.context.TestPropertySource(
        locations = "classpath:application-test.properties")
class LecturerServiceTest {

    @Autowired
    private LecturerService lecturerService;

    @MockBean
    private LecturerRepository lecturerRepository;

    private static Lecturer l1;
    private static List<Lecturer> lecturerList;

    /**
     * Initializing the tests.
     */
    @org.junit.jupiter.api.BeforeAll
    public static void init() {
        lecturerList = new ArrayList<Lecturer>();
        for (int i = 1; i < 5; i++) {
            Lecturer l = new Lecturer("name" + i, "100." + i, i,
                    LocalDateTime.of(2021, 1, 1, 12, i));

            l.setId(i);
            lecturerList.add(l);
        }

        l1 = new Lecturer("name", "Exampleip", 5, LocalDateTime.of(2021, 1, 1, 12, 10));
        l1.setId(10);
    }

    /**
     * Test Save.
     */
    @Test
    void save() {
        when(lecturerRepository.save(l1)).thenReturn(l1);
        assertEquals(l1, lecturerService.save(l1));
    }

    /**
     * Test Find all.
     */
    @Test
    void findAll() {
        when(lecturerRepository.findAll()).thenReturn(lecturerList);
        assertEquals(lecturerList, lecturerService.findAll());
    }

    /**
     * Test Find by id.
     */
    @Test
    void findById() {
        when(lecturerRepository.findById(10L)).thenReturn(java.util.Optional.of(l1));
        assertEquals(Optional.of(l1), lecturerService.findById(10));
    }

    /**
     * Test Find first lecturer.
     */
    @Test
    void findFirstLecturer() {
        Lecturer l = new Lecturer("another lecturer", "127.100.", 1,
                LocalDateTime.of(2021, 1, 1, 12, 50));
        l.setId(15);
        lecturerList.add(l);

        Lecturer ret = lecturerList.stream().filter(x -> x.getRoomId() == 1)
                .collect(Collectors.toList()).get(0);
        when(lecturerRepository.findAll()).thenReturn(lecturerList);
        assertEquals(ret, lecturerService.findFirstLecturer(1));
    }
}